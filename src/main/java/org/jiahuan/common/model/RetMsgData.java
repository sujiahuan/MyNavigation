package org.jiahuan.common.model;

public class RetMsgData<T> {

    private Integer state;
    private String msg;
    private T data;

    public RetMsgData() {
        this.setState(State.RET_STATE_SUCCESS);
    }

    public int getState() {
        return this.state;
    }

    public void setState(State retStateSuccess) {
        this.state = retStateSuccess.getState();
        this.msg = retStateSuccess.getMsg();
    }

    public String getMsg() {
        return this.msg;
    }

    public void setMsg(String msg) {
        this.state=State.RET_STATE_SYSTEM_ERROR.getState();
        this.msg = msg;
    }

    public T getData() {
        return this.data;
    }

    public void setData(T data) {
        this.data = data;
    }

}
