package top.sharehome.project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import top.sharehome.procommon.model.UserInterfaceInfo;

/**
 * @Description
 * @Author:AntonyCheng
 * @CreateTime:2023/1/20 19:25
 */

public interface UserInterfaceInfoService extends IService<UserInterfaceInfo> {
    void validUserInterfaceInfo(UserInterfaceInfo userInterfaceInfo, boolean add);

    /**
     * 调用接口统计
     *
     * @param interfaceInfoId
     * @param userId
     * @return
     */
    boolean invokeCount(long interfaceInfoId, long userId);
}
