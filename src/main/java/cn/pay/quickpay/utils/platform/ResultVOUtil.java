package cn.pay.quickpay.utils.platform;


import cn.pay.quickpay.VO.ResultVO;
import cn.pay.quickpay.enums.ResultEnum;

/**
 * @author bmr
 * @time 2019-01-11 16:15
 */
public class ResultVOUtil {

    public static ResultVO success(Object object){
        ResultVO resultVO=new ResultVO();
        resultVO.setRetCode("0000");
        resultVO.setRetMsg("成功");
        resultVO.setData(object);
        return resultVO;
    }

    public static ResultVO success(){
        return success(null);
    }

    public static ResultVO error(String code,String msg){
        ResultVO resultVO=new ResultVO();
        resultVO.setRetCode(code);
        resultVO.setRetMsg(msg);
        return resultVO;
    }


    public static ResultVO error(ResultEnum resultEnum){
        ResultVO resultVO=new ResultVO();
        resultVO.setRetCode(resultEnum.getCode());
        resultVO.setRetMsg(resultEnum.getMessage());
        return resultVO;
    }

    public static ResultVO error(ResultVO resultVO){
        return resultVO;
    }

}
