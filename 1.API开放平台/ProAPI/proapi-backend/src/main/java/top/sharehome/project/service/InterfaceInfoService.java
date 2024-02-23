package top.sharehome.project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import top.sharehome.procommon.model.InterfaceInfo;

/**
* @author admin
* @description 针对表【interface_info(接口信息)】的数据库操作Service
* @createDate 2023-01-12 20:01:23
*/
public interface InterfaceInfoService extends IService<InterfaceInfo> {

    void validInterfaceInfo(InterfaceInfo interfaceInfo, boolean b);
}
