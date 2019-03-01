package cn.pay.quickpay.handler;

import cn.pay.quickpay.VO.ResultVO;
import cn.pay.quickpay.exception.QuickPayException;
import cn.pay.quickpay.utils.platform.ResultVOUtil;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author bmr
 * @time 2019-02-15 08:14:51
 */
@ControllerAdvice
public class QuickpayExceptionHandler {


    //捕获控制层抛出的异常信息进行处理
    @ExceptionHandler(value = QuickPayException.class)
    @ResponseBody
    public ResultVO handlerSellerException(QuickPayException e){
        return ResultVOUtil.error(e.getCode(),e.getMessage());
    }
}
