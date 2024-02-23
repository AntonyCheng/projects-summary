package top.sharehome.procommon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import top.sharehome.procommon.model.InterfaceInfo;

/**
 * @author admin
 * @description 针对表【interface_info(接口信息)】的数据库操作Service
 * @createDate 2023-01-12 20:01:23
 */
public interface InnerInterfaceInfoService {
    /**
     * 调用接口信息
     *
     * @param path
     * @param method
     * @return
     */
    InterfaceInfo getInterfaceInfo(String path, String method);
}
