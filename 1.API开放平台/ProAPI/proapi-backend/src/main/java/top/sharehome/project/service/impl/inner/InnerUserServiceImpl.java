package top.sharehome.project.service.impl.inner;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;
import top.sharehome.procommon.model.User;
import top.sharehome.procommon.service.InnerUserService;
import top.sharehome.project.common.ErrorCode;
import top.sharehome.project.exception.BusinessException;
import top.sharehome.project.mapper.UserMapper;

import javax.annotation.Resource;

/**
 * @Description
 * @Author:AntonyCheng
 * @CreateTime:2023/1/20 19:18
 */
@DubboService
public class InnerUserServiceImpl implements InnerUserService {
    @Resource
    private UserMapper userMapper;

    @Override
    public User getInvokeUser(String accessKey) {
        if (StringUtils.isAnyBlank(accessKey)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QueryWrapper<User> query = new QueryWrapper<User>();
        query.eq("accessKey", accessKey);
        return userMapper.selectOne(query);
    }
}
