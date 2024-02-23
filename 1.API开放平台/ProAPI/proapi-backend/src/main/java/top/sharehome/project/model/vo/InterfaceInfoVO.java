package top.sharehome.project.model.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import top.sharehome.procommon.model.InterfaceInfo;

/**
 * @Description 接口信息封装视图
 * @Author:AntonyCheng
 * @CreateTime:2023/1/21 18:48
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class InterfaceInfoVO extends InterfaceInfo {
    /**
     * 调用次数
     */
    private Integer totalNum;
    private static final long serialVersionUID = 1L;
}
