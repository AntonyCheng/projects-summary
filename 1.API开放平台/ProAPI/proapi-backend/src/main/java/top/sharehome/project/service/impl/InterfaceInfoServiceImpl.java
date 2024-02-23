package top.sharehome.project.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import top.sharehome.procommon.model.InterfaceInfo;
import top.sharehome.project.common.ErrorCode;
import top.sharehome.project.exception.BusinessException;
import top.sharehome.project.mapper.InterfaceInfoMapper;
import top.sharehome.project.service.InterfaceInfoService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * @author admin
 * @description 针对表【interface_info(接口信息)】的数据库操作Service实现
 * @createDate 2023-01-12 20:01:23
 */
@Service
public class InterfaceInfoServiceImpl extends ServiceImpl<InterfaceInfoMapper, InterfaceInfo> implements InterfaceInfoService {
    @Override
    public void validInterfaceInfo(InterfaceInfo interfaceInfo, boolean add) {
        if (interfaceInfo == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Long id = interfaceInfo.getId();
        String name = interfaceInfo.getName();
        String description = interfaceInfo.getDescription();
        String url = interfaceInfo.getUrl();
        String requestHeader = interfaceInfo.getRequestHeader();
        String responseHeader = interfaceInfo.getResponseHeader();
        String requestParams = interfaceInfo.getRequestParams();
        Long userId = interfaceInfo.getUserId();
        Integer status = interfaceInfo.getStatus();
        String method = interfaceInfo.getMethod();
        // 创建时，所有参数必须非空
        if (add) {
            if (StringUtils.isAnyBlank(name, url, method)) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR);
            }
        }
        if (StringUtils.isNotBlank(name) && name.length() > 50) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "名称过长");
        }
    }
}




