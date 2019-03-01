package cn.pay.quickpay.utils.platform.valid;

import cn.pay.quickpay.VO.ResultVO;
import cn.pay.quickpay.bean.MerchantBean;
import cn.pay.quickpay.bean.MerchantChannelBean;
import cn.pay.quickpay.bean.TransactionBean;
import cn.pay.quickpay.enums.ResultEnum;
import cn.pay.quickpay.utils.platform.ResultVOUtil;
import cn.pay.quickpay.xmlbean.ChannelXmlBean;
import cn.pay.quickpay.xmlbean.VirtualChannelXmlBean;
import lombok.extern.slf4j.Slf4j;

/**
 * @author bmr
 * @time 2019-02-21 15:02:51
 * 商户进件时费率的前置校验
 * 与配置的虚拟通道表中的费率进行校验
 */
@Slf4j
public class VirtualChannelValid {

    /**
     * 有积分虚拟通道入驻、修改费率校验
     * @param merchantBean
     * @param virtualChannelXmlBean
     * @return
     */
    public static ResultVO integralChannelEnterCheck(MerchantBean merchantBean, VirtualChannelXmlBean virtualChannelXmlBean){
        //商户结算方式T1、D0
        if(merchantBean.getSettleType()==1){//T1
            //-1为小于、0为等于、1为大于
            if(merchantBean.getFee0().compareTo(virtualChannelXmlBean.getFee1())==-1){//积分T1费率
//                log.info("【有积分通道费率修改】bizType:{}，agentNo：{}，错误信息：{}",bizType,agentNo,"商户费率修改T1交易费率不能低于渠道签约费率");
                return ResultVOUtil.error(ResultEnum.MERCHANT_OPEN_TRANS_FEE_ERROR.getCode(),ResultEnum.MERCHANT_OPEN_TRANS_FEE_ERROR.getMessage()+"[T1有积分]");
            }
            //-1为小于、0为等于、1为大于
            //商户交易费率不能超过渠道交易费率范围
            if(merchantBean.getFee0().compareTo(virtualChannelXmlBean.getMin_rate_score1())==-1||merchantBean.getFee0().compareTo(virtualChannelXmlBean.getMax_rate_score1())==1){//积分T1费率
//                log.info("【有积分通道费率修改】bizType:{}，agentNo：{}，错误信息：{}",bizType,agentNo,"商户费率修改T1交易费率不在渠道设定费率范围内");
                return ResultVOUtil.error(ResultEnum.MERCHANT_OPEN_TRANS_FEE_SCORE_ERROR.getCode(),ResultEnum.MERCHANT_OPEN_TRANS_FEE_SCORE_ERROR.getMessage()+"[T1有积分]");

            }

        }else if(merchantBean.getSettleType()==2){//D0
            //-1为小于、0为等于、1为大于
            if(merchantBean.getFee0().compareTo(virtualChannelXmlBean.getFee0())==-1){//积分D0费率
//                log.info("【有积分通道费率修改】bizType:{}，agentNo：{}，错误信息：{}",bizType,agentNo,"商户费率修改D0交易费率不能低于渠道签约费率");
                return ResultVOUtil.error(ResultEnum.MERCHANT_OPEN_TRANS_FEE_ERROR.getCode(),ResultEnum.MERCHANT_OPEN_TRANS_FEE_ERROR.getMessage()+"[D0有积分]");

            }
            //-1为小于、0为等于、1为大于
            //商户交易费率不能超过渠道交易费率范围
            if(merchantBean.getFee0().compareTo(virtualChannelXmlBean.getMin_rate_score0())==-1||merchantBean.getFee0().compareTo(virtualChannelXmlBean.getMax_rate_score0())==1){//积分D0费率
//                log.info("【有积分通道费率修改】bizType:{}，agentNo：{}，错误信息：{}",bizType,agentNo,"商户费率修改D0交易费率不在渠道设定费率范围内");
                return ResultVOUtil.error(ResultEnum.MERCHANT_OPEN_TRANS_FEE_SCORE_ERROR.getCode(),ResultEnum.MERCHANT_OPEN_TRANS_FEE_SCORE_ERROR.getMessage()+"[D0有积分]");

            }

        }
        //
        if(merchantBean.getD0fee()<virtualChannelXmlBean.getDfee()){//代付费
//            log.info("【有积分通道费率修改】bizType:{}，agentNo：{}，错误信息：{}",bizType,agentNo,"商户代付费不能低于渠道签约代付费");
            return ResultVOUtil.error(ResultEnum.MERCHANT_OPEN_DF_FEE_ERROR.getCode(),ResultEnum.MERCHANT_OPEN_DF_FEE_ERROR.getMessage()+"[有积分]");

        }
        //
        if(merchantBean.getD0fee()<virtualChannelXmlBean.getMin_dfee_score()||merchantBean.getD0fee()>virtualChannelXmlBean.getMax_dfee_score()){//代付费
//            log.info("【有积分通道费率修改】bizType:{}，agentNo：{}，错误信息：{}",bizType,agentNo,"商户进件代付费率在渠道设定费率范围内");
            return ResultVOUtil.error(ResultEnum.MERCHANT_OPEN_DF_FEE_ERROR.getCode(),ResultEnum.MERCHANT_OPEN_DF_FEE_ERROR.getMessage()+"[有积分]");

        }

        return ResultVOUtil.success();
    }

