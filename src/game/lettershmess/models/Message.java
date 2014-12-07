package game.lettershmess.models;

import java.io.Serializable;

import android.graphics.Color;

public class Message implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1458597731689085096L;
	public String text;
	private int tick;
	private int ticks;
	private int x;
	private int y;
	private int color;
	private boolean isVisible;
	
	public Message(String text, int x, int y, int color, int ticks)
	{
		this.text = text;
		this.x = x;
		this.y = y;
		this.ticks = ticks;
		this.tick = 0;
		this.color = color;
		this.isVisible = true;
	}
	
	public void Update()
	{
		if(isVisible)
			tick++;
		
		int alpha = (int)(255 * Math.sin(Math.PI * tick/ticks));
		color = Color.argb(alpha, Color.red(color) , Color.green(color), Color.blue(color));
		
		y -= 1;
		
		if (tick>=ticks)
		{
			isVisible = false;
			tick = 0;
		}
	}
	
	public boolean IsVisible()
	{
		return isVisible;
	}
	
	public String Text()
	{
		return text;
	}
	
	public int X()
	{
		return x;
	}
	
	public int Y()
	{
		return y;
	}
	
	public int Color()
	{
		return color;
	}
}
