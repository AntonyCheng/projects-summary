package top.sharehome.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;
import top.sharehome.reggie.common.R;
import top.sharehome.reggie.dto.OrdersDto;
import top.sharehome.reggie.entity.OrderDetail;
import top.sharehome.reggie.entity.Orders;
import top.sharehome.reggie.service.OrderDetailService;
import top.sharehome.reggie.service.OrdersService;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description
 * @Author:AntonyCheng
 * @CreateTime:2023/2/2 23:43
 */
@RestController
@RequestMapping("/order")
@Slf4j
public class OrdersController {
    @Resource
    private OrdersService ordersService;

    @Resource
    private OrderDetailService orderDetailService;

    @PostMapping("/submit")
    public R<String> submit(@RequestBody Orders orders, HttpSession session) {
        ordersService.submit(orders, session);
        return R.success("下单成功！");
    }

    @GetMapping("/userPage")
    public R<Page<OrdersDto>> userPage(@RequestParam("page") Integer page, @RequestParam("pageSize") Integer pageSize, HttpSession session) {

        Long userId = (Long) session.getAttribute("userId");
        Page<Orders> ordersPage = new Page<Orders>(page, pageSize);
        ordersService.page(ordersPage, new LambdaQueryWrapper<Orders>().eq(Orders::getUserId, userId).orderByDesc(Orders::getOrderTime));

        Page<OrdersDto> ordersDtoPage = new Page<OrdersDto>(page, pageSize);
        BeanUtils.copyProperties(ordersPage, ordersDtoPage, "records");

        List<Orders> ordersList = ordersPage.getRecords();
        List<OrdersDto> ordersDtoList = ordersList.stream().map(orders -> {
            OrdersDto ordersDto = new OrdersDto();
            BeanUtils.copyProperties(orders, ordersDto);
            Long ordersId = orders.getId();
            List<OrderDetail> orderDetailList = orderDetailService.list(new LambdaQueryWrapper<OrderDetail>().eq(OrderDetail::getOrderId, ordersId));
            ordersDto.setOrderDetails(orderDetailList);
            return ordersDto;
        }).collect(Collectors.toList());

        ordersDtoPage.setRecords(ordersDtoList);
        return R.success(ordersDtoPage);
    }
}
