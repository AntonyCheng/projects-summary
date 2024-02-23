package top.sharehome.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import top.sharehome.reggie.common.R;
import top.sharehome.reggie.entity.Category;
import top.sharehome.reggie.service.CategoryService;

import javax.annotation.Resource;
import java.util.List;

/**
 * 菜单分类相关接口
 *
 * @author AntonyCheng
 */
@RestController
@RequestMapping("/category")
@Slf4j
public class CategoryController {
    @Resource
    private CategoryService categoryService;

    /**
     * 对菜品分类的分页操作
     *
     * @param page     当前页数
     * @param pageSize 每一页的分裂条数
     * @return 返回封装好的Page对象
     */
    @GetMapping("/page")
    public R<Page<Category>> page(@RequestParam("page") Integer page, @RequestParam("pageSize") Integer pageSize) {
        Page<Category> categoryPage = new Page<>(page, pageSize);
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<Category>();
        queryWrapper.orderByAsc(Category::getSort);
        queryWrapper.orderByAsc(Category::getType);
        categoryService.page(categoryPage, queryWrapper);
        return R.success(categoryPage);
    }

    /**
     * 对菜品分类进行保存操作
     *
     * @param category 从前端传输过来的分类对象
     * @return 保存后的结果R对象
     */
    @PostMapping("")
    public R<String> save(@RequestBody Category category) {
        categoryService.save(category);
        return R.success("分类添加成功！");
    }

    /**
     * 根据id删除菜品分类
     *
     * @param id 要删除的菜品分类的id
     * @return 删除后的结果R对象
     */
    @DeleteMapping("")
    public R<String> delete(Long id) {
        categoryService.remove(id);
        return R.success("删除成功！");
    }

    /**
     * 根据id更新菜品分类
     *
     * @param category 从前端传入的需要更新的菜品对象
     * @return 返回更新后的结果R对象
     */
    @PutMapping("")
    @Transactional(rollbackFor = Exception.class)
    public R<String> update(@RequestBody Category category) {
        Category categoryBackend = categoryService.getById(category.getId());
        if (categoryBackend.getName().equals(category.getName()) && categoryBackend.getSort().equals(category.getSort())) {
            return R.error("修改后数据和原有数据相同！");
        }
        categoryService.updateById(category);
        return R.success("分类修改成功！");
    }

    /**
     * 添加菜品时动态获取菜品分类
     *
     * @param category 将type包装好的分类对象
     * @return
     */
    @GetMapping("/list")
    public R<List<Category>> list(Category category) {
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<Category>();
        queryWrapper.eq(category.getType() != null, Category::getType, category.getType());
        queryWrapper.orderByAsc(Category::getSort).orderByDesc(Category::getCreateTime);
        List<Category> list = categoryService.list(queryWrapper);
        return R.success(list);
    }

}
