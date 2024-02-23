package top.sharehome.procommon.service;

public interface InnerUserInterfaceInfoService {
    /**
     * 调用接口统计
     *
     * @param interfaceInfoId
     * @param userId
     * @return
     */
    boolean invokeCount(Long interfaceInfoId, Long userId);

    /**
     * 查询接口调用次数是否合规
     *
     * @param interfaceInfoId
     * @param userId
     * @return
     */
    boolean queryInvokeAble(Long interfaceInfoId, Long userId);
}
