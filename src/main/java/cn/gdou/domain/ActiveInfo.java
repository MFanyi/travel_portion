package cn.gdou.domain;

import java.io.Serializable;

public class ActiveInfo implements Serializable {
    //激活状态码
    private int activeCode;
    //返回信息
    private String activeMsg;

    public int getActiveCode() {
        return activeCode;
    }

    public void setActiveCode(int activeCode) {
        this.activeCode = activeCode;
    }

    public String getActiveMsg() {
        return activeMsg;
    }

    public void setActiveMsg(String activeMsg) {
        this.activeMsg = activeMsg;
    }
}
