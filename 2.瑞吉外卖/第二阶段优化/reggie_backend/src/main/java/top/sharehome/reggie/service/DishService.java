package top.sharehome.reggie.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import top.sharehome.reggie.dto.DishDto;
import top.sharehome.reggie.entity.Dish;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 菜品相关服务类
 *
 * @author AntonyCheng
 */
public interface DishService extends IService<Dish> {
    public void saveWithFlavor(DishDto dishDto);

    public void deleteWithFlavor(List<String> dishIds);

    public DishDto getByIdWithFlavor(Long id);

    public void updateWithFlavor(DishDto dishDto);

    Page<DishDto> getPage(Integer page, Integer pageSize, String name);

    List<DishDto> getList(Long categoryId, Integer status);

}
