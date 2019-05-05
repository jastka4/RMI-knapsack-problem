package org.jastka4.rmi.register;

import org.jastka4.rmi.OS;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class RegisterImpl implements Register {
	private List<OS> registeredServers;

	public static void main(String[] args) {
		try {
			Registry registry = LocateRegistry.createRegistry(Register.PORT);
			Register stub = (Register) UnicastRemoteObject.exportObject(new RegisterImpl(), 0);
			registry.rebind("Register", stub);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	private RegisterImpl() {
		this.registeredServers = new ArrayList<>();
	}

	@Override
	public boolean register(final OS os) {
		if (os != null) {
			registeredServers.add(os);
			printAllServers();
			return registeredServers.contains(os);
		}
		return false;
	}

	private void printAllServers() {
		System.out.println("Available servers:");
		for (OS registeredServer : registeredServers) {
			System.out.println(registeredServer.getName());
		}
	}

	@Override
	public List<OS> getServers() throws RemoteException {
		return this.registeredServers;
	}
}
