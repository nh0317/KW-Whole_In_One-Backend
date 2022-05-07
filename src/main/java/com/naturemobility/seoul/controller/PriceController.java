package com.naturemobility.seoul.controller;

import com.naturemobility.seoul.config.BaseException;
import com.naturemobility.seoul.config.BaseResponse;
import com.naturemobility.seoul.domain.price.*;
import com.naturemobility.seoul.service.price.PriceService;
import com.naturemobility.seoul.utils.CheckUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static com.naturemobility.seoul.config.BaseResponseStatus.SUCCESS;

@RestController
@Slf4j
@RequestMapping("price")
public class PriceController {
    @Autowired
    CheckUserService checkUserService;
    @Autowired
    PriceService priceService;

    /**
     * 주말/평일 등록 API
     * [Post] /price/register_week
     *
     * @return BaseResponse<PostWeekRes>
     */
    @PostMapping("/register_week")
    public BaseResponse<PostWeekRes> registerWeek(@RequestBody PostWeekReq postWeekReq) throws BaseException{
        Long partnerIdx = checkUserService.getPartnerIdx();
        PostWeekRes postWeekRes = priceService.setWeek(postWeekReq,partnerIdx);
        return new BaseResponse<>(SUCCESS, postWeekRes);
    }
    /**
     * 가격 등록 API
     * [Post] /price/{storePriceIdx}
     *
     * @return BaseResponse<PostPriceRes>
     */
    @PostMapping(value = {"/register_price/{storePriceIdx}", "/register_price"})
    public BaseResponse<PostPriceRes> registerPrice(@RequestBody PostPriceReq postPriceReq,
                                                        @PathVariable(value = "storePriceIdx", required = false)Long storePriceIdx) throws BaseException{
        Long partnerIdx = checkUserService.getPartnerIdx();
        PostPriceRes postWeekRes = priceService.registerPrice(postPriceReq,partnerIdx,storePriceIdx);
        return new BaseResponse<>(SUCCESS, postWeekRes);
    }
    /**
     * 주말/평일 조회 API
     * [Get] /price/week
     *
     * @return BaseResponse<List<String>>
     */
    @GetMapping("/week")
    public BaseResponse<List<String>> getWeek(@RequestParam(value = "isHoliday") Boolean isHoliday) throws BaseException{
        Long partnerIdx = checkUserService.getPartnerIdx();
        List<String> weeks = priceService.getWeek(partnerIdx,isHoliday);
        return new BaseResponse<>(SUCCESS, weeks);
    }
    /**
     * 주말/평일 가격 조회 API
     * [Get] /price/week_price
     *
     * @return BaseResponse<List<String>>
     */
    @GetMapping("/{storeIdx}/week_price")
    public BaseResponse<List<GetPriceRes>> getWeekPrice(@PathVariable("storeIdx") Long storeIdx,
                                                        @RequestParam(value = "isHoliday",required = false) Boolean isHoliday) throws BaseException{
        List<GetPriceRes> weeks = priceService.getPrice(storeIdx,isHoliday);
        return new BaseResponse<>(SUCCESS, weeks);
    }
    /**
     * 특정 기간 가격 조회 API
     * [Get] /{store_idx}/period_price
     *
     * @return BaseResponse<List<GetPriceRes>>
     */
    @GetMapping("/{storeIdx}/period_price")
    public BaseResponse<List<GetPriceRes>> getPeriodPrice(@PathVariable("storeIdx") Long storeIdx,
                                                          @RequestParam(value = "isHoliday",required = false) Boolean isHoliday,
                                                          @RequestParam(value = "all",required = false) Boolean all)
            throws BaseException{
        List<GetPriceRes> weeks = priceService.getPeriodPrice(isHoliday,storeIdx,all);
        return new BaseResponse<>(SUCCESS, weeks);
    }
    /**
     * 특정 날짜 및 시간의 가격 조회 API
     * [Get] /{store_idx}/current_price
     *
     * @return BaseResponse<Integer>
     */
    @PostMapping("/{storeIdx}/current_price")
    public BaseResponse<Integer> getCurrentPrice(@PathVariable("storeIdx") Long storeIdx,
                                                 @RequestBody GetCurPriceReq getCurPriceReq) throws BaseException{
        Integer price = priceService.getCurrentPrice(getCurPriceReq, storeIdx);
        return new BaseResponse<>(SUCCESS, price);
    }

    /**
     * 가격 삭제 API
     * [Del] /{storePriceIdx}
     *
     */
    @DeleteMapping("/{storePriceIdx}")
    public BaseResponse<Void> deletePrice(@PathVariable(value = "storePriceIdx")Long storePriceIdx) throws BaseException{
        Long partnerIdx = checkUserService.getPartnerIdx();
        priceService.deletePrice(partnerIdx,storePriceIdx);
        return new BaseResponse<>(SUCCESS);
    }
}
