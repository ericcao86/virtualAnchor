package com.iflytek.tps.beans.virtual;

public class VirtualAddData {

    private String taskId;
    private String url;
    private Integer status;
    private String duration;
    private Integer validate;

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public Integer getValidate() {
        return validate;
    }

    public void setValidate(Integer validate) {
        this.validate = validate;
    }

    @Override
    public String toString() {
        return "VirtualAddData{" +
                "taskId='" + taskId + '\'' +
                ", url='" + url + '\'' +
                ", status=" + status +
                ", duration='" + duration + '\'' +
                ", validate=" + validate +
                '}';
    }
}
