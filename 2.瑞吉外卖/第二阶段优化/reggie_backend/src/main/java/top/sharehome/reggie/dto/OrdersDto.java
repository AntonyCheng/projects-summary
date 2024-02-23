package top.sharehome.reggie.dto;

import lombok.Data;
import top.sharehome.reggie.entity.OrderDetail;
import top.sharehome.reggie.entity.Orders;

import java.util.List;

/**
 * 订单数据传输对象
 *
 * @author AntonyCheng
 */
@Data
public class OrdersDto extends Orders {
    /**
     * 订单细节信息
     */
    List<OrderDetail> orderDetails;

}
