package net.caucse.opinion;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class SentiWordReader {
	BufferedReader br;
	
	public SentiWordReader(String filename) throws FileNotFoundException {
		br = new BufferedReader(new FileReader(filename));
	}
	
	public void close() throws IOException {
		br.close();
	}
	
	public SentiWord read() throws IOException {
		String line;
		while (true) {
			line = br.readLine();
			if (line == null) return null;
			if (line.charAt(0) != '#') break;
		}
		String[] data = line.split("\t");
		String[] terms = data[4].split(" ");
		for (int i = 0; i < terms.length; i++) {
			int sharp = terms[i].indexOf('#');
			if (sharp > 0) {
				terms[i] = terms[i].substring(0, sharp);
			}
		}
		
		return new SentiWord(data[0].charAt(0), Integer.parseInt(data[1]),
				Float.parseFloat(data[2]), Float.parseFloat(data[3]), terms, data[5]);
	}
}
