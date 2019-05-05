package org.jastka4.rmi.server;

import org.jastka4.knapsack.KnapsackAlgorithmConstants;
import org.jastka4.knapsack.ProblemInstance;
import org.jastka4.knapsack.Solution;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;

public interface Server extends Remote {
	Solution solve(final ProblemInstance problemInstance) throws RemoteException;
	boolean register(final Registry registry) throws RemoteException;
	String getName() throws RemoteException;
	KnapsackAlgorithmConstants getAlgorithm() throws RemoteException;
}