    /**
     * 无积分虚拟通道入驻、修改费率校验
     * @param merchantBean
     * @param virtualChannelXmlBean
     * @return
     */
    public static ResultVO noIntegralChannelEnterCheck(MerchantBean merchantBean, VirtualChannelXmlBean virtualChannelXmlBean){
        //商户结算方式T1、D0
        if(merchantBean.getSettleType()==1){//T1

            //-1为小于、0为等于、1为大于
            if(merchantBean.getNoIntegralFee0().compareTo(virtualChannelXmlBean.getFee1())==-1){//无积分T1费率
//                log.info("【无积分通道费率修改】bizType:{}，agentNo：{}，错误信息：{}",bizType,agentNo,"商户进件费率修改T1交易费率不能低于渠道签约费率");
                return ResultVOUtil.error(ResultEnum.MERCHANT_OPEN_TRANS_FEE_ERROR.getCode(),ResultEnum.MERCHANT_OPEN_TRANS_FEE_ERROR.getMessage()+"[T1无积分]");

            }
            //-1为小于、0为等于、1为大于
            //商户交易费率不能超过渠道交易费率范围
            if(merchantBean.getNoIntegralFee0().compareTo(virtualChannelXmlBean.getMin_rate_score1())==-1||merchantBean.getNoIntegralFee0().compareTo(virtualChannelXmlBean.getMax_rate_score1())==1){//无积分T1费率
//                log.info("【无积分通道费率修改】bizType:{}，agentNo：{}，错误信息：{}",bizType,agentNo,"商户进件费率修改T1交易费率在渠道设定费率范围外");
                return ResultVOUtil.error(ResultEnum.MERCHANT_OPEN_TRANS_FEE_SCORE_ERROR.getCode(),ResultEnum.MERCHANT_OPEN_TRANS_FEE_SCORE_ERROR.getMessage()+"[T1无积分]");

            }

        }else if(merchantBean.getSettleType()==2){//D0
            //-1为小于、0为等于、1为大于
            if(merchantBean.getNoIntegralFee0().compareTo(virtualChannelXmlBean.getFee0())==-1){//无积分D0费率
//                log.info("【无积分通道费率修改】bizType:{}，agentNo：{}，错误信息：{}",bizType,agentNo,"商户进件费率修改D0交易费率不能低于渠道签约费率");
                return ResultVOUtil.error(ResultEnum.MERCHANT_OPEN_TRANS_FEE_ERROR.getCode(),ResultEnum.MERCHANT_OPEN_TRANS_FEE_ERROR.getMessage()+"[D0无积分]");

            }
            //-1为小于、0为等于、1为大于
            //商户交易费率不能超过渠道交易费率范围
            if(merchantBean.getNoIntegralFee0().compareTo(virtualChannelXmlBean.getMin_rate_score0())==-1||merchantBean.getNoIntegralFee0().compareTo(virtualChannelXmlBean.getMax_rate_score0())==1){//无积分D0费率
//                log.info("【无积分通道费率修改】bizType:{}，agentNo：{}，错误信息：{}",bizType,agentNo,"商户进件费率修改D0交易费率在渠道设定费率范围外");
                return ResultVOUtil.error(ResultEnum.MERCHANT_OPEN_TRANS_FEE_SCORE_ERROR.getCode(),ResultEnum.MERCHANT_OPEN_TRANS_FEE_SCORE_ERROR.getMessage()+"[D0无积分]");

            }

        }
        //
        if(merchantBean.getNoIntegralFee()<virtualChannelXmlBean.getDfee()){//代付费
//            log.info("【无积分通道费率修改】bizType:{}，agentNo：{}，错误信息：{}",bizType,agentNo,"商户代付费不能低于渠道签约代付费");
            return ResultVOUtil.error(ResultEnum.MERCHANT_OPEN_DF_FEE_ERROR.getCode(),ResultEnum.MERCHANT_OPEN_DF_FEE_ERROR.getMessage()+"[无积分]");

        }
        //
        if(merchantBean.getNoIntegralFee()<virtualChannelXmlBean.getMin_dfee_score()||merchantBean.getNoIntegralFee()>virtualChannelXmlBean.getMax_dfee_score()){//代付费
//            log.info("【无积分通道费率修改】bizType:{}，agentNo：{}，错误信息：{}",bizType,agentNo,"商户进件代付费率在渠道设定费率范围内");
            return ResultVOUtil.error(ResultEnum.MERCHANT_OPEN_DF_FEE_SCORE_ERROR.getCode(),ResultEnum.MERCHANT_OPEN_DF_FEE_SCORE_ERROR.getMessage()+"[无积分]");

        }

        //全部校验通过，返回0000
        return ResultVOUtil.success();

    }


