package org.jastka4.rmi.client;

import org.jastka4.knapsack.Item;
import org.jastka4.knapsack.ProblemInstance;
import org.jastka4.knapsack.Solution;
import org.jastka4.rmi.OS;
import org.jastka4.rmi.register.Register;
import org.jastka4.rmi.server.Server;

import java.math.BigDecimal;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ClientImpl implements Client {
	public static void main(String... args) {
		try {
			// Getting the registry
			final Registry registry = LocateRegistry.getRegistry(Register.PORT);

			// Looking up the registry for the remote object
			final Register register = (Register) registry.lookup("Register");

			final ProblemInstance problemInstance = generateProblemInstance();
			for (OS os: register.getServers()) {
				Server stub = (Server) registry.lookup(os.getName());
				Solution solution = stub.solve(problemInstance);
				System.out.println(solution);
			}
		} catch (Exception e) {
			System.err.println("Client exception: " + e.toString());
			e.printStackTrace();
		}
	}

	private static ProblemInstance generateProblemInstance() {
		List<Item> items = new ArrayList<>();
		final Random random = new Random();
		int test = random.nextInt(20 - 1) + 1;
		for (int i = 0; i < test; i++) {
			Item item = new Item("item" + i, BigDecimal.valueOf(random.nextDouble() * (50 - 1) + 1), random.nextInt(100 - 1) + 1);
			items.add(item);
		}
		return new ProblemInstance(items, random.nextInt(1000 - 100) + 100);
	}
}
