package org.jastka4.rmi.server;

import org.jastka4.knapsack.KnapsackAlgorithmConstants;
import org.jastka4.knapsack.ProblemInstance;
import org.jastka4.knapsack.Solution;
import org.jastka4.rmi.OS;
import org.jastka4.rmi.register.Register;

import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class ServerImpl implements Server {
	private String name;
	private KnapsackAlgorithmConstants algorithm;

	private ServerImpl(String name, KnapsackAlgorithmConstants algorithm) {
		this.name = name;
		this.algorithm = algorithm;
	}

	public static void main(String args[]) {
		final Server server = new ServerImpl("Server 1", KnapsackAlgorithmConstants.BRUTE_FORCE);
		try {
			final Registry registry = LocateRegistry.getRegistry(Register.PORT);
			server.register(registry);
			final Server stub = (Server) UnicastRemoteObject.exportObject(server, 0);
			registry.bind(server.getName(), stub);
		} catch (RemoteException | AlreadyBoundException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Solution solve(final ProblemInstance problemInstance) {
		return null;
	}

	@Override
	public boolean register(final Registry registry) {
		try {
			final Register register = (Register) registry.lookup("Register");
			return register.register(new OS(name, algorithm));
		} catch (RemoteException | NotBoundException e) {
			e.printStackTrace();
		}
		return false;
	}

	public String getName() {
		return name;
	}

	public KnapsackAlgorithmConstants getAlgorithm() {
		return algorithm;
	}
}
