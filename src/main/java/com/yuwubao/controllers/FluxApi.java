package com.yuwubao.controllers;

import com.yuwubao.entities.FluxEntity;
import com.yuwubao.services.FluxService;
import com.yuwubao.util.Const;
import com.yuwubao.util.RestApiResponse;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@CrossOrigin
public class FluxApi {
    @Resource
    private FluxService fluxService;

    @PostMapping("getFlux")
    public RestApiResponse<Object> getFlux() {
        RestApiResponse<Object> result = new RestApiResponse<>();
        try {
            FluxEntity fluxEntity = fluxService.getFlux();
            result.successResponse(Const.SUCCESS, fluxEntity);
        } catch (Exception e) {
            result.failedApiResponse(Const.FAILED, "失败");
        }
        return result;
    }
}
