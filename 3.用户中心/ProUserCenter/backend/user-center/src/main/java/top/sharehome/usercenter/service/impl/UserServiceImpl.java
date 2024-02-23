package top.sharehome.usercenter.service.impl;

import cn.hutool.core.util.ReUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.sharehome.usercenter.common.R;
import top.sharehome.usercenter.common.RCodeEnum;
import top.sharehome.usercenter.constant.UserConstant;
import top.sharehome.usercenter.exception.customize.CustomizeReturnException;
import top.sharehome.usercenter.mapper.UserMapper;
import top.sharehome.usercenter.model.dto.UserLoginDto;
import top.sharehome.usercenter.model.entity.User;
import top.sharehome.usercenter.service.UserService;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

/**
 * @author admin
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Resource
    private UserMapper userMapper;
    private static final Long LIMIT_ACCOUNT_LENGTH = 4L;

    private static final Long LIMIT_PASSWORD_LENGTH = 8L;

    private static final String MATCHER_PASSWORD_REGEX = "^[\u4E00-\u9FA5A-Za-z0-9]{2,20}$";

    private static final String PASSWORD_SALT = "PASSWORD_SALT";

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long userRegister(User checkUser, String checkPassword) {
        // 各种校验
        if (!checkUser.getUserPassword().equals(checkPassword)) {
            throw new CustomizeReturnException(
                    R.failure(RCodeEnum.DUPLICATE_PASSWORDS_ARE_INCONSISTENT),
                    "用户的密码和校验密码不一致");
        }

        if (StringUtils.isAnyBlank(checkPassword, checkUser.getUserPassword(), checkUser.getUserAccount())) {
            throw new CustomizeReturnException(
                    R.failure(RCodeEnum.REQUEST_REQUIRED_PARAMETER_IS_EMPTY),
                    "用户的账户或者密码或者校验密码为空");
        }

        if (checkUser.getUserAccount().length() < LIMIT_ACCOUNT_LENGTH) {
            throw new CustomizeReturnException(
                    R.failure(RCodeEnum.USERNAME_LENGTH_IS_NOT_ENOUGH),
                    "用户账户长度不满足" + LIMIT_ACCOUNT_LENGTH + "位");
        }

        if (checkUser.getUserPassword().length() < LIMIT_PASSWORD_LENGTH || checkPassword.length() < LIMIT_PASSWORD_LENGTH) {
            throw new CustomizeReturnException(
                    R.failure(RCodeEnum.PASSWORD_LENGTH_IS_NOT_ENOUGH),
                    "用户密码或者校验密码长度不满足" + LIMIT_PASSWORD_LENGTH + "位");
        }

        if (!ReUtil.isMatch(MATCHER_PASSWORD_REGEX, checkUser.getUserAccount())) {
            throw new CustomizeReturnException(
                    R.failure(RCodeEnum.USERNAME_CONTAINS_SPECIAL_CHARACTERS),
                    "用户的账号中存在特殊字符，已经被正则表达式过滤");
        }

        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUserAccount, checkUser.getUserAccount());
        long count = userMapper.selectCount(queryWrapper);
        if (count > 0) {
            throw new CustomizeReturnException(
                    R.failure(RCodeEnum.USERNAME_ALREADY_EXISTS),
                    "该用户账户已经存在于数据库中");
        }

        // 密码加密，一定要给加密内容加盐，为了避免注册时前端数据泄露
        checkUser.setUserPassword(DigestUtil.md5Hex(checkUser.getUserPassword() + PASSWORD_SALT));

        // 保存该注册用户
        long saveResult = userMapper.insert(checkUser);
        if (saveResult == 0) {
            throw new CustomizeReturnException(R.failure(
                    RCodeEnum.ERRORS_OCCURRED_IN_THE_DATABASE_SERVICE),
                    "保存该用户，从数据库返回的影响行数为0，且在之前没有报出异常");
        }
        // 返回注册结果
        return saveResult;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserLoginDto userLogin(User loginUser, HttpSession session) {
        // 各种校验
        if (StringUtils.isAnyBlank(loginUser.getUserPassword(), loginUser.getUserAccount()) || session == null) {
            throw new CustomizeReturnException(
                    R.failure(RCodeEnum.REQUEST_REQUIRED_PARAMETER_IS_EMPTY),
                    "用户的账户或者密码为null 或者 session传送到service层后为null");
        }

        if (loginUser.getUserAccount().length() < LIMIT_ACCOUNT_LENGTH) {
            throw new CustomizeReturnException(
                    R.failure(RCodeEnum.USERNAME_LENGTH_IS_NOT_ENOUGH),
                    "用户账户长度不满足" + LIMIT_ACCOUNT_LENGTH + "位");
        }

        if (loginUser.getUserPassword().length() < LIMIT_PASSWORD_LENGTH) {
            throw new CustomizeReturnException(
                    R.failure(RCodeEnum.PASSWORD_LENGTH_IS_NOT_ENOUGH),
                    "用户密码或者校验密码长度不满足" + LIMIT_PASSWORD_LENGTH + "位");
        }

        if (!ReUtil.isMatch(MATCHER_PASSWORD_REGEX, loginUser.getUserAccount())) {
            throw new CustomizeReturnException(
                    R.failure(RCodeEnum.USERNAME_CONTAINS_SPECIAL_CHARACTERS),
                    "用户的账号中存在特殊字符，已经被正则表达式过滤");
        }

        // 密码加密，一定要给加密内容加盐，为了避免注册时前端数据泄露
        loginUser.setUserPassword(DigestUtil.md5Hex(loginUser.getUserPassword() + PASSWORD_SALT));

        // 查询账号密码是否匹配并返回匹配结果
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUserAccount, loginUser.getUserAccount());
        User user = userMapper.selectOne(queryWrapper);
        if (user == null) {
            throw new CustomizeReturnException(
                    R.failure(RCodeEnum.USER_ACCOUNT_DOES_NOT_EXIST),
                    "按用户名查询结果为空，用户不存在");
        }

        if (!user.getUserPassword().equals(loginUser.getUserPassword())) {
            throw new CustomizeReturnException(
                    R.failure(RCodeEnum.WRONG_USER_PASSWORD),
                    "用户输入的密码和数据库中密码不匹配");
        }

        // 使用DTO对象进行数据脱敏
        UserLoginDto userLoginDto = new UserLoginDto();
        BeanUtils.copyProperties(user, userLoginDto, User.COL_USER_PASSWORD, User.COL_CREATE_TIME, User.COL_UPDATE_TIME, User.COL_IS_DELETED);

        // 存储对象DTO状态
        session.setAttribute(UserConstant.USER_LOGIN_STATE, userLoginDto);

        return userLoginDto;
    }

    @Override
    public Boolean userLogout(HttpSession session) {
        if (session == null) {
            throw new CustomizeReturnException(
                    R.failure(RCodeEnum.REQUEST_REQUIRED_PARAMETER_IS_EMPTY)
                    , "session传送到service层后为null");
        }
        session.removeAttribute(UserConstant.USER_LOGIN_STATE);
        return true;
    }
}
