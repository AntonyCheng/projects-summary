package top.sharehome.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import top.sharehome.reggie.entity.ShoppingCart;

/**
 * 购物车相关Mapper
 *
 * @author AntonyCheng
 */
@Mapper
public interface ShoppingCartMapper extends BaseMapper<ShoppingCart> {
}