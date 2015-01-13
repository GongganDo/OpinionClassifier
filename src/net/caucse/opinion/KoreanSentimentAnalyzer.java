package net.caucse.opinion;

import java.io.FileNotFoundException;
import java.io.IOException;

import net.caucse.opinion.SentiWord.POS;

public class KoreanSentimentAnalyzer extends SentimentAnalyzer {
	
	public KoreanSentimentAnalyzer(String filename) throws FileNotFoundException, IOException {
		super(filename);
	}
	
	public KoreanSentimentAnalyzer(SentiWordReader reader) throws IOException {
		super(reader);
	}
	
	@Override
	protected String preProcess(String word, SentiWord sentiWord) {
		if (word.length() < 2) return null;
		if (word.charAt(0) == '_') return null;
		if (!word.matches(".*[가-힣].*")) return null;
		
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
		
		return word;
	}
	
}
