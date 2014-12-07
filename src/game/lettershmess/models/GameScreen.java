package game.lettershmess.models;

import java.util.ArrayList;
import java.util.Random;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

import game.lettershmess.R;

public class GameScreen extends BaseScreen {
	private static final String TAG = GameScreen.class.getSimpleName();

	private Game game;

	public GameScreen(String name, GameLogic gameLogic) {
		super(name, gameLogic);
		Log.i(TAG, "GameScreen()");
	}
	
	public void RegisterGame(Game game)
	{
		this.game = game;
	}
	
	@Override
	public void OnCreate() {
		Log.i(TAG, "Create()");
	}

	@Override
	public void OnDestroy() {
	}

	@Override
	public void SwitchScreen(IScreen screen) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Update() {
		//ball.update();
		//paddle.update();
	}

	@Override
	public void Destroy() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Render(Canvas canvas) 
	{
		game.Render(canvas);
	}
	
	public void update()
	{
		
	}
	
	public void CreateSurface(int width, int height) {
		Log.i(TAG, "CreateSurface: " + super.Name());
	}

	@Override
	public void OnHide()
	{
	}
	
	@Override
	public void OnShow()
	{
		game.Show();
	}
	
	@Override
	public void OnTouch(float x, float y) {

		//если нажат левый верхний угол!
		if(x<game.CardSize()*2 && y<game.CardSize())
		{
			SwitchScreen("Menu");
			if(game.GameOver())
				GetGameLogic().RemoveGame(game);
		}

		game.Touched(x, y);
	}
}
