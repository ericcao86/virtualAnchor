package com.iflytek.tps.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iflytek.tps.WSServerConfigure;
import com.iflytek.tps.beans.dictation.IatSessionParam;
import com.iflytek.tps.beans.dictation.IatSessionResponse;
import com.iflytek.tps.foun.dto.AppResponse;
import com.iflytek.tps.service.client.IatClient;
import com.iflytek.tps.service.impl.IatSessionResponseImpl;
import com.iflytek.tps.service.util.CommonMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.EOFException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
@ServerEndpoint(value = "/ws" ,configurator = WSServerConfigure.class)
public class WebSocketService {

    @Resource
    private ObjectMapper objectMapper;

    @Value("${iat.url}")
    private String iatUrl;

    private static Logger log = LoggerFactory.getLogger(WebSocketService.class);

    private static final String END_IDENTITY = "{\"end\": true}";
    private static Map<String, String> taskIds = new ConcurrentHashMap<>();

    private IatSessionParam iatSessionParam;
    private IatClient iatClient;
    private IatSessionResponse iatSessionResponse;
    private String taskId;
    private Session session;

    @OnOpen
    public void onOpen(Session session)  {
        this.session = session;
        log.info("当前调用方法：onOpen");
        log.info("websocket开始连接。。。");
        log.info("当前session id{}",session.getId());
        taskId = UUID.randomUUID().toString().replaceAll("-", "");
        iatSessionParam = new IatSessionParam(taskId,"16k");
        iatClient = new IatClient(iatUrl,iatSessionParam);
        iatSessionResponse = new IatSessionResponseImpl(this.session,taskId);
        boolean ret = iatClient.connect(iatSessionResponse);
        if(!ret){
            log.error("【连接异常】sid : {}", taskId);
        }
        log.info("websocket 连接正常");
        sendMessage(this.session, AppResponse.success("websocket 已连接"));
    }

    @OnMessage
    public void onMessage(Session session, byte[] message) throws Exception {
        log.info("当前调用方法：onMessage");
        log.info("当前字节大小{}",message.length);
        if(this.session == null){
            this.session = session;
            taskId = UUID.randomUUID().toString().replaceAll("-", "");
            iatSessionParam = new IatSessionParam(taskId,"16k");
            iatClient = new IatClient(iatUrl,iatSessionParam);
            iatSessionResponse = new IatSessionResponseImpl(this.session,taskId);
            boolean ret = iatClient.connect(iatSessionResponse);
            if(!ret){
                log.error("【连接异常】sid : {}", taskId);
            }
        }
        log.info("当前session id{}",this.session.getId());
        if(message.length>0){
            log.info("接收到传递过来的信息{}",message.toString());
            if(Arrays.equals(END_IDENTITY.getBytes(), message)){
                log.info(" 发送结束标识");
                message = new byte[0];
                iatClient.end();
                this.session =null;
                log.info(" 发送结束");
            }else{
                log.info("开始往引擎发送信息");
                byte [] bytes =message;
                int z = 1028;//每次发送的字节数
                //总长度
                int bylenth =bytes.length;
                //如果发送的字节小于1280，直接发送引擎
                if(bylenth <=z){
                    iatClient.post(bytes);
                }else{
                    //如果是接收的字节数是1280的倍数，循环发送
                    if(bylenth % z == 0){
                        for(int j=0;j<bylenth;j+=z){
                            byte [] s2 = new byte[z];
                            System.arraycopy(bytes,j,s2,0,z);
                            iatClient.post(s2);
                        }
                    }else{
                        //如果不是整数倍
                        int n = bylenth/z;//倍数
                        int n1 = bylenth%z;//余数
                        for(int n2=0;n2<n*z;n2+=z){
                            byte [] s3 = new byte[z];
                            System.arraycopy(bytes,n2,s3,0,z);
                            iatClient.post(s3);

                        }
                        int start = bylenth - n*z;
                        byte [] s4 = new byte[n1];
                        System.arraycopy(bytes,start,s4,0,n1);
                        iatClient.post(s4);
                    }
                }
                log.info("发送结束");
            }

        }else{
            sendMessage(session,AppResponse.success("发送字节为空"));
        }

    }

    @OnError
    public void onError(Throwable throwable, Session session) {
        log.info("当前调用方法：onError");
        if(throwable instanceof EOFException){
            log.info("客户端主动关闭连接");
        }else{
            log.error("【连接发生异常】");
        }
    }

    @OnClose
    public void onClose(Session session)throws Exception {
        log.info("当前调用方法：onClose");
    }

    /**
     * 数据发送
     * @param session 当前回话
     * @param message 待发送的数据
     */
    private void sendMessage(Session session, Object message) {
        try {
            if(session.isOpen()){
                session.getBasicRemote().sendText(objectMapper.writeValueAsString(message));
            }
        } catch (Exception e) {
            log.error("sessionId: {}, 【数据传输客户端异常】，result：{}，exception：{}", message.toString(), e);
        }
    }

}
