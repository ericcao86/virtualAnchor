package com.iflytek.tps.foun.dto;


import com.iflytek.tps.foun.util.GenericUtils;

@SuppressWarnings("ALL")
public class AppResponse<T> implements IResponse<T> {
    private static final long serialVersionUID = -5991398366571839361L;

    @Override
    public Class<T> clazz() {
        return GenericUtils.getSuperClassGenericType(this.getClass());
    }

    public boolean result;

    public String code;

    public String msg;

    public T data;

    @SuppressWarnings("unused")
    public AppResponse() {
    }

    private AppResponse(boolean result, IMessageCode mc) {
        this.result = result;
        this.code = mc.code();
        this.msg = mc.msg();
    }

    private AppResponse(boolean result, IMessageCode mc, T data) {
        this.result = result;
        this.code = mc.code();
        this.msg = mc.msg();
        this.data = data;
    }


    public static <R> AppResponse<R> success(IMessageCode mc, R data){
        return new AppResponse<>(true, mc, data);
    }

    public static <R> AppResponse<R> success(R data){
        return new AppResponse<>(true, CommonCode.Ok, data);
    }

    public static AppResponse<Void> success(){
        return new AppResponse<>(true, CommonCode.Ok);
    }

    public static AppResponse<Void> failed(IMessageCode mc){
        return new AppResponse<>(false, mc);
    }

    public static <R> AppResponse<R> failed(IMessageCode mc, R data) {
        return new AppResponse<>(false, mc, data);
    }
}