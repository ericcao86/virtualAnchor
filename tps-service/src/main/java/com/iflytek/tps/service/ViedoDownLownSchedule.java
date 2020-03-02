package com.iflytek.tps.service;

import com.alibaba.fastjson.JSONObject;
import com.iflytek.tps.beans.virtual.VirtualAddObject;
import com.iflytek.tps.foun.dto.DateFormat;
import com.iflytek.tps.foun.util.DateUtils;
import com.iflytek.tps.foun.util.HttpRestUtils;
import com.iflytek.tps.service.util.CommonMap;
import com.iflytek.tps.service.util.SignUtils;
import okhttp3.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ViedoDownLownSchedule {

    private static Logger logger = LoggerFactory.getLogger(ViedoDownLownSchedule.class);

    private static final String secret = "ye3712c7123fa7a70a139e2c4751f123";

    private static final String TASK_JSON="{\"base\":{\"appid\":\"a2te42kf\",\"sid\":\"17d82f98-d7c4-4c83-8de0-76568d172ce7\",\"timestamp\":${timestamp},\"sign\":\"${sign}\"},\"param\":{\"taskId\":\"${taskId}\"}}";

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    @Value("${task.url}")
    private String taskUrl;

    @Scheduled(fixedDelay = 1000*10)
    public void downLoadVieo(){
        ConcurrentHashMap map = CommonMap.getTaskMap();
        String now = DateUtils.now( DateFormat.StrikeDateTime);
        logger.info("定时器开始,当前时间{}",now);
        map.forEach((k,v)->{
            //k为指定的视频名称，v为任务的taskId
            logger.info("开始获取视频地址。。。。。");
          String videoUrl = getVideoUrl((String)k, (String) v);
          logger.info("当前视频地址为{}",videoUrl);
          if(StringUtils.isNotBlank(videoUrl)){//如果已经生成了url
            //下载url
              logger.info("开始下载视频。。。。");
              download(videoUrl,"D:\\video\\"+(String) k+".mp4");
              logger.info("视频下载完成。。。");
              CommonMap.getTaskMap().remove(k);
          }
        });
        logger.info("定时器结束,结束时间为{}",DateUtils.now( DateFormat.StrikeDateTime));
    }

    private String buildParamJson(String taskId){
        String taskJson = TASK_JSON.replace("${taskId}",taskId).replace("${timestamp}",System.currentTimeMillis()+"");
        JSONObject taskObject = JSONObject.parseObject(taskJson);
        String sign1 = SignUtils.buildSign(taskObject,secret);
        taskJson = taskJson.replace("${sign}",sign1);
        return taskJson;
    }

    private String getVideoUrl(String key,String taskId){
        String url="";
        String taskJson = buildParamJson(taskId);
        RequestBody taskBody = RequestBody.create(JSON, taskJson);
        VirtualAddObject task= HttpRestUtils.post(taskUrl,taskBody, VirtualAddObject.class);
        if(task.getData().getStatus() == 0){
            CommonMap.getTaskMap().remove(key);
        }
        logger.info("查询返回当前信息 {}",task.toString());
        if(StringUtils.isNotBlank(task.getData().getUrl())){
            url = task.getData().getUrl();
        }
        return url;
    }

    private void download(String httpUrl,String fileName){
        // 1.下载网络文件
        int byteRead;
        URL url =null;
        try {
            url = new URL(httpUrl);
        } catch (MalformedURLException e1) {
            e1.printStackTrace();
        }
        try {
            //2.获取链接
            URLConnection conn = url.openConnection();
            //3.输入流
            InputStream inStream = conn.getInputStream();
            //3.写入文件
            FileOutputStream fs = new FileOutputStream(fileName);

            byte[] buffer = new byte[1024];
            while ((byteRead = inStream.read(buffer)) != -1) {
                fs.write(buffer, 0, byteRead);
            }
            inStream.close();
            fs.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();

        }

    }

    private void writeFile(Response response,String fileName) {
        InputStream is = null;
        FileOutputStream fos = null;
        is = response.body().byteStream();
        String path = "D:\\";
        File file = new File(path, fileName+".mp4");
        try {
            fos = new FileOutputStream(file);
            byte[] bytes = new byte[1024];
            int len = 0;
            while ((len = is.read(bytes)) != -1) {
                fos.write(bytes);

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        logger.info("myTag", "下载成功");
    }

}
