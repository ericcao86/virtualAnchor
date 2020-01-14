package com.iflytek.tps.controller;


import com.iflytek.tps.foun.dto.AppResponse;
import com.iflytek.tps.service.IstService;
import com.iflytek.tps.service.request.RequestDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.Map;

@Api(value = "ist",description = "实时引擎服务")
@RequestMapping(value = "/api/v1/business/")
@Controller
public class IstController {

    private Logger logger = LoggerFactory.getLogger(IstController.class);
    @Autowired
    private IstService istService;

    @PostMapping(value = "ist")
    @ResponseBody
    @ApiOperation("实时转写服务引擎接口")
    public AppResponse<Map<String,String>> istBusiness(@RequestBody @Valid RequestDto requestDto){
        requestDto.verify();
        return AppResponse.success(istService.doConvert(requestDto));
    }
}
