package cn.pay.quickpay.enums;

import lombok.Getter;

/**
 * @author bmr
 * @time 2019-02-22 10:43
 * 业务类型枚举
 */
@Getter
public enum BizTypeEnum {
    REAL_AUTH("realAuth","实名认证"),
    ADD_MERCHANT_INFO("addMem","商户进件"),
    UPDATE_MERCHANT_INFO("updateMem","商户进件信息修改"),
    ADD_ORDER("addOrder","商户预下单"),
    PAY_CONFIRM("quickPayConfirm","确认支付"),
    QUERY_ORDER("queryOrder","订单查询"),
    PAY_FOR_ANOTHER("payForAnotherOne","代付"),
    OPEN_CARD_CODE("openCardCode","绑卡发送短信验证码"),
    OPEN_CARD("openCard","确认绑卡"),
    GET_AGENT_BALANCE("agentBalance","机构账户余额查询"),

    ;

    private String bizTypeCode;

    private String bizTypeMessage;

    BizTypeEnum(String bizTypeCode, String bizTypeMessage) {
        this.bizTypeCode = bizTypeCode;
        this.bizTypeMessage = bizTypeMessage;
    }
}
