package cn.pay.quickpay.VO;

import lombok.Data;

/**
 * @author bmr
 * @time 2019-02-15 17:29
 */
@Data
public class AddMerResultVO {

    private String platformMerNo;

    private String platformMerKey;

    public AddMerResultVO() {
    }

    public AddMerResultVO(String platformMerNo, String platformMerKey) {
        this.platformMerNo = platformMerNo;
        this.platformMerKey = platformMerKey;
    }
}
