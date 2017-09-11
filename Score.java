import java.io.*;

public class Score implements Serializable, Comparable<Score>
{
	private String name;
	private int score;
	
	public Score (String name, int score)
	{
		this.name = name;
		this.score = score;
	}
	
	public String getName()
	{
		return name;
	}
	
	public int getScore()
	{
		return score;
	}
	
	@Override
	public String toString()
	{
		return String.format("%-10.10s %6d", name, score);
	}
	
	@Override
	public boolean equals(Object other)
	{
		if(!(other instanceof Score))
			return false;
		Score otherScore = (Score) other;
		return (this.getScore() == otherScore.getScore() &&
			this.getName() == otherScore.getName());
	}
	
	public int compareTo(Score other)
	{
		if (this.equals(other))
			return 0;
		if (this.getScore() == other.getScore())
		{
			if ((this.getName()).compareTo(other.getName()) > 0)
				return 1;
			if ((this.getName()).compareTo(other.getName()) < 0)
				return -1;
		}
		if (this.getScore() > other.getScore())
			return 1;
		if (this.getScore() < other.getScore())
			return -1;
		else
			return 0;
	}
}