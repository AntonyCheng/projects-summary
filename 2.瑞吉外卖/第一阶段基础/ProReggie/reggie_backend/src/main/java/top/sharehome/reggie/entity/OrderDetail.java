package top.sharehome.reggie.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
    * 订单明细表
    */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "reggie.order_detail")
public class OrderDetail implements Serializable {
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 名字
     */
    @TableField(value = "`name`")
    private String name;

    /**
     * 图片
     */
    @TableField(value = "image")
    private String image;

    /**
     * 订单id
     */
    @TableField(value = "order_id")
    private Long orderId;

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

    private static final long serialVersionUID = 1L;

    public static final String COL_ID = "id";

    public static final String COL_NAME = "name";

    public static final String COL_IMAGE = "image";

    public static final String COL_ORDER_ID = "order_id";

    public static final String COL_DISH_ID = "dish_id";

    public static final String COL_SETMEAL_ID = "setmeal_id";

    public static final String COL_DISH_FLAVOR = "dish_flavor";

    public static final String COL_NUMBER = "number";

    public static final String COL_AMOUNT = "amount";
}