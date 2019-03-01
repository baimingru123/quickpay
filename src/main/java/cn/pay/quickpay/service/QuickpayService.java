package cn.pay.quickpay.service;

import cn.pay.quickpay.VO.ResultVO;
import cn.pay.quickpay.form.QuickpayForm;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author bmr
 * @time 2019-02-15 8:19
 */
public interface QuickpayService {

    ResultVO checkChannel(QuickpayForm quickpayForm, HttpServletRequest request, HttpServletResponse response);
}