    /**
     * 划扣虚拟通道入驻、修改费率校验
     * @param merchantBean
     * @param virtualChannelXmlBean
     * @return
     */
    public static ResultVO delimitChannelEnterCheck(MerchantBean merchantBean, VirtualChannelXmlBean virtualChannelXmlBean){
        //商户结算方式T1、D0
        if(merchantBean.getSettleType()==1){//T1

            //-1为小于、0为等于、1为大于
            if(merchantBean.getDelimitFee0().compareTo(virtualChannelXmlBean.getFee1())==-1){//无积分T1费率
//                log.info("【划扣通道费率修改】bizType:{}，agentNo：{}，错误信息：{}",bizType,agentNo,"商户进件费率修改T1交易费率不能低于渠道签约费率");
                return ResultVOUtil.error(ResultEnum.MERCHANT_OPEN_TRANS_FEE_ERROR.getCode(),ResultEnum.MERCHANT_OPEN_TRANS_FEE_ERROR.getMessage()+"[T1划扣]");

            }
            //-1为小于、0为等于、1为大于
            //商户交易费率不能超过渠道交易费率范围
            if(merchantBean.getDelimitFee0().compareTo(virtualChannelXmlBean.getMin_rate_score1())==-1||merchantBean.getDelimitFee0().compareTo(virtualChannelXmlBean.getMax_rate_score1())==1){//无积分T1费率
//                log.info("【划扣通道费率修改】bizType:{}，agentNo：{}，错误信息：{}",bizType,agentNo,"商户进件费率修改T1交易费率在渠道设定费率范围外");
                return ResultVOUtil.error(ResultEnum.MERCHANT_OPEN_TRANS_FEE_SCORE_ERROR.getCode(),ResultEnum.MERCHANT_OPEN_TRANS_FEE_SCORE_ERROR.getMessage()+"[T1划扣]");

            }


        }else if(merchantBean.getSettleType()==2){//D0
            //-1为小于、0为等于、1为大于
            if(merchantBean.getDelimitFee0().compareTo(virtualChannelXmlBean.getFee0())==-1){//无积分D0费率
//                log.info("【划扣通道费率修改】bizType:{}，agentNo：{}，错误信息：{}",bizType,agentNo,"商户进件费率修改D0交易费率不能低于渠道签约费率");
                return ResultVOUtil.error(ResultEnum.MERCHANT_OPEN_TRANS_FEE_ERROR.getCode(),ResultEnum.MERCHANT_OPEN_TRANS_FEE_ERROR.getMessage()+"[D0无积分]");

            }
            //-1为小于、0为等于、1为大于
            //商户交易费率不能超过渠道交易费率范围
            if(merchantBean.getDelimitFee0().compareTo(virtualChannelXmlBean.getMin_rate_score0())==-1||merchantBean.getDelimitFee0().compareTo(virtualChannelXmlBean.getMax_rate_score0())==1){//无积分D0费率
//                log.info("【划扣通道费率修改】bizType:{}，agentNo：{}，错误信息：{}",bizType,agentNo,"商户进件费率修改D0交易费率在渠道设定费率范围外");
                return ResultVOUtil.error(ResultEnum.MERCHANT_OPEN_TRANS_FEE_SCORE_ERROR.getCode(),ResultEnum.MERCHANT_OPEN_TRANS_FEE_SCORE_ERROR.getMessage()+"[D0无积分]");

            }

        }

        if(merchantBean.getDelimitFee()<virtualChannelXmlBean.getDfee()){//代付费
//            log.info("【划扣通道费率修改】bizType:{}，agentNo：{}，错误信息：{}",bizType,agentNo,"商户代付费不能低于渠道签约代付费");
            return ResultVOUtil.error(ResultEnum.MERCHANT_OPEN_DF_FEE_ERROR.getCode(),ResultEnum.MERCHANT_OPEN_DF_FEE_ERROR.getMessage()+"[划扣]");

        }

        if(merchantBean.getDelimitFee()<virtualChannelXmlBean.getMin_dfee_score()||merchantBean.getDelimitFee()>virtualChannelXmlBean.getMax_dfee_score()){//代付费
//            log.info("【无积分通道费率修改】bizType:{}，agentNo：{}，错误信息：{}",bizType,agentNo,"商户进件代付费率在渠道设定费率范围内");
            return ResultVOUtil.error(ResultEnum.MERCHANT_OPEN_DF_FEE_SCORE_ERROR.getCode(),ResultEnum.MERCHANT_OPEN_DF_FEE_SCORE_ERROR.getMessage()+"[划扣]");

        }


        if(merchantBean.getWithholdFee()<virtualChannelXmlBean.getWithhold_fee()){//代扣费
            return ResultVOUtil.error(ResultEnum.MERCHANT_OPEN_HK_FEE_ERROR.getCode(),ResultEnum.MERCHANT_OPEN_HK_FEE_ERROR.getMessage()+"[划扣]");
        }

        if(merchantBean.getWithholdFee()<virtualChannelXmlBean.getMin_withhold_score()||merchantBean.getWithholdFee()>virtualChannelXmlBean.getMax_withhold_score()){//代扣费
            return ResultVOUtil.error(ResultEnum.MERCHANT_OPEN_HK_FEE_SCORE_ERROR.getCode(),ResultEnum.MERCHANT_OPEN_HK_FEE_SCORE_ERROR.getMessage()+"[划扣]");

        }

        //全部校验通过，返回0000
        return ResultVOUtil.success();

    }


