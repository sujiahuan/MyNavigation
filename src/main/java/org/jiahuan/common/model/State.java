package org.jiahuan.common.model;

public enum State {

	RET_STATE_SUCCESS(0, "请求成功"),

	RET_STATE_SYSTEM_ERROR(1, "系统异常");

	private int state;
	private String msg;

	State (int state,String msg) {
		this.state=state;
		this.msg=msg;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}


}
