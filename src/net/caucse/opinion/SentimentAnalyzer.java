package net.caucse.opinion;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;

import net.caucse.paperlibrary.CountMap;
import net.caucse.paperlibrary.ScoreMap;

public class SentimentAnalyzer {
	private ScoreMap<String> positiveWordScore;
	private ScoreMap<String> negativeWordScore;
	
	public SentimentAnalyzer(String filename) throws FileNotFoundException, IOException {
		SentiWordReader reader = new SentiWordReader(filename);
		load(reader);
		reader.close();
	}
	
	public SentimentAnalyzer(SentiWordReader reader) throws IOException {
		load(reader);
	}
	
	private void load(SentiWordReader reader) throws IOException {
		SentiWord sentiWord;
		
		positiveWordScore = new ScoreMap<String>();
		negativeWordScore = new ScoreMap<String>();
		CountMap<String> wordCount = new CountMap<String>();
		
		while ( (sentiWord = reader.read()) != null) {
			for (String word : sentiWord.getSynsetTerms()) {
				
				word = preProcess(word, sentiWord);
				if (word == null) continue;
				
				wordCount.add(word);
				positiveWordScore.add(word, sentiWord.getPosScore());
				negativeWordScore.add(word, sentiWord.getNegScore());
			}
		}
		
		for (String word : wordCount.keySet()) {
			int count = wordCount.get(word);
			double posScore = positiveWordScore.get(word);
			double negScore = negativeWordScore.get(word);
			positiveWordScore.put(word, posScore / count);
			negativeWordScore.put(word, negScore / count);
		}
	}
	
	/**
	 * SentiWordNet 데이터를 불러올 때 전처리가 필요한 부분 정의
	 * @param word 불러온 단어
	 * @param sentiWord 불러온 단어의 SentiWordNet 정보
	 * @return 전처리를 수행한 후의 단어, 불용어인 경우 null을 반환
	 */
	protected String preProcess(String word, SentiWord sentiWord) {
		word = word.replace('_', ' ');
		return word;
	}
	
	public SentimentResult analyze(String[] words) {
		double posScore = 0.0, negScore = 0.0;
		for (String word : words) {
			if (positiveWordScore.containsKey(word)) {
				posScore += positiveWordScore.get(word);
				negScore += negativeWordScore.get(word);
			}
		}
		return new SentimentResult(posScore / words.length, negScore / words.length);
	}
	
	public SentimentResult analyze(Collection<? extends String> words) {
		double posScore = 0.0, negScore = 0.0;
		for (String word : words) {
			if (positiveWordScore.containsKey(word)) {
				posScore += positiveWordScore.get(word);
				negScore += negativeWordScore.get(word);
			}
		}
		return new SentimentResult(posScore / words.size(), negScore / words.size());
	}
}
