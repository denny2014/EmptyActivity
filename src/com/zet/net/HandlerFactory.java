package com.zet.net;


public class HandlerFactory {
	public static BaseHandler getHandler(Class<? extends BaseHandler> myclass,
			BaseNetHandler handler) {
		try {
			BaseHandler baseHandler = myclass.newInstance();
			baseHandler.setHandler(handler);
			return baseHandler;
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
