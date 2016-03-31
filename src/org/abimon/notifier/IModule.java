package org.abimon.notifier;

public interface IModule {
	
	public void initialise();
	
	/** This is called once every second, to check if there's any new updates */
	public void checkForUpdates();
}
