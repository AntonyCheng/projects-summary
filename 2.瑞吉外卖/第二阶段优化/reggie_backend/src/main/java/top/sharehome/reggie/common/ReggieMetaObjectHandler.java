package top.sharehome.reggie.common;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 自动字段填充器
 *
 * @author AntonyCheng
 */
@Component
@Slf4j
public class ReggieMetaObjectHandler implements MetaObjectHandler {
    /**
     * 插入时自动填充的字段
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        if (metaObject.hasSetter("createTime")) {
            metaObject.setValue("createTime", new Date(System.currentTimeMillis()));
        }
        if (metaObject.hasSetter("updateTime")) {
            metaObject.setValue("updateTime", new Date(System.currentTimeMillis()));
        }
        if (metaObject.hasSetter("createUser")) {
            metaObject.setValue("createUser", BaseContext.getCurrentId());
        }
        if (metaObject.hasSetter("updateUser")) {
            metaObject.setValue("updateUser", BaseContext.getCurrentId());
        }
        if (metaObject.hasSetter("isDeleted")) {
            metaObject.setValue("isDeleted", 0);
        }
    }

    /**
     * 更新时自动填充的字段
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        if (metaObject.hasSetter("updateTime")) {
            metaObject.setValue("updateTime", new Date(System.currentTimeMillis()));
        }
        if (metaObject.hasSetter("updateUser")) {
            metaObject.setValue("updateUser", BaseContext.getCurrentId());
        }
    }
}
