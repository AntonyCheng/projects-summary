package top.sharehome.reggie.service.impl;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import top.sharehome.reggie.entity.ShoppingCart;
import top.sharehome.reggie.mapper.ShoppingCartMapper;
import top.sharehome.reggie.service.ShoppingCartService;

@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {

}


