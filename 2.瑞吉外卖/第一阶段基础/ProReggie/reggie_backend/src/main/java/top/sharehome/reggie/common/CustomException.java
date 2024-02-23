package top.sharehome.reggie.common;

/**
 * @Description
 * @Author:AntonyCheng
 * @CreateTime:2023/1/31 15:35
 */

public class CustomException extends RuntimeException {
    public CustomException(String message) {
        super(message);
    }
}
