package top.sharehome.usercenter.service;

import com.baomidou.mybatisplus.extension.service.IService;
import top.sharehome.usercenter.model.dto.UserLoginDto;
import top.sharehome.usercenter.model.entity.User;

import javax.servlet.http.HttpSession;

/**
 * 用户服务
 *
 * @author admin
 */
public interface UserService extends IService<User> {

    /**
     * 用户注册
     *
     * @param checkUser     注册的用户信息
     * @param checkPassword 用于检验用户二次重复的密码
     * @return 新用户Id
     */
    Long userRegister(User checkUser, String checkPassword);

    /**
     * 用户登录
     *
     * @param loginUser 登陆的用户信息
     * @param session   存放用户登录状态
     * @return 返回脱敏后的用户信息
     */
    UserLoginDto userLogin(User loginUser, HttpSession session);

    /**
     * 用户注销
     *
     * @param session 用户登录状态
     */
    Boolean userLogout(HttpSession session);
}
