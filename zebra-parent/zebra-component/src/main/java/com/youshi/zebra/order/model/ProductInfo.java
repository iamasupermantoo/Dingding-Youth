package com.youshi.zebra.order.model;


/**
 * 商品的一些属性信息，数据库中查询出的商品，请转换成这种统一格式
 * 
 * @author wangsch
 * @date 2017年4月18日
 */
public class ProductInfo {
	/**
	 * 价格，单位分
	 */
	private Integer price;
	
	/**
	 * 充值数，单位个
	 */
	private Integer amount;
	
	/**
	 * 商品id
	 */
	private Integer productId;
	
	/**
	 * {@link ProductType}
	 */
	private Integer productType;
	
	/**
	 * 商品名
	 */
	private String productName;
	
	/**
	 * 商品描述
	 */
	private String productDesc;
	
	public ProductInfo() {}
	
	public ProductInfo(Integer price, Integer amount, Integer productId, Integer productType, String productName) {
		this.price = price;
		this.amount = amount;
		this.productId = productId;
		this.productType = productType;
		this.productName = productName;
	}

	public Integer getPrice() {
		return price;
	}

	public Integer getAmount() {
		return amount;
	}
	
	public Integer getProductId() {
		return productId;
	}
	
	public Integer getProductType() {
		return productType;
	}
	
	public String getProductName() {
		return productName;
	}
	
	public String getProductDesc() {
		return productDesc;
	}
	
	public void setPrice(Integer price) {
		this.price = price;
	}
	
	public void setAmount(Integer amount) {
		this.amount = amount;
	}
	
	public void setProductId(Integer productId) {
		this.productId = productId;
	}
	
	public void setProductType(Integer productType) {
		this.productType = productType;
	}
	
	public void setProductName(String productName) {
		this.productName = productName;
	}
	
	public void setProductDesc(String productDesc) {
		this.productDesc = productDesc;
	}
}