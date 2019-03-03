package com.westars.framework.plug.pay;

/**
 * 商品信息
 * 
 * @author Aports
 * 
 */
public class OrderInfo {

	private String order_id; // 订单ID
	private String order_sn; // 订单号
	private String order_title; // 商品标题
	private String order_body; // 商品详情
	private String order_free; // 商品价格

	public String getOrder_id() {
		return order_id;
	}

	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}

	public String getOrder_sn() {
		return order_sn;
	}

	public void setOrder_sn(String order_sn) {
		this.order_sn = order_sn;
	}

	public String getOrder_title() {
		return order_title;
	}

	public void setOrder_title(String order_title) {
		this.order_title = order_title;
	}

	public String getOrder_body() {
		return order_body;
	}

	public void setOrder_body(String order_body) {
		this.order_body = order_body;
	}

	public String getOrder_free() {
		return order_free;
	}

	public void setOrder_free(String order_free) {
		this.order_free = order_free;
	}

	@Override
	public String toString() {
		return "OrderInfo [order_id=" + order_id + ", order_sn=" + order_sn + ", order_title=" + order_title + ", order_body=" + order_body
				+ ", order_free=" + order_free + "]";
	}

}
