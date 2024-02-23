package top.sharehome.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.BeanUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import top.sharehome.reggie.common.R;
import top.sharehome.reggie.dto.SetmealDishDto;
import top.sharehome.reggie.dto.SetmealDto;
import top.sharehome.reggie.entity.Setmeal;
import top.sharehome.reggie.entity.SetmealDish;
import top.sharehome.reggie.service.CategoryService;
import top.sharehome.reggie.service.DishService;
import top.sharehome.reggie.service.SetmealDishService;
import top.sharehome.reggie.service.SetmealService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description
 * @Author:AntonyCheng
 * @CreateTime:2023/1/31 23:48
 */
@RestController
@RequestMapping("/setmeal")
public class SetmealController {
    @Resource
    private SetmealService setmealService;

    @Resource
    private SetmealDishService setmealDishService;

    @Resource
    private CategoryService categoryService;

    @Resource
    private DishService dishService;

    /**
     * 套餐信息分页查询
     *
     * @param page     当前的页数
     * @param pageSize 每页的条数
     * @param name     在查询时可能会传入的搜索内容
     * @return 返回Mybatis-plus封装后的Page对象
     */
    @GetMapping("/page")
    @Transactional
    public R<Page<SetmealDto>> page(@RequestParam("page") Integer page, @RequestParam("pageSize") Integer pageSize, @RequestParam(value = "name", required = false) String name) {
        // 首先按套餐表进行分页
        Page<Setmeal> setmealPage = new Page<Setmeal>(page, pageSize);
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<Setmeal>();
        queryWrapper.like(name != null, Setmeal::getName, name);
        queryWrapper.orderByAsc(Setmeal::getPrice);
        queryWrapper.orderByDesc(Setmeal::getCreateTime);
        setmealService.page(setmealPage, queryWrapper);
        // 将分好页的套餐分页对象非records的内容复制到套餐菜品关系表，因为records内容中差一个categoryName
        Page<SetmealDto> setmealDtoPage = new Page<SetmealDto>();
        BeanUtils.copyProperties(setmealPage, setmealDtoPage, "records");
        // 从菜品分页信息中获Setmeal分页对象中的records内容，将内容转换成SetmealDto分页对象中的records内容，即加上categoryName的内容
        List<Setmeal> setmealList = setmealPage.getRecords();
        List<SetmealDto> setmealDtoList = setmealList.stream().map((item) -> {
            SetmealDto setmealDto = new SetmealDto();
            BeanUtils.copyProperties(item, setmealDto);
            setmealDto.setCategoryName(categoryService.getById(item.getCategoryId()).getName());
            return setmealDto;
        }).collect(Collectors.toList());
        // 将上面的Setmeal分页对象中的records内容赋值给setmealDtoPage
        setmealDtoPage.setRecords(setmealDtoList);
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
     * （批量）删除套餐
     *
     * @param ids 套餐的id，一个就是删除，大于一个就是批量删除
     * @return 删除后的信息
     */
    @DeleteMapping("")
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
    public R<List<SetmealDto>> list(@RequestParam("categoryId") Long categoryId, @RequestParam("status") Integer status) {
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<Setmeal>();
        queryWrapper.eq(categoryId != null, Setmeal::getCategoryId, categoryId);
        queryWrapper.eq(Setmeal::getStatus, status);
        queryWrapper.orderByAsc(Setmeal::getPrice);
        queryWrapper.orderByDesc(Setmeal::getCreateTime);
        List<Setmeal> setmealList = setmealService.list(queryWrapper);
        List<SetmealDto> setmealDtoList = new ArrayList<SetmealDto>();
        setmealDtoList = setmealList.stream().map(setmeal -> {
            SetmealDto setmealDto = new SetmealDto();
            BeanUtils.copyProperties(setmeal, setmealDto);
            setmealDto.setSetmealDishes(setmealDishService.list(new LambdaUpdateWrapper<SetmealDish>().eq(SetmealDish::getSetmealId, setmeal.getId())));
            return setmealDto;
        }).collect(Collectors.toList());
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
        List<SetmealDish> setmealDishList = setmealDishService.list(new LambdaQueryWrapper<SetmealDish>().eq(SetmealDish::getSetmealId, setmealId));
        List<SetmealDishDto> setmealDishDtoList = new ArrayList<SetmealDishDto>();
        setmealDishDtoList = setmealDishList.stream().map(setmealDish -> {
            SetmealDishDto setmealDishDto = new SetmealDishDto();
            BeanUtils.copyProperties(setmealDish, setmealDishDto);
            setmealDishDto.setImage(dishService.getById(setmealDish.getDishId()).getImage());
            setmealDishDto.setDescription(dishService.getById(setmealDish.getDishId()).getDescription());
            return setmealDishDto;
        }).collect(Collectors.toList());
        return R.success(setmealDishDtoList);
    }
}
