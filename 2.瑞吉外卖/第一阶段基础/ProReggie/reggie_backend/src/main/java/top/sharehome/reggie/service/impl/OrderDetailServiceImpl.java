package top.sharehome.reggie.service.impl;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import top.sharehome.reggie.mapper.OrderDetailMapper;
import top.sharehome.reggie.entity.OrderDetail;
import top.sharehome.reggie.service.OrderDetailService;
@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService{

}
