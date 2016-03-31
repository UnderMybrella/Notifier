package org.abimon.notifier;

public class Notification {
	String name, desc;
	
	public Notification(String name, String desc){
		this.name = name;
		this.desc = desc;
	}

	public String getName() {
		return name;
	}
	
	public String getDesc(){
		return desc;
	}
}
