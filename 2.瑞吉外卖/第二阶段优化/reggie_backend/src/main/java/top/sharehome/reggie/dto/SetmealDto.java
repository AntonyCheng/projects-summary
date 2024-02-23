package top.sharehome.reggie.dto;

import lombok.Data;
import top.sharehome.reggie.entity.Setmeal;
import top.sharehome.reggie.entity.SetmealDish;

import java.util.List;
/**
 * 套餐数据传输对象
 *
 * @author AntonyCheng
 */
@Data
public class SetmealDto extends Setmeal {
    /**
     * 套餐菜品信息
     */
    private List<SetmealDish> setmealDishes;
    /**
     * 菜单分类名称
     */
    private String categoryName;

}
