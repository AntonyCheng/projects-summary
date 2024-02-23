package top.sharehome.reggie.dto;

import lombok.Data;
import top.sharehome.reggie.entity.SetmealDish;

/**
 * 套餐菜品数据传输对象
 *
 * @author AntonyCheng
 */
@Data
public class SetmealDishDto extends SetmealDish {
    /**
     * 套餐图片
     */
    private String image;
    /**
     * 套餐相关描述
     */
    private String description;

}
