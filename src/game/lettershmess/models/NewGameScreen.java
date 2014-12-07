package game.lettershmess.models;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

public class NewGameScreen extends BaseScreen {
	private static final String TAG = NewGameScreen.class.getSimpleName();
	
	public NewGameScreen(String name, GameLogic gameLogic) {
		super(name, gameLogic);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void OnCreate() {
	}

	@Override
	public void OnTouch(float x, float y) {
		// TODO Auto-generated method stub
		Log.i(TAG, "New Game touched: " + x + ", " + y);
		SwitchScreen("Menu");
	}

	@Override
	public void OnDestroy() {
		//menuButtons.clear();
	}

	@Override
	public void SwitchScreen(IScreen screen) {
		// TODO Auto-generated method stub
	}

	@Override
	public void Render(Canvas canvas) {
		canvas.drawColor(Color.argb(255, 240, 240, 240));

		Paint paint = new Paint();
		paint.setColor(Color.RED);
		paint.setAntiAlias(true);
		canvas.drawText("This is New Game", 50, 50, paint);
		canvas.drawText("Click for MENU", (float) (50 + Math.random()*20-10), (float) (150 + Math.random()*20-10), paint);
	}

	@Override
	public void Destroy() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Update() {
		// TODO Auto-generated method stub
		
	}
}