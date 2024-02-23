package top.sharehome.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import top.sharehome.reggie.common.R;
import top.sharehome.reggie.entity.AddressBook;
import top.sharehome.reggie.service.AddressBookService;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

/**
 * 用户地址相关接口
 *
 * @author AntonyCheng
 */
@RestController
@RequestMapping("/addressBook")
@Slf4j
public class AddressBookController {
    @Resource
    private AddressBookService addressBookService;

    /**
     * 查询指定用户的全部地址信息
     *
     * @param session 获取userId
     * @return 返回对应指定用户的地址列表
     */
    @GetMapping("/list")
    public R<List<AddressBook>> list(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper<AddressBook>();
        queryWrapper.eq(AddressBook::getUserId, userId);
        queryWrapper.orderByDesc(AddressBook::getIsDefault);
        queryWrapper.orderByAsc(AddressBook::getCreateTime);
        List<AddressBook> addressBookList = addressBookService.list(queryWrapper);
        return R.success(addressBookList);
    }

    /**
     * 新增地址
     *
     * @param addressBook 从前端传来的地址对象
     * @param session     需要进行userId的定位
     * @return 新增结果信息
     */
    @PostMapping("")
    @Transactional(rollbackFor = Exception.class)
    public R<String> save(@RequestBody AddressBook addressBook, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        addressBook.setUserId(userId);
        addressBook.setIsDefault(addressBookService.list(new LambdaQueryWrapper<AddressBook>().eq(AddressBook::getUserId, userId)).size() == 0);
        addressBookService.save(addressBook);
        return R.success("新增地址成功！");
    }

    /**
     * 修改默认地址
     *
     * @param map     从前端传来的id信息
     * @param session 用于userId的定位
     * @return 更新结果信息
     */
    @PutMapping("/default")
    @Transactional(rollbackFor = Exception.class)
    public R<String> setDefault(@RequestBody Map<String, Long> map, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        Long id = map.get("id");
        if (addressBookService.getById(id).getIsDefault()) {
            return R.error("该地址已经是默认地址，不可更改！");
        }

        LambdaUpdateWrapper<AddressBook> updateWrapperOfOld = new LambdaUpdateWrapper<AddressBook>();
        updateWrapperOfOld.set(AddressBook::getIsDefault, false);
        updateWrapperOfOld.eq(AddressBook::getIsDefault, true);
        updateWrapperOfOld.eq(AddressBook::getUserId, userId);
        addressBookService.update(updateWrapperOfOld);

        LambdaUpdateWrapper<AddressBook> updateWrapperOfNew = new LambdaUpdateWrapper<AddressBook>();
        updateWrapperOfNew.set(AddressBook::getIsDefault, true);
        updateWrapperOfNew.eq(AddressBook::getUserId, userId);
        updateWrapperOfNew.eq(AddressBook::getId, id);
        addressBookService.update(updateWrapperOfNew);

        return R.success("修改默认地址成功！");
    }

    /**
     * 根据id查询地址
     *
     * @param id 前端传来的id
     * @return 返回查询得到的地址
     */
    @GetMapping("/{id}")
    public R<AddressBook> get(@PathVariable("id") Long id) {
        AddressBook addressBook = addressBookService.getById(id);
        return R.success(addressBook);
    }

    /**
     * 在支付页面添加默认地址
     *
     * @param session 为了获取用户ID
     * @return 返回默认地址
     */
    @GetMapping("/default")
    public R<AddressBook> getDefault(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        AddressBook addressBook = addressBookService.getOne(new LambdaQueryWrapper<AddressBook>().eq(AddressBook::getUserId, userId).eq(AddressBook::getIsDefault, true));
        if (addressBook == null) {
            R.error("获取默认地址失败！");
        }
        return R.success(addressBook);
    }

    /**
     * 删除所选地址
     *
     * @param ids 地址的id
     * @return 返回删除的结果
     */
    @DeleteMapping("")
    @Transactional(rollbackFor = Exception.class)
    public R<String> delete(@RequestParam("ids") Long ids) {
        AddressBook addressBook = addressBookService.getById(ids);
        if (addressBook.getIsDefault()) {
            return R.error("无法删除默认地址！");
        }
        addressBookService.update(new LambdaUpdateWrapper<AddressBook>().set(AddressBook::getConsignee, addressBook.getConsignee() + "[" + System.currentTimeMillis() + "]").eq(AddressBook::getId, addressBook.getId()));
        addressBookService.removeById(ids);
        return R.success("删除成功！");
    }
}
