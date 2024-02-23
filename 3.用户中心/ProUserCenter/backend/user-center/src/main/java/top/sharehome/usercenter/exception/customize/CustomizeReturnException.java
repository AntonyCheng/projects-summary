package top.sharehome.usercenter.exception.customize;

import lombok.Data;
import top.sharehome.usercenter.common.R;

/**
 * 自定义返回错误异常类
 *
 * @author admin
 */
@Data
public class CustomizeReturnException extends RuntimeException {
    private String description;
    private R failure;

    public <T> CustomizeReturnException(R<T> failure) {
        this.failure = failure;
        this.description = "";
    }

    public <T> CustomizeReturnException(R<T> failure, String description) {
        this.failure = failure;
        this.description = description;
    }
}
