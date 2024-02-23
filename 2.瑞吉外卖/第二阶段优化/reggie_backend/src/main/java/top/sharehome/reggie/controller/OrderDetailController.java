package top.sharehome.reggie.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.sharehome.reggie.service.OrderDetailService;

import javax.annotation.Resource;

/**
 * 订单细节操作相关接口
 *
 * @author AntonyCheng
 */
@RestController
@RequestMapping("")
@Slf4j
public class OrderDetailController {
    @Resource
    private OrderDetailService orderDetailService;
}
