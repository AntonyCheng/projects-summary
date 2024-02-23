package top.sharehome.reggie.service;

import top.sharehome.reggie.dto.SetmealDto;
import top.sharehome.reggie.entity.Setmeal;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface SetmealService extends IService<Setmeal>{
    public void saveWithDish(SetmealDto setmealDto);

    public void deleteWithDish(List<String> ids);

    public SetmealDto getByIdWithDish(Long id);

    public void updateWithDish(SetmealDto setmealDto);
}
