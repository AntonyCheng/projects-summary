package top.sharehome.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.sharehome.reggie.entity.AddressBook;
import top.sharehome.reggie.mapper.AddressBookMapper;
import top.sharehome.reggie.service.AddressBookService;

/**
 * 地址相关服务实现类
 *
 * @author AntonyCheng
 */
@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressBookService {

}
