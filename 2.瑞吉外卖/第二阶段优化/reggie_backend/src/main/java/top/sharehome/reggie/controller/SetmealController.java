package top.sharehome.reggie.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import top.sharehome.reggie.common.R;
import top.sharehome.reggie.dto.SetmealDishDto;
import top.sharehome.reggie.dto.SetmealDto;
import top.sharehome.reggie.entity.Setmeal;
import top.sharehome.reggie.service.SetmealService;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 套餐操作相关接口
 *
 * @author AntonyCheng
 */
@RestController
@RequestMapping("/setmeal")
@Slf4j
public class SetmealController {
    @Resource
    private SetmealService setmealService;

    /**
     * 套餐信息分页查询
     *
     * @param page     当前的页数
     * @param pageSize 每页的条数
     * @param name     在查询时可能会传入的搜索内容
     * @return 返回Mybatis-plus封装后的Page对象
     */
    @GetMapping("/page")
    public R<Page<SetmealDto>> page(@RequestParam("page") Integer page, @RequestParam("pageSize") Integer pageSize, @RequestParam(value = "name", required = false) String name) {
        Page<SetmealDto> setmealDtoPage = setmealService.getPage(page, pageSize, name);
        return R.success(setmealDtoPage);
    }

    /**
     * 修改套餐的售卖状态
     *
     * @param ids    套餐的id，一个就是修改状态，大于一个就是批量修改状态
     * @param status 从前端直接传入修改后的状态
     * @return 状态修改后的信息
     */
    @PostMapping("/status/{status}")
    @Transactional
    @CacheEvict(value = "setmealCache", allEntries = true)
    public R<String> status(String ids, @PathVariable("status") Integer status) {
        List<String> setmeatIds = Arrays.asList(ids.split(","));
        List<Setmeal> setmealList = setmeatIds.stream().map(setmeatId -> {
            Setmeal setmeal = setmealService.getById(setmeatId);
            setmeal.setStatus(status);
            return setmeal;
        }).collect(Collectors.toList());
        setmealService.updateBatchById(setmealList);
        if (setmealList.size() > 1) {
            return R.success("售卖状态批量修改成功！");
        }
        return R.success("售卖状态修改成功！");
    }

    /**
     * 删除（批量）套餐
     *
     * @param ids 套餐的id，一个就是删除，大于一个就是批量删除
     * @return 删除后的信息
     */
    @DeleteMapping("")
    @CacheEvict(value = "setmealCache", allEntries = true)
    public R<String> delete(String ids) {
        List<String> setmealIds = Arrays.asList(ids.split(","));
        setmealService.deleteWithDish(setmealIds);
        if (setmealIds.size() > 1) {
            return R.success("批量删除成功！");
        }
        return R.success("删除成功！");
    }

    /**
     * 保存套餐及其菜品
     *
     * @param setmealDto 套餐菜品对象
     * @return 保存成功的数据
     */
    @PostMapping("")
    @CacheEvict(value = "setmealCache", allEntries = true)
    public R<String> save(@RequestBody SetmealDto setmealDto) {
        setmealService.saveWithDish(setmealDto);
        return R.success("套餐添加成功！");
    }

    /**
     * 用于修改套餐时的回显
     *
     * @param id 套餐id
     * @return 套餐SetmealDto对象
     */
    @GetMapping("/{id}")
    public R<SetmealDto> get(@PathVariable("id") Long id) {
        SetmealDto setmealDto = setmealService.getByIdWithDish(id);
        return R.success(setmealDto);
    }

    /**
     * 更新套餐及其套餐菜品
     *
     * @param setmealDto 套餐菜品对象
     * @return 修改后的信息
     */
    @PutMapping("")
    @CacheEvict(value = "setmealCache", key = "'setmeal_category_id_' + #p0.categoryId + '_status_2'")
    public R<String> update(@RequestBody SetmealDto setmealDto) {
        setmealService.updateWithDish(setmealDto);
        return R.success("套餐修改成功！");
    }

    /**
     * 查询套餐，用于添加或者修改购物车时可以选择套餐的添加
     *
     * @param categoryId 分类为套餐，需要传入一个分类id，不然选项框中会出现菜品类型
     * @return 菜品列表
     */
    @GetMapping("/list")
    @Cacheable(value = "setmealCache", key = "'setmeal_category_id_' + #p0 + '_status_2'", unless = "#result.data == null")
    public R<List<SetmealDto>> list(@RequestParam("categoryId") Long categoryId, @RequestParam(value = "status", required = false) Integer status) {
        List<SetmealDto> setmealDtoList = setmealService.getList(categoryId, status);
        if (setmealDtoList == null) {
            return R.error("查询内容失败！");
        }
        return R.success(setmealDtoList);
    }

    /**
     * 用于回显套餐的详细信息页面
     *
     * @param setmealId 套餐id
     * @return 返回改套餐中包含的所有菜品信息
     */
    @GetMapping("/dish/{setmealId}")
    public R<List<SetmealDishDto>> dish(@PathVariable("setmealId") Long setmealId) {
        List<SetmealDishDto> setmealDishDtoList = setmealService.getDish(setmealId);
        return R.success(setmealDishDtoList);
    }
}
