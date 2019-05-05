package org.jastka4.rmi.server;

import org.jastka4.knapsack.*;
import org.jastka4.rmi.OS;
import org.jastka4.rmi.register.Register;

import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerImpl implements Server {
	private static final Logger LOG = Logger.getLogger(Server.class.getName());

	private String name;
	private KnapsackAlgorithmConstants algorithm;

	private ServerImpl(String name, KnapsackAlgorithmConstants algorithm) {
		this.name = name;
		this.algorithm = algorithm;
	}

	public static void main(String... args) {
		KnapsackAlgorithmConstants algorithm = getAlgorithmByNumber(Integer.parseInt(args[0]));
		final Server server = new ServerImpl("Server_" + algorithm.toString(), algorithm);
		try {
			final Registry registry = LocateRegistry.getRegistry(Register.PORT);
			server.register(registry);
			final Server stub = (Server) UnicastRemoteObject.exportObject(server, 0);
			registry.bind(server.getName(), stub);
		} catch (RemoteException | AlreadyBoundException | NotBoundException e) {
			LOG.log(Level.SEVERE, e.getMessage());
		}
	}


	private static KnapsackAlgorithmConstants getAlgorithmByNumber(final int number) {
		switch (number) {
			case 2:
				return KnapsackAlgorithmConstants.DYNAMIC_PROGRAMMING;
			case 3:
				return KnapsackAlgorithmConstants.GREEDY_ALGORITHM;
			case 4:
				return KnapsackAlgorithmConstants.RANDOM_SEARCH;
			case 1:
			default:
				return KnapsackAlgorithmConstants.BRUTE_FORCE;
		}
	}

	@Override
	public Solution solve(final ProblemInstance problemInstance) {
		LOG.log(Level.INFO, "Solving knapsack problem: {0}", problemInstance);
		final KnapsackAlgorithmFactory factory = new KnapsackAlgorithmFactory();
		final KnapsackAlgorithm knapsack = factory.createKnapsackAlgorithm(algorithm, problemInstance);
		return knapsack.solve();
	}

	@Override
	public boolean register(final Registry registry) throws RemoteException, NotBoundException {
		LOG.log(Level.INFO, "Registering {0}!", name);
		final Register register = (Register) registry.lookup(Register.NAME);
		return register.register(new OS(name, algorithm));
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public KnapsackAlgorithmConstants getAlgorithm() {
		return algorithm;
	}
}
