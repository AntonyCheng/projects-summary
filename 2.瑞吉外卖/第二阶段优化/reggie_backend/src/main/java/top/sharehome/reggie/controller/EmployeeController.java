package top.sharehome.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;
import top.sharehome.reggie.common.BaseContext;
import top.sharehome.reggie.common.R;
import top.sharehome.reggie.entity.Employee;
import top.sharehome.reggie.service.EmployeeService;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.Map;


/**
 * 员工操作相关接口
 *
 * @author AntonyCheng
 */
@RestController
@RequestMapping("/employee")
@Slf4j
public class EmployeeController {
    @Resource
    private EmployeeService employeeService;

    private static final String ADMIN_PERMISSIONS = "admin";

    /**
     * 用户登录功能
     *
     * @param employee 接收从前端传来的用户账号密码
     * @param session  这里需要往前端session中存入登录用户的id
     * @return 返回统一包装后的R类型
     */
    @PostMapping("/login")
    public R<Employee> login(@RequestBody Employee employee, HttpSession session) {
        String password = employee.getPassword();
        // ①. 将页面提交的密码password进行md5加密处理, 得到加密后的字符串
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<Employee>();
        // 这里使用lambda表达式是为了避免硬编码，相关问题见 https://blog.csdn.net/yangzhe19931117/article/details/128246653?spm=1001.2101.3001.6650.2&utm_medium=distribute.pc_relevant.none-task-blog-2%7Edefault%7EYuanLiJiHua%7EPosition-2-128246653-blog-77905826.pc_relevant_recovery_v2&depth_1-utm_source=distribute.pc_relevant.none-task-blog-2%7Edefault%7EYuanLiJiHua%7EPosition-2-128246653-blog-77905826.pc_relevant_recovery_v2&utm_relevant_index=5
        // 即此处可以使用"username"字符串，但是这样会造成硬编码
        queryWrapper.eq(Employee::getUsername, employee.getUsername());
        // ②. 根据页面提交的用户名username查询数据库中员工数据信息
        Employee employeeInBackend = employeeService.getOne(queryWrapper);
        // ③. 如果没有查询到, 则返回登录失败结果
        if (employeeInBackend == null) {
            return R.error("登陆失败！没有此用户！");
        }
        // ④. 密码比对，如果不一致, 则返回登录失败结果
        if (!employeeInBackend.getPassword().equals(password)) {
            return R.error("登录失败！密码错误！");
        }
        // ⑤. 查看员工状态，如果为已禁用状态，则返回员工已禁用结果
        if (employeeInBackend.getStatus() == 0) {
            return R.error("登陆失败！该用户已被禁用！");
        }
        // ⑥. 登录成功，将员工id存入Session, 并返回登录成功结果
        session.setAttribute("loginUserId", employeeInBackend.getId());
        return R.success(employeeInBackend);
    }

    /**
     * 用户退出功能
     *
     * @param session 退出时需要删除掉session中用户登陆的id
     * @return 返回退出的日志信息
     */
    @PostMapping("/logout")
    public R<String> logout(HttpSession session) {
        session.removeAttribute("loginUserId");
        return R.success("退出成功");
    }

