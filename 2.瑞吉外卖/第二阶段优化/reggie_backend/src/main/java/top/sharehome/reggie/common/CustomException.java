package top.sharehome.reggie.common;

/**
 * 自定义异常处理器
 *
 * @author AntonyCheng
 */
public class CustomException extends RuntimeException {
    public CustomException(String message) {
        super(message);
    }
}
