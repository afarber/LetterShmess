package game.lettershmess.models;

import java.util.ArrayList;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

public abstract class BasePlayer implements IPlayer{

	private String name;
	private int score;
	private LettersField gameField;
	private int x;
	private int y;
	private int textX;
	private int textY;
	private ColorTheme theme;
	private	ArrayList<String> words;
	private int playerOrder;
	
	public BasePlayer(String name, int playerOrder, ColorTheme theme, int x, int y, int textX, int textY)
	{
		this.name = name;
		this.score = 0;
		this.x = x;
		this.y = y;
		this.textX = textX;
		this.textY = textY;
		this.theme = theme;
		this.playerOrder = playerOrder;
		this.words = new ArrayList<String>();
	}
	
	public void AddWord(String word)
	{
		words.add(word);
	}
	
	public ArrayList<String> Words()
	{
		return words;
	}
	
	public String LastWord()
	{
		if(words.size()>0)
			return words.get(words.size()-1);
		else return "";
	}

	public String GetName()
	{
		return this.name;
	}
	
	public Bitmap Icon()
	{
		return null;
	}
	
	public int Score()
	{
		return this.score;
	}
	
	public void SetScore(int value)
	{
		this.score = value;
	}
	
	public void draw(Canvas canvas, LettersField gameField) {
		this.gameField = gameField;
		Paint pu1 = new Paint();
		pu1.setAntiAlias(true);
		pu1.setColor(playerOrder == 1 ? theme.ItemPlayer1Color : theme.ItemPlayer2Color);
		
		canvas.drawCircle(x, y, gameField.getCardSize()/2, pu1);
		
		pu1.setColor(theme.TextColor);
		
		pu1.setTextSize((int)(gameField.getCardSize()*0.2));
		canvas.drawText(this.name, textX, textY, pu1);

		//players inner circle
		pu1.setColor(theme.BackgroundColor);
		canvas.drawCircle(x, y, gameField.getCardSize()/3, pu1);
		//canvas.drawCircle(x, y, gameField.getCardSize()/3, pu1);

	}
}
