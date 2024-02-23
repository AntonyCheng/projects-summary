package top.sharehome.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;
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
import java.util.concurrent.TimeUnit;

/**
 * 用户操作相关接口
 *
 * @author AntonyCheng
 */
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {
    @Resource
    private UserService userService;

    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    /**
     * 发送手机短信验证码
     *
     * @param map 封装好phone和当前时间戳的user对象
     * @return 短信发送结果
     */
    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody Map<String, String> map) {
        String phone = map.get("phone");
        String nowTime = map.get("nowTime");
        String codeTimeInRedis = (String) redisTemplate.opsForValue().get("code_repeat_time_" + phone);
        if (codeTimeInRedis == null) {
            redisTemplate.opsForValue().set("code_repeat_time_" + phone, nowTime);
        } else {
            long codeTimeInSystem = System.currentTimeMillis();
            long returnSeconds = (codeTimeInSystem - Long.parseLong(codeTimeInRedis)) / 1000;
            if (returnSeconds < 60) {
                return R.error("重复接收验证码！" + (60 - returnSeconds) + "秒后重试！");
            }
            redisTemplate.opsForValue().set("code_repeat_time_" + phone, nowTime);
        }
        if (StringUtils.isNotEmpty(phone)) {
            String code = String.valueOf(ValidateCodeUtils.generateValidateCode(4));
            //SMSUtils.sendMessage("阿里云短信测试", "SMS_154950909", phone, code);
            redisTemplate.opsForValue().set("code_of_phone_" + phone, code, 5, TimeUnit.MINUTES);
            redisTemplate.opsForValue().set("code_time_out_redis_" + phone, nowTime, 1, TimeUnit.MINUTES);
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
    @Transactional(rollbackFor = Exception.class)
    public R<User> login(@RequestBody Map<String, String> map, HttpSession session) {
        String phone = map.get("phone");
        String codeInFront = map.get("code");
        String code = (String) redisTemplate.opsForValue().get("code_of_phone_" + phone);
        if (code == null) {
            return R.error("验证码已超时！请重新获取！");
        }
        if (!code.equals(codeInFront)) {
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
        redisTemplate.delete("code_of_phone_" + phone);
        redisTemplate.delete("code_repeat_time_" + phone);
        redisTemplate.delete("code_time_out_redis_" + phone);
        return R.success(one);
    }

    /**
     * 用户退出系统
     *
     * @param session 获取用户的userId
     * @return 返回退出结果
     */
    @PostMapping("/loginout")
    public R<String> loginout(HttpSession session) {
        session.removeAttribute("userId");
        return R.success("退出成功");
    }
}
