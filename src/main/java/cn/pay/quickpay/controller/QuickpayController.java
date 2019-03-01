package cn.pay.quickpay.controller;

import cn.pay.quickpay.VO.ResultVO;
import cn.pay.quickpay.enums.ResultEnum;
import cn.pay.quickpay.exception.QuickPayException;
import cn.pay.quickpay.form.QuickpayForm;
import cn.pay.quickpay.service.QuickpayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

/**
 * @author bmr
 * @time 2019-02-14 17:32
 * 项目入口controller
 */
@RestController
@Slf4j
public class QuickpayController {

    @Autowired
    private QuickpayService quickpayService;

    @RequestMapping("/")
    public String index(){
        return "欢迎使用聚合支付";
    }

    @RequestMapping("/gateway")
    public ResultVO gateway(@Valid QuickpayForm quickpayForm, BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response) {

        if (bindingResult.hasErrors()) {
            log.error("【请求入口controller】参数不正确，quickpayForm={}", quickpayForm);
            throw new QuickPayException(ResultEnum.PARAM_ERROR.getCode(),
                    bindingResult.getFieldError().getDefaultMessage());
        }

        return quickpayService.checkChannel(quickpayForm, request,response);



    }











}
