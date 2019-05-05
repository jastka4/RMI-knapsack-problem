package org.jastka4.rmi.client;

import org.jastka4.knapsack.Item;
import org.jastka4.knapsack.ProblemInstance;
import org.jastka4.rmi.OS;
import org.jastka4.rmi.register.Register;

import java.math.BigDecimal;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;

public class ClientImpl implements Client {
	public static void main(String[] args) {
		try {
			// Getting the registry
			Registry registry = LocateRegistry.getRegistry(Register.PORT);

			// Looking up the registry for the remote object
			Register register = (Register) registry.lookup("Register");

			for (OS os: register.getServers()) {
				System.out.println(os.getName() + " " + os.getAlgorithm());
			}

//			Server stub = (Server) registry.lookup("Server 1");

			// Calling the remote method using the obtained object
			List<Item> items = new ArrayList<>();
			items.add(new Item("item 1", new BigDecimal(10), 5));
			items.add(new Item("item 2", new BigDecimal(2), 1));
			items.add(new Item("item 3", new BigDecimal(4), 3));
			items.add(new Item("item 4", new BigDecimal(6.5), 4));
			ProblemInstance problemInstance = new ProblemInstance(items,10);
//			stub.solve(problemInstance);

			 System.out.println("Remote method invoked");
		} catch (Exception e) {
			System.err.println("Client exception: " + e.toString());
			e.printStackTrace();
		}
	}
}