    /**
     * 智能代还虚拟通道入驻、修改费率校验
     * @param merchantBean
     * @param virtualChannelXmlBean
     * @return
     */
    public static ResultVO repayChannelEnterCheck(MerchantBean merchantBean, VirtualChannelXmlBean virtualChannelXmlBean){
        //商户结算方式T1、D0
        if(merchantBean.getSettleType()==1){//T1

            //-1为小于、0为等于、1为大于
            if(merchantBean.getRepayFee0().compareTo(virtualChannelXmlBean.getFee1())==-1){//智能代还T1费率
//                log.info("【智能代还通道费率修改】bizType:{}，agentNo：{}，错误信息：{}",bizType,agentNo,"商户进件费率修改T1交易费率不能低于渠道签约费率");
                return ResultVOUtil.error(ResultEnum.MERCHANT_OPEN_TRANS_FEE_ERROR.getCode(),ResultEnum.MERCHANT_OPEN_TRANS_FEE_ERROR.getMessage()+"[T1智能代还]");

            }
            //-1为小于、0为等于、1为大于
            //商户交易费率不能超过渠道交易费率范围
            if(merchantBean.getRepayFee0().compareTo(virtualChannelXmlBean.getMin_rate_score1())==-1||merchantBean.getRepayFee0().compareTo(virtualChannelXmlBean.getMax_rate_score1())==1){//无积分T1费率
//                log.info("【智能代还通道费率修改】bizType:{}，agentNo：{}，错误信息：{}",bizType,agentNo,"商户进件费率修改T1交易费率在渠道设定费率范围外");
                return ResultVOUtil.error(ResultEnum.MERCHANT_OPEN_TRANS_FEE_SCORE_ERROR.getCode(),ResultEnum.MERCHANT_OPEN_TRANS_FEE_SCORE_ERROR.getMessage()+"[T1智能代还]");

            }


        }else if(merchantBean.getSettleType()==2){//D0
            //-1为小于、0为等于、1为大于
            if(merchantBean.getRepayFee0().compareTo(virtualChannelXmlBean.getFee0())==-1){//智能代还D0费率
//                log.info("【智能代还通道费率修改】bizType:{}，agentNo：{}，错误信息：{}",bizType,agentNo,"商户进件费率修改D0交易费率不能低于渠道签约费率");
                return ResultVOUtil.error(ResultEnum.MERCHANT_OPEN_TRANS_FEE_ERROR.getCode(),ResultEnum.MERCHANT_OPEN_TRANS_FEE_ERROR.getMessage()+"[D0智能代还]");

            }
            //-1为小于、0为等于、1为大于
            //商户交易费率不能超过渠道交易费率范围
            if(merchantBean.getRepayFee0().compareTo(virtualChannelXmlBean.getMin_rate_score0())==-1||merchantBean.getRepayFee0().compareTo(virtualChannelXmlBean.getMax_rate_score0())==1){//无积分D0费率
//                log.info("【智能代还通道费率修改】bizType:{}，agentNo：{}，错误信息：{}",bizType,agentNo,"商户进件费率修改D0交易费率在渠道设定费率范围外");
                return ResultVOUtil.error(ResultEnum.MERCHANT_OPEN_TRANS_FEE_SCORE_ERROR.getCode(),ResultEnum.MERCHANT_OPEN_TRANS_FEE_SCORE_ERROR.getMessage()+"[D0智能代还]");

            }

        }

        if(merchantBean.getRepayFee()<virtualChannelXmlBean.getDfee()){//代付费
//            log.info("【智能代还通道费率修改】bizType:{}，agentNo：{}，错误信息：{}",bizType,agentNo,"商户代付费不能低于渠道签约代付费");
            return ResultVOUtil.error(ResultEnum.MERCHANT_OPEN_DF_FEE_ERROR.getCode(),ResultEnum.MERCHANT_OPEN_DF_FEE_ERROR.getMessage()+"[智能代还]");

        }

        if(merchantBean.getRepayFee()<virtualChannelXmlBean.getMin_dfee_score()||merchantBean.getRepayFee()>virtualChannelXmlBean.getMax_dfee_score()){//代付费
//            log.info("【智能代还通道费率修改】bizType:{}，agentNo：{}，错误信息：{}",bizType,agentNo,"商户进件代付费率在渠道设定费率范围内");
            return ResultVOUtil.error(ResultEnum.MERCHANT_OPEN_DF_FEE_SCORE_ERROR.getCode(),ResultEnum.MERCHANT_OPEN_DF_FEE_SCORE_ERROR.getMessage()+"[智能代还]");

        }


        if(merchantBean.getRepayFee1()<virtualChannelXmlBean.getRepayment_fee()){//代还费
            return ResultVOUtil.error(ResultEnum.MERCHANT_OPEN_DH_FEE_ERROR.getCode(),ResultEnum.MERCHANT_OPEN_DH_FEE_ERROR.getMessage()+"[智能代还]");

        }

        if(merchantBean.getRepayFee1()<virtualChannelXmlBean.getMin_repayment_score()||merchantBean.getRepayFee1()>virtualChannelXmlBean.getMax_repayment_score()){//代还费
            return ResultVOUtil.error(ResultEnum.MERCHANT_OPEN_DH_FEE_SCORE_ERROR.getCode(),ResultEnum.MERCHANT_OPEN_DH_FEE_SCORE_ERROR.getMessage()+"[智能代还]");
        }


        //全部校验通过，返回0000
        return ResultVOUtil.success();

    }


