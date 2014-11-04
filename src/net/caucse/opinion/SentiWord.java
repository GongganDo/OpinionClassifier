package net.caucse.opinion;

import java.util.Arrays;

public class SentiWord {
	enum POS {
		ADJECTIVE('a'), NOUN('n'), ADVERB('r'), VERB('v');
		private char pos;
		POS(char pos) {
			this.pos = pos;
		}
		public char getPos() {
			return pos;
		}
	}
	
	private POS pos;
	private int id;
	private float posScore;
	private float negScore;
	private String[] synsetTerms;
	private String gloss;
	
	public SentiWord(POS pos, int id, float posScore, float negScore, String[] synsetTerms, String gloss) {
		this.pos = pos;
		this.id = id;
		this.posScore = posScore;
		this.negScore = negScore;
		this.synsetTerms = synsetTerms;
		this.gloss = gloss;
	}
	public POS getPos() {
		return pos;
	}
	public void setPos(POS pos) {
		this.pos = pos;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public float getPosScore() {
		return posScore;
	}
	public void setPosScore(float posScore) {
		this.posScore = posScore;
	}
	public float getNegScore() {
		return negScore;
	}
	public void setNegScore(float negScore) {
		this.negScore = negScore;
	}
	public String[] getSynsetTerms() {
		return synsetTerms;
	}
	public void setSynsetTerms(String[] synsetTerms) {
		this.synsetTerms = synsetTerms;
	}
	public String getGloss() {
		return gloss;
	}
	public void setGloss(String gloss) {
		this.gloss = gloss;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("SentiWord [pos=").append(pos).append(", id=")
				.append(id).append(", posScore=").append(posScore)
				.append(", negScore=").append(negScore)
				.append(", synsetTerms=").append(Arrays.toString(synsetTerms))
				.append(", gloss=").append(gloss).append("]");
		return builder.toString();
	}
}