package net.caucse.opinion.test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;

import net.caucse.opinion.SentimentAnalyzer;
import net.caucse.paperlibrary.WordList;
import net.caucse.paperlibrary.WordListReader;

public class SentiWordTest {
	public static void main(String[] args) {
		try {
			
			SentimentAnalyzer analyzer = SentimentAnalyzer.load("SentiWordNet/SentiWordNet_KR.txt");
			PrintStream ps = new PrintStream("ilbe-opinion-2014.789");
			
			WordListReader wlr = new WordListReader("ilbe-words-2014.789"); 
			WordList list;
			//int i = 0;
			while ( (list = wlr.read()) != null) {
				//double score = 0.0;
				for (List<String> line : list) {
					//score += analyzer.analyze(line);
					ps.print(analyzer.analyze(line));
					ps.print(' ');
				}
				//System.out.println(++i + ": " + score);
				ps.println();
				//ps.println(score);
			}
			ps.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
