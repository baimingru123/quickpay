package cn.pay.quickpay.exception;

import cn.pay.quickpay.enums.ResultEnum;
import lombok.Getter;

/**
 * @author bmr
 * @time 2019-02-14 17:52
 */
@Getter
public class QuickPayException extends RuntimeException {

    private String code;

    public QuickPayException(ResultEnum resultEnum) {
        super(resultEnum.getMessage());
        this.code = resultEnum.getCode();
    }

    public QuickPayException(String code,String message) {
        super(message);
        this.code = code;
    }
}
