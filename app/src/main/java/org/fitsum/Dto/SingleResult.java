package org.fitsum.Dto;

import com.google.gson.annotations.SerializedName;


public class SingleResult<T> extends CommonResult {
    // CommonResult를 상속받아 API 요청 결과도 같이 출력

    @SerializedName("data")
    private T data;



    public T getData() {
        return data;
    }


    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "SingleResult{" +
                "data=" + data +
                '}';
    }




}
