package org.jastka4.rmi.register;

import org.jastka4.rmi.OS;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RegisterImpl implements Register {
	private static final Logger LOG = Logger.getLogger(Register.class.getName());

	private List<OS> registeredServers;

	public static void main(String... args) {
		try {
			Registry registry = LocateRegistry.createRegistry(Register.PORT);
			// 0 - random available port for RMI service port
			Register stub = (Register) UnicastRemoteObject.exportObject(new RegisterImpl(), 0);
			registry.rebind(Register.NAME, stub);
		} catch (RemoteException e) {
			LOG.log(Level.SEVERE, e.getMessage());
		}
	}

	private RegisterImpl() {
		this.registeredServers = new ArrayList<>();
	}

	@Override
	public boolean register(final OS os) {
		if (Objects.nonNull(os)) {
			registeredServers.add(os);
			LOG.log(Level.INFO, "Registered new server {0}!", os.getName());
			printAllServers();
			return true;
		}
		LOG.log(Level.INFO, "Server {0} already registred!", os.getName());
		return false;
	}

	private void printAllServers() {
		StringBuilder stringBuilder = new StringBuilder().append("Available servers:");
		for (OS registeredServer : registeredServers) {
			stringBuilder.append("\n").append(registeredServer.getName());
		}
		LOG.log(Level.INFO, "{0}", stringBuilder);
	}

	@Override
	public List<OS> getServers() throws RemoteException {
		LOG.log(Level.INFO, "Fetching all servers!");
		return this.registeredServers;
	}
}
