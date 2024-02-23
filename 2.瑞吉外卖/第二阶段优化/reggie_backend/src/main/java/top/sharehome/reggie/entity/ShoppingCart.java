package top.sharehome.reggie.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 购物车
 *
 * @author AntonyCheng
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "reggie.shopping_cart")
public class ShoppingCart implements Serializable {
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 名称
     */
    @TableField(value = "`name`")
    private String name;

    /**
     * 图片
     */
    @TableField(value = "image")
    private String image;

    /**
     * 主键
     */
    @TableField(value = "user_id")
    private Long userId;

    /**
     * 菜品id
     */
    @TableField(value = "dish_id")
    private Long dishId;

    /**
     * 套餐id
     */
    @TableField(value = "setmeal_id")
    private Long setmealId;

    /**
     * 口味
     */
    @TableField(value = "dish_flavor")
    private String dishFlavor;

    /**
     * 数量
     */
    @TableField(value = "`number`")
    private Integer number;

    /**
     * 金额
     */
    @TableField(value = "amount")
    private BigDecimal amount;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 是否删除
     */
    @TableField(value = "is_deleted", fill = FieldFill.INSERT_UPDATE)
    @TableLogic
    private Integer isDeleted;

    private static final long serialVersionUID = 1L;

    public static final String COL_ID = "id";

    public static final String COL_NAME = "name";

    public static final String COL_IMAGE = "image";

    public static final String COL_USER_ID = "user_id";

    public static final String COL_DISH_ID = "dish_id";

    public static final String COL_SETMEAL_ID = "setmeal_id";

    public static final String COL_DISH_FLAVOR = "dish_flavor";

    public static final String COL_NUMBER = "number";

    public static final String COL_AMOUNT = "amount";

    public static final String COL_CREATE_TIME = "create_time";

    public static final String COL_IS_DELETED = "is_deleted";
}