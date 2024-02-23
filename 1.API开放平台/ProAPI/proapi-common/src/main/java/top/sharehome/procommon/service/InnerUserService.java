package top.sharehome.procommon.service;


import top.sharehome.procommon.model.User;


/**
 * 用户服务
 *
 * @author yupi
 */
public interface InnerUserService {
    /**
     * 获取调用的用户信息
     *
     * @param accessKey
     * @return
     */
    User getInvokeUser(String accessKey);

}
