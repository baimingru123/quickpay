package cn.pay.quickpay.VO;

import lombok.Data;

import java.io.Serializable;

/**
 * http请求的最外层对象
 * @author bmr
 * @time 2019-02-14 17:41:51
 */
@Data
public class ResultVO<T> implements Serializable {

    private static final long serialVersionUID = -1939668422824880799L;

    /** 返回码. */
    private String retCode;

    /** 返回信息描述. */
    private String retMsg;

    /** 返回内容. */
    private T data;
}
