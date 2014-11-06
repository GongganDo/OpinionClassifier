package net.caucse.opinion.test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import net.caucse.opinion.SentiWord;
import net.caucse.opinion.SentiWord.POS;
import net.caucse.opinion.SentiWordReader;
import net.caucse.paperlibrary.ScoreMap;
import net.caucse.paperlibrary.WordList;
import net.caucse.paperlibrary.WordListReader;

public class SentiWordTest {
	public static void main(String[] args) {
		try {
			SentiWordReader swr = new SentiWordReader("SentiWordNet/SentiWordNet_KR.txt");
			SentiWord sentiWord;
			
			ScoreMap<String> wordScore = new ScoreMap<String>();
			
			while ( (sentiWord = swr.read()) != null) {
				for (String word : sentiWord.getSynsetTerms()) {
					if (word.length() < 2) continue;
					if (word.charAt(0) == '_') continue;
					if (!word.matches(".*[가-힣].*")) continue;
					
					if (sentiWord.getPos() == POS.ADJECTIVE) {
						int last = word.length() - 1;
						if (word.charAt(last) == '의' || word.charAt(last) == '한' && word.charAt(last) == '인') {
							word = word.substring(0, last);
						} else if (word.charAt(last-1) == '하' && word.charAt(last) == '는') {
							word = word.substring(0, last-1);
						} else if (word.charAt(last) == '는') {
							word = word.substring(0, last);
						}
					} else if (sentiWord.getPos() == POS.VERB) {
						int last = word.length() - 1;
						if (word.charAt(last-1) == '하' && word.charAt(last) == '다') {
							word = word.substring(0, last-1);
						} else if (word.charAt(last) == '다') {
							word = word.substring(0, last);
						}
					}
					
					double score = sentiWord.getPosScore() - sentiWord.getNegScore();
					
					if (wordScore.containsKey(word)) {
						double prevScore = wordScore.get(word);
						wordScore.put(word, (score + prevScore) / 2.0);
					} else {
						wordScore.put(word, score);
					}
				}
			}
			
			swr.close();
			
			WordListReader wlr = new WordListReader("ilbe-words-2014.789"); 
			WordList list;
			int i = 0;
			while ( (list = wlr.read()) != null) {
				double score = 0.0;
				for (List<String> line : list) {
					for (String w : line) {
						if (wordScore.containsKey(w)) {
							score += wordScore.get(w);
						}
					}
				}
				System.out.println(++i + ": " + score);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
