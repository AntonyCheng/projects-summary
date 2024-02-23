package top.sharehome.reggie.service.impl;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import top.sharehome.reggie.entity.DishFlavor;
import top.sharehome.reggie.mapper.DishFlavorMapper;
import top.sharehome.reggie.service.DishFlavorService;
@Service
public class DishFlavorServiceImpl extends ServiceImpl<DishFlavorMapper, DishFlavor> implements DishFlavorService{

}
