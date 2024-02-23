package top.sharehome.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.sharehome.reggie.dto.SetmealDto;
import top.sharehome.reggie.entity.Setmeal;
import top.sharehome.reggie.entity.SetmealDish;
import top.sharehome.reggie.mapper.SetmealMapper;
import top.sharehome.reggie.service.CategoryService;
import top.sharehome.reggie.service.DishService;
import top.sharehome.reggie.service.SetmealDishService;
import top.sharehome.reggie.service.SetmealService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {
    @Resource
    private CategoryService categoryService;
    @Resource
    private SetmealDishService setmealDishService;
    @Resource
    private DishService dishService;

    @Resource
    private SetmealService setmealService;

    /**
     * 保存套餐的基本信息并且保存套餐菜品关系表
     *
     * @param setmealDto 套餐菜品表
     */
    @Override
    @Transactional
    public void saveWithDish(SetmealDto setmealDto) {
        super.save(setmealDto);
        Long setmealId = setmealDto.getId();
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        setmealDishes = setmealDishes.stream().map(setmealDish -> {
            setmealDish.setSetmealId(setmealId);
            setmealDish.setSort(dishService.getById(setmealDish.getDishId()).getSort());
            return setmealDish;
        }).collect(Collectors.toList());
        setmealDishService.saveBatch(setmealDishes);
    }

    /**
     * 删除套餐的基本信息并且保存套餐菜品关系表
     *
     * @param ids 需要删除的套餐id
     */
    @Override
    public void deleteWithDish(List<String> ids) {
        ids.forEach(id -> {
            LambdaUpdateWrapper<Setmeal> updateWrapper = new LambdaUpdateWrapper<Setmeal>();
            updateWrapper.set(Setmeal::getName, setmealService.getById(id).getName() + "[" + System.currentTimeMillis() + "]");
            updateWrapper.eq(Setmeal::getId, id);
            setmealService.update(updateWrapper);
        });
        super.removeByIds(ids);
        ids.forEach(id -> {
            LambdaUpdateWrapper<SetmealDish> updateWrapper = new LambdaUpdateWrapper<SetmealDish>();
            updateWrapper.eq(SetmealDish::getSetmealId, id);
            setmealDishService.remove(updateWrapper);
        });
    }

    /**
     * 根据id查询套餐和对应的菜品，用于修改回显
     *
     * @param id 传入套餐id
     * @return SetmealDto
     */
    @Override
    public SetmealDto getByIdWithDish(Long id) {
        Setmeal setmeal = super.getById(id);
        SetmealDto setmealDto = new SetmealDto();
        BeanUtils.copyProperties(setmeal, setmealDto);
        setmealDto.setCategoryName(categoryService.getById(super.getById(setmealDto.getId()).getCategoryId()).getName());
        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<SetmealDish>();
        queryWrapper.eq(SetmealDish::getSetmealId, setmealDto.getId());
        List<SetmealDish> setmealDishList = setmealDishService.list(queryWrapper);
        setmealDto.setSetmealDishes(setmealDishList);
        return setmealDto;
    }

    /**
     * 修改套餐，思路：先保存套餐表，然后删除原有的套餐菜品关系表相关数据，随后向套餐菜品关系表中插入新数据
     * 此处有一个逻辑冲突，那就是我们使用的是逻辑删除，主键唯一，如果按照上面的思路，逻辑删除后再插入就会报异常说主键重复
     * 那么下面就构建了一种解决思路，可以先将主键修改，然后将修改后的主键进行逻辑删除，然后按照修改前的主键进行插入
     *
     * @param setmealDto
     */
    @Override
    public void updateWithDish(SetmealDto setmealDto) {
        super.updateById(setmealDto);
        List<SetmealDish> setmealDishes = setmealDishService.list(new LambdaUpdateWrapper<SetmealDish>().eq(SetmealDish::getSetmealId, setmealDto.getId()));
        ArrayList<Long> oldIds = new ArrayList<>();
        ArrayList<Long> newIds = new ArrayList<>();
        setmealDishes.forEach(setmealDish -> {
            oldIds.add(setmealDish.getId());
            long id = setmealDish.getId() + System.currentTimeMillis();
            newIds.add(id);
        });
        for (int i = 0; i < oldIds.size(); i++) {
            setmealDishService.update(new LambdaUpdateWrapper<SetmealDish>().set(SetmealDish::getId, newIds.get(i)).eq(SetmealDish::getId, oldIds.get(i)));
        }
        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<SetmealDish>();
        queryWrapper.eq(SetmealDish::getSetmealId, setmealDto.getId());
        setmealDishService.remove(queryWrapper);
        List<SetmealDish> setmealDishList = setmealDto.getSetmealDishes();
        setmealDishList = setmealDishList.stream().map(setmealDish -> {
            setmealDish.setSetmealId(setmealDto.getId());
            return setmealDish;
        }).collect(Collectors.toList());
        setmealDishService.saveBatch(setmealDishList);
    }
}
