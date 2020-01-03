package com.iflytek.tps.foun.util;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import javafx.util.Pair;
import okhttp3.*;
import okhttp3.internal.Util;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("unused")
public final class HttpRestUtils {
    private static final Logger LOG = LoggerFactory.getLogger(HttpRestUtils.class);
    private static final String X_DOWNLOAD = "X_DOWNLOAD";
    private static final String PAIR_S = "=";
    private static final String CON_S = "&";
    private static final String QUE_S = "?";
    private static final int TIMEOUT = 10;

    /** ok http client */
    private static final OkHttpClient OK_CLIENT = new OkHttpClient().newBuilder()
            .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT, TimeUnit.SECONDS)
            .addInterceptor(chain -> {
                //ok http 统一拦截器操作
                Response response = null;
                Request request = chain.request();
                Headers.Builder hBuilder = request.headers().newBuilder();
                if(LOG.isInfoEnabled() && StringUtils.isBlank(hBuilder.get(X_DOWNLOAD))) {
                    LOG.info("ok http request: {}", request.toString());
                    LOG.info("ok http request headers: {}", request.headers().toString());

                    response = chain.proceed(request);

                    LOG.info("ok http response: {}", response.toString());
                    LOG.info("ok http response headers: {}", response.headers().toString());
                }
                hBuilder.removeAll(X_DOWNLOAD);
                try {
                    response = null == response ? chain.proceed(request) : response;
                }catch (Exception e){
                    Connection connection = chain.connection();
                    if(null != connection) {
                        Socket socket = connection.socket();
                        if (null != socket && socket.isConnected() && !socket.isClosed()) {
                            socket.close();
                        }
                    }
                }
                if(!response.isSuccessful()){
                    throw new RuntimeException("request service error: " + response.toString());
                }
                return response;
            }).build();

    private HttpRestUtils() {
    }

    /** 拼接参数生成完整 URL */
    public static String url(String uri, List<Pair<String, String>> queries){
        String query = builderQuery(queries);
        if(StringUtils.isBlank(query)){
            return uri;
        }
        if(uri.contains(QUE_S)){
            if(uri.endsWith(CON_S) || uri.endsWith(QUE_S)){
                return uri.concat(query);
            }else {
                return uri.concat(CON_S).concat(query);
            }
        }
        return uri.concat(QUE_S).concat(query);
    }

    /** 请求 URL 问号后面的 Query 生成器 */
    public static String builderQuery(List<Pair<String, String>> queries){
        List<String> queryList = Lists.newArrayList();
        for(Pair<String, String> pair: queries){
            queryList.add(pair.getKey().concat(PAIR_S).concat(pair.getValue()));
        }
        return Joiner.on(CON_S).join(queryList);
    }

    /** 异步 GET 请求没有自定义 HEADER */
    public static void asyncGet(String url, OkHttpCallBack callBack){
        asyncNoBody(url, Lists.newArrayList(), RequestMethod.GET, callBack);
    }

    /** 异步 GET 请求带自定义 HEADER */
    public static void asyncGet(String url, List<Pair<String, String>> headers, OkHttpCallBack callBack){
        asyncNoBody(url, headers, RequestMethod.GET, callBack);
    }

    /** 异步 GET 请求下载文件没有自定义 HEADER */
    public static void download(String url, OkHttpCallBack callBack){
        asyncNoBody(url, Lists.newArrayList(new Pair<>(X_DOWNLOAD, X_DOWNLOAD)), RequestMethod.GET, callBack);
    }

    /** 异步 GET 请求下载文件带自定义 HEADER */
    public static void download(String url, List<Pair<String, String>> headers, OkHttpCallBack callBack){
        asyncNoBody(url, CollectionUtils.isNullOrEmpty(headers)
                        ? Lists.newArrayList(new Pair<>(X_DOWNLOAD, X_DOWNLOAD))
                        : headers,
                RequestMethod.GET,
                callBack);
    }

    /** 同步 GET 请求没自定义 HEADER，返回单个对象 */
    public static <T> T get(String url, Class<T> clazz){
        String rs = noBody(url, Lists.newArrayList(), RequestMethod.GET);
        return String.class.equals(clazz) ? (T)rs : JsonUtils.parseObject(rs, clazz);
    }

    /** 同步 GET 请求带自定义 HEADER，返回单个对象 */
    public static <T> T get(String url, List<Pair<String, String>> headers, Class<T> clazz){
        String rs = noBody(url, headers, RequestMethod.GET);
        return String.class.equals(clazz) ? (T)rs : JsonUtils.parseObject(rs, clazz);
    }

    /** 同步 GET 请求没有自定义 HEADER，返回对象数组 */
    public static <T> List<T> listGet(String url, Class<T> clazz){
        return JsonUtils.parseArray(noBody(url, Lists.newArrayList(), RequestMethod.GET), clazz);
    }

    /** 同步 GET 请求带自定义 HEADER，返回对象数组 */
    public static <T> List<T> listGet(String url, List<Pair<String, String>> headers, Class<T> clazz){
        return JsonUtils.parseArray(noBody(url, headers, RequestMethod.GET), clazz);
    }

    /** 异步 POST 请求没有自定义 HEADER */
    public static void asyncPost(String url, OkHttpCallBack callBack){
        asyncWithBody(url, Lists.newArrayList(), RequestMethod.POST, Util.EMPTY_REQUEST, callBack);
    }

    /** 异步 POST 请求带自定义 HEADER */
    public static void asyncPost(String url, List<Pair<String, String>> headers, OkHttpCallBack callBack){
        asyncWithBody(url, headers, RequestMethod.POST, Util.EMPTY_REQUEST, callBack);
    }

    /** 异步 POST 请求没有自定义 HEADER，有 RequestBody 请求体*/
    public static void asyncPost(String url, RequestBody body, OkHttpCallBack callBack){
        asyncWithBody(url, Lists.newArrayList(), RequestMethod.POST, body, callBack);
    }

    /** 异步 POST 请求带自定义 HEADER, 有 RequestBody 请求体 */
    public static void asyncPost(String url, List<Pair<String, String>> headers, RequestBody body, OkHttpCallBack callBack){
        asyncWithBody(url, headers, RequestMethod.POST, body, callBack);
    }

    /** 异步 POST 请求没有自定义 HEADER, 上传文件 */
    public static void upload(String url, RequestBody body, OkHttpCallBack callBack){
        asyncWithBody(url, Lists.newArrayList(), RequestMethod.POST, body, callBack);
    }

    /** 异步 POST 请求带自定义 HEADER, 上传文件 */
    public static void upload(String url, List<Pair<String, String>> headers, RequestBody body, OkHttpCallBack callBack){
        asyncWithBody(url, headers, RequestMethod.POST, body, callBack);
    }

    /** 同步 POST 请求没有自定义 HEADER，没有 RequestBody 请求体，返回单个对象 */
    public static <T> T post(String url, Class<T> clazz){
        String rs = withBody(url, Lists.newArrayList(), RequestMethod.POST, Util.EMPTY_REQUEST);
        return String.class.equals(clazz) ? (T)rs : JsonUtils.parseObject(rs, clazz);
    }

    /** 同步 POST 请求带自定义 HEADER，没有 RequestBody 请求体，返回单个对象 */
    public static <T> T post(String url, List<Pair<String, String>> headers, Class<T> clazz){
        return JsonUtils.parseObject(withBody(url, headers, RequestMethod.POST, Util.EMPTY_REQUEST), clazz);
    }

    /** 同步 POST 请求没有自定义 HEADER，有 RequestBody 请求体，返回单个对象 */
    public static <T> T post(String url, RequestBody body, Class<T> clazz){
        String rs = withBody(url, Lists.newArrayList(), RequestMethod.POST, body);
        return String.class.equals(clazz) ? (T)rs : JsonUtils.parseObject(rs, clazz);
    }

    /** 同步 POST 请求带自定义 HEADER，有 RequestBody 请求体，返回单个对象 */
    public static <T> T post(String url, List<Pair<String, String>> headers, RequestBody body, Class<T> clazz){
        String rs = withBody(url, headers, RequestMethod.POST, body);
        return String.class.equals(clazz) ? (T)rs : JsonUtils.parseObject(rs, clazz);
    }

    /** 同步 POST 请求没有自定义 HEADER，没有 RequestBody 请求体，返回对象数组 */
    public static <T> List<T> listPost(String url, Class<T> clazz){
        return JsonUtils.parseArray(withBody(url, Lists.newArrayList(), RequestMethod.POST, Util.EMPTY_REQUEST), clazz);
    }

    /** 同步 POST 请求带自定义 HEADER，没有 RequestBody 请求体，返回对象数组 */
    public static <T> List<T> listPost(String url, List<Pair<String, String>> headers, Class<T> clazz){
        return JsonUtils.parseArray(withBody(url, headers, RequestMethod.POST, Util.EMPTY_REQUEST), clazz);
    }

    /** 同步 POST 请求没有自定义 HEADER，有 RequestBody 请求体，返回对象数组 */
    public static <T> List<T> listPost(String url, RequestBody body, Class<T> clazz){
        return JsonUtils.parseArray(withBody(url, Lists.newArrayList(), RequestMethod.POST, body), clazz);
    }

    /** 同步 POST 请求带自定义 HEADER，有 RequestBody 请求体，返回对象数组 */
    public static <T> List<T> listPost(String url, List<Pair<String, String>> headers, RequestBody body, Class<T> clazz){
        return JsonUtils.parseArray(withBody(url, headers, RequestMethod.POST, body), clazz);
    }

    /** RequestBody 请求体生成器 */
    public static RequestBody buildBody(org.springframework.http.MediaType mediaType, Object body){
        if(null == body){
            return Util.EMPTY_REQUEST;
        }
        MediaType mt = MediaType.parse(mediaType.toString());
        if(body instanceof File){
            return RequestBody.create(mt, (File)body);
        }
        LOG.info("ok http request body: {}", JsonUtils.toJSONString(body));
        return RequestBody.create(mt, JsonUtils.toJSONBytes(body));
    }

    /** Form 表单 请求 RequestBody 体生成器 */
    public static RequestBody buildForm(List<Pair<String, String>> form){
        LOG.info("ok http form request body: {}", JsonUtils.toJSONString(form));
        FormBody.Builder builder = new FormBody.Builder();
        for(Pair<String, String> pair: form){
            if(!StringUtils.isBlank(pair.getKey())) {
                builder.add(pair.getKey(), StringUtils.defaultString(pair.getValue()));
            }
        }
        return builder.build();
    }

    /** 没有 RequestBody 请求体 */
    private static String noBody(String url, List<Pair<String, String>> headers, RequestMethod method){
        return withBody(url, headers, method, null);
    }

    /** 带有 RequestBody 请求体 */
    private static String withBody(String url, List<Pair<String, String>> headers, RequestMethod method, RequestBody body){
        Response response = null;
        Call call = null;
        try {
            call = buildCall(url, headers, method, body);
            response = call.execute();
            if(LOG.isDebugEnabled()) {
                LOG.debug("ok http response body: {}", response);
            }
            if(response.isSuccessful()) {
                ResponseBody rb = response.body();
                return null != rb ? rb.string() : StringUtils.EMPTY;
            }else {
                throw new RuntimeException("service response unsuccessful msg: " + response.toString());
            }
        }catch (Exception e){
            throw new RuntimeException("request service error......", e);
        }finally {
            if(null != response){
                response.close();
            }
            if(null != call && !call.isCanceled()){
                call.cancel();
            }
        }
    }

    /** 异步请求 */
    private static void asyncNoBody(String url, List<Pair<String, String>> headers, RequestMethod method, OkHttpCallBack callBack) {
        asyncWithBody(url, headers, method, null, callBack);
    }

    /** 异步请求 */
    private static void asyncWithBody(String url, List<Pair<String, String>> headers, RequestMethod method, RequestBody body, OkHttpCallBack callBack) {
        if(null == callBack){
            throw new RuntimeException("ok http asyncSend request must provider CallBack");
        }
        buildCall(url, headers, method, body).enqueue(callBack);
    }

    /** 生成请求 Call */
    private static Call buildCall(String url, List<Pair<String, String>> headers, RequestMethod method, RequestBody body){
        Request.Builder builder = builderHeader(headers);
        switch (method){
            case GET:
                builder.url(url).get();
                break;
            case HEAD:
                builder.url(url).head();
                break;
            case POST:
                builder.url(url).post(body);
                break;
            case PUT:
                builder.url(url).put(body);
                break;
            case PATCH:
                builder.url(url).patch(body);
                break;
            case DELETE:
                builder.url(url).delete(body);
                break;
            case OPTIONS:
            case TRACE:
                throw new RuntimeException("ok http request method:" + method.name() + " error.....");
        }
        return OK_CLIENT.newCall(builder.build());
    }

    /** 添加 HEADER 生成 Request.Builder */
    private static Request.Builder builderHeader(List<Pair<String, String>> headers) {
        Request.Builder builder = new Request.Builder();
        if(!CollectionUtils.isNullOrEmpty(headers)){
            Set<String> headSet = Sets.newHashSet();
            for(Pair<String, String> header: headers){
                if(headSet.contains(header.getKey())) {
                    builder.addHeader(header.getKey(), header.getValue());
                }else {
                    builder.header(header.getKey(), header.getValue());
                }
            }
        }
        return builder;
    }

    /** 异步请求 CallBack 类 */
    public static abstract class OkHttpCallBack implements Callback {
        @Override
        public void onFailure(Call call, IOException e) {
            LOG.error("ok http async failure.....", e);
            if(null != call) {
                call.cancel();
            }
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            try {
                if (response.isSuccessful()) {
                    try {
                        success(response.headers(), response.body());
                    } catch (Exception e) {
                        LOG.error("ok http async send do response error: ", e);
                    }
                } else {
                    LOG.error("ok http async send request: {}, response: {}", call.request().toString(), response.toString());
                }
            }finally {
                if(null != response) {
                    response.close();
                }
                if(null != call && !call.isCanceled()){
                    call.cancel();
                }
            }
        }

        protected abstract void success(Headers headers, ResponseBody body) throws IOException;
    }
}
