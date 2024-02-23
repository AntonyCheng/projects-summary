package top.sharehome.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.sharehome.reggie.entity.Employee;
import top.sharehome.reggie.mapper.EmployeeMapper;
import top.sharehome.reggie.service.EmployeeService;

/**
 * 员工相关服务实现类
 *
 * @author AntonyCheng
 */
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {
    /**
     * 删除员工，并且更新删除后的名字和时间
     *
     * @param id 员工ID
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void remove(Long id) {
        Employee employee = new Employee();
        employee.setId(id);
        employee.setUsername(super.getById(id).getUsername() + "[" + System.currentTimeMillis() + "]");
        super.updateById(employee);
        super.removeById(id);
    }
}
