package cn.com.maoh.constants;

/**
 * Author: Stephen
 * Create Date: 2018/12/3
 * Version: 1.0
 * Comments:
 */
public enum MsgStatusEnum {

    UNPROCESS(1),

    PROCESSED(2);

    private Integer status;

    MsgStatusEnum(Integer status){
        this.status = status;
    }

    public Integer value(){
        return this.status;
    }

    public boolean equals(Integer status){
        return this.status.equals(status);
    }
}
