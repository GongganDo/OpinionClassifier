package net.caucse.classifier;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public abstract class OpinionClassifier {
	private List<Set<String>> wordset;
	
	public OpinionClassifier() {
		wordset = new ArrayList<Set<String>>(); 
	}
	
	public void addWordset(Collection<? extends String> c) {
		HashSet<String> words = new HashSet<String>(c);
		wordset.add(words);
	}
}
