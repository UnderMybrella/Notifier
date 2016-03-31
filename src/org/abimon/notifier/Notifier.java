package org.abimon.notifier;

import java.util.LinkedList;
import java.util.Scanner;

public class Notifier implements Runnable{

	public static IModule[] modules = new IModule[]{};
	public static Scanner in = new Scanner(System.in);
	
	private static LinkedList<IModule> enabledModules = new LinkedList<IModule>();
	private static LinkedList<Notified> clientsToNotify = new LinkedList<Notified>();
	
	public static long passkey = 11037;
	
	private final ClientAcceptor clientAcceptor;
	
	private final Thread internalThread;
	private final Thread acceptanceThread;
	
	public Notifier() {
		
		System.out.print("Please enter a security key (Digits only, any number of characters): ");
		passkey = Long.parseLong(in.nextLine());
		
		for(IModule module : modules){
			System.out.println("Enable " + module.getClass().getSimpleName() + "? (Y/n)");
			boolean enable = in.nextLine().toLowerCase().charAt(0) == 'y';
			if(enable){
				module.initialise();
				enabledModules.add(module);
			}
		}
		
		internalThread = new Thread(this);
		internalThread.setName("Internal Notifications");
		internalThread.start();
		
		clientAcceptor = new ClientAcceptor();
		
		acceptanceThread = new Thread(clientAcceptor);
		acceptanceThread.setDaemon(true);
		acceptanceThread.setName("Accepting All Visitors");
		acceptanceThread.start();
	}

	public void run(){
		while(true){
			try{
				for(IModule module : enabledModules)
					module.checkForUpdates();
				Thread.sleep(1000);
			}
			catch(Throwable th){}
		}
	}
	
	public static void sendNotification(Notification notification){
		for(Notified notify : clientsToNotify)
			notify.sendNotification(notification);
	}
	
	public static void addNotifier(Notified notifier){
		clientsToNotify.add(notifier);
	}
	
	public static void main(String[] args) {
		new Notifier();
	}

}
