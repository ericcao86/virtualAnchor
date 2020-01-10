package com.iflytek.tps.controller;

import com.iflytek.tps.service.IatService;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Api(value = "iat",description = "听写引擎服务")
@Controller
@RequestMapping(value = "/api/v1/business/iat")
public class IatController {
    private Logger logger = LoggerFactory.getLogger(IatController.class);
    @Autowired
    private IatService iatService;
}
