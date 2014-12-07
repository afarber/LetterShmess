package game.lettershmess.models;

import java.io.Serializable;

public class IamPlayer extends BasePlayer  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 976380010908549678L;

	//conctructor
	public IamPlayer(String name, int playerOrder, ColorTheme theme, int x, int y, int textX, int textY)
	{
		super(name, playerOrder, theme, x, y, textY, textY);
	}
	
}
