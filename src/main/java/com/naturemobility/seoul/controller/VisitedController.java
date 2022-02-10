package com.naturemobility.seoul.controller;

import com.naturemobility.seoul.config.BaseException;
import com.naturemobility.seoul.config.BaseResponse;
import com.naturemobility.seoul.domain.visited.GetVisitedByUserIdx;
import com.naturemobility.seoul.service.visited.VisitedService;
import com.naturemobility.seoul.utils.CheckUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static com.naturemobility.seoul.config.BaseResponseStatus.*;

@RestController
@RequestMapping("visited")
public class VisitedController {
    @Autowired
    VisitedService visitedService;

    @Autowired
    CheckUserService checkUserService;

    /**
     * 방문한 매장의 목록을 반환함
     * [POST] /visited
     * @return BaseResponse<List<GetVisitedByUserIdx>>
     */
    @ResponseBody
    @GetMapping("")
    public BaseResponse<List<GetVisitedByUserIdx>> getVisiteds(@RequestParam(value = "page",required = false) Integer page) throws BaseException {
        Long userIdx=0L;
        userIdx = checkUserService.getUserIdx();
        List<GetVisitedByUserIdx> allVistiedStore = visitedService.findAllVisitedStore(userIdx, page);
        return new BaseResponse<>(SUCCESS, allVistiedStore);
    }

    @GetMapping("total_page")
    public BaseResponse<Map<String,Integer>> getTotalPage() throws BaseException {
        Long userIdx = checkUserService.getUserIdx();
        Map<String, Integer> result = visitedService.getTotalPage(userIdx);
        return new BaseResponse<>(SUCCESS, result);
    }
}
