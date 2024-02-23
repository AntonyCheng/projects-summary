package top.sharehome.usercenter.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import top.sharehome.usercenter.model.entity.User;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}