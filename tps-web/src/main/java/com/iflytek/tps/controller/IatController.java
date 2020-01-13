package com.iflytek.tps.controller;

import com.iflytek.tps.beans.request.RequestDto;
import com.iflytek.tps.service.IatService;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
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
    public Map<String,String> iatBusiness(@RequestBody RequestDto requestDto){
       return iatService.doConvert(requestDto);
    }
}
