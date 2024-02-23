package top.sharehome.reggie.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 菜品管理
 *
 * @author AntonyCheng
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "reggie.dish")
public class Dish implements Serializable {
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 菜品名称
     */
    @TableField(value = "`name`")
    private String name;

    /**
     * 菜品分类id
     */
    @TableField(value = "category_id")
    private Long categoryId;

    /**
     * 菜品价格
     */
    @TableField(value = "price")
    private BigDecimal price;

    /**
     * 商品码
     */
    @TableField(value = "code")
    private String code;

    /**
     * 图片
     */
    @TableField(value = "image")
    private String image;

    /**
     * 描述信息
     */
    @TableField(value = "description")
    private String description;

    /**
     * 0 停售 1 起售
     */
    @TableField(value = "`status`")
    private Integer status;

    /**
     * 顺序
     */
    @TableField(value = "sort")
    private Integer sort;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    /**
     * 创建人
     */
    @TableField(value = "create_user", fill = FieldFill.INSERT)
    private Long createUser;

    /**
     * 修改人
     */
    @TableField(value = "update_user", fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;

    /**
     * 逻辑删除
     */
    @TableField(value = "is_deleted", fill = FieldFill.INSERT)
    @TableLogic
    private Integer isDeleted;


    private static final long serialVersionUID = 1L;

    public static final String COL_ID = "id";

    public static final String COL_NAME = "name";

    public static final String COL_CATEGORY_ID = "category_id";

    public static final String COL_PRICE = "price";

    public static final String COL_CODE = "code";

    public static final String COL_IMAGE = "image";

    public static final String COL_DESCRIPTION = "description";

    public static final String COL_STATUS = "status";

    public static final String COL_SORT = "sort";

    public static final String COL_CREATE_TIME = "create_time";

    public static final String COL_UPDATE_TIME = "update_time";

    public static final String COL_CREATE_USER = "create_user";

    public static final String COL_UPDATE_USER = "update_user";

    public static final String COL_IS_DELETED = "is_deleted";
}