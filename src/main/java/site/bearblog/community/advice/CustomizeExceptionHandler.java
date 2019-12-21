package site.bearblog.community.advice;

import com.alibaba.fastjson.JSON;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import site.bearblog.community.dto.ResultDOT;
import site.bearblog.community.exception.CustomizeErrorCode;
import site.bearblog.community.exception.CustomizeException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@ControllerAdvice
public class CustomizeExceptionHandler {
    @ExceptionHandler(Exception.class)
    Object handle(Throwable throwable, Model model,
                  HttpServletRequest request,
                  HttpServletResponse response){

        String contentType = request.getContentType();
        if ("application/json".equals(contentType)){
            // 返回json
            ResultDOT resultDOT;
            if (throwable instanceof CustomizeException){
                resultDOT = ResultDOT.errorOf((CustomizeException) throwable);
            }else {
                resultDOT = ResultDOT.errorOf(CustomizeErrorCode.SYS_ERROR);
            }
            try {
                response.setContentType("application/json");
                response.setStatus(200);
                response.setCharacterEncoding("utf-8");
                PrintWriter writer = response.getWriter();
                writer.write(JSON.toJSONString(resultDOT));
                writer.close();
            }catch (IOException ioe){
            }
            return null;
        }else {
            // 错误页面跳转
            if (throwable instanceof CustomizeException){
                model.addAttribute("message", throwable.getMessage());
            }else {
                model.addAttribute("message", CustomizeErrorCode.SYS_ERROR.getMessage());
            }
            return new ModelAndView("error");
        }

    }
}
