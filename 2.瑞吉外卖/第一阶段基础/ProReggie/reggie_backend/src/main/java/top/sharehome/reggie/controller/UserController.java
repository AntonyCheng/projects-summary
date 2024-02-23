package top.sharehome.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.sharehome.reggie.common.R;
import top.sharehome.reggie.entity.User;
import top.sharehome.reggie.service.UserService;
import top.sharehome.reggie.utils.ValidateCodeUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * @Description
 * @Author:AntonyCheng
 * @CreateTime:2023/2/3 16:35
 */
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {
    @Resource
    private UserService userService;
    private final static String CODE_IN_ATTRIBUTE = "phone";

    private final static String CODE_NOW_TIME = "nowTime";

    /**
     * 发送手机短信验证码
     *
     * @param map 封装好phone和当前时间戳的user对象
     * @return 短信发送结果
     */
    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody Map<String, String> map, HttpSession session) {
        Long nowTime = Long.parseLong(map.get("nowTime"));
        Long codeTimeInSession = (Long) session.getAttribute(CODE_NOW_TIME);
        if (codeTimeInSession == null) {
            session.setAttribute(CODE_NOW_TIME, nowTime);
        } else {
            Long codeTimeInSystem = System.currentTimeMillis();
            Long returnSeconds = (codeTimeInSystem - codeTimeInSession) / 1000;
            if (returnSeconds < 60) {
                return R.error("重复接收验证码！" + (60 - returnSeconds) + "秒后重试！");
            }
            session.removeAttribute(CODE_NOW_TIME);
            session.setAttribute(CODE_NOW_TIME, nowTime);
        }
        String phone = map.get("phone");
        if (StringUtils.isNotEmpty(phone)) {
            String code = String.valueOf(ValidateCodeUtils.generateValidateCode(4));
            //SMSUtils.sendMessage("阿里云短信测试", "SMS_154950909", phone, code);
            session.setAttribute(phone, code);
            return R.success("手机验证码发送成功！（测试--验证码为：" + code + "）");
        }
        return R.error("手机验证码发送失败");
    }

    /**
     * 移动端用户登录
     *
     * @param map     获取到用户手机和验证码
     * @param session 用于校验验证码时获取session中的验证码
     * @return 返回登陆后的user
     */
    @PostMapping("/login")
    public R<User> login(@RequestBody Map<String, String> map, HttpSession session) {
        String phone = map.get("phone");
        String code = map.get("code");
        if (session.getAttribute(CODE_IN_ATTRIBUTE) != null && !session.getAttribute(CODE_IN_ATTRIBUTE).equals(code)) {
            return R.error("验证码错误！");
        }
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<User>();
        queryWrapper.eq(User::getPhone, phone);
        User one = userService.getOne(queryWrapper);
        if (one == null) {
            one = new User();
            one.setPhone(phone);
            one.setStatus(1);
            userService.save(one);
        }
        session.setAttribute("userId", one.getId());
        return R.success(one);
    }

    @PostMapping("/loginout")
    public R<String> loginout(HttpSession session) {
        session.removeAttribute("userId");
        return R.success("退出成功");
    }
}
