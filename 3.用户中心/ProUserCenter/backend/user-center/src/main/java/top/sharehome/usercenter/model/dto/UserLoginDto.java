package top.sharehome.usercenter.model.dto;

import lombok.Data;
import top.sharehome.usercenter.model.entity.User;

import java.io.Serializable;

/**
 * 用户登录的数据传输对象
 *
 * @author admin
 */
@Data
public class UserLoginDto extends User implements Serializable {
    private static final long serialVersionUID = -8762400232361389267L;
}
