package top.sharehome.reggie.dto;

import lombok.Data;
import top.sharehome.reggie.entity.Dish;
import top.sharehome.reggie.entity.DishFlavor;

import java.util.List;

/**
 * 菜品数据传输对象
 *
 * @author AntonyCheng
 */
@Data
public class DishDto extends Dish {
    /**
     * 菜品对应的口味信息
     */
    private List<DishFlavor> flavors;
    /**
     * 菜单分类名称
     */
    private String categoryName;
    /**
     * 份数
     */
    private Integer copies;

}
