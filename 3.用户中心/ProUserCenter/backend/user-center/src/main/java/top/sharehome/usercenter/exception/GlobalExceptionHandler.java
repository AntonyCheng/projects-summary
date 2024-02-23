package top.sharehome.usercenter.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import top.sharehome.usercenter.common.R;
import top.sharehome.usercenter.exception.customize.CustomizeReturnException;

/**
 * 全局异常处理器
 *
 * @author AntonyCheng
 */
@ResponseBody
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(value = {CustomizeReturnException.class})
    public <T> R<T> returnExceptionHandler(CustomizeReturnException exception) {
        log.warn("Exception:{},Description:{}", CustomizeReturnException.class, exception.getDescription());
        return exception.getFailure();
    }
}
