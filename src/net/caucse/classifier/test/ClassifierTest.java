package net.caucse.classifier.test;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Scanner;

import net.caucse.classifier.NGramNaiveBayesClassifier;
import net.caucse.paperlibrary.WordDocument;
import net.caucse.paperlibrary.WordDocumentReader;
import net.caucse.paperlibrary.WordList;
import net.caucse.paperlibrary.WordListReader;

public class ClassifierTest {
	
	public static void main(String[] args) throws Exception {
		String[] clss = {"ilbe", "todayhumor"};
		String[] filename = {"ilbe-n3-list", "todayhumor-n3-list"};
		NGramNaiveBayesClassifier.Builder builder = new NGramNaiveBayesClassifier.Builder(clss, filename, 3);
		NGramNaiveBayesClassifier nbc = builder.create();
		
		WordListReader wlr = new WordListReader("ilbe-words-2014.10.26", Charset.forName("UTF-8"));
		WordList wl = null;
		int c = 0;
		int no = 0;int no2 = 0;
		while ( (wl = wlr.read()) != null) {
			String cl = nbc.classify(wl);
			System.out.println(++c + " " + cl);
			if (clss[0].equals(cl)) ++no;
			if (clss[1].equals(cl)) ++no2;
		}
		System.err.println(no + " " + no2);
		wlr.close();
	}
	
	public static void main_old(String[] args) {
		
		Tester tester = Tester.getInstance();
		
		try {
			Map<String, Double> imap = tester.getTfMap("result-tf-ilbe");
			Map<String, Double> tmap = tester.getTfMap("result-tf-todayhumor");
			
			/*double ni = 0, nt = 0;
			for (double i : imap.values()) {
				ni += i;
			}
			for (double t : tmap.values()) {
				nt += t;
			}*/
			
			int ic = 694216, tc = 923671, iw = imap.size(), tw = tmap.size();
			int id = 430, td = 447; 
			double ip  = ic / (double)id, tp = tc / (double)td;
			
			WordDocumentReader wdr = new WordDocumentReader("twitterdata", Charset.forName("UTF-8"));
			WordDocument wd = null;
			int c = 0;
			while ( (wd = wdr.read()) != null) {
				//int selectI = 0, selectT = 0;
				double pi = Math.log(ip), pt = Math.log(tp);
				for (String w : wd.keySet()) {
					Double ival = imap.get(w);
					Double tval = tmap.get(w);
					
					if (ival == null) ival = 1.0/(ic+iw);
					if (tval == null) tval = 1.0/(tc+tw);
					
					pi += Math.log(ival);
					pt += Math.log(tval);
					
					
					//double iv = (double)ival / ni;
					//double tv = (double)tval / nt;
					
					//if (iv > tv) ++selectI;
					//else if (iv < tv) ++selectT;
				}
				//System.out.println(++c + "> select_i: " + selectI + ", select_t: " + selectT + ", result: " + (selectI>selectT?"I":"T"));
				//System.out.println(++c + "> pi: " + pi + ", pt: " + pt + ", result: " + (pi>pt?"I":"T"));
				
				if (pi > pt) {
					System.out.println(wd);
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static class Tester {
		private static Tester instance;
		private Tester() { }
		
		public static Tester getInstance() {
			if (instance == null) {
				instance = new Tester();
			}
			return instance;
		}
		
		public Map<String, Double> getTfMap(String filename) throws FileNotFoundException, IOException {
			Map<String, Double> map = new HashMap<String, Double>();
			
			Scanner scanner = new Scanner(new FileReader(filename));
			while (scanner.hasNext()) {
				String w = scanner.next();
				try {
					double f = scanner.nextDouble();
					
					map.put(w, f);
				} catch (NoSuchElementException e) {
					;
				}
			}
			System.err.println(filename + ": " + map.size());
			scanner.close();
			
			return map;
		}
	}

}