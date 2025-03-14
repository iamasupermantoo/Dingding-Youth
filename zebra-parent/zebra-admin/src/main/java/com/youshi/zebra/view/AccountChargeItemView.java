package com.youshi.zebra.view;

import com.youshi.zebra.account.model.AccountChargeItemModel;
import com.youshi.zebra.core.utils.DateTimeUtils;
import com.youshi.zebra.core.web.view.ZebraBuildContext;
import com.youshi.zebra.order.model.ProductPriceModel;

public class AccountChargeItemView {
	private AccountChargeItemModel delegate;
	private ZebraBuildContext context;
	private ProductPriceModel productPrice;
	
	public AccountChargeItemView(AccountChargeItemModel delegate, ZebraBuildContext context) {
		this.delegate = delegate;
		this.context = context;
		
		this.productPrice = delegate.getProductPrice();
		
	}
	
	public String getId() {
		return delegate.getUuid();
	}

	public int getPlusAmount() {
		return delegate.getPlusAmount();
	}

	public String getPrice() {
		return productPrice.getPrice() / 100 + "元";
	}
	
	public String getAppleProductId() {
		return productPrice.getAppleProductId();
	}
	
	public long getCreatetime() {
		return delegate.getCreateTime();
	}
}