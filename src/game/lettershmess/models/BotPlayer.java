package game.lettershmess.models;

import java.io.Serializable;

public class BotPlayer extends BasePlayer implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -9058557832822579556L;
	private double IQ;//complexity: 0-simple; 1-clever bot, Wasserman!
	
	//constructor
	public BotPlayer(String name, int playerOrder, ColorTheme theme, double IQ, int x, int y, 
			int textX, int textY)
	{
		super(name, playerOrder, theme, x, y, textX, textY);
		this.IQ = IQ;
	}

	public double IQ()
	{
		return this.IQ;
	}
}
