package game.lettershmess.models;

import java.io.Serializable;

import android.graphics.Color;

public class ColorTheme implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6952156437118927855L;
	public int BackgroundColor; 
	public int ItemColor; 
	public int ItemColorOdd; 
	public int ItemPlayer1Color; 
	public int ItemPlayer2Color; 
	public int ItemPlayer1LockedColor; 
	public int ItemPlayer2LockedColor; 
	public int ItemPlayerFontColor; 
	public int ItemPlayerFontLockedColor1;
	public int ItemPlayerFontLockedColor2;
	public int TextColor;

	public static ColorTheme Pop()
	{
		ColorTheme theme = new ColorTheme();
		theme.BackgroundColor = Color.BLACK;// Color.argb(255, 49, 49, 49);
		theme.ItemColor = Color.argb(255, 55, 55, 55);
		theme.ItemColorOdd = Color.argb(255, 57, 57, 57);
		theme.ItemPlayer1Color = Color.argb(255, 152, 24, 123);
		theme.ItemPlayer2Color = Color.argb(255, 87, 152, 24);
		theme.ItemPlayer1LockedColor = Color.argb(255, 255, 0, 198);
		theme.ItemPlayer2LockedColor = Color.argb(255, 126, 255, 0);
		theme.ItemPlayerFontColor = Color.argb(200, 236, 236, 236);
		theme.ItemPlayerFontLockedColor1 = Color.argb(200, 0, 0, 0);
		theme.ItemPlayerFontLockedColor2 = Color.argb(200, 0, 0, 0);
		theme.TextColor = Color.argb(255, 236, 236, 236);
		return theme;
	}
	
	public static ColorTheme Light()
	{
		ColorTheme theme = new ColorTheme();
		theme.BackgroundColor = Color.argb(255, 240, 240, 240);
		theme.ItemColor = Color.argb(255, 230, 230, 230);
		theme.ItemColorOdd = Color.argb(255, 232, 232, 232);
		theme.ItemPlayer1Color = Color.argb(255, 255, 192, 203);
		theme.ItemPlayer2Color = Color.argb(255, 128, 192, 255);
		theme.ItemPlayer1LockedColor = Color.argb(255, 200, 70, 70);
		theme.ItemPlayer2LockedColor = Color.argb(255, 70, 70, 200);
		theme.ItemPlayerFontColor = Color.argb(200, 40, 40, 40);
		theme.ItemPlayerFontLockedColor1 = Color.argb(200, 0, 0, 0);
		theme.ItemPlayerFontLockedColor2 = Color.argb(200, 0, 0, 0);
		theme.TextColor = Color.argb(255, 40, 40, 40);
		return theme;
	}
	
	public static ColorTheme YellowGreen()
	{
		ColorTheme theme = new ColorTheme();
		theme.BackgroundColor = Color.argb(255, 224, 230, 216);
		theme.ItemColor = Color.argb(255, 215, 220, 207);
		theme.ItemColorOdd = Color.argb(255, 217, 223, 209);
		theme.ItemPlayer1Color = Color.argb(255, 122, 164, 137);
		theme.ItemPlayer2Color = Color.argb(255, 239, 193, 108);
		theme.ItemPlayer1LockedColor = Color.argb(255, 21, 99, 59);
		theme.ItemPlayer2LockedColor = Color.argb(255, 255, 156, 0);
		theme.ItemPlayerFontColor = Color.argb(200, 43, 44, 41);
		theme.ItemPlayerFontLockedColor1 = Color.argb(200, 208, 223, 215);
		theme.ItemPlayerFontLockedColor2 = Color.argb(200, 43, 44, 41);
		theme.TextColor = Color.argb(255, 75, 73, 64);
		return theme;
	}
	
}
