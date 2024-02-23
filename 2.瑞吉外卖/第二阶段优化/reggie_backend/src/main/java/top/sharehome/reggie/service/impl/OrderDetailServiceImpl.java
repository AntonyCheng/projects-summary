package top.sharehome.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.sharehome.reggie.entity.OrderDetail;
import top.sharehome.reggie.mapper.OrderDetailMapper;
import top.sharehome.reggie.service.OrderDetailService;

/**
 * 订单细节相关服务实现类
 *
 * @author AntonyCheng
 */
@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {

}
