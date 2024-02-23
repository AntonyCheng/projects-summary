package top.sharehome.reggie.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import top.sharehome.reggie.common.R;
import top.sharehome.reggie.dto.DishDto;
import top.sharehome.reggie.entity.Dish;
import top.sharehome.reggie.service.DishService;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 菜品操作相关接口
 *
 * @author AntonyCheng
 */
@RestController
@RequestMapping("/dish")
@Slf4j
public class DishController {
    @Resource
    private DishService dishService;

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
        Page<DishDto> dishDtoPage = dishService.getPage(page, pageSize, name);
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
    @CacheEvict(value = "dishCache", allEntries = true)
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
    @Cacheable(value = "dishCache", key = "'dish_category_id_' + #p0 + '_status_1'", unless = "#result.data == null")
    public R<List<DishDto>> list(@RequestParam("categoryId") Long categoryId, @RequestParam(value = "status", required = false) Integer status) {
        List<DishDto> dishDtoList = dishService.getList(categoryId, status);
        if (dishDtoList == null) {
            return R.error("查询内容失败！");
        }
        return R.success(dishDtoList);
    }

    /**
     * （批量）删除菜品
     *
     * @param ids 菜品的id，一个就是删除，大于一个就是批量删除
     * @return 删除后的信息
     */
    @DeleteMapping("")
    @CacheEvict(value = "dishCache", allEntries = true)
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
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = "dishCache", allEntries = true)
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
    @CacheEvict(value = "dishCache", key = "'dish_category_id_' + #p0.categoryId + '_status_1'")
    public R<String> update(@RequestBody DishDto dishDto) {
        dishService.updateWithFlavor(dishDto);
        return R.success("菜品修改成功！");
    }
}