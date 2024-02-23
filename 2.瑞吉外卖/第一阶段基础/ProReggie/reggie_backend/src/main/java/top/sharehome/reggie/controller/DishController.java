package top.sharehome.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;
import top.sharehome.reggie.common.R;
import top.sharehome.reggie.dto.DishDto;
import top.sharehome.reggie.entity.Dish;
import top.sharehome.reggie.entity.DishFlavor;
import top.sharehome.reggie.service.CategoryService;
import top.sharehome.reggie.service.DishFlavorService;
import top.sharehome.reggie.service.DishService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description
 * @Author:AntonyCheng
 * @CreateTime:2023/1/31 22:27
 */
@RestController
@RequestMapping("/dish")
public class DishController {
    @Resource
    private DishService dishService;

    @Resource
    private CategoryService categoryService;

    @Resource
    private DishFlavorService dishFlavorService;

    /**
     * 菜品信息分页查询
     *
     * @param page     当前的页数
     * @param pageSize 每页的条数
     * @param name     在查询时可能会传入的搜索内容
     * @return 返回Mybatis-plus封装后的Page对象
     */
    @GetMapping("/page")
    public R<Page<DishDto>> page(@RequestParam("page") Integer page, @RequestParam("pageSize") Integer pageSize, @RequestParam(value = "name", required = false) String name) {
        // 首先按照菜品表进行分页
        Page<Dish> dishPage = new Page<Dish>(page, pageSize);
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<Dish>();
        queryWrapper.like(name != null, Dish::getName, name);
        queryWrapper.orderByAsc(Dish::getPrice);
        queryWrapper.orderByDesc(Dish::getCreateTime);
        dishService.page(dishPage, queryWrapper);
        // 将分好页的菜品分页对象非records的内容复制到菜品口味关系表，因为records内容中差一个categoryName
        Page<DishDto> dishDtoPage = new Page<DishDto>();
        BeanUtils.copyProperties(dishPage, dishDtoPage, "records");
        // 从菜品分页信息中获取Dish分页对象中的records内容，将内容转换成DishDto分页对象中的records内容，即加上categoryName的内容
        List<Dish> dishList = dishPage.getRecords();
        List<DishDto> dishDtoList = dishList.stream().map((item) -> {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item, dishDto);
            String categoryName = categoryService.getById(item.getCategoryId()).getName();
            dishDto.setCategoryName(categoryName);
            return dishDto;
        }).collect(Collectors.toList());
        // 将上面的DishDto records内容赋值给dishDtoPage
        dishDtoPage.setRecords(dishDtoList);
        return R.success(dishDtoPage);
    }

    /**
     * 用于修改菜品时的回显
     *
     * @param id 菜品id
     * @return 菜品dishDto对象
     */
    @GetMapping("/{id}")
    public R<DishDto> get(@PathVariable Long id) {
        DishDto dishDto = dishService.getByIdWithFlavor(id);
        return R.success(dishDto);
    }

    /**
     * 保存菜品及其风味
     *
     * @param dishDto 菜品风味对象
     * @return 保存成功的数据
     */
    @PostMapping("")
    public R<String> save(@RequestBody DishDto dishDto) {
        dishService.saveWithFlavor(dishDto);
        return R.success("菜品添加成功！");
    }

    /**
     * 查询菜品，用于添加或者修改购物车时可以选择菜品的添加
     *
     * @param categoryId 分类为菜品，需要传入一个分类id，不然选项框中会出现套餐类型
     * @return 菜品列表
     */
    @GetMapping("/list")
    public R<List<DishDto>> list(@RequestParam("categoryId") Long categoryId, @RequestParam("status") Integer status) {
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<Dish>();
        queryWrapper.eq(categoryId != null, Dish::getCategoryId, categoryId);
        queryWrapper.eq(Dish::getStatus, status);
        queryWrapper.orderByAsc(Dish::getPrice);
        queryWrapper.orderByDesc(Dish::getCreateTime);
        List<Dish> dishList = dishService.list(queryWrapper);
        List<DishDto> dishDtoList = new ArrayList<DishDto>();
        dishDtoList = dishList.stream().map(dish -> {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(dish, dishDto);
            dishDto.setFlavors(dishFlavorService.list(new LambdaUpdateWrapper<DishFlavor>().eq(DishFlavor::getDishId, dish.getId())));
            return dishDto;
        }).collect(Collectors.toList());
        return R.success(dishDtoList);
    }

    /**
     * （批量）删除菜品
     *
     * @param ids 菜品的id，一个就是删除，大于一个就是批量删除
     * @return 删除后的信息
     */
    @DeleteMapping("")
    public R<String> delete(String ids) {
        List<String> dishIds = Arrays.asList(ids.split(","));
        dishService.deleteWithFlavor(dishIds);
        if (dishIds.size() > 1) {
            return R.success("批量删除成功！");
        }
        return R.success("删除成功！");
    }

    /**
     * 修改菜品的售卖状态
     *
     * @param ids    菜品的id，一个就是修改状态，大于一个就是批量修改状态
     * @param status 从前端直接传入修改后的状态
     * @return 状态修改后的信息
     */
    @PostMapping("/status/{status}")
    public R<String> status(String ids, @PathVariable("status") Integer status) {
        List<String> dishIds = Arrays.asList(ids.split(","));
        List<Dish> dishList = dishIds.stream().map(dishId -> {
            Dish dish = dishService.getById(dishId);
            dish.setStatus(status);
            return dish;
        }).collect(Collectors.toList());
        dishService.updateBatchById(dishList);
        if (dishList.size() > 1) {
            return R.success("售卖状态批量修改成功！");
        }
        return R.success("售卖状态修改成功！");
    }

    /**
     * 更新菜品及其菜品口味
     *
     * @param dishDto 菜品口味对象
     * @return 修改后的信息
     */
    @PutMapping("")
    public R<String> update(@RequestBody DishDto dishDto) {
        dishService.updateWithFlavor(dishDto);
        return R.success("菜品修改成功！");
    }
}