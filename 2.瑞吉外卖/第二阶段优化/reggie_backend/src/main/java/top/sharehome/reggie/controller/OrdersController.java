package top.sharehome.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import top.sharehome.reggie.common.R;
import top.sharehome.reggie.dto.OrdersDto;
import top.sharehome.reggie.entity.Orders;
import top.sharehome.reggie.service.OrdersService;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.Date;

/**
 * 订单操作相关接口
 *
 * @author AntonyCheng
 */
@RestController
@RequestMapping("/order")
@Slf4j
public class OrdersController {
    @Resource
    private OrdersService ordersService;

    /**
     * 用于提交订单
     *
     * @param orders  订单的部分信息
     * @param session 用于获取用户ID
     * @return 返回提交信息
     */
    @PostMapping("/submit")
    public R<String> submit(@RequestBody Orders orders, HttpSession session) {
        ordersService.postSubmit(orders, session);
        return R.success("下单成功！");
    }

    /**
     * 用于需要的页面回显购物车信息
     *
     * @param page     当前的页数
     * @param pageSize 每页的条数
     * @param session  获取用户ID
     * @return 返回封装好的购物车商品信息
     */
    @GetMapping("/userPage")
    public R<Page<OrdersDto>> userPage(@RequestParam("page") Integer page, @RequestParam("pageSize") Integer pageSize, HttpSession session) {
        Page<OrdersDto> ordersDtoPage = ordersService.getUserPage(page, pageSize, session);
        return R.success(ordersDtoPage);
    }

    /**
     * 后台订单的分页与搜索
     *
     * @param page      当前页
     * @param pageSize  页面项数目
     * @param number    模糊查询订单号
     * @param beginTime 查询范围开始时间
     * @param endTime   查询范围结束时间
     * @return 返回页面信息
     */
    @GetMapping("/page")
    public R<Page<Orders>> page(@RequestParam("page") Integer page,
                                @RequestParam("pageSize") Integer pageSize,
                                @RequestParam(value = "number", required = false) Long number,
                                @RequestParam(value = "beginTime", required = false)
                                @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
                                @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date beginTime,
                                @RequestParam(value = "endTime", required = false)
                                @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
                                @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date endTime) {
        Page<Orders> ordersPage = new Page<>(page, pageSize);
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<Orders>();
        queryWrapper.like(number != null, Orders::getNumber, number);
        queryWrapper.ge(beginTime != null, Orders::getOrderTime, beginTime);
        queryWrapper.le(endTime != null, Orders::getOrderTime, endTime);
        ordersService.page(ordersPage, queryWrapper);
        return R.success(ordersPage);
    }

    /**
     * 更改订单的状态
     *
     * @param orders 订单部分信息
     * @return 返回订单修改状态信息
     */
    @PutMapping("")
    public R<String> status(@RequestBody Orders orders) {
        LambdaUpdateWrapper<Orders> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(Orders::getStatus, orders.getStatus());
        updateWrapper.eq(Orders::getId, orders.getId());
        ordersService.update(updateWrapper);
        return R.success("修改状态成功！");
    }
}
