package cn.pay.quickpay.enums;

import lombok.Getter;

/**
 * @author bmr
 * @time 2019-02-21 17:24
 */
@Getter
public enum PayTypeEnum {

    QUICK(0,BizTypeEnum.ADD_ORDER.getBizTypeCode(),"快捷消费"),
    REAPY_PAY(1,BizTypeEnum.ADD_ORDER.getBizTypeCode(),"智能代还消费"),
    REAPY_RE(2,BizTypeEnum.ADD_ORDER.getBizTypeCode(),"智能代还还款"),
    PAY_FOR_ANOTHER(3,BizTypeEnum.PAY_FOR_ANOTHER.getBizTypeCode(),"代付"),
    UPGRADE(4,BizTypeEnum.ADD_ORDER.getBizTypeCode(),"升级"),
    DELIMIT(5,BizTypeEnum.ADD_ORDER.getBizTypeCode(),"划扣");


    private Integer typeCode;

    private String bizTypeDesc;

    private String typeDesc;



    PayTypeEnum(Integer typeCode, String bizTypeDesc,String typeDesc) {
        this.typeCode = typeCode;
        this.bizTypeDesc = bizTypeDesc;
        this.typeDesc = typeDesc;
    }

    public static boolean contains(Integer typeCode){
        for(PayTypeEnum typeEnum : PayTypeEnum.values()){
            if(typeEnum.typeCode.equals(typeCode)){
                return true;
            }
        }
        return false;
    }


}