    /**
     * 新增员工
     *
     * @param employee 前端传来封装后员工对象
     * @return 返回保存员工的日志信息
     */
    @PostMapping("")
    public R<String> save(@RequestBody Employee employee) {
        // 由于mysql数据库中有一个username的唯一索引，所以需要查看该用户名是否已经存在
        // 可以单独查询进行排除，还可以对于save()方法进行try-catch抓取来自数据库的异常进行排除
        // 单独查询进行排查会再一次查询数据库，从而增加程序占用资源，下面是单独查询的代码
        // LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<Employee>();
        // queryWrapper.eq(Employee::getUsername, employee.getUsername());
        // if (employeeService.getOne(queryWrapper) != null) {
        //     return R.error("该用户名已存在");
        // }
        // 进行try-catch抓取异常进行排除可以完美解决问题，但是一个系统内部过多的try-catch会影响代码的可读性
        // 所以我们可以统一写一个全局异常抓取类：top.sharehome.reggie.common.GlobalExceptionHandler

        // 所以这里在最下面进行全局异常处理的排查方式
        // 设置员工其他的默认属性，这个地方部分公共字段使用字段自动填充器进行构造
        employee.setPassword(DigestUtils.md5DigestAsHex(employee.getPassword().getBytes()));
        // 设置创建时间
        // employee.setCreateTime(new Date(System.currentTimeMillis()));
        // 设置更新时间
        // employee.setUpdateTime(new Date(System.currentTimeMillis()));
        // 设置员工状态
        employee.setStatus(1);
        // 设置创建人
        // employee.setCreateUser((Long) request.getSession().getAttribute("loginUserId"));
        // 设置更新人
        // employee.setUpdateUser((Long) request.getSession().getAttribute("loginUserId"));
        // 设置逻辑删除
        // employee.setIsDeleted(0);
        // 员工的id在数据库中并没有自增，如果传入null值，mybatis-plus中的save方法会通过雪花算法自动生成id

        // 以下展示try-catch形式的处理方式
        // try {
        //     employeeService.save(employee);
        // }catch (Exception e){
        //     R.error("该用户名已存在");
        // }
        // 如果使用了全局异常处理器，我们就可以不用显式进行try-catch操作，基于AOP思想由Spring框架帮助我们进行异常抓取和处理
        employeeService.save(employee);
        return R.success("添加员工成功");
    }

    /**
     * 员工信息分页查询
     *
     * @param page     当前的页数
     * @param pageSize 每页的条数
     * @param name     在查询时可能会传入的搜索内容
     * @return 返回Mybatis-plus封装后的Page对象
     */
    @GetMapping("/page")
    public R<Page<Employee>> page(Integer page, Integer pageSize, String name) {
        // 构造分页构造器
        Page<Employee> pageInfo = new Page<Employee>(page, pageSize);

        // 构造条件构造器
        // 添加过滤条件
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<Employee>();
        queryWrapper.like(StringUtils.isNotEmpty(name), Employee::getName, name);
        // 添加排序条件
        queryWrapper.orderByAsc(Employee::getCreateTime);

        //执行查询，这里不需要接收，因为page()方法内部就会对传输进去的Page对象进行包装
        employeeService.page(pageInfo, queryWrapper);
        return R.success(pageInfo);
    }

