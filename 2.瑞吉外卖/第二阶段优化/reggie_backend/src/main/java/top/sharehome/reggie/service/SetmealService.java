package top.sharehome.reggie.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import top.sharehome.reggie.dto.SetmealDishDto;
import top.sharehome.reggie.dto.SetmealDto;
import top.sharehome.reggie.entity.Setmeal;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 套餐相关服务类
 *
 * @author AntonyCheng
 */
public interface SetmealService extends IService<Setmeal> {
    public void saveWithDish(SetmealDto setmealDto);

    public void deleteWithDish(List<String> ids);

    public SetmealDto getByIdWithDish(Long id);

    public void updateWithDish(SetmealDto setmealDto);

    List<SetmealDto> getList(Long categoryId, Integer status);

    List<SetmealDishDto> getDish(Long setmealId);

    Page<SetmealDto> getPage(Integer page, Integer pageSize, String name);

}
