package top.sharehome.reggie.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 地址管理
 *
 * @author AntonyCheng
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "reggie.address_book")
public class AddressBook implements Serializable {
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 用户id
     */
    @TableField(value = "user_id")
    private Long userId;

    /**
     * 收货人
     */
    @TableField(value = "consignee")
    private String consignee;

    /**
     * 性别 0 女 1 男
     */
    @TableField(value = "sex")
    private Byte sex;

    /**
     * 手机号
     */
    @TableField(value = "phone")
    private String phone;

    /**
     * 省级区划编号
     */
    @TableField(value = "province_code")
    private String provinceCode;

    /**
     * 省级名称
     */
    @TableField(value = "province_name")
    private String provinceName;

    /**
     * 市级区划编号
     */
    @TableField(value = "city_code")
    private String cityCode;

    /**
     * 市级名称
     */
    @TableField(value = "city_name")
    private String cityName;

    /**
     * 区级区划编号
     */
    @TableField(value = "district_code")
    private String districtCode;

    /**
     * 区级名称
     */
    @TableField(value = "district_name")
    private String districtName;

    /**
     * 详细地址
     */
    @TableField(value = "detail")
    private String detail;

    /**
     * 标签
     */
    @TableField(value = "`label`")
    private String label;

    /**
     * 默认 0 否 1是
     */
    @TableField(value = "is_default")
    private Boolean isDefault;

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
     * 是否删除
     */
    @TableField(value = "is_deleted", fill = FieldFill.INSERT)
    @TableLogic
    private Integer isDeleted;

    private static final long serialVersionUID = 1L;

    public static final String COL_ID = "id";

    public static final String COL_USER_ID = "user_id";

    public static final String COL_CONSIGNEE = "consignee";

    public static final String COL_SEX = "sex";

    public static final String COL_PHONE = "phone";

    public static final String COL_PROVINCE_CODE = "province_code";

    public static final String COL_PROVINCE_NAME = "province_name";

    public static final String COL_CITY_CODE = "city_code";

    public static final String COL_CITY_NAME = "city_name";

    public static final String COL_DISTRICT_CODE = "district_code";

    public static final String COL_DISTRICT_NAME = "district_name";

    public static final String COL_DETAIL = "detail";

    public static final String COL_LABEL = "label";

    public static final String COL_IS_DEFAULT = "is_default";

    public static final String COL_CREATE_TIME = "create_time";

    public static final String COL_UPDATE_TIME = "update_time";

    public static final String COL_CREATE_USER = "create_user";

    public static final String COL_UPDATE_USER = "update_user";

    public static final String COL_IS_DELETED = "is_deleted";
}