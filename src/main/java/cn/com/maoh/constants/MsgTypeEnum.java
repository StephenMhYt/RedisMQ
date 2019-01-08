package cn.com.maoh.constants;

/**
 * Author: Stephen
 * Create Date: 2018/12/4
 * Version: 1.0
 * Comments:
 */
public enum MsgTypeEnum {

    QUEUE(1),

    TOPIC(2);

    private Integer type;

    MsgTypeEnum(Integer type){
        this.type = type;
    }

    public Integer value(){
        return this.type;
    }

    public boolean equals(Integer type){
        return this.type.equals(type);
    }
}
