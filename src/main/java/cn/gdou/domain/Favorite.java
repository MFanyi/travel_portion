package cn.gdou.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * 收藏实体类
 */
public class Favorite implements Serializable {
    private int rid;
    private int uid;
    private Date date;

    public int getRid() {
        return rid;
    }

    public void setRid(int rid) {
        this.rid = rid;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * 无参构造方法
     */
    public Favorite() {
    }

}