    /**
     * 结算虚拟通道入驻、修改费率校验
     * @param merchantBean
     * @param virtualChannelXmlBean
     * @return
     */
    public static ResultVO settleChannelEnterCheck(MerchantBean merchantBean, VirtualChannelXmlBean virtualChannelXmlBean){

        if(merchantBean.getBalanceFee()<virtualChannelXmlBean.getBalance_fee()){//结算费
            return ResultVOUtil.error(ResultEnum.MERCHANT_OPEN_JS_FEE_ERROR.getCode(),ResultEnum.MERCHANT_OPEN_JS_FEE_ERROR.getMessage()+"[结算]");
        }

        if(merchantBean.getBalanceFee()<virtualChannelXmlBean.getMin_balance_score()||merchantBean.getBalanceFee()>virtualChannelXmlBean.getMax_balance_score()){//结算费
            return ResultVOUtil.error(ResultEnum.MERCHANT_OPEN_DF_FEE_SCORE_ERROR.getCode(),ResultEnum.MERCHANT_OPEN_DF_FEE_SCORE_ERROR.getMessage()+"[结算]");

        }


        //全部校验通过，返回0000
        return ResultVOUtil.success();

    }

    /**
     * 无积分虚拟通道交易前置校验
     * @param merchantBean
     * @param virtualChannelXmlBean
     * @return
     */
    public static ResultVO noIntegralChannelTransCheck(TransactionBean transactionBean,MerchantBean merchantBean, VirtualChannelXmlBean virtualChannelXmlBean){

        //商户交易金额不能超过渠道商虚拟通道交易金额范围
        if(transactionBean.getOrderAmount().compareTo(virtualChannelXmlBean.getMin_money_score())==-1||transactionBean.getOrderAmount().compareTo(virtualChannelXmlBean.getMax_money_score())==1){//交易限额
            return ResultVOUtil.error(ResultEnum.TRANS_MONEY_SCORE_ERROR.getCode(),"单笔限额:" + virtualChannelXmlBean.getMin_money_score()+ "元-" + virtualChannelXmlBean.getMax_money_score() + "元");

        }

        if(merchantBean.getNoIntegralFee0().compareTo(virtualChannelXmlBean.getMin_rate_score0())==-1||merchantBean.getNoIntegralFee0().compareTo(virtualChannelXmlBean.getMax_rate_score0())==1){
            return ResultVOUtil.error(ResultEnum.MERCHANT_OPEN_TRANS_FEE_SCORE_ERROR.getCode(),"无积分交易费率范围"+virtualChannelXmlBean.getMin_rate_score0()+"%-"+virtualChannelXmlBean.getMax_rate_score0()+"%");
        }

        if(merchantBean.getNoIntegralFee()<virtualChannelXmlBean.getMin_dfee_score()||merchantBean.getNoIntegralFee()>virtualChannelXmlBean.getMax_dfee_score()){
            return ResultVOUtil.error(ResultEnum.MERCHANT_OPEN_DF_FEE_SCORE_ERROR.getCode(),"无积分代付费范围:"+virtualChannelXmlBean.getMin_dfee_score()+"分-"+virtualChannelXmlBean.getMax_dfee_score()+"分");

        }


        //全部校验通过，返回0000
        return ResultVOUtil.success();

    }

