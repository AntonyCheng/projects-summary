package top.sharehome.reggie.service.impl;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import top.sharehome.reggie.entity.User;
import top.sharehome.reggie.mapper.UserMapper;
import top.sharehome.reggie.service.UserService;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}

