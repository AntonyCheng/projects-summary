package top.sharehome.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.sharehome.reggie.entity.Category;
import top.sharehome.reggie.entity.Employee;
import top.sharehome.reggie.mapper.EmployeeMapper;
import top.sharehome.reggie.service.EmployeeService;

@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {
    /**
     * 删除员工，并且更新删除后的名字和时间
     *
     * @param id
     */
    @Override
    public void remove(Long id) {
        Employee employee = new Employee();
        employee.setId(id);
        employee.setUsername(super.getById(id).getUsername() + "[" + System.currentTimeMillis() + "]");
        super.updateById(employee);
        super.removeById(id);
    }
}
