package top.sharehome.usercenter.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;
import top.sharehome.usercenter.common.R;
import top.sharehome.usercenter.common.RCodeEnum;
import top.sharehome.usercenter.constant.UserConstant;
import top.sharehome.usercenter.exception.customize.CustomizeReturnException;
import top.sharehome.usercenter.model.dto.UserLoginDto;
import top.sharehome.usercenter.model.entity.User;
import top.sharehome.usercenter.model.request.UserLoginRequest;
import top.sharehome.usercenter.model.request.UserRegisterRequest;
import top.sharehome.usercenter.service.UserService;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @author admin
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    private UserService userService;


    @PostMapping("/register")
    public R<String> register(@RequestBody UserRegisterRequest userRegisterRequest) {
        if (userRegisterRequest == null) {
            throw new CustomizeReturnException(
                    R.failure(RCodeEnum.REQUEST_REQUIRED_PARAMETER_IS_EMPTY),
                    "前端传来的参数为null");
        }
        if (StringUtils.isAnyBlank(userRegisterRequest.getUserAccount(), userRegisterRequest.getUserPassword(), userRegisterRequest.getCheckPassword())) {
            throw new CustomizeReturnException(
                    R.failure(RCodeEnum.REQUEST_REQUIRED_PARAMETER_IS_EMPTY),
                    "用户的账户或密码或校验密码为空串");
        }
        String checkPassword = userRegisterRequest.getCheckPassword();
        User user = new User();
        user.setUserAccount(userRegisterRequest.getUserAccount());
        user.setUserPassword(userRegisterRequest.getUserPassword());
        Long affectsRow = userService.userRegister(user, checkPassword);
        if (affectsRow == 0) {
            throw new CustomizeReturnException(
                    R.failure(RCodeEnum.USERNAME_ALREADY_EXISTS),
                    "该用户账户在数据库中已经存在");
        }
        return R.success("注册成功！");
    }

    @PostMapping("/login")
    public R<UserLoginDto> login(@RequestBody UserLoginRequest userLoginRequest, HttpSession session) {
        if (userLoginRequest == null) {
            throw new CustomizeReturnException(
                    R.failure(RCodeEnum.REQUEST_REQUIRED_PARAMETER_IS_EMPTY),
                    "前端传来的参数为null");
        }
        if (StringUtils.isAnyBlank(userLoginRequest.getUserAccount(), userLoginRequest.getUserPassword())) {
            throw new CustomizeReturnException(
                    R.failure(RCodeEnum.REQUEST_REQUIRED_PARAMETER_IS_EMPTY),
                    "用户的账户或密码为空串");
        }
        User user = new User();
        user.setUserAccount(userLoginRequest.getUserAccount());
        user.setUserPassword(userLoginRequest.getUserPassword());
        UserLoginDto userLoginDto = userService.userLogin(user, session);
        return R.success(userLoginDto, "登陆成功！");
    }

    @GetMapping("/search")
    public R<List<User>> search(@RequestParam(value = "userName", required = false) String userName, HttpSession session) {
        if (session == null) {
            throw new CustomizeReturnException(
                    R.failure(RCodeEnum.REQUEST_REQUIRED_PARAMETER_IS_EMPTY),
                    "前端传来的参数为null");
        }
        UserLoginDto userLoginDto = (UserLoginDto) session.getAttribute(UserConstant.USER_LOGIN_STATE);
        if (userLoginDto == null || userLoginDto.getUserRole() != UserConstant.ADMIN_ROLE) {
            throw new CustomizeReturnException(
                    R.failure(RCodeEnum.ACCESS_UNAUTHORIZED),
                    "操作用户不是管理员权限");
        }
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(userName != null, User::getUserName, userName);
        List<User> users = userService.list(queryWrapper);
        return R.success(users);
    }

    @DeleteMapping("/delete")
    public R<String> delete(@RequestBody Long id, HttpSession session) {
        if (id == null || session == null) {
            throw new CustomizeReturnException(
                    R.failure(RCodeEnum.REQUEST_REQUIRED_PARAMETER_IS_EMPTY),
                    "前端传来的参数为null");
        }
        if (id < 0) {
            throw new CustomizeReturnException(
                    R.failure(RCodeEnum.PARAMETER_FORMAT_MISMATCH),
                    "用户id为负，参数无效");
        }
        UserLoginDto userLoginDto = (UserLoginDto) session.getAttribute(UserConstant.USER_LOGIN_STATE);
        if (userLoginDto == null || userLoginDto.getUserRole() != UserConstant.ADMIN_ROLE) {
            throw new CustomizeReturnException(
                    R.failure(RCodeEnum.ACCESS_UNAUTHORIZED),
                    "操作用户不是管理员权限");
        }
        if (userLoginDto.getId().equals(id)) {
            throw new CustomizeReturnException(
                    R.failure(RCodeEnum.CAN_NOT_DELETE_YOURSELF),
                    "操作用户不能删除自己的信息");
        }
        boolean deleteResult = userService.removeById(id);
        if (!deleteResult) {
            throw new CustomizeReturnException(R.failure(
                    RCodeEnum.ERRORS_OCCURRED_IN_THE_DATABASE_SERVICE),
                    "删除该用户，得到的结果为false，且在之前没有报出异常");
        }
        return R.success("删除成功！");
    }

    @GetMapping("/current")
    public R<UserLoginDto> current(HttpSession session) {
        if (session == null) {
            throw new CustomizeReturnException(
                    R.failure(RCodeEnum.REQUEST_REQUIRED_PARAMETER_IS_EMPTY),
                    "前端传来的参数为null");
        }
        UserLoginDto currentUser = (UserLoginDto) session.getAttribute(UserConstant.USER_LOGIN_STATE);
        if (currentUser == null) {
            throw new CustomizeReturnException(
                    R.failure(RCodeEnum.ACCESS_UNAUTHORIZED),
                    "用户状态不存在，没有权限继续操作");
        }
        Long userId = currentUser.getId();
        User userInBackend = userService.getById(userId);
        if (userInBackend == null) {
            throw new CustomizeReturnException(
                    R.failure(RCodeEnum.USER_ACCOUNT_DOES_NOT_EXIST),
                    "用户在数据库中不存在");
        }
        if (userInBackend.getUserStates() != 0) {
            throw new CustomizeReturnException(
                    R.failure(RCodeEnum.USER_ACCOUNT_BANNED),
                    "用户已经被封禁");
        }
        UserLoginDto userLoginDto = new UserLoginDto();
        BeanUtils.copyProperties(userInBackend, userLoginDto, User.COL_USER_PASSWORD, User.COL_CREATE_TIME, User.COL_UPDATE_TIME, User.COL_IS_DELETED);
        return R.success(userLoginDto);
    }

    @PostMapping("/logout")
    public R<String> userLogout(HttpSession session) {
        if (session == null) {
            throw new CustomizeReturnException(
                    R.failure(RCodeEnum.REQUEST_REQUIRED_PARAMETER_IS_EMPTY),
                    "前端传来的参数为null");
        }
        userService.userLogout(session);
        return R.success("退出成功！");
    }
}
