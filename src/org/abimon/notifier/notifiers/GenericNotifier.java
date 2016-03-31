package org.abimon.notifier.notifiers;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.abimon.notifier.Notification;
import org.abimon.notifier.Notified;

public class GenericNotifier implements Notified{

	public InputStream in;
	public OutputStream out;
	
	public GenericNotifier(InputStream in, OutputStream out){
		this.in = in;
		this.out = out;
	}
	
	@Override
	public void sendNotification(Notification notification) {
		try{
			out.write(("notification_name:" + notification.getName()).getBytes());
			out.write(("notification_desc:" + notification.getDesc()).getBytes());
			
			out.flush();
		}
		catch(IOException e){}
	}
	
}
