package net.caucse.classifier;

import weka.core.Attribute;
import weka.core.FastVector;

public class BasicOpinionClassifier extends OpinionClassifier {
	public BasicOpinionClassifier() {
		super();
	}
	
	public void execute() {
		FastVector fv = new FastVector();
		fv.addElement("ilbe");
		fv.addElement("todayhumor");
		Attribute attr = new Attribute("group", fv);
		
		
	}
}
