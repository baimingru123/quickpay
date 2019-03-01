package cn.pay.quickpay.form;

import lombok.Data;

import javax.validation.constraints.NotEmpty;


/**
 * @author bmr
 * @time 2019-02-14 17:42
 */
@Data
public class QuickpayForm {

    @NotEmpty(message = "机构号必填")
    private String agentNo;

    @NotEmpty(message = "业务类型必填")
    private String bizType;

    @NotEmpty(message = "签名必填")
    private String sign;

    private String platformMerNo;



}
