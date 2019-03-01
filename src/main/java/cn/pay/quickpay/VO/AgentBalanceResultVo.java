package cn.pay.quickpay.VO;

import lombok.Data;

/**
 * @author bmr
 * @time 2019-02-22 14:54
 */
@Data
public class AgentBalanceResultVo {

    private String authBalance;

    private String balance;

    public AgentBalanceResultVo() {
    }

    public AgentBalanceResultVo(String authBalance, String balance) {
        this.authBalance = authBalance;
        this.balance = balance;
    }
}
