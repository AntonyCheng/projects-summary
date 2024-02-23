package top.sharehome.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.sharehome.reggie.common.CustomException;
import top.sharehome.reggie.dto.OrdersDto;
import top.sharehome.reggie.entity.*;
import top.sharehome.reggie.mapper.OrdersMapper;
import top.sharehome.reggie.service.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * 订单相关服务实现类
 *
 * @author AntonyCheng
 */
@Service
public class OrdersServiceImpl extends ServiceImpl<OrdersMapper, Orders> implements OrdersService {
    @Resource
    private ShoppingCartService shoppingCartService;
    @Resource
    private UserService userService;
    @Resource
    private AddressBookService addressBookService;
    @Resource
    private OrderDetailService orderDetailService;

    /**
     * 订单提交
     *
     * @param orders  订单信息
     * @param session 获取用户ID
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void postSubmit(Orders orders, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");

        List<ShoppingCart> shoppingCartList = shoppingCartService.list(new LambdaQueryWrapper<ShoppingCart>().eq(ShoppingCart::getUserId, userId));
        if (shoppingCartList == null || shoppingCartList.size() == 0) {
            throw new CustomException("购物车为空，不能下单！");
        }

        User user = userService.getById(userId);

        AddressBook addressBook = addressBookService.getById(orders.getAddressBookId());

        if (addressBook == null) {
            throw new CustomException("用户地址信息有误！不能下单！");
        }

        //组装订单数据
        long orderId = IdWorker.getId();

        AtomicInteger amount = new AtomicInteger(0);

        List<OrderDetail> orderDetailList = shoppingCartList.stream().map(shoppingCart -> {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrderId(orderId);
            orderDetail.setNumber(shoppingCart.getNumber());
            orderDetail.setDishFlavor(shoppingCart.getDishFlavor());
            orderDetail.setDishId(shoppingCart.getDishId());
            orderDetail.setSetmealId(shoppingCart.getSetmealId());
            orderDetail.setName(shoppingCart.getName());
            orderDetail.setImage(shoppingCart.getImage());
            orderDetail.setAmount(shoppingCart.getAmount());
            amount.addAndGet(shoppingCart.getAmount().multiply(new BigDecimal(shoppingCart.getNumber())).intValue());
            return orderDetail;
        }).collect(Collectors.toList());

        orders.setId(orderId);
        orders.setOrderTime(new Date(System.currentTimeMillis()));
        orders.setCheckoutTime(new Date(System.currentTimeMillis()));
        orders.setStatus(2);
        //总金额
        orders.setAmount(new BigDecimal(amount.get()));
        orders.setUserId(userId);
        orders.setNumber(String.valueOf(orderId));
        orders.setUserName(user.getName());
        orders.setConsignee(addressBook.getConsignee());
        orders.setPhone(addressBook.getPhone());
        orders.setAddress((addressBook.getProvinceName() == null ? "" : addressBook.getProvinceName())
                + (addressBook.getCityName() == null ? "" : addressBook.getCityName())
                + (addressBook.getDistrictName() == null ? "" : addressBook.getDistrictName())
                + (addressBook.getDetail() == null ? "" : addressBook.getDetail()));
        //向订单表插入数据，一条数据
        this.save(orders);
        orderDetailService.saveBatch(orderDetailList);

        long nowTime = System.currentTimeMillis();
        shoppingCartList = shoppingCartList.stream().peek(shoppingCart ->
                shoppingCart.setName(shoppingCart.getName() + "[" + nowTime + "]"))
                .collect(Collectors.toList());
        shoppingCartService.updateBatchById(shoppingCartList);

        shoppingCartService.remove(new LambdaQueryWrapper<ShoppingCart>().eq(ShoppingCart::getUserId, userId));
    }

    /**
     * 用于需要的页面回显购物车信息
     *
     * @param page     当前的页数
     * @param pageSize 每页的条数
     * @param session  获取用户ID
     * @return 返回封装好的购物车商品信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Page<OrdersDto> getUserPage(Integer page, Integer pageSize, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        Page<Orders> ordersPage = new Page<Orders>(page, pageSize);
        super.page(ordersPage, new LambdaQueryWrapper<Orders>().eq(Orders::getUserId, userId).orderByDesc(Orders::getOrderTime));

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

        return ordersDtoPage;
    }
}
