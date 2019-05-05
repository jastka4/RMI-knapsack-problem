package org.jastka4.rmi;

import org.jastka4.knapsack.KnapsackAlgorithmConstants;

import java.io.Serializable;

public class OS implements Serializable {
	private String name;
	private KnapsackAlgorithmConstants algorithm;

	public OS(String name, KnapsackAlgorithmConstants algorithm) {
		this.name = name;
		this.algorithm = algorithm;
	}

	public String getName() {
		return name;
	}

	public KnapsackAlgorithmConstants getAlgorithm() {
		return algorithm;
	}
}
