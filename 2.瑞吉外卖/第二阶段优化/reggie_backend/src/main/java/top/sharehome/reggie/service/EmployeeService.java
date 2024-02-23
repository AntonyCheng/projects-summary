package top.sharehome.reggie.service;

import top.sharehome.reggie.entity.Employee;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 员工相关服务类
 *
 * @author AntonyCheng
 */
public interface EmployeeService extends IService<Employee> {

    void remove(Long id);

}
