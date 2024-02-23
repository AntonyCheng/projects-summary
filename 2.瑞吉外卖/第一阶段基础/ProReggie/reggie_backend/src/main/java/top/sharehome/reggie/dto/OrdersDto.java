package top.sharehome.reggie.dto;

import lombok.Data;
import top.sharehome.reggie.entity.OrderDetail;
import top.sharehome.reggie.entity.Orders;

import java.util.List;

/**
 * @Description
 * @Author:AntonyCheng
 * @CreateTime:2023/2/6 18:17
 */
@Data
public class OrdersDto extends Orders {
    List<OrderDetail> orderDetails;
}
