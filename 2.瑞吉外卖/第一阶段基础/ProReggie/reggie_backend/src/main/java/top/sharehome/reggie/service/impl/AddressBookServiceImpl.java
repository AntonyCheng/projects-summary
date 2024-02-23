package top.sharehome.reggie.service.impl;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import top.sharehome.reggie.entity.AddressBook;
import top.sharehome.reggie.mapper.AddressBookMapper;
import top.sharehome.reggie.service.AddressBookService;
@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressBookService{

}
