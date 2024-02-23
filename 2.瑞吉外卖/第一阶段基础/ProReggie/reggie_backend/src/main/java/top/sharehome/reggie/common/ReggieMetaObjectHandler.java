package top.sharehome.reggie.common;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @Description 自动字段填充器
 * @Author:AntonyCheng
 * @CreateTime:2023/1/30 21:52
 */
@Component
@Slf4j
public class ReggieMetaObjectHandler implements MetaObjectHandler {
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
