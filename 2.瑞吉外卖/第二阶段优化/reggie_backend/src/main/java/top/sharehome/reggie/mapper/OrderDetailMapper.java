package top.sharehome.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import top.sharehome.reggie.entity.OrderDetail;

/**
 * 订单细节相关Mapper
 *
 * @author AntonyCheng
 */
@Mapper
public interface OrderDetailMapper extends BaseMapper<OrderDetail> {
}