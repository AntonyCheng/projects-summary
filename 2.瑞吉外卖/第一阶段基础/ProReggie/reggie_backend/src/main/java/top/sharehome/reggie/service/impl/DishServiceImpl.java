package top.sharehome.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.sharehome.reggie.dto.DishDto;
import top.sharehome.reggie.entity.Dish;
import top.sharehome.reggie.entity.DishFlavor;
import top.sharehome.reggie.mapper.DishMapper;
import top.sharehome.reggie.service.DishFlavorService;
import top.sharehome.reggie.service.DishService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {
    @Resource
    private DishFlavorService dishFlavorService;

    /**
     * 保存菜品的基本信息并且保存菜品喜好关系表
     *
     * @param dishDto
     */
    @Override
    @Transactional
    public void saveWithFlavor(DishDto dishDto) {
        // 保存菜品的基本信息到菜品表
        this.save(dishDto);
        Long dishId = dishDto.getId();

        // 给喜好添加dishId
        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors = flavors.stream().map((item) -> {
            item.setDishId(dishId);
            return item;
        }).collect(Collectors.toList());

        // 保存喜好
        dishFlavorService.saveBatch(flavors);
    }

    /**
     * 删除菜品的基本信息并且保存菜品喜好关系表
     * @param dishIds
     */
    @Override
    @Transactional
    public void deleteWithFlavor(List<String> dishIds) {
        dishIds.forEach(dishId -> {
            LambdaUpdateWrapper<Dish> updateWrapper = new LambdaUpdateWrapper<Dish>();
            updateWrapper.set(Dish::getName, super.getById(dishId).getName() + "[" + System.currentTimeMillis() + "]");
            updateWrapper.eq(Dish::getId, dishId);
            super.update(updateWrapper);
        });
        super.removeByIds(dishIds);
        dishIds.forEach(dishId -> {
            LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<DishFlavor>();
            queryWrapper.eq(DishFlavor::getDishId, dishId);
            dishFlavorService.remove(queryWrapper);
        });
    }

    /**
     * 根据id查询菜品和对应的口味，用于修改回显
     *
     * @param id 传入菜品id
     * @return DishDto
     */
    @Override
    @Transactional
    public DishDto getByIdWithFlavor(Long id) {
        // 由dishId获取到dish，再将dish内容拷贝到一个新的dishDto对象中
        // 此时dishDto对象还差一个口味List，其他属性在回显时不起作用，所以不做考虑
        Dish dish = super.getById(id);
        DishDto dishDto = new DishDto();
        BeanUtils.copyProperties(dish, dishDto);
        // 由dishId在菜品口味表中查询对应的口味，随后赋值给dishDto对象
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<DishFlavor>();
        queryWrapper.eq(DishFlavor::getDishId, dish.getId());
        List<DishFlavor> flavors = dishFlavorService.list(queryWrapper);
        dishDto.setFlavors(flavors);
        return dishDto;
    }

    /**
     * 修改菜品，思路：先保存菜品表，然后删除原有的菜品口味关系表相关数据，随后向菜品口味关系表中插入新数据
     * 此处有一个逻辑冲突，那就是我们使用的是逻辑删除，主键唯一，如果按照上面的思路，逻辑删除后再插入就会报异常说主键重复
     * 那么下面就构建了一种解决思路，可以先将主键修改，然后将修改后的主键进行逻辑删除，然后按照修改前的主键进行插入
     *
     * @param dishDto
     */
    @Override
    @Transactional
    public void updateWithFlavor(DishDto dishDto) {
        // 修改菜品表
        super.updateById(dishDto);
        // 将口味表中对应菜品Id的口味Id添加上时间因素
        // 由于时间变化极快，我们需要保存修改前后的口味数据id
        List<DishFlavor> dishFlavors = dishFlavorService.list(new LambdaQueryWrapper<DishFlavor>().eq(DishFlavor::getDishId, dishDto.getId()));
        List<Long> oldIds = new ArrayList<Long>();
        List<Long> newIds = new ArrayList<Long>();
        dishFlavors.forEach(dishFlavor -> {
            oldIds.add(dishFlavor.getId());
            long id = dishFlavor.getId() + System.currentTimeMillis();
            newIds.add(id);
        });
        // 对id进行新旧调换
        for (int i = 0; i < oldIds.size(); i++) {
            dishFlavorService.update(new LambdaUpdateWrapper<DishFlavor>().set(DishFlavor::getId, newIds.get(i)).eq(DishFlavor::getId, oldIds.get(i)));
        }
        // 将对应菜品id的数据进行逻辑删除
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<DishFlavor>();
        queryWrapper.eq(DishFlavor::getDishId, dishDto.getId());
        dishFlavorService.remove(queryWrapper);
        // 由于前面旧的口味id已经发生了变化，这里插入旧的口味id就不会报主键重复异常
        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors = flavors.stream().map(flavor -> {
            flavor.setDishId(dishDto.getId());
            return flavor;
        }).collect(Collectors.toList());
        dishFlavorService.saveBatch(flavors);
    }
}
