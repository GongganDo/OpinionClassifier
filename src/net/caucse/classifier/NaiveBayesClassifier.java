package net.caucse.classifier;

import java.io.File;
import java.io.FileNotFoundException;
import java.security.InvalidParameterException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;

import net.caucse.paperlibrary.WordList;

public class NaiveBayesClassifier extends OpinionClassifier {
	
	private HashMap<String, Map<String, Double>> pSet;
	private HashMap<String, Double> pMin;
	public NaiveBayesClassifier(String[] clss) {
		pSet = new HashMap<String, Map<String, Double>>(clss.length);
		pMin = new HashMap<String, Double>(clss.length);
		for (String c : clss) {
			pSet.put(c, new HashMap<String, Double>());
			pMin.put(c, 1.0);
		}
	}
	
	public NaiveBayesClassifier(Map<String, Map<String, Double>> pSet) {
		this.pSet = new HashMap<String, Map<String, Double>>(pSet.size());
		this.pMin = new HashMap<String, Double>(pSet.size());
		for (Entry<String, Map<String, Double>> entry : pSet.entrySet()) {
			String key = entry.getKey();
			Map<String, Double> value = entry.getValue();
			this.pSet.put(key, new HashMap<String, Double>(value));
			this.pMin.put(key, Collections.min(value.values()));
		}
	}
	
	public String classify(WordList doc) {
		int len = pSet.size();
		String[] clss = new String[len];
		double[] probability = new double[len];
		for (Entry<String, Map<String, Double>> entry : pSet.entrySet()) {
			Map<String, Double> pMap = entry.getValue();
			clss[--len] = entry.getKey();
			probability[len] = 0.0;
			
			for (List<String> list : doc) {
				for (String w : list) {
					double prob;
					if (pMap.containsKey(w)) {
						prob = pMap.get(w);
					} else {
						prob = pMin.get(clss[len]);
					}
					probability[len] += Math.log(prob);
				}
			}
		}
		
		String maxClass = clss[0];
		double maxProbability = probability[0];
		for (int i = 1; i < clss.length; i++) {
			if (maxProbability < probability[i]) {
				maxProbability = probability[i];
				maxClass = clss[i];
			}
		}
		return maxClass;
	}
	
	public static class Builder {
		
		HashMap<String, File> classToFile;
		public Builder(String[] clss, String[] filename) {
			if (clss.length != filename.length) {
				throw new InvalidParameterException("class parameter and filename parameter MUST be the same length each other");
			}
			
			int len = clss.length;
			
			classToFile = new HashMap<String, File>(len);
			for (int i = 0; i < len; i++) {
				classToFile.put(clss[i], new File(filename[i]));
			}
			
		}
		
		public NaiveBayesClassifier create() throws FileNotFoundException {
			HashMap<String, Map<String, Double>> pSet = new HashMap<String, Map<String, Double>>(classToFile.size());
			for (Entry<String, File> entry : classToFile.entrySet()) {
				Map<String, Double> p = load(entry.getValue());
				pSet.put(entry.getKey(), p);
			}
			return new NaiveBayesClassifier(pSet);
		}
		
		private Map<String, Double> load(File file) throws FileNotFoundException {
			Scanner scanner = new Scanner(file);
			HashMap<String, Double> p = new HashMap<String, Double>();
			while (scanner.hasNext()) {
				String key = scanner.next();
				double value = scanner.nextDouble();
				p.put(key, value);
			}
			scanner.close();
			return p;
		}
	}
}
