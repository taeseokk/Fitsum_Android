package org.fitsum.Dto;

import com.google.gson.annotations.SerializedName;


public class CommonResult {
    @SerializedName("success")
    private boolean success;

    @SerializedName("code")
    private int code;

    @SerializedName("msg")
    private String msg;


    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }


    @Override
    public String toString() {
        return "CommonResult{" +
                "success=" + success +
                ", code=" + code +
                ", msg='" + msg + '\'' +
                '}';
    }


}
