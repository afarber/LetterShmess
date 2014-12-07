package game.lettershmess.models;

import java.util.ArrayList;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public interface IPlayer {
	
	String GetName();
	int Score();
	void SetScore(int value);
	void draw(Canvas canvas, LettersField gameField);
	Bitmap Icon();
	void AddWord(String word);
	ArrayList<String> Words();
	String LastWord();
}
