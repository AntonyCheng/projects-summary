package top.sharehome.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.sharehome.reggie.entity.User;
import top.sharehome.reggie.mapper.UserMapper;
import top.sharehome.reggie.service.UserService;

/**
 * 用户相关服务实现类
 *
 * @author AntonyCheng
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}

