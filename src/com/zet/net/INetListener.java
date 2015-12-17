package com.zet.net;

public interface INetListener {
	public boolean success(String type, Object objects);

	public boolean failure(String type, Object objects);

	public boolean dataFailure(String type, Object objects);

	public boolean start(String type, Object objects);

	public boolean cancel(String type, Object objects);
	
	public boolean finish(String type, Object objects);
	
	public boolean progress(String type, Object objects);
}
