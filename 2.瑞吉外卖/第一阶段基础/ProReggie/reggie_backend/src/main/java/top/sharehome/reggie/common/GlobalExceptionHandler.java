package top.sharehome.reggie.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * 全局异常处理类，这里主要运用AOP的思想对异常进行拦截和统一处理，
 * 即只要抓取到对应的异常，那么就执行这个全局异常处理类中的方法
 *
 * @ControllerAdvice注解 标记需要拦截的Controller，这些Controller上有@RestController或者@Controller注解
 * @ResponseBody注解 使得响应数据以JSON形式进行返回
 */
@ControllerAdvice(annotations = {RestController.class, Controller.class})
@ResponseBody
@Slf4j
public class GlobalExceptionHandler {
    /**
     * 进行异常处理方法，处理字段数据重复的异常
     *
     * @return
     * @ExceptionHandler注解 指明要抓取的异常，使其注入进我们的方法中，这个异常名称通常需要在开发的过程中通过控制台报错信息去查找
     */
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public R<String> saveDuplicateExceptionHandler(SQLIntegrityConstraintViolationException exception) {
        // 当重复添加用户时，异常是SQLIntegrityConstraintViolationException，报错信息是Duplicate entry 'AntonyCheng' for key 'employee.idx_username'
        // 我们就判断异常信息中是否包含"Duplicate entry"报错信息，然后进行动态的数据返回
        if (exception.getMessage().contains("Duplicate entry") && exception.getMessage().contains("for key")) {
            String[] s = exception.getMessage().split(" ");
            return R.error(s[2].substring(1, s[2].length() - 1) + "已经存在");
        }
        return R.error("未知错误，请联系后台管理员……");
    }

    /**
     * 当分类关联菜品或者套餐时，不允许拦截
     * @param exception
     * @return
     */
    @ExceptionHandler(CustomException.class)
    public R<String> customException(CustomException exception) {
        if (exception.getMessage().contains("当前分类下关联了") && exception.getMessage().contains("不能删除")) {
            return R.error(exception.getMessage());
        }
        return R.error("未知错误，请联系后台管理员……");
    }
}
