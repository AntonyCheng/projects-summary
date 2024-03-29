package top.sharehome.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import top.sharehome.reggie.common.R;
import top.sharehome.reggie.entity.ShoppingCart;
import top.sharehome.reggie.service.ShoppingCartService;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 购物车操作相关接口
 *
 * @author AntonyCheng
 */
@RestController
@RequestMapping("/shoppingCart")
@Slf4j
public class ShoppingCartController {
    @Resource
    private ShoppingCartService shoppingCartService;

    /**
     * 展示购物车全部菜品/套餐
     *
     * @param session 获取用户ID
     * @return 返回指定用户的购物车数据
     */
    @GetMapping("/list")
    public R<List<ShoppingCart>> list(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<ShoppingCart>();
        queryWrapper.eq(ShoppingCart::getUserId, userId);
        queryWrapper.orderByDesc(ShoppingCart::getCreateTime);
        List<ShoppingCart> shoppingCartList = shoppingCartService.list(queryWrapper);
        return R.success(shoppingCartList);
    }

    /**
     * 添加菜品/套餐到购物车
     *
     * @param shoppingCart 购物车实体信息
     * @param session      获取用户ID
     * @return 返回添加信息
     */
    @PostMapping("/add")
    @Transactional(rollbackFor = Exception.class)
    public R<String> add(@RequestBody ShoppingCart shoppingCart, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        shoppingCart.setUserId(userId);
        Long dishId = shoppingCart.getDishId();
        Long setmealId = shoppingCart.getSetmealId();
        if (dishId != null && setmealId != null) {
            return R.success("参数有误，添加失败！");
        }
        if (dishId != null) {
            if (shoppingCartService.getOne(new LambdaQueryWrapper<ShoppingCart>().eq(ShoppingCart::getUserId, userId).eq(ShoppingCart::getDishId, dishId)) == null) {
                shoppingCartService.save(shoppingCart);
                return R.success("增添菜品成功！");
            }
            shoppingCartService.update(new LambdaUpdateWrapper<ShoppingCart>().setSql("number = number + 1").eq(ShoppingCart::getUserId, userId).eq(ShoppingCart::getDishId, dishId));
            return R.success("增添菜品成功！");
        }
        if (setmealId != null) {
            if (shoppingCartService.getOne(new LambdaQueryWrapper<ShoppingCart>().eq(ShoppingCart::getUserId, userId).eq(ShoppingCart::getSetmealId, setmealId)) == null) {
                shoppingCartService.save(shoppingCart);
                return R.success("增添套餐成功！");
            }
            shoppingCartService.update(new LambdaUpdateWrapper<ShoppingCart>().setSql("number = number + 1").eq(ShoppingCart::getUserId, userId).eq(ShoppingCart::getSetmealId, setmealId));
            return R.success("增添套餐成功！");
        }
        return R.success("参数有误，增添失败！");
    }

    /**
     * 减少菜品/套餐到购物车
     *
     * @param shoppingCart 购物车实体信息
     * @param session      获取用户ID
     * @return 返回减少信息
     */
    @PostMapping("/sub")
    @Transactional(rollbackFor = Exception.class)
    public R<String> sub(@RequestBody ShoppingCart shoppingCart, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        Long dishId = shoppingCart.getDishId();
        Long setmealId = shoppingCart.getSetmealId();
        if (dishId != null && setmealId != null) {
            return R.success("参数有误，添加失败！");
        }
        if (dishId != null) {
            if (shoppingCartService.getOne(
                    new LambdaQueryWrapper<ShoppingCart>()
                            .eq(ShoppingCart::getUserId, userId)
                            .eq(ShoppingCart::getDishId, dishId)).getNumber() > 1
            ) {
                shoppingCartService.update(
                        new LambdaUpdateWrapper<ShoppingCart>().
                                setSql("number = number - 1")
                                .eq(ShoppingCart::getUserId, userId)
                                .eq(ShoppingCart::getDishId, dishId)
                );
                return R.success("减少菜品成功！");
            }

            shoppingCartService.update(
                    new LambdaUpdateWrapper<ShoppingCart>()
                            .set(ShoppingCart::getName, shoppingCartService.getOne(
                                    new LambdaQueryWrapper<ShoppingCart>()
                                            .eq(ShoppingCart::getUserId, userId)
                                            .eq(ShoppingCart::getDishId, dishId)).getName() + "[" + System.currentTimeMillis() + "]")
                            .eq(ShoppingCart::getUserId, userId)
                            .eq(ShoppingCart::getDishId, dishId)
            );

            shoppingCartService.remove(
                    new LambdaQueryWrapper<ShoppingCart>()
                            .eq(ShoppingCart::getUserId, userId)
                            .eq(ShoppingCart::getDishId, dishId)
            );
        }
        if (setmealId != null) {
            if (shoppingCartService.getOne(
                    new LambdaQueryWrapper<ShoppingCart>()
                            .eq(ShoppingCart::getUserId, userId)
                            .eq(ShoppingCart::getSetmealId, setmealId)).getNumber() > 1
            ) {
                shoppingCartService.update(
                        new LambdaUpdateWrapper<ShoppingCart>().
                                setSql("number = number - 1")
                                .eq(ShoppingCart::getUserId, userId)
                                .eq(ShoppingCart::getSetmealId, setmealId)
                );
                return R.success("减少套餐成功！");
            }

            shoppingCartService.update(
                    new LambdaUpdateWrapper<ShoppingCart>()
                            .set(ShoppingCart::getName, shoppingCartService.getOne(
                                    new LambdaQueryWrapper<ShoppingCart>()
                                            .eq(ShoppingCart::getUserId, userId)
                                            .eq(ShoppingCart::getSetmealId, setmealId)).getName() + "[" + System.currentTimeMillis() + "]")
                            .eq(ShoppingCart::getUserId, userId)
                            .eq(ShoppingCart::getSetmealId, setmealId)
            );

            shoppingCartService.remove(
                    new LambdaQueryWrapper<ShoppingCart>()
                            .eq(ShoppingCart::getUserId, userId)
                            .eq(ShoppingCart::getSetmealId, setmealId)
            );
        }
        return R.success("参数有误，添加失败！");
    }

    /**
     * 清空购物车
     *
     * @param session 用户获取用户ID
     * @return 返回清空信息
     */
    @DeleteMapping("/clean")
    @Transactional(rollbackFor = Exception.class)
    public R<String> clean(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        Long nowTime = System.currentTimeMillis();
        List<ShoppingCart> shoppingCartList = shoppingCartService.list(new LambdaQueryWrapper<ShoppingCart>().eq(ShoppingCart::getUserId, userId));
        shoppingCartList = shoppingCartList.stream().map(shoppingCart -> {
            shoppingCart.setName(shoppingCart.getName() + "[" + nowTime + "]");
            return shoppingCart;
        }).collect(Collectors.toList());
        shoppingCartService.updateBatchById(shoppingCartList);

        shoppingCartService.remove(new LambdaQueryWrapper<ShoppingCart>().eq(ShoppingCart::getUserId, userId));
        return R.success("清空购物车成功！");
    }

}
