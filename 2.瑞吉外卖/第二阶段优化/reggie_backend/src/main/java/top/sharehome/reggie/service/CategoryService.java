package top.sharehome.reggie.service;

import top.sharehome.reggie.entity.Category;
import com.baomidou.mybatisplus.extension.service.IService;
/**
 * 菜品分类相关服务类
 *
 * @author AntonyCheng
 */
public interface CategoryService extends IService<Category> {
    public void remove(Long id);

}
