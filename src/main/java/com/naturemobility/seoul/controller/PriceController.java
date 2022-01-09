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

    @PostMapping("/register_week")
    public BaseResponse<List<PostWeekRes>> registerWeek(@RequestBody PostWeekReq postWeekReq){
        try{
            Long partnerIdx = checkUserService.getPartnerIdx();
            List<PostWeekRes> postWeekRes = priceService.setWeek(postWeekReq,partnerIdx);
            return new BaseResponse<>(SUCCESS, postWeekRes);
        }catch (BaseException exception){
            exception.printStackTrace();
            return new BaseResponse<>(exception.getStatus());
        }
    }

    @PostMapping(value = {"/register_price/{storePriceIdx}", "/register_price"})
    public BaseResponse<PostPriceRes> registerPrice(@RequestBody PostPriceReq postPriceReq,
                                                        @PathVariable(value = "storePriceIdx", required = false)Long storePriceIdx){
        try{
            Long partnerIdx = checkUserService.getPartnerIdx();
            PostPriceRes postWeekRes = priceService.registerPrice(postPriceReq,partnerIdx,storePriceIdx);
            return new BaseResponse<>(SUCCESS, postWeekRes);
        }catch (BaseException exception){
            exception.printStackTrace();
            return new BaseResponse<>(exception.getStatus());
        }
    }

    @GetMapping("/week")
    public BaseResponse<List<String>> getWeek(@RequestParam(value = "isHoliday") Boolean isHoliday){
        try{
            Long partnerIdx = checkUserService.getPartnerIdx();
            List<String> weeks = priceService.getWeek(partnerIdx,isHoliday);
            return new BaseResponse<>(SUCCESS, weeks);
        }catch (BaseException exception){
            exception.printStackTrace();
            return new BaseResponse<>(exception.getStatus());
        }
    }
    @GetMapping("/week_price")
    public BaseResponse<List<GetPriceRes>> getWeekPrice(@RequestParam(value = "isHoliday",required = false) Boolean isHoliday){
        try{
            Long partnerIdx = checkUserService.getPartnerIdx();
            List<GetPriceRes> weeks = priceService.getPrice(partnerIdx,isHoliday);
            return new BaseResponse<>(SUCCESS, weeks);
        }catch (BaseException exception){
            exception.printStackTrace();
            return new BaseResponse<>(exception.getStatus());
        }
    }
    @GetMapping("/period_price")
    public BaseResponse<List<GetPriceRes>> getPeriodPrice(@RequestParam(value = "isHoliday",required = false) Boolean isHoliday){
        try{
            Long partnerIdx = checkUserService.getPartnerIdx();
            List<GetPriceRes> weeks = priceService.getPeriodPrice(isHoliday,partnerIdx);
            return new BaseResponse<>(SUCCESS, weeks);
        }catch (BaseException exception){
            exception.printStackTrace();
            return new BaseResponse<>(exception.getStatus());
        }
    }
    @GetMapping("/current_price")
    public BaseResponse<Integer> getCurrentPrice(@RequestParam("hole")Integer hole, @RequestParam("date") String date,
                                                 @RequestParam("time") String time){
        try{
            Long partnerIdx = checkUserService.getPartnerIdx();
            Integer price = priceService.getCurrentPrice(date,time,hole,partnerIdx);
            return new BaseResponse<>(SUCCESS, price);
        }catch (BaseException exception){
            exception.printStackTrace();
            return new BaseResponse<>(exception.getStatus());
        }
    }
    @DeleteMapping("/{storePriceIdx}")
    public BaseResponse<Void> deletePrice(@PathVariable(value = "storePriceIdx")Long storePriceIdx){
        try{
            Long partnerIdx = checkUserService.getPartnerIdx();
            priceService.deletePrice(partnerIdx,storePriceIdx);
            return new BaseResponse<>(SUCCESS);
        }catch (BaseException exception){
            exception.printStackTrace();
            return new BaseResponse<>(exception.getStatus());
        }
    }
}
