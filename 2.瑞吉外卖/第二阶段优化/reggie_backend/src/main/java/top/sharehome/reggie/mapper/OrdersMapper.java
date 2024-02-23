package top.sharehome.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import top.sharehome.reggie.entity.Orders;

/**
 * 订单相关Mapper
 *
 * @author AntonyCheng
 */
@Mapper
public interface OrdersMapper extends BaseMapper<Orders> {
}