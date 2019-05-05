package org.jastka4.rmi.register;

import org.jastka4.rmi.OS;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface Register extends Remote {
	int PORT = 1099;

	boolean register(OS os) throws RemoteException;
	List<OS> getServers() throws RemoteException;
}
