package top.sharehome.reggie.dto;

import lombok.Data;
import top.sharehome.reggie.entity.SetmealDish;

/**
 * @Description
 * @Author:AntonyCheng
 * @CreateTime:2023/2/5 02:30
 */
@Data
public class SetmealDishDto extends SetmealDish {

    private String image;

    private String description;

}
