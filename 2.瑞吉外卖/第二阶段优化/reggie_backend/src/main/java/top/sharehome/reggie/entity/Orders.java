package top.sharehome.reggie.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 订单表
 *
 * @author AntonyCheng
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "reggie.orders")
public class Orders implements Serializable {
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 订单号
     */
    @TableField(value = "`number`")
    private String number;

    /**
     * 订单状态 1待付款，2待派送，3已派送，4已完成，5已取消
     */
    @TableField(value = "`status`")
    private Integer status;

    /**
     * 下单用户
     */
    @TableField(value = "user_id")
    private Long userId;

    /**
     * 地址id
     */
    @TableField(value = "address_book_id")
    private Long addressBookId;

    /**
     * 下单时间
     */
    @TableField(value = "order_time")
    private Date orderTime;

    /**
     * 结账时间
     */
    @TableField(value = "checkout_time")
    private Date checkoutTime;

    /**
     * 支付方式 1微信,2支付宝
     */
    @TableField(value = "pay_method")
    private Integer payMethod;

    /**
     * 实收金额
     */
    @TableField(value = "amount")
    private BigDecimal amount;

    /**
     * 备注
     */
    @TableField(value = "remark")
    private String remark;

    @TableField(value = "phone")
    private String phone;

    @TableField(value = "address")
    private String address;

    @TableField(value = "user_name")
    private String userName;

    @TableField(value = "consignee")
    private String consignee;

    private static final long serialVersionUID = 1L;

    public static final String COL_ID = "id";

    public static final String COL_NUMBER = "number";

    public static final String COL_STATUS = "status";

    public static final String COL_USER_ID = "user_id";

    public static final String COL_ADDRESS_BOOK_ID = "address_book_id";

    public static final String COL_ORDER_TIME = "order_time";

    public static final String COL_CHECKOUT_TIME = "checkout_time";

    public static final String COL_PAY_METHOD = "pay_method";

    public static final String COL_AMOUNT = "amount";

    public static final String COL_REMARK = "remark";

    public static final String COL_PHONE = "phone";

    public static final String COL_ADDRESS = "address";

    public static final String COL_USER_NAME = "user_name";

    public static final String COL_CONSIGNEE = "consignee";
}