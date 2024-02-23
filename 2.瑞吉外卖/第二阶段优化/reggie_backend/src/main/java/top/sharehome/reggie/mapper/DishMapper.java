package top.sharehome.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import top.sharehome.reggie.entity.Dish;

/**
 * 菜品相关Mapper
 *
 * @author AntonyCheng
 */
@Mapper
public interface DishMapper extends BaseMapper<Dish> {
}