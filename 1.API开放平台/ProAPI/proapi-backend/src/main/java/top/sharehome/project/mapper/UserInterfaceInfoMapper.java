package top.sharehome.project.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import top.sharehome.procommon.model.UserInterfaceInfo;

import java.util.List;

public interface UserInterfaceInfoMapper extends BaseMapper<UserInterfaceInfo> {
    List<UserInterfaceInfo> listTopInvokeInterfaceInfo(int limit);
}