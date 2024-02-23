package top.sharehome.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.sharehome.reggie.common.CustomException;
import top.sharehome.reggie.entity.Category;
import top.sharehome.reggie.entity.Dish;
import top.sharehome.reggie.entity.Setmeal;
import top.sharehome.reggie.mapper.CategoryMapper;
import top.sharehome.reggie.service.CategoryService;
import top.sharehome.reggie.service.DishService;
import top.sharehome.reggie.service.SetmealService;

import javax.annotation.Resource;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    @Resource
    private DishService dishService;

    @Resource
    private SetmealService setmealService;

    /**
     * 根据id删除分类，在删除之前进行判断该分类是否关联了菜品和套餐
     *
     * @param id
     */
    @Override
    public void remove(Long id) {
        LambdaQueryWrapper<Dish> queryWrapperDish = new LambdaQueryWrapper<Dish>();
        queryWrapperDish.eq(Dish::getCategoryId, id);
        int countDish = dishService.count(queryWrapperDish);
        if (countDish != 0) {
            throw new CustomException("当前分类下关联了菜品，不能删除！");
        }
        LambdaQueryWrapper<Setmeal> queryWrapperSetmeal = new LambdaQueryWrapper<Setmeal>();
        queryWrapperSetmeal.eq(Setmeal::getCategoryId, id);
        int countSetmeal = setmealService.count(queryWrapperSetmeal);
        if (countSetmeal != 0) {
            throw new CustomException("当前分类下关联了套餐，不能删除！");
        }
        Category category = new Category();
        category.setId(id);
        category.setName(super.getById(id).getName() + "[" + System.currentTimeMillis() + "]");
        super.updateById(category);
        super.removeById(id);
    }
}
