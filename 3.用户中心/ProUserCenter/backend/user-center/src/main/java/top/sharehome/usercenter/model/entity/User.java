package top.sharehome.usercenter.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户表
 *
 * @author admin
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "user_center.t_user")
public class User implements Serializable {
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 用户姓名
     */
    @TableField(value = "user_name", fill = FieldFill.INSERT)
    private String userName;

    /**
     * 用户账号
     */
    @TableField(value = "user_account")
    private String userAccount;

    /**
     * 用户密码
     */
    @TableField(value = "user_password")
    private String userPassword;

    /**
     * 用户角色
     */
    @TableField(value = "user_role")
    private Integer userRole;

    /**
     * 用户头像链接
     */
    @TableField(value = "user_avatar_url")
    private String userAvatarUrl;

    /**
     * 用户性别
     */
    @TableField(value = "user_gender")
    private Byte userGender;

    /**
     * 用户手机
     */
    @TableField(value = "user_phone")
    private String userPhone;

    /**
     * 用户邮箱
     */
    @TableField(value = "user_email")
    private String userEmail;

    /**
     * 用户状态（0表示正常，1表示不正常）
     */
    @TableField(value = "user_states")
    private Integer userStates;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time")
    private LocalDateTime updateTime;

    /**
     * 是否删除（0表示没删除，1表示删除）
     */
    @TableField(value = "is_deleted", fill = FieldFill.INSERT_UPDATE)
    @TableLogic
    private Integer isDeleted;

    private static final long serialVersionUID = 1L;

    public static final String COL_ID = "id";

    public static final String COL_USER_NAME = "user_name";

    public static final String COL_USER_ACCOUNT = "user_account";

    public static final String COL_USER_PASSWORD = "user_password";

    public static final String COL_USER_ROLE = "user_role";

    public static final String COL_USER_AVATAR_URL = "user_avatar_url";

    public static final String COL_USER_GENDER = "user_gender";

    public static final String COL_USER_PHONE = "user_phone";

    public static final String COL_USER_EMAIL = "user_email";

    public static final String COL_USER_STATES = "user_states";

    public static final String COL_CREATE_TIME = "create_time";

    public static final String COL_UPDATE_TIME = "update_time";

    public static final String COL_IS_DELETED = "is_deleted";
}