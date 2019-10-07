package com.fet.crm.nspMicro.util.bean;

public class HttpResult {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer status;
	private String result;

	/**
	 * 
	 * @param status
	 *            : 狀態碼
	 * @param result
	 *            : 回傳內容
	 */
	public HttpResult(Integer status, String result) {
		super();
		this.status = status;
		this.result = result;
	}

	/**
	 * @return 狀態碼
	 */
	public Integer getStatus() {
		return status;
	}

	/**
	 * @return 回傳內容
	 */
	public String getResult() {
		return result;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public void setResult(String result) {
		this.result = result;
	}

	@Override
	public String toString() {
		return "HttpResult [status=" + status + ", result=" + result + "]";
	}

}
