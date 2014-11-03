package net.caucse.classifier;

import java.io.File;
import java.io.FileNotFoundException;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;

import net.caucse.paperlibrary.ScoreMap;
import net.caucse.paperlibrary.WordList;

public class NGramNaiveBayesClassifier extends OpinionClassifier {
	
	private HashMap<String, ScoreMap<List<String>>> pSet;
	private ScoreMap<String> pMin;
	private int n;
	public NGramNaiveBayesClassifier(String[] clss, int n) {
		pSet = new HashMap<String, ScoreMap<List<String>>>(clss.length);
		pMin = new ScoreMap<String>(clss.length);
		for (String c : clss) {
			pSet.put(c, new ScoreMap<List<String>>());
			pMin.put(c, 1.0);
		}
		this.n = n;
	}
	
	public NGramNaiveBayesClassifier(Map<String, ScoreMap<List<String>>> pSet, int n) {
		this.pSet = new HashMap<String, ScoreMap<List<String>>>(pSet.size());
		this.pMin = new ScoreMap<String>(pSet.size());
		for (Entry<String, ScoreMap<List<String>>> entry : pSet.entrySet()) {
			String key = entry.getKey();
			ScoreMap<List<String>> value = entry.getValue();
			this.pSet.put(key, new ScoreMap<List<String>>(value));
			this.pMin.put(key, Collections.min(value.values()));
		}
		this.n = n;
	}
	
	public String classify(WordList doc) {
		int len = pSet.size();
		String[] clss = new String[len];
		double[] probability = new double[len];
		for (Entry<String, ScoreMap<List<String>>> entry : pSet.entrySet()) {
			ScoreMap<List<String>> pMap = entry.getValue();
			clss[--len] = entry.getKey();
			probability[len] = 0.0;
			
			for (List<String> list : doc) {
				int size = list.size();
				if (size < n) continue;
				for (int i = 0; i < size-n; i++) {
					ArrayList<String> arr = new ArrayList<String>(n); 
					for (int j = 0; j < n; j++) {
						String w = list.get(j+i);
						arr.add(w);
					}
					
					double prob;
					if (pMap.containsKey(arr)) {
						prob = pMap.get(arr);
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
	
	public int getN() {
		return n;
	}
	
	public static class Builder {
		
		private HashMap<String, File> classToFile;
		private int n;
		public Builder(String[] clss, String[] filename, int n) {
			if (clss.length != filename.length) {
				throw new InvalidParameterException("class parameter and filename parameter MUST be the same length each other");
			}
			
			int len = clss.length;
			
			classToFile = new HashMap<String, File>(len);
			for (int i = 0; i < len; i++) {
				classToFile.put(clss[i], new File(filename[i]));
			}
			
			this.n = n;
			
		}
		
		public NGramNaiveBayesClassifier create() throws FileNotFoundException {
			HashMap<String, ScoreMap<List<String>>> pSet = new HashMap<String, ScoreMap<List<String>>>(classToFile.size());
			for (Entry<String, File> entry : classToFile.entrySet()) {
				ScoreMap<List<String>> p = load(entry.getValue());
				pSet.put(entry.getKey(), p);
			}
			return new NGramNaiveBayesClassifier(pSet, n);
		}
		
		private ScoreMap<List<String>> load(File file) throws FileNotFoundException {
			Scanner scanner = new Scanner(file);
			ScoreMap<List<String>> p = new ScoreMap<List<String>>();
			while (scanner.hasNext()) {
				ArrayList<String> ngram = new ArrayList<String>();
				for (int i = 0; i < n; i++) {
					ngram.add(scanner.next());
				}
				double value = scanner.nextDouble();
				p.put(ngram, value);
			}
			scanner.close();
			return p;
		}
	}
}
