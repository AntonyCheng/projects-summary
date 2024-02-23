package top.sharehome.usercenter.common;

/**
 * 响应码枚举类
 *
 * @author admin
 */
public enum RCodeEnum {

    // 以下（Axxxx，Bxxxx，Cxxxx）完全遵守阿里巴巴开发规范中给出的响应码进行设计

    /**
     * 一切OK 00000
     */
    SUCCESS("00000", "请求正常响应"),

    /**
     * 用户名校验失败 A0110
     */
    USERNAME_VALIDATION_FAILED("A0110", "用户名校验失败"),

    /**
     * 用户名已经存在 A0111
     */
    USERNAME_ALREADY_EXISTS("A0111", "用户名已经存在"),

    /**
     * 用户名包含特殊字符 A0113
     */
    USERNAME_CONTAINS_SPECIAL_CHARACTERS("A0113", "用户名包含特殊字符"),

    /**
     * 密码校验失败 A0120
     */
    PASSWORD_VERIFICATION_FAILED("A0120", "密码校验失败"),

    /**
     * 密码长度不够 A0121
     */
    PASSWORD_LENGTH_IS_NOT_ENOUGH("A0121", "密码长度不够"),

    /**
     * 验证码输入错误 A0130
     */
    INCORRECT_VERIFICATION_CODE("A0130", "验证码输入错误"),

    /**
     * 用户基本信息校验失败 A0150
     */
    USER_BASIC_INFORMATION_VERIFICATION_FAILED("A0150", "用户基本信息校验失败"),

    /**
     * 手机格式校验失败 A0151
     */
    PHONE_FORMAT_VERIFICATION_FAILED("A0151", "手机格式校验失败"),

    /**
     * 邮箱格式校验失败 A0153
     */
    EMAIL_FORMAT_VERIFICATION_FAILED("A0153", "邮箱格式校验失败"),

    /**
     * 用户账户不存在 A0201
     */
    USER_ACCOUNT_DOES_NOT_EXIST("A0201", "用户账户不存在"),

    /**
     * 用户账户被封禁 A0202
     */
    USER_ACCOUNT_BANNED("A0202", "用户账户被封禁"),

    /**
     * 用户密码错误 A0210
     */
    WRONG_USER_PASSWORD("A0210", "用户密码错误"),

    /**
     * 用户登录已过期 A0230
     */
    USER_LOGIN_HAS_EXPIRED("A0230", "用户登录已过期"),

    /**
     * 访问未授权 A0301
     */
    ACCESS_UNAUTHORIZED("A0301", "访问未授权"),

    /**
     * 请求必填参数为空 A0410
     */
    REQUEST_REQUIRED_PARAMETER_IS_EMPTY("A0410", "请求必填参数为空"),

    /**
     * 参数格式不匹配 A0421
     */
    PARAMETER_FORMAT_MISMATCH("A0421", "参数格式不匹配"),

    /**
     * 用户操作异常 A0440
     */
    ABNORMAL_USER_OPERATION("A0440", "用户操作异常"),

    /**
     * 用户上传文件类型不匹配 A0701
     */
    USER_UPLOADED_FILE_TYPE_MISMATCH("A0701", "用户上传文件类型不匹配"),

    /**
     * 用户上传文件太大 A0702
     */
    USER_UPLOADED_FILE_IS_TOO_LARGE("A0702", "用户上传文件太大"),

    /**
     * 用户上传图片太大 A0703
     */
    USER_UPLOADED_IMAGE_TOO_LARGE("A0703", "用户上传图片太大"),

    /**
     * 用户上传视频太大 A0704
     */
    USER_UPLOADED_VIDEO_IS_TOO_LARGE("A0704", "用户上传视频太大"),

    /**
     * 用户设备异常 A1000
     */
    ABNORMAL_USER_EQUIPMENT("A1000", "用户设备异常"),

    /**
     * 中间件服务出错 C0100
     */
    MIDDLEWARE_SERVICE_ERROR("C0100", "中间件服务出错"),

    /**
     * 网关服务出错 C0154
     */
    GATEWAY_SERVICE_ERROR("C0154", "网关服务出错"),

    /**
     * 数据库服务出错 C0300
     */
    ERRORS_OCCURRED_IN_THE_DATABASE_SERVICE("C0300", "数据库服务出错"),


    // 如需添加其他，请先查阅“阿里巴巴开发手册响应码.md”，如果有，请按照上方格式在上方进行添加，如果没有，请按照上方格式在下方进行添加
    // 注释约定：注释名即message内容，后需空格，然后跟上响应码，响应码即code内容
    // 名称约定：按照百度翻译，谷歌翻译等进行直译，全大写，下划线分割单词


    /**
     * 不能删除你自己 D0100
     */
    CAN_NOT_DELETE_YOURSELF("D0100", "不能删除你自己"),

    /**
     * 重复密码不一致 D0101
     */
    DUPLICATE_PASSWORDS_ARE_INCONSISTENT("D0101", "重复密码不一致"),

    /**
     * 用户名长度不够 A0102
     */
    USERNAME_LENGTH_IS_NOT_ENOUGH("A0102", "用户名长度不够"),


    ;

    /**
     * 枚举响应码
     */
    private String code;

    /**
     * 枚举响应消息
     */
    private String message;

    RCodeEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
