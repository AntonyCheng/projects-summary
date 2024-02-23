package top.sharehome.reggie.dto;

import lombok.Data;
import top.sharehome.reggie.entity.Setmeal;
import top.sharehome.reggie.entity.SetmealDish;

import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
