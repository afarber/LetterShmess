package game.lettershmess.models;

import android.graphics.Rect;

public class GameButton {
	private String text;
	private int x;
	private int y;
	private int width;
	private int height;
	private boolean visible;
	
	public GameButton(int x, int y, String text, int width, int height)
	{
		this.x = x;
		this.y = y;
		this.text = text;
		this.width = width;
		this.height = height;
		this.visible = true;
	}
	
	public boolean Clicked(float x, float y)
	{
		return (x >= this.x && x <= (this.x + this.width) && y >= this.y && y <= (this.y + this.height));
	}
	
	public Rect Rectangle()
	{
		return new Rect(x, y, x+width, y+height);
	}
	
	public int Width()
	{
		return width;
	}
	
	public int Height()
	{
		return height;
	}
	
	public String Text()
	{
		return text;
	}
	
	public void SetVisible(boolean visible)
	{
		this.visible = visible;
	}
	
	public boolean Visible()
	{
		return this.visible;
	}
}
