package top.sharehome.usercenter.model.request;

import lombok.Data;
import top.sharehome.usercenter.model.entity.User;

import java.io.Serializable;

/**
 * @Description
 * @Author:AntonyCheng
 * @CreateTime:2023/2/15 19:51
 */
@Data
public class UserRegisterRequest extends User implements Serializable {

    private static final long serialVersionUID = 4365354546914991679L;

    private String checkPassword;
}
