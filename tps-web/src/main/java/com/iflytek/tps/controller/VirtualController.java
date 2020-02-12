package com.iflytek.tps.controller;

import com.iflytek.tps.foun.dto.AppResponse;
import com.iflytek.tps.service.IatService;
import com.iflytek.tps.service.VirtualService;
import com.iflytek.tps.service.request.RequestDto;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
public class VirtualController {

    private Logger logger = LoggerFactory.getLogger(IatController.class);
    @Autowired
    private VirtualService virtualService;

    @PostMapping(value = "virtual")
    @ResponseBody
    @ApiOperation("听写引擎服务接口")
    public AppResponse iatBusiness(@RequestBody Map map){
        return AppResponse.success(virtualService.getVideoUrl(map));
    }
}
