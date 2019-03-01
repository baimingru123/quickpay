package cn.pay.quickpay.valid;

import cn.pay.quickpay.VO.ResultVO;
import cn.pay.quickpay.enums.BizTypeEnum;
import cn.pay.quickpay.enums.PayTypeEnum;
import cn.pay.quickpay.enums.ResultEnum;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * 校验api接口参数
 * @author bmr
 * @time 2019-02-15 14:32
 */
public class QuickpayRequsetValid {

    public static ResultVO realAuthParamVaild(HttpServletRequest request){
        ResultVO resultVO=new ResultVO();

        if(StringUtils.isEmpty(request.getParameter("idCard"))){
            resultVO.setRetCode(ResultEnum.PARAM_ERROR.getCode());
            resultVO.setRetMsg("身份证号必填");
            return resultVO;
        }

        if(StringUtils.isEmpty(request.getParameter("settleBankCardNo"))){
            resultVO.setRetCode(ResultEnum.PARAM_ERROR.getCode());
            resultVO.setRetMsg("结算卡号必填");
            return resultVO;
        }

        if(StringUtils.isEmpty(request.getParameter("settleName"))){
            resultVO.setRetCode(ResultEnum.PARAM_ERROR.getCode());
            resultVO.setRetMsg("开户姓名必填");
            return resultVO;
        }

        if(StringUtils.isEmpty(request.getParameter("settlePhone"))){
            resultVO.setRetCode(ResultEnum.PARAM_ERROR.getCode());
            resultVO.setRetMsg("银行预留手机号必填");
            return resultVO;
        }

        resultVO.setRetCode(ResultEnum.SUCCESS.getCode());
        return resultVO;
    }


    public static ResultVO addMerParamVaild(HttpServletRequest request){
        ResultVO resultVO=new ResultVO();

        if(StringUtils.isEmpty(request.getParameter("idCard"))){
            resultVO.setRetCode(ResultEnum.PARAM_ERROR.getCode());
            resultVO.setRetMsg("身份证号必填");
            return resultVO;
        }

        if(StringUtils.isEmpty(request.getParameter("merNo"))){
            resultVO.setRetCode(ResultEnum.PARAM_ERROR.getCode());
            resultVO.setRetMsg("机构唯一商户号必填");
            return resultVO;
        }

        if(StringUtils.isEmpty(request.getParameter("merName"))){
            resultVO.setRetCode(ResultEnum.PARAM_ERROR.getCode());
            resultVO.setRetMsg("商户名称必填");
            return resultVO;
        }

        if(request.getParameter("merName").length()>12||request.getParameter("merName").length()<5){
            resultVO.setRetCode(ResultEnum.PARAM_ERROR.getCode());
            resultVO.setRetMsg("商户名称5-12字");
            return resultVO;
        }

        if(StringUtils.isEmpty(request.getParameter("merAddress"))){
            resultVO.setRetCode(ResultEnum.PARAM_ERROR.getCode());
            resultVO.setRetMsg("商户地址必填");
            return resultVO;
        }

        if(StringUtils.isEmpty(request.getParameter("settleBank"))){
            resultVO.setRetCode(ResultEnum.PARAM_ERROR.getCode());
            resultVO.setRetMsg("开户银行必填");
            return resultVO;
        }

        if(StringUtils.isEmpty(request.getParameter("settleBankBranch"))){
            resultVO.setRetCode(ResultEnum.PARAM_ERROR.getCode());
            resultVO.setRetMsg("联行号号必填");
            return resultVO;
        }

        if(StringUtils.isEmpty(request.getParameter("settleBankCardNo"))){
            resultVO.setRetCode(ResultEnum.PARAM_ERROR.getCode());
            resultVO.setRetMsg("结算卡号必填");
            return resultVO;
        }

        if(StringUtils.isEmpty(request.getParameter("settleBankNo"))){
            resultVO.setRetCode(ResultEnum.PARAM_ERROR.getCode());
            resultVO.setRetMsg("开户行简码必填");
            return resultVO;
        }

        if(StringUtils.isEmpty(request.getParameter("settleBankSub"))){
            resultVO.setRetCode(ResultEnum.PARAM_ERROR.getCode());
            resultVO.setRetMsg("开户支行必填");
            return resultVO;
        }

        if(StringUtils.isEmpty(request.getParameter("settleName"))){
            resultVO.setRetCode(ResultEnum.PARAM_ERROR.getCode());
            resultVO.setRetMsg("开户姓名必填");
            return resultVO;
        }

        if(StringUtils.isEmpty(request.getParameter("settlePhone"))){
            resultVO.setRetCode(ResultEnum.PARAM_ERROR.getCode());
            resultVO.setRetMsg("银行预留手机号必填");
            return resultVO;
        }

        if(StringUtils.isEmpty(request.getParameter("settleSubProvince"))){
            resultVO.setRetCode(ResultEnum.PARAM_ERROR.getCode());
            resultVO.setRetMsg("结算卡开户省份必填");
            return resultVO;
        }

        if(StringUtils.isEmpty(request.getParameter("settleSubCity"))){
            resultVO.setRetCode(ResultEnum.PARAM_ERROR.getCode());
            resultVO.setRetMsg("结算卡开户市必填");
            return resultVO;
        }

        if(StringUtils.isEmpty(request.getParameter("fee0")) && StringUtils.isEmpty(request.getParameter("d0fee"))
                && StringUtils.isEmpty(request.getParameter("noIntegralFee0")) && StringUtils.isEmpty(request.getParameter("noIntegralFee"))
                && StringUtils.isEmpty(request.getParameter("repayFee0")) && StringUtils.isEmpty(request.getParameter("repayFee"))
                && StringUtils.isEmpty(request.getParameter("repayFee1")) && StringUtils.isEmpty(request.getParameter("delimitFee0"))
                && StringUtils.isEmpty(request.getParameter("delimitFee")) && StringUtils.isEmpty(request.getParameter("withholdFee"))
                && StringUtils.isEmpty(request.getParameter("balanceFee")) ){
            resultVO.setRetCode(ResultEnum.PARAM_ERROR.getCode());
            resultVO.setRetMsg("请输入正确的费率，有积分、无积分、智能代还、结算、划扣必填一项");
            return resultVO;
        }

        resultVO.setRetCode(ResultEnum.SUCCESS.getCode());
        return resultVO;
    }


