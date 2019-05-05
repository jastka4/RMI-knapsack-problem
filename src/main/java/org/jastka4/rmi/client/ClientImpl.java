package org.jastka4.rmi.client;

import org.jastka4.knapsack.Item;
import org.jastka4.knapsack.ProblemInstance;
import org.jastka4.knapsack.Solution;
import org.jastka4.rmi.OS;
import org.jastka4.rmi.register.Register;
import org.jastka4.rmi.server.Server;

import java.math.BigDecimal;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.*;

public class ClientImpl implements Client {
	private static Registry registry;
	private static Register register;

	public static void main(String... args) {
		ProblemInstance problemInstance = null;
		Server server = null;

		try {
			// Getting the registry
			registry = LocateRegistry.getRegistry(Register.PORT);
			// Looking up the registry for the remote object
			register = (Register) registry.lookup(Register.NAME);
		} catch (NotBoundException | RemoteException e) {
			e.printStackTrace();
		}

		final Scanner scanner = new Scanner(System.in);
		int option;
		do {
			clearConsole();
			System.out.print(getMenu());
			option = scanner.nextInt();

			switch (option) {
				case 1:
					printServers();
					break;
				case 2:
					printServers();
					server = chooseServer(scanner);
					break;
				case 3:
					problemInstance = generateProblemInstance();
					break;
				case 4:
					if (Objects.nonNull(problemInstance) && Objects.nonNull(server)) {
						System.out.println(getSolution(server, problemInstance));
					} else {
						System.out.println("Choose server or generate problem instance first!");
					}
					break;
				default:
					break;
			}
		} while (option != 0);
	}

	private static Solution getSolution(final Server server, final ProblemInstance problemInstance) {
		try {
			return server.solve(problemInstance);
		} catch (RemoteException e) {
			return null;
		}
	}

	private static ProblemInstance generateProblemInstance() {
		List<Item> items = new ArrayList<>();
		final Random random = new Random();
		int test = random.nextInt(20 - 1) + 1;
		for (int i = 0; i < test; i++) {
			Item item = new Item("item" + i, BigDecimal.valueOf(random.nextDouble() * (50 - 1) + 1), random.nextInt(100 - 1) + 1);
			items.add(item);
			System.out.println(item);
		}
		return new ProblemInstance(items, random.nextInt(1000 - 100) + 100);
	}

	private static void printServers() {
		try {
			List<OS> servers = register.getServers();
			for (int i = 0; i < servers.size(); i++) {
				System.out.println(i + 1 + ". Server: " + servers.get(i).getName() + ", algorithm: " + servers.get(i).getAlgorithm());
			}
		} catch (RemoteException e) {
			System.out.println("Couldn't get any servers!");
		}
	}

	private static Server chooseServer(final Scanner scanner) {
		final int serverNumber = scanner.nextInt();
		final OS os;
		try {
			os = register.getServers().get(serverNumber - 1);
			return (Server) registry.lookup(os.getName());
		} catch (RemoteException | NotBoundException e) {
			System.out.println("Couldn't connect to the server!");
			return null;
		}
	}

	private static String getMenu() {
		return "         MENU\n" +
				"=====================\n" +
				"1. Show available servers\n" +
				"2. Choose server\n" +
				"3. Generate random knapsack problem instance\n" +
				"4. Solve \n" +
				"Input: ";
	}

	private static void clearConsole() {
		try {
			final String os = System.getProperty("os.name");

			if (os.contains("Windows")) {
				Runtime.getRuntime().exec("cls");
			} else {
				Runtime.getRuntime().exec("clear");
			}
		} catch (final Exception e) {
			// do nothing
		}
	}

}
