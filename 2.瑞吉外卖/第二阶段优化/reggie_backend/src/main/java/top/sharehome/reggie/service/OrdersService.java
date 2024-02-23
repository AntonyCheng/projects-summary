package top.sharehome.reggie.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import top.sharehome.reggie.dto.OrdersDto;
import top.sharehome.reggie.entity.Orders;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpSession;

/**
 * 订单相关服务类
 *
 * @author AntonyCheng
 */
public interface OrdersService extends IService<Orders> {

    void postSubmit(Orders orders, HttpSession session);

    Page<OrdersDto> getUserPage(Integer page, Integer pageSize, HttpSession session);

}
