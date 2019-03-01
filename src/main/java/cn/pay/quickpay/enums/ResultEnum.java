package cn.pay.quickpay.enums;

import lombok.Getter;

/**
 * @author bmr
 * @time 2019-01-16 13:58
 */
@Getter
public enum ResultEnum {

    SUCCESS("0000","成功"),

    PARAM_ERROR("0001","参数不正确"),

    AGENT_NOT_EXIST("0002","机构不存在"),

    AGENT_TYPE_ERROR("0003","机构类型错误，不可用该接口"),

    MERCHANT_NOT_EXIST("0004","商户不存在"),

    MERCHANT_STATUS_ERROR("0005","商户状态异常"),

    SIGN_CHECK_ERROR("0006","签名校验失败"),

    AGENT_AUTH_FEE_INSUFFICIENT("0007","机构鉴权费用不足"),

    CHANNEL_CANNOT_USE("0008","通道不可用"),

    AGENT_NOT_SUPPORT_OPEN_CHANNEL("0009","机构不支持开通此类通道"),

    GET_CHANNEL_CODE_ERROR("0010","获取通道code异常"),

    CHANNEL_NOT_EXIST("0011","通道不存在"),

    MERCHANT_OPEN_TRANS_FEE_ERROR("0012","商户进件交易费率不能低于渠道签约费率"),

    MERCHANT_OPEN_TRANS_FEE_SCORE_ERROR("0013","商户进件交易费率不在渠道设定费率范围内"),

    MERCHANT_OPEN_DF_FEE_ERROR("0014","商户进件代付费率不能低于渠道签约费率"),

    MERCHANT_OPEN_DF_FEE_SCORE_ERROR("0015","商户进件代付费率不在渠道设定费率范围内"),

    MERCHANT_OPEN_DH_FEE_ERROR("0016","商户进件代还费不能低于渠道签约费率"),

    MERCHANT_OPEN_DH_FEE_SCORE_ERROR("0017","商户进件代还费用不在渠道设定费率范围内"),

    MERCHANT_OPEN_JS_FEE_ERROR("0018","商户进件结算费不能低于渠道签约费率"),

    MERCHANT_OPEN_JS_FEE_SCORE_ERROR("0019","商户进件结算费用不在渠道设定费率范围内"),

    MERCHANT_OPEN_HK_FEE_ERROR("0020","商户进件划扣费不能低于渠道签约费率"),

    MERCHANT_OPEN_HK_FEE_SCORE_ERROR("0021","商户进件划扣费用不在渠道设定费率范围内"),

    NO_AUTH("0022","未进行实名认证"),

    NO_PASS_AUTH("0023","实名认证未通过"),

    ORDER_NOT_EXIST("0024","订单不存在"),

    MERCHANT_TRANS_FEE_SCORE_ERROR("0025","商户交易费率在渠道设定费率范围内"),

    MERCHANT_DF_FEE_SCORE_ERROR("0026","商户交易代付费不在渠道设定费率范围内"),

    MERCHANT_JS_FEE_SCORE_ERROR("0027","商户结算费用不在渠道设定费率范围内"),

    TRANS_TIME_SCORE_ERROR("0028","交易时间不合法"),

    TRANS_MONEY_SCORE_ERROR("0029","交易单笔限额不合法"),

    ORDER_NO_IS_EXIST("0030","订单号已存在"),

    NOT_SUPPORT_TRANS_TYPE("0031","不支持此类交易"),

    AGENT_HAVE_NULL_USED_CHANNEL("0032","机构无此类可用通道"),

    BIZ_TYPE_ERROR("0033","bizType不正确"),

    AGENT_DF_BALANCE_INSUFFICIENT("0034","机构代付余额不足"),

    TRANS_FAIL("0035","交易失败"),

    SYSTEM_ERROR("1000","系统异常"),







//    CONVERT_ERROR(2,"转换异常"),
//
//    CART_IS_EMPTY(3,"购物车信息为空"),
//
//    ORDER_OWNER_ERROR(4,"该订单不属于当前用户"),
//
//    WECHAT_ERROR(5,"微信方面异常"),
//
//    LOGIN_FAIL(6,"登录失败，登录信息不正确"),
//
//    LOGOUT_SUCCESS(7,"登出成功"),
//
//    PRODUCT_NOT_EXIST(10,"商品不存在"),
//
//    PRODUCT_STOCK_ERROR(11,"库存不正确"),
//
//    ORDER_NOT_EXIST(12,"订单不存在"),
//
//    ORDERDETAIL_NOT_EXIST(12,"订单详情不存在"),
//
//    ORDER_STATUS_ERROR(13,"订单状态不正确"),
//
//    ORDER_STATUS_UPDATE_ERROR(14,"订单状态修改失败"),
//
//    ORDER_PAY_STATUS_ERROR(15,"订单支付状态不正确"),
//
//    WXPAY_NOTIFY_MONEY_VERIFY_ERROR(16,"微信支付异步通知金额校验不通过"),
//
//    ORDER_CANCEL_SUCCESS(17,"订单取消成功"),
//
//    ORDER_FINISH_SUCCESS(18,"订单完结成功"),
//
//    PRODUCT_STATUS_ERROR(19,"商品状态错误"),
//
//    PRODUCT_ON_SALE_SUCCESS(20,"商品上架成功"),
//
//    PRODUCT_OFF_SALE_SUCCESS(21,"商品下架成功"),
    ;

    private String code;

    private String message;

    ResultEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