    /**
     * 根据ID修改员工
     *
     * @param employee 从前端传来封装后的Employee
     * @param session  从session中获取用户id
     * @return 返回修改结果
     */
    @PutMapping("")
    @Transactional(rollbackFor = Exception.class)
    public R<String> update(@RequestBody Employee employee, HttpSession session) {
        Long loginUserId = (Long) session.getAttribute("loginUserId");
        // 修改用户信息页面
        if (!StringUtils.isEmpty(employee.getName())
                && !StringUtils.isEmpty(employee.getUsername())
                && !StringUtils.isEmpty(employee.getPhone())
                && !StringUtils.isEmpty(employee.getIdNumber())
                && !StringUtils.isEmpty(employee.getPassword())) {
            if (!ADMIN_PERMISSIONS.equals(employeeService.getById(loginUserId).getUsername()) && !employee.getId().equals(loginUserId)) {
                return R.error("权限有误！修改用户状态失败！");
            }
            String password = employeeService.getById(employee.getId()).getPassword();
            if (!DigestUtils.md5DigestAsHex(employee.getPassword().getBytes()).equals(password)) {
                return R.error("密码错误！修改用户信息失败！");
            }
            Employee employeeBackend = employeeService.getById(employee.getId());
            employeeBackend.setPassword(employee.getPassword());
            if (employee.equals(employeeBackend)) {
                return R.error("参数错误！修改后和修改前信息一致！");
            }
            employee.setPassword(null);
            // employee.setUpdateUser(loginUserId);
            // employee.setUpdateTime(new Date(System.currentTimeMillis()));
            employeeService.updateById(employee);
            return R.success("修改用户信息成功！");
        }
        // 修改用户状态
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getId, loginUserId);
        Employee user = employeeService.getOne(queryWrapper);
        if (user == null || !ADMIN_PERMISSIONS.equals(user.getUsername()) || employee.getId() == null) {
            return R.error("权限有误！修改用户状态失败！");
        }
        LambdaUpdateWrapper<Employee> updateWrapper = new LambdaUpdateWrapper<Employee>();
        Integer status = employee.getStatus();
        updateWrapper.set(status == 1, Employee::getStatus, 0);
        updateWrapper.set(status == 0, Employee::getStatus, 1);
        // updateWrapper.set(Employee::getUpdateTime, new Date(System.currentTimeMillis()));
        // updateWrapper.set(Employee::getUpdateUser, loginUserId);
        updateWrapper.eq(Employee::getId, employee.getId());
        employeeService.update(updateWrapper);
        return R.success("修改用户状态成功！");
    }

    /**
     * 根据ID修改员工密码
     *
     * @param params  旧密码，新密码，新密码的确认密码，用户ID
     * @param session 修改完之后需要把session登陆状态给删除掉，以防止用户没有重新登录就继续操作
     * @return 返回修改密码成功与否的信息
     */
    @PostMapping("/password")
    @Transactional(rollbackFor = Exception.class)
    public R<String> updatePassword(@RequestBody Map<String, Object> params, HttpSession session) {
        String oldPassword = (String) params.get("oldPassword");
        String newPassword = (String) params.get("newPassword");
        String rePassword = (String) params.get("rePassword");
        Long userId = Long.parseLong((String) params.get("userId"));
        if (StringUtils.isEmpty(oldPassword) || StringUtils.isEmpty(newPassword) || StringUtils.isEmpty(rePassword) || userId == null) {
            return R.error("参数异常，请重试！");
        }
        if (!newPassword.equals(rePassword)) {
            return R.error("重复密码错误，请重试！");
        }
        if (!DigestUtils.md5DigestAsHex(oldPassword.getBytes()).equals(employeeService.getById(userId).getPassword())) {
            return R.error("旧密码错误，请重试！");
        }
        if (oldPassword.equals(newPassword)) {
            return R.error("新旧密码重复，请重试！");
        }
        LambdaUpdateWrapper<Employee> updateWrapper = new LambdaUpdateWrapper<Employee>();
        updateWrapper.set(Employee::getPassword, DigestUtils.md5DigestAsHex(newPassword.getBytes()));
        // updateWrapper.set(Employee::getUpdateUser, userId);
        // updateWrapper.set(Employee::getUpdateTime, new Date(System.currentTimeMillis()));
        updateWrapper.eq(Employee::getId, userId);
        employeeService.update(updateWrapper);
        session.removeAttribute("loginUserId");
        return R.success("修改密码成功！请退出后重新登录！");
    }

    /**
     * 修改用户时回显员工信息
     *
     * @param id 回显查询时的id
     * @return 返回查询到的员工信息
     */
    @GetMapping("/{id}")
    public R<Employee> getById(@PathVariable("id") Long id) {
        Employee employee = employeeService.getById(id);
        if (employee != null) {
            return R.success(employee);
        }
        return R.error("没有查询到员工信息");
    }

    /**
     * 逻辑删除员工信息
     *
     * @param employee 员工信息
     * @return 返回删除的结果
     */
    @DeleteMapping("")
    @Transactional(rollbackFor = Exception.class)
    public R<String> delete(@RequestBody Employee employee) {
        // 从ThreadLocal中获取用户Id
        Long loginUserId = BaseContext.getCurrentId();
        Employee user = employeeService.getById(loginUserId);
        if (user == null || !ADMIN_PERMISSIONS.equals(user.getUsername()) || employee.getId() == null) {
            return R.error("权限有误！修改用户状态失败！");
        }
        // employee.setUpdateTime(new Date(System.currentTimeMillis()));
        // employee.setUpdateUser(loginUserId);
        employeeService.remove(employee.getId());
        return R.success("删除成功");
    }
}
