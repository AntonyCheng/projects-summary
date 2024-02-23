package top.sharehome.project.service.impl.inner;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;
import top.sharehome.procommon.model.InterfaceInfo;
import top.sharehome.procommon.service.InnerInterfaceInfoService;
import top.sharehome.project.common.ErrorCode;
import top.sharehome.project.exception.BusinessException;
import top.sharehome.project.mapper.InterfaceInfoMapper;

import javax.annotation.Resource;

/**
 * @Description
 * @Author:AntonyCheng
 * @CreateTime:2023/1/20 19:19
 */
@DubboService
public class InnerInterfaceInfoServiceImpl implements InnerInterfaceInfoService {
    @Resource
    private InterfaceInfoMapper interfaceInfoMapper;

    @Override
    public InterfaceInfo getInterfaceInfo(String url, String method) {
        if (StringUtils.isAnyBlank(url, method)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QueryWrapper<InterfaceInfo> query = new QueryWrapper<InterfaceInfo>();
        query.eq("url", url);
        query.eq("method", method);
        return interfaceInfoMapper.selectOne(query);
    }
}
