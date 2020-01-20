package com.iflytek.tps.controller;

import com.iflytek.tps.foun.dto.AppResponse;
import com.iflytek.tps.service.IatService;
import com.iflytek.tps.service.request.RequestDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@Api(value = "iat",description = "听写引擎服务")
@Controller
@RequestMapping(value = "/api/v1/business/")
public class IatController {
    private Logger logger = LoggerFactory.getLogger(IatController.class);
    @Autowired
    private IatService iatService;

    @PostMapping(value = "iat")
    @ResponseBody
    @ApiOperation("听写引擎服务接口")
    public AppResponse<Map<String,String>> iatBusiness(@RequestBody @Valid RequestDto requestDto){
        requestDto.verify();
       return AppResponse.success(iatService.doConvert(requestDto));
    }
}
