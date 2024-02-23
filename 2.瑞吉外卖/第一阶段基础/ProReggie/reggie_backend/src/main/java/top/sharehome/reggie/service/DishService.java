package top.sharehome.reggie.service;

import top.sharehome.reggie.dto.DishDto;
import top.sharehome.reggie.entity.Dish;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface DishService extends IService<Dish> {
    public void saveWithFlavor(DishDto dishDto);

    public void deleteWithFlavor(List<String> dishIds);

    public DishDto getByIdWithFlavor(Long id);

    public void updateWithFlavor(DishDto dishDto);
}
