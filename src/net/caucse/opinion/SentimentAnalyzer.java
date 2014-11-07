package net.caucse.opinion;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;

import net.caucse.opinion.SentiWord.POS;
import net.caucse.paperlibrary.ScoreMap;

public class SentimentAnalyzer {
	private ScoreMap<String> wordScore;
	private SentimentAnalyzer(ScoreMap<String> wordScore) {
		this.wordScore = wordScore;
	}
	
	public static SentimentAnalyzer load(String filename) throws FileNotFoundException, IOException {
		SentiWordReader reader = new SentiWordReader(filename);
		SentimentAnalyzer analyzer = load(reader);
		reader.close();
		return analyzer;
	}
	
	public static SentimentAnalyzer load(SentiWordReader reader) throws IOException {
		SentiWord sentiWord;
		
		ScoreMap<String> wordScore = new ScoreMap<String>();
		
		while ( (sentiWord = reader.read()) != null) {
			for (String word : sentiWord.getSynsetTerms()) {
				if (word.length() < 2) continue;
				if (word.charAt(0) == '_') continue;
				if (!word.matches(".*[가-힣].*")) continue;
				
				if (sentiWord.getPos() == POS.ADJECTIVE) {
					int last = word.length() - 1;
					if (word.charAt(last) == '의' || word.charAt(last) == '한' && word.charAt(last) == '인') {
						word = word.substring(0, last);
					} else if (word.length() >= 4 && word.charAt(last-1) == '하' && word.charAt(last) == '는') {
						word = word.substring(0, last-1);
					} else if (word.charAt(last) == '는') {
						word = word.substring(0, last);
					} else if (word.charAt(last) == '운') {
						// ~운 ==> ~ㅂ다
						char lastm1 = word.charAt(last-1);
						word = word.substring(0, last-1);
						word += (char)(lastm1 + 17);
					}
				} else if (sentiWord.getPos() == POS.VERB) {
					int last = word.length() - 1;
					if (word.charAt(last-1) == '하' && word.charAt(last) == '다') {
						if (word.length() == 3) {
							word = word.substring(0, last);
						} else {
							word = word.substring(0, last-1);
						}
					} else if (word.charAt(last) == '다') {
						word = word.substring(0, last);
					}
				}
				
				double score = sentiWord.getPosScore() - sentiWord.getNegScore();
				if (score == 0.0) continue;
				
				if (wordScore.containsKey(word)) {
					double prevScore = wordScore.get(word);
					wordScore.put(word, (score + prevScore) / 2.0);
				} else {
					wordScore.put(word, score);
				}
			}
		}
		
		return new SentimentAnalyzer(wordScore);
	}
	
	public double analyze(String[] words) {
		double score = 0.0;
		for (String word : words) {
			if (wordScore.containsKey(word)) {
				score += wordScore.get(word);
			}
		}
		return score;
	}
	
	public double analyze(Collection<? extends String> words) {
		double score = 0.0;
		for (String word : words) {
			if (wordScore.containsKey(word)) {
				score += wordScore.get(word);
			}
		}
		return score;
	}
}
