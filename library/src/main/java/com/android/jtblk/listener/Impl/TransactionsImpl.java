package com.android.jtblk.listener.Impl;

import java.util.HashMap;
import java.util.Map;

import com.android.jtblk.client.Request;
import com.android.jtblk.listener.RemoteInter;
import com.android.jtblk.pubsub.Publisher;

public class TransactionsImpl extends Publisher implements RemoteInter,Runnable{

	public static interface events<T> extends Publisher.Callback<T> {}

	public static interface OnTransaction extends TransactionsImpl.events<String> {}

	private String message;

	public TransactionsImpl(String message) {
		this.message = message;
	}

	public TransactionsImpl() {
	}

	@Override
	public String submit(Request request) throws Exception {
		Map params = new HashMap();
		params.put("streams", new String[] {"transactions"});		
		return request.submit(params);
	}

	@Override
	public void run() {
		try {
			emit(OnTransaction.class, message);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