    /**
     * 有积分虚拟通道交易前置校验
     * @param merchantBean
     * @param virtualChannelXmlBean
     * @return
     */
    public static ResultVO integralChannelTransCheck(TransactionBean transactionBean,MerchantBean merchantBean, VirtualChannelXmlBean virtualChannelXmlBean){


        //商户交易金额不能超过渠道商虚拟通道交易金额范围
        if(transactionBean.getOrderAmount().compareTo(virtualChannelXmlBean.getMin_money_score())==-1||transactionBean.getOrderAmount().compareTo(virtualChannelXmlBean.getMax_money_score())==1){//交易限额
            return ResultVOUtil.error(ResultEnum.TRANS_MONEY_SCORE_ERROR.getCode(),"单笔限额:" + virtualChannelXmlBean.getMin_money_score()+ "元-" + virtualChannelXmlBean.getMax_money_score() + "元");
        }
        if(merchantBean.getFee0().compareTo(virtualChannelXmlBean.getMin_rate_score0())==-1||merchantBean.getFee0().compareTo(virtualChannelXmlBean.getMax_rate_score0())==1){
            return ResultVOUtil.error(ResultEnum.MERCHANT_OPEN_TRANS_FEE_SCORE_ERROR.getCode(),"有积分交易费率范围"+virtualChannelXmlBean.getMin_rate_score0()+"%-"+virtualChannelXmlBean.getMax_rate_score0()+"%");
        }
        //元转分
        if(merchantBean.getD0fee()<virtualChannelXmlBean.getMin_dfee_score()||merchantBean.getD0fee()>virtualChannelXmlBean.getMax_dfee_score()){
            return ResultVOUtil.error(ResultEnum.MERCHANT_OPEN_DF_FEE_SCORE_ERROR.getCode(),"有积分代付费范围:"+virtualChannelXmlBean.getMin_dfee_score()+"分-"+virtualChannelXmlBean.getMax_dfee_score()+"分");
        }


        //全部校验通过，返回0000
        return ResultVOUtil.success();

    }

    /**
     * 智能代还消费虚拟通道交易前置校验
     * @param merchantBean
     * @param virtualChannelXmlBean
     * @return
     */
    public static ResultVO repayCostChannelTransCheck(TransactionBean transactionBean, MerchantBean merchantBean, VirtualChannelXmlBean virtualChannelXmlBean){

        //商户交易金额不能超过渠道商虚拟通道交易金额范围
        if(transactionBean.getOrderAmount().compareTo(virtualChannelXmlBean.getMin_money_score())==-1||transactionBean.getOrderAmount().compareTo(virtualChannelXmlBean.getMax_money_score())==1){//交易限额
            return ResultVOUtil.error(ResultEnum.TRANS_MONEY_SCORE_ERROR.getCode(),"单笔限额:" + virtualChannelXmlBean.getMin_money_score()+ "元-" + virtualChannelXmlBean.getMax_money_score() + "元");
        }

        if(merchantBean.getRepayFee0().compareTo(virtualChannelXmlBean.getMin_rate_score0())==-1||merchantBean.getRepayFee0().compareTo(virtualChannelXmlBean.getMax_rate_score0())==1){
            return ResultVOUtil.error(ResultEnum.MERCHANT_OPEN_TRANS_FEE_SCORE_ERROR.getCode(),"智能代还交易费率范围"+virtualChannelXmlBean.getMin_rate_score0()+"%-"+virtualChannelXmlBean.getMax_rate_score0()+"%");
        }

        if(merchantBean.getRepayFee()<virtualChannelXmlBean.getMin_dfee_score()||merchantBean.getRepayFee()>virtualChannelXmlBean.getMax_dfee_score()){
            return ResultVOUtil.error(ResultEnum.MERCHANT_OPEN_DF_FEE_SCORE_ERROR.getCode(),"智能代还代付费范围:"+virtualChannelXmlBean.getMin_dfee_score()+"分-"+virtualChannelXmlBean.getMax_dfee_score()+"分");

        }

        //全部校验通过，返回0000
        return ResultVOUtil.success();

    }


    /**
     * 智能代还还款虚拟通道交易前置校验
     * @param merchantBean
     * @param virtualChannelXmlBean
     * @return
     */
    public static ResultVO repaymentChannelTransCheck(TransactionBean transactionBean, MerchantBean merchantBean, VirtualChannelXmlBean virtualChannelXmlBean){

        //商户交易金额不能超过渠道商虚拟通道交易金额范围
        if(transactionBean.getOrderAmount().compareTo(virtualChannelXmlBean.getMin_money_score())==-1||transactionBean.getOrderAmount().compareTo(virtualChannelXmlBean.getMax_money_score())==1){//交易限额
            return ResultVOUtil.error(ResultEnum.TRANS_MONEY_SCORE_ERROR.getCode(),"单笔限额:" + virtualChannelXmlBean.getMin_money_score()+ "元-" + virtualChannelXmlBean.getMax_money_score() + "元");
        }

        if(merchantBean.getRepayFee1()<virtualChannelXmlBean.getMin_repayment_score()||merchantBean.getRepayFee1()>virtualChannelXmlBean.getMax_repayment_score()){
            return ResultVOUtil.error(ResultEnum.MERCHANT_OPEN_DH_FEE_SCORE_ERROR.getCode(),"代还费范围:"+virtualChannelXmlBean.getMin_dfee_score()+"分-"+virtualChannelXmlBean.getMax_dfee_score()+"分");

        }

        //全部校验通过，返回0000
        return ResultVOUtil.success();

    }


    /**
     * 代付虚拟通道交易前置校验
     * @param merchantBean
     * @param virtualChannelXmlBean
     * @return
     */
    public static ResultVO payForAnotherChannelTransCheck(TransactionBean transactionBean, MerchantBean merchantBean, VirtualChannelXmlBean virtualChannelXmlBean){

        //商户交易金额不能超过渠道商虚拟通道交易金额范围
        if(transactionBean.getOrderAmount().compareTo(virtualChannelXmlBean.getMin_money_score())==-1||transactionBean.getOrderAmount().compareTo(virtualChannelXmlBean.getMax_money_score())==1){//交易限额
            return ResultVOUtil.error(ResultEnum.TRANS_MONEY_SCORE_ERROR.getCode(),"单笔限额:" + virtualChannelXmlBean.getMin_money_score()+ "元-" + virtualChannelXmlBean.getMax_money_score() + "元");
        }

        if(merchantBean.getBalanceFee()<virtualChannelXmlBean.getMin_balance_score()||merchantBean.getBalanceFee()>virtualChannelXmlBean.getMax_balance_score()){
            return ResultVOUtil.error(ResultEnum.MERCHANT_OPEN_DF_FEE_SCORE_ERROR.getCode(),"代付费范围:"+virtualChannelXmlBean.getMin_dfee_score()+"分-"+virtualChannelXmlBean.getMax_dfee_score()+"分");

        }

        //全部校验通过，返回0000
        return ResultVOUtil.success();

    }

    /**
     * 升级虚拟通道交易前置校验
     * @param merchantBean
     * @param virtualChannelXmlBean
     * @return
     */
    public static ResultVO upgradeChannelTransCheck(TransactionBean transactionBean, MerchantBean merchantBean, VirtualChannelXmlBean virtualChannelXmlBean){

        //商户交易金额不能超过渠道商虚拟通道交易金额范围
        if(transactionBean.getOrderAmount().compareTo(virtualChannelXmlBean.getMin_money_score())==-1||transactionBean.getOrderAmount().compareTo(virtualChannelXmlBean.getMax_money_score())==1){//交易限额
            return ResultVOUtil.error(ResultEnum.TRANS_MONEY_SCORE_ERROR.getCode(),"单笔限额:" + virtualChannelXmlBean.getMin_money_score()+ "元-" + virtualChannelXmlBean.getMax_money_score() + "元");
        }

        //全部校验通过，返回0000
        return ResultVOUtil.success();

    }


    /**
     * 划扣虚拟通道交易前置校验
     * @param merchantBean
     * @param virtualChannelXmlBean
     * @return
     */
    public static ResultVO delimitChannelTransCheck(TransactionBean transactionBean, MerchantBean merchantBean, VirtualChannelXmlBean virtualChannelXmlBean){

        //商户交易金额不能超过渠道商虚拟通道交易金额范围
        if(transactionBean.getOrderAmount().compareTo(virtualChannelXmlBean.getMin_money_score())==-1||transactionBean.getOrderAmount().compareTo(virtualChannelXmlBean.getMax_money_score())==1){//交易限额
            return ResultVOUtil.error(ResultEnum.TRANS_MONEY_SCORE_ERROR.getCode(),"单笔限额:" + virtualChannelXmlBean.getMin_money_score()+ "元-" + virtualChannelXmlBean.getMax_money_score() + "元");
        }


        if(merchantBean.getDelimitFee0().compareTo(virtualChannelXmlBean.getMin_rate_score0())==-1||merchantBean.getDelimitFee0().compareTo(virtualChannelXmlBean.getMax_rate_score0())==1){
            return ResultVOUtil.error(ResultEnum.MERCHANT_OPEN_TRANS_FEE_SCORE_ERROR.getCode(),"划扣交易费率范围"+virtualChannelXmlBean.getMin_rate_score0()+"%-"+virtualChannelXmlBean.getMax_rate_score0()+"%");
        }

        if(merchantBean.getDelimitFee()<virtualChannelXmlBean.getMin_dfee_score()||merchantBean.getDelimitFee()>virtualChannelXmlBean.getMax_dfee_score()){
            return ResultVOUtil.error(ResultEnum.MERCHANT_OPEN_DF_FEE_SCORE_ERROR.getCode(),"划扣代付费范围"+virtualChannelXmlBean.getMin_dfee_score()+"分-"+virtualChannelXmlBean.getMax_dfee_score()+"分");
        }
        //元转分
        if(merchantBean.getWithholdFee()<virtualChannelXmlBean.getMin_withhold_score()||merchantBean.getWithholdFee()>virtualChannelXmlBean.getMax_withhold_score()){
            return ResultVOUtil.error(ResultEnum.MERCHANT_OPEN_HK_FEE_SCORE_ERROR.getCode(),"划扣代扣费范围"+virtualChannelXmlBean.getMin_withhold_score()+"分-"+virtualChannelXmlBean.getMax_withhold_score()+"分");

        }

        //全部校验通过，返回0000
        return ResultVOUtil.success();

    }

}
