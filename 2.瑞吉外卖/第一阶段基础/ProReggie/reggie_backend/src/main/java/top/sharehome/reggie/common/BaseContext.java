package top.sharehome.reggie.common;

/**
 * @Description 基于ThreadLocal的封装类，用于使用ThreadLocal在线程中传输值
 * @Author:AntonyCheng
 * @CreateTime:2023/1/30 22:25
 */

public class BaseContext {
    private static ThreadLocal<Long> threadLocal = new ThreadLocal<Long>();

    /**
     * 在ThreadLocal中设置值
     *
     * @param id
     */
    public static void setCurrentId(Long id) {
        threadLocal.set(id);

    }

    /**
     * 在ThreadLocal中获取值
     *
     * @return
     */
    public static Long getCurrentId() {
        return threadLocal.get();
    }

    /**
     * 销毁ThreadLocal中的值
     */
    public static void removeCurrentId() {
        threadLocal.remove();
    }

}
