package top.sharehome.project.service.impl.inner;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.dubbo.config.annotation.DubboService;
import top.sharehome.procommon.model.UserInterfaceInfo;
import top.sharehome.procommon.service.InnerUserInterfaceInfoService;
import top.sharehome.project.mapper.UserInterfaceInfoMapper;
import top.sharehome.project.service.UserInterfaceInfoService;

import javax.annotation.Resource;

/**
 * @Description
 * @Author:AntonyCheng
 * @CreateTime:2023/1/20 19:22
 */
@DubboService
public class InnerUserInterfaceInfoServiceImpl implements InnerUserInterfaceInfoService {
    @Resource
    private UserInterfaceInfoService userInterfaceInfoService;
    @Resource
    private UserInterfaceInfoMapper userInterfaceInfoMapper;

    @Override
    public boolean invokeCount(Long interfaceInfoId, Long userId) {
        return userInterfaceInfoService.invokeCount(interfaceInfoId, userId);
    }

    @Override
    public boolean queryInvokeAble(Long interfaceInfoId, Long userId) {
        QueryWrapper<UserInterfaceInfo> queryWrapper = new QueryWrapper<UserInterfaceInfo>();
        queryWrapper.eq("interfaceInfoId", interfaceInfoId);
        queryWrapper.eq("userId", userId);
        UserInterfaceInfo userInterfaceInfo = userInterfaceInfoMapper.selectOne(queryWrapper);
        if (userInterfaceInfo == null) {
            return false;
        }
        if (userInterfaceInfo.getLeftNum() <= 0) {
            return false;
        }
        return userInterfaceInfo.getTotalNum() >= 0;
    }
}