    /**
     * 校验更新商户入驻信息时的请求参数
     * @param request
     * @return
     */
    public static ResultVO updateMerParamVaild(HttpServletRequest request){
        ResultVO resultVO=new ResultVO();

        if(StringUtils.isEmpty(request.getParameter("platformMerNo"))){
            resultVO.setRetCode(ResultEnum.PARAM_ERROR.getCode());
            resultVO.setRetMsg("平台商户号必填");
            return resultVO;
        }

        if(StringUtils.isEmpty(request.getParameter("merFlag"))){
            resultVO.setRetCode(ResultEnum.PARAM_ERROR.getCode());
            resultVO.setRetMsg("修改类型必填");
            return resultVO;
        }

        if(request.getParameter("merFlag").equals(1)){
            if(StringUtils.isEmpty(request.getParameter("idCard"))){
                resultVO.setRetCode(ResultEnum.PARAM_ERROR.getCode());
                resultVO.setRetMsg("身份证号必填");
                return resultVO;
            }

            if(StringUtils.isEmpty(request.getParameter("settleBank"))){
                resultVO.setRetCode(ResultEnum.PARAM_ERROR.getCode());
                resultVO.setRetMsg("开户银行必填");
                return resultVO;
            }

            if(StringUtils.isEmpty(request.getParameter("settleBankBranch"))){
                resultVO.setRetCode(ResultEnum.PARAM_ERROR.getCode());
                resultVO.setRetMsg("联行号号必填");
                return resultVO;
            }

            if(StringUtils.isEmpty(request.getParameter("settleBankCardNo"))){
                resultVO.setRetCode(ResultEnum.PARAM_ERROR.getCode());
                resultVO.setRetMsg("结算卡号必填");
                return resultVO;
            }

            if(StringUtils.isEmpty(request.getParameter("settleBankNo"))){
                resultVO.setRetCode(ResultEnum.PARAM_ERROR.getCode());
                resultVO.setRetMsg("开户行简码必填");
                return resultVO;
            }

            if(StringUtils.isEmpty(request.getParameter("settleBankSub"))){
                resultVO.setRetCode(ResultEnum.PARAM_ERROR.getCode());
                resultVO.setRetMsg("开户支行必填");
                return resultVO;
            }

            if(StringUtils.isEmpty(request.getParameter("settleName"))){
                resultVO.setRetCode(ResultEnum.PARAM_ERROR.getCode());
                resultVO.setRetMsg("开户姓名必填");
                return resultVO;
            }

            if(StringUtils.isEmpty(request.getParameter("settlePhone"))){
                resultVO.setRetCode(ResultEnum.PARAM_ERROR.getCode());
                resultVO.setRetMsg("银行预留手机号必填");
                return resultVO;
            }

            if(StringUtils.isEmpty(request.getParameter("settleSubProvince"))){
                resultVO.setRetCode(ResultEnum.PARAM_ERROR.getCode());
                resultVO.setRetMsg("结算卡开户省份必填");
                return resultVO;
            }

            if(StringUtils.isEmpty(request.getParameter("settleSubCity"))){
                resultVO.setRetCode(ResultEnum.PARAM_ERROR.getCode());
                resultVO.setRetMsg("结算卡开户市必填");
                return resultVO;
            }
        }

        if(request.getParameter("merFlag").equals(2)){
            if(StringUtils.isEmpty(request.getParameter("fee0")) && StringUtils.isEmpty(request.getParameter("d0fee"))
                    && StringUtils.isEmpty(request.getParameter("noIntegralFee0")) && StringUtils.isEmpty(request.getParameter("noIntegralFee"))
                    && StringUtils.isEmpty(request.getParameter("repayFee0")) && StringUtils.isEmpty(request.getParameter("repayFee"))
                    && StringUtils.isEmpty(request.getParameter("repayFee1")) && StringUtils.isEmpty(request.getParameter("delimitFee0"))
                    && StringUtils.isEmpty(request.getParameter("delimitFee")) && StringUtils.isEmpty(request.getParameter("withholdFee"))
                    && StringUtils.isEmpty(request.getParameter("balanceFee")) ){
                resultVO.setRetCode(ResultEnum.PARAM_ERROR.getCode());
                resultVO.setRetMsg("请输入正确的费率，有积分、无积分、智能代还、结算、划扣必填一项");
                return resultVO;
            }
        }

        resultVO.setRetCode(ResultEnum.SUCCESS.getCode());
        return resultVO;
    }


