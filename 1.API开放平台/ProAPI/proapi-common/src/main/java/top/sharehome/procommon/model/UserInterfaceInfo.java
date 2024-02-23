package top.sharehome.procommon.model;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
    * 用户调用接口关系
    */
@Data
@TableName(value = "user_interface_info")
public class UserInterfaceInfo implements Serializable {
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;

    /**
     * 调用者 id
     */
    @TableField(value = "userId")
    private Long userId;

    /**
     * 接口 id
     */
    @TableField(value = "interfaceInfoId")
    private Long interfaceInfoId;

    /**
     * 总调用次数
     */
    @TableField(value = "totalNum")
    private Integer totalNum;

    /**
     * 剩余调用次数
     */
    @TableField(value = "leftNum")
    private Integer leftNum;

    /**
     * 0-表示正常，1-表示禁用
     */
    @TableField(value = "`status`")
    private Integer status;

    /**
     * 是否删除(0-未删, 1-已删)
     */
    @TableField(value = "isDeleted")
    @TableLogic
    private Byte isDeleted;

    /**
     * 创建时间
     */
    @TableField(value = "createTime")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField(value = "updateTime")
    private Date updateTime;

    private static final long serialVersionUID = 1L;

    public static final String COL_ID = "id";

    public static final String COL_USERID = "userId";

    public static final String COL_INTERFACEINFOID = "interfaceInfoId";

    public static final String COL_TOTALNUM = "totalNum";

    public static final String COL_LEFTNUM = "leftNum";

    public static final String COL_STATUS = "status";

    public static final String COL_ISDELETED = "isDeleted";

    public static final String COL_CREATETIME = "createTime";

    public static final String COL_UPDATETIME = "updateTime";
}