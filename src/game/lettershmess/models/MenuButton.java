package game.lettershmess.models;

import android.graphics.Bitmap;
import android.graphics.Rect;

public class MenuButton {
	int position;
	Bitmap icon;
	String title;
	Rect rect;
	private String screenName;
	
	public MenuButton(String screenName)
	{
		this.screenName = screenName;
	}
	
	public String ScreenName()
	{
		return this.screenName;
	}
	
	public void SetRect(Rect rect)
	{
		this.rect = rect;
	}
	
	public Rect Rect()
	{
		return this.rect;
	}
	
	public boolean Clicked(float x, float y)
	{
		int w = rect.right - rect.left;
		return (x>=rect.left && x<=(rect.right + w*3) && y>=rect.top && y<=rect.bottom);
	}
}