    /**
     * 校验预下单时的请求参数
     * @param request
     * @return
     */
    public static ResultVO addOrderParamVaild(HttpServletRequest request){
        ResultVO resultVO=new ResultVO();

        if(StringUtils.isEmpty(request.getParameter("platformMerNo"))){
            resultVO.setRetCode(ResultEnum.PARAM_ERROR.getCode());
            resultVO.setRetMsg("平台商户号必填");
            return resultVO;
        }

        if(StringUtils.isEmpty(request.getParameter("orderNumber"))){
            resultVO.setRetCode(ResultEnum.PARAM_ERROR.getCode());
            resultVO.setRetMsg("商户订单号必填");
            return resultVO;
        }

        if(StringUtils.isEmpty(request.getParameter("payerName"))){
            resultVO.setRetCode(ResultEnum.PARAM_ERROR.getCode());
            resultVO.setRetMsg("付款人姓名必填");
            return resultVO;
        }

        if(StringUtils.isEmpty(request.getParameter("idCardNo"))){
            resultVO.setRetCode(ResultEnum.PARAM_ERROR.getCode());
            resultVO.setRetMsg("身份证号必填");
            return resultVO;
        }

        if(StringUtils.isEmpty(request.getParameter("bankCardNo"))){
            resultVO.setRetCode(ResultEnum.PARAM_ERROR.getCode());
            resultVO.setRetMsg("付款卡号必填");
            return resultVO;
        }

        if(StringUtils.isEmpty(request.getParameter("bankCardName"))){
            resultVO.setRetCode(ResultEnum.PARAM_ERROR.getCode());
            resultVO.setRetMsg("付款卡银行名称必填");
            return resultVO;
        }

        if(StringUtils.isEmpty(request.getParameter("phone"))){
            resultVO.setRetCode(ResultEnum.PARAM_ERROR.getCode());
            resultVO.setRetMsg("银行预留手机号必填");
            return resultVO;
        }

        if(StringUtils.isEmpty(request.getParameter("orderAmount"))){
            resultVO.setRetCode(ResultEnum.PARAM_ERROR.getCode());
            resultVO.setRetMsg("订单金额必填");
            return resultVO;
        }

        if(StringUtils.isEmpty(request.getParameter("notifyUrl"))){
            resultVO.setRetCode(ResultEnum.PARAM_ERROR.getCode());
            resultVO.setRetMsg("异步通知地址必填");
            return resultVO;
        }


        if(!StringUtils.isEmpty(request.getParameter("type"))){
            int type=Integer.valueOf(request.getParameter("type"));
            if(!PayTypeEnum.contains(type)){
                resultVO.setRetCode(ResultEnum.PARAM_ERROR.getCode());
                resultVO.setRetMsg("请填写正确的交易类型");
                return resultVO;
            }

            if(type==3 && !PayTypeEnum.PAY_FOR_ANOTHER.getBizTypeDesc().equals(request.getParameter("bizType"))){
                resultVO.setRetCode(ResultEnum.PARAM_ERROR.getCode());
                resultVO.setRetMsg("业务类型和交易类型不符");
                return resultVO;
            }


            if(type==5){
                int payType=Integer.valueOf(request.getParameter("payType"));
                if(payType!=5 && payType!=6){
                    resultVO.setRetCode(ResultEnum.PARAM_ERROR.getCode());
                    resultVO.setRetMsg("请填入支付方式：5协议支付、6代扣");
                }
            }
        }


        resultVO.setRetCode(ResultEnum.SUCCESS.getCode());
        return resultVO;
    }


