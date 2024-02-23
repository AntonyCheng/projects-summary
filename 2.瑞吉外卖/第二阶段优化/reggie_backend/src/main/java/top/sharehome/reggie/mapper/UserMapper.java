package top.sharehome.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import top.sharehome.reggie.entity.User;

/**
 * 用户相关Mapper
 *
 * @author AntonyCheng
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}