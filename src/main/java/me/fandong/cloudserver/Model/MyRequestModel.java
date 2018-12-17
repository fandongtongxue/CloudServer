package me.fandong.cloudserver.Model;

import java.io.Serializable;

public class MyRequestModel implements Serializable {
    private String url;
    private String params;
    private String result;
    private String status;
    private String msg;

    public MyRequestModel(String url, String params, String result, String status, String msg) {
        this.url = url;
        this.params = params;
        this.result = result;
        this.status = status;
        this.msg = msg;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "MyRequestModel{" +
                "url='" + url + '\'' +
                ", params='" + params + '\'' +
                ", result='" + result + '\'' +
                ", status='" + status + '\'' +
                ", msg='" + msg + '\'' +
                '}';
    }
}
