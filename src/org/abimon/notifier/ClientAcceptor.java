package org.abimon.notifier;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

import org.abimon.notifier.notifiers.GenericNotifier;

public class ClientAcceptor implements Runnable{

	HashMap<String, Long> ipsLocked = new HashMap<String, Long>();
	
	public void run(){
		ServerSocket server = null;
		while(true){
			try{
				if(server == null)
					server = new ServerSocket(11037);
				Socket client = server.accept();
				
				DataInputStream in = new DataInputStream(client.getInputStream());
				DataOutputStream out = new DataOutputStream(client.getOutputStream());
				
				if(ipsLocked.containsKey(client.getInetAddress().toString())){
					long lockedTime = ipsLocked.get(client.getInetAddress().toString());
					long currentTime = System.currentTimeMillis();
					
					if(currentTime - lockedTime > 2000)
						ipsLocked.remove(client.getInetAddress().toString());
					else{
						out.write("timed_out".getBytes());
						out.close();
					}
				}
				
				long pin = in.readLong();
				
				if(pin == Notifier.passkey){
					Notifier.addNotifier(new GenericNotifier(in, out));
				}
				else{
					out.write("invalid_pin".getBytes());
					out.close();
					ipsLocked.put(client.getInetAddress().toString(), System.currentTimeMillis());
				}
				
				Thread.sleep(100);
			}
			catch(Throwable th){}
		}
	}
}
