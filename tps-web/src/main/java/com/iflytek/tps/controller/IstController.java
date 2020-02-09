package com.iflytek.tps.controller;


import com.iflytek.tps.foun.dto.AppResponse;
import com.iflytek.tps.foun.dto.CommonCode;
import com.iflytek.tps.foun.dto.IMessageCode;
import com.iflytek.tps.service.IstService;
import com.iflytek.tps.service.request.RequestDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    @PostMapping(value = "file",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseBody
    @ApiOperation("录音文件上传转写")
    public AppResponse fileTrans(@RequestParam(value = "sid") String sid, @RequestParam(value = "wav")MultipartFile file){
      if(file.isEmpty()){
          return AppResponse.failed(CommonCode.Error,"文件不存在，请重新上传");
      }
     return AppResponse.success(istService.fileTrans(sid,file));
    }
}
