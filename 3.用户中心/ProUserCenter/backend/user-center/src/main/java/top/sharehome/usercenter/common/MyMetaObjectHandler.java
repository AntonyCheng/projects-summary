package top.sharehome.usercenter.common;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * 自动字段填充器
 *
 * @author admin
 */
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {
    /**
     * 需要处理的字段名
     */
    public static final String CREATE_TIME = "createTime";
    public static final String UPDATE_TIME = "updateTime";
    public static final String USER_STATUS = "userStatus";
    public static final String IS_DELETED = "isDeleted";
    public static final String USER_NAME = "userName";
    public static final String USER_ROLE = "userRole";

    /**
     * 插入时自动填充的字段
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        if (metaObject.hasSetter(CREATE_TIME)) {
            metaObject.setValue(CREATE_TIME, LocalDateTime.now());
        }

        if (metaObject.hasSetter(UPDATE_TIME)) {
            metaObject.setValue(UPDATE_TIME, LocalDateTime.now());
        }

        if (metaObject.hasSetter(USER_STATUS)) {
            metaObject.setValue(USER_STATUS, 0);
        }

        if (metaObject.hasSetter(IS_DELETED)) {
            metaObject.setValue(IS_DELETED, 0);
        }

        if (metaObject.hasSetter(USER_NAME)) {
            metaObject.setValue(USER_NAME, UUID.randomUUID().toString().split("-")[0].toUpperCase());
        }

        if (metaObject.hasSetter(USER_ROLE)) {
            metaObject.setValue(USER_ROLE, 0);
        }

    }

    /**
     * 更新时自动填充的字段
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        if (metaObject.hasSetter(CREATE_TIME)) {
            metaObject.setValue(CREATE_TIME, LocalDateTime.now());
        }

        if (metaObject.hasSetter(UPDATE_TIME)) {
            metaObject.setValue(UPDATE_TIME, LocalDateTime.now());
        }

    }
}
