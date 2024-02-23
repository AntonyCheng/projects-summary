package top.sharehome.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import top.sharehome.reggie.entity.AddressBook;

/**
 * 地址相关Mapper
 *
 * @author AntonyCheng
 */
@Mapper
public interface AddressBookMapper extends BaseMapper<AddressBook> {
}