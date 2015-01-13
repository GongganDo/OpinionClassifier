package net.caucse.opinion;


public class SentimentResult {
	private double posScore;
	private double negScore;
	
	public SentimentResult() {
		this.posScore = this.negScore = 0.0;
	}
	
	public SentimentResult(double posScore, double negScore) {
		setPosScore(posScore);
		setNegScore(negScore);
	}

	/**
	 * Return positive score
	 * @return positive score (0~1)
	 */
	public double getPosScore() {
		return posScore;
	}

	public void setPosScore(double posScore) {
		if (posScore > 1.0) posScore = 1.0;
		this.posScore = posScore;
	}

	/**
	 * Return negative score
	 * @return negative score (0~1)
	 */
	public double getNegScore() {
		return negScore;
	}

	public void setNegScore(double negScore) {
		if (negScore > 1.0) negScore = 1.0;
		this.negScore = negScore;
	}
	
	/**
	 * Return objective score
	 * Objective score is calculated by 1 - (posScore + negScore)
	 * @return objective score (0~1)
	 */
	public double getObjScore() {
		return 1.0 - posScore - negScore;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("SentimentResult [posScore=").append(posScore)
				.append(", negScore=").append(negScore).append(", objScore=")
				.append(getObjScore()).append("]");
		return builder.toString();
	}
}
