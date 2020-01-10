package com.iflytek.tps.controller;

import com.iflytek.tps.service.IstService;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Api(value = "ist",description = "实时引擎服务")
@RequestMapping(value = "/api/v1/business/ist")
@Controller
public class IstController {

    private Logger logger = LoggerFactory.getLogger(IstController.class);
    @Autowired
    private IstService istService;
}
