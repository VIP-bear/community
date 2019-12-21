package site.bearblog.community.dto;

import lombok.Data;
import site.bearblog.community.exception.CustomizeErrorCode;
import site.bearblog.community.exception.CustomizeException;

import java.util.List;

@Data
public class ResultDOT<T> {
    private Integer code;
    private String message;
    private T data;

    public static ResultDOT errorOf(Integer code, String message){
        ResultDOT resultDOT = new ResultDOT();
        resultDOT.setCode(code);
        resultDOT.setMessage(message);
        return resultDOT;
    }

    public static ResultDOT errorOf(CustomizeErrorCode errorCode) {
        return errorOf(errorCode.getCode(), errorCode.getMessage());
    }
    public static ResultDOT okOf(){
        ResultDOT resultDOT = new ResultDOT();
        resultDOT.setCode(200);
        resultDOT.setMessage("请求成功");
        return resultDOT;
    }

    public static <T> ResultDOT okOf(T t){
        ResultDOT resultDOT = new ResultDOT();
        resultDOT.setCode(200);
        resultDOT.setMessage("请求成功");
        resultDOT.setData(t);
        return resultDOT;
    }

    public static ResultDOT errorOf(CustomizeException e) {
        return errorOf(e.getCode(), e.getMessage());
    }
}
