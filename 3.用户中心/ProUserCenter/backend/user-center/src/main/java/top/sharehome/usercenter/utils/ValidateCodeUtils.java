package top.sharehome.usercenter.utils;

import java.util.Random;

/**
 * 随机生成验证码工具类
 *
 * @author AntonyCheng
 */
public class ValidateCodeUtils {
    /**
     * 防止硬编码
     */
    private static final Integer NUMBER_0 = 0;
    private static final Integer NUMBER_4 = 4;
    private static final Integer NUMBER_6 = 6;
    private static final Integer NUMBER_1000 = 1000;
    private static final Integer NUMBER_9999 = 9999;
    private static final Integer NUMBER_100000 = 100000;
    private static final Integer NUMBER_999999 = 999999;

    /**
     * 限制位数的枚举类
     */
    enum LimitValidateNumber {
        FOUR(4), SIX(6);

        private Integer number;

        LimitValidateNumber(Integer number) {
            this.number = number;
        }

        public Integer getNumber() {
            return number;
        }
    }

    /**
     * 随机生成验证码
     *
     * @param limitEnum 长度为4位或者6位
     * @return
     */
    public static Integer generateValidateCode(ValidateCodeUtils.LimitValidateNumber limitEnum) {
        Integer code = null;
        if (limitEnum.getNumber().equals(NUMBER_4)) {
            //生成随机数，最大为9999
            code = new Random().nextInt(NUMBER_9999);
            if (code < NUMBER_1000) {
                //保证随机数为4位数字
                code = code + NUMBER_1000;
            }
        } else if (limitEnum.getNumber().equals(NUMBER_6)) {
            //生成随机数，最大为999999
            code = new Random().nextInt(NUMBER_999999);
            if (code < NUMBER_100000) {
                //保证随机数为6位数字
                code = code + NUMBER_100000;
            }
        } else {
            throw new RuntimeException("只能生成4位或6位数字验证码");
        }
        return code;
    }

    /**
     * 随机生成指定长度字符串验证码
     *
     * @param length 长度
     * @return
     */
    public static String generateValidateCode4String(int length) {
        Random rdm = new Random();
        String hash1 = Integer.toHexString(rdm.nextInt());
        return hash1.substring(NUMBER_0, length);
    }
}
