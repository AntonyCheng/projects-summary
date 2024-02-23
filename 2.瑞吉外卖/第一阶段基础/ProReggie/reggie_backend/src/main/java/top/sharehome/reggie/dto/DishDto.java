package top.sharehome.reggie.dto;

import lombok.Data;
import top.sharehome.reggie.entity.Dish;
import top.sharehome.reggie.entity.DishFlavor;

import java.util.List;

/**
 * @Description
 * @Author:AntonyCheng
 * @CreateTime:2023/2/1 00:12
 */
@Data
public class DishDto extends Dish {
    private List<DishFlavor> flavors;

    private String categoryName;

    private Integer copies;
}
