package top.sharehome.reggie.service;

import top.sharehome.reggie.entity.Orders;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpSession;

public interface OrdersService extends IService<Orders>{

    void submit(Orders orders, HttpSession session);
}