    /**
     * 校验确认支付时的请求参数
     * @param request
     * @return
     */
    public static ResultVO payConfirmParamVaild(HttpServletRequest request){
        ResultVO resultVO=new ResultVO();


        if(StringUtils.isEmpty(request.getParameter("platformMerNo"))){
            resultVO.setRetCode(ResultEnum.PARAM_ERROR.getCode());
            resultVO.setRetMsg("平台商户号必填");
            return resultVO;
        }

        if(StringUtils.isEmpty(request.getParameter("orderNumber"))){
            resultVO.setRetCode(ResultEnum.PARAM_ERROR.getCode());
            resultVO.setRetMsg("商户订单号必填");
            return resultVO;
        }


        resultVO.setRetCode(ResultEnum.SUCCESS.getCode());
        return resultVO;
    }


    /**
     * 校验订单查询时的请求参数
     * @param request
     * @return
     */
    public static ResultVO queryOrderParamVaild(HttpServletRequest request){
        ResultVO resultVO=new ResultVO();


        if(StringUtils.isEmpty(request.getParameter("platformMerNo"))){
            resultVO.setRetCode(ResultEnum.PARAM_ERROR.getCode());
            resultVO.setRetMsg("平台商户号必填");
            return resultVO;
        }

        if(StringUtils.isEmpty(request.getParameter("orderNumber"))){
            resultVO.setRetCode(ResultEnum.PARAM_ERROR.getCode());
            resultVO.setRetMsg("商户订单号必填");
            return resultVO;
        }


        resultVO.setRetCode(ResultEnum.SUCCESS.getCode());
        return resultVO;
    }


    /**
     * 卡签约发送短信验证码
     * @param request
     * @return
     */
    public static ResultVO openCardCodeParamVaild(HttpServletRequest request){
        ResultVO resultVO=new ResultVO();

        if(StringUtils.isEmpty(request.getParameter("platformMerNo"))){
            resultVO.setRetCode(ResultEnum.PARAM_ERROR.getCode());
            resultVO.setRetMsg("平台商户号必填");
            return resultVO;
        }

        if(StringUtils.isEmpty(request.getParameter("idCardNo"))){
            resultVO.setRetCode(ResultEnum.PARAM_ERROR.getCode());
            resultVO.setRetMsg("身份证号必填");
            return resultVO;
        }

        if(StringUtils.isEmpty(request.getParameter("bankCardNo"))){
            resultVO.setRetCode(ResultEnum.PARAM_ERROR.getCode());
            resultVO.setRetMsg("银行卡号必填");
            return resultVO;
        }

        if(StringUtils.isEmpty(request.getParameter("fullName"))){
            resultVO.setRetCode(ResultEnum.PARAM_ERROR.getCode());
            resultVO.setRetMsg("开户姓名必填");
            return resultVO;
        }

        if(StringUtils.isEmpty(request.getParameter("phone"))){
            resultVO.setRetCode(ResultEnum.PARAM_ERROR.getCode());
            resultVO.setRetMsg("银行预留手机号必填");
            return resultVO;
        }

        if(StringUtils.isEmpty(request.getParameter("outTradeNo"))){
            resultVO.setRetCode(ResultEnum.PARAM_ERROR.getCode());
            resultVO.setRetMsg("绑卡交易号必填");
            return resultVO;
        }


        resultVO.setRetCode(ResultEnum.SUCCESS.getCode());
        return resultVO;
    }


    /**
     * 卡签约确认request参数校验
     * @param request
     * @return
     */
    public static ResultVO openCardParamVaild(HttpServletRequest request){
        ResultVO resultVO=new ResultVO();

        if(StringUtils.isEmpty(request.getParameter("platformMerNo"))){
            resultVO.setRetCode(ResultEnum.PARAM_ERROR.getCode());
            resultVO.setRetMsg("平台商户号必填");
            return resultVO;
        }

        if(StringUtils.isEmpty(request.getParameter("smsCode"))){
            resultVO.setRetCode(ResultEnum.PARAM_ERROR.getCode());
            resultVO.setRetMsg("短信验证码必填");
            return resultVO;
        }

        if(StringUtils.isEmpty(request.getParameter("platformOutTradeNo"))){
            resultVO.setRetCode(ResultEnum.PARAM_ERROR.getCode());
            resultVO.setRetMsg("平台绑卡交易号必填");
            return resultVO;
        }


        resultVO.setRetCode(ResultEnum.SUCCESS.getCode());
        return resultVO;
    }

}
