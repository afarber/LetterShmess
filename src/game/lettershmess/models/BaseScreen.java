package game.lettershmess.models;

import android.graphics.Canvas;
import android.util.Log;

public class BaseScreen implements IScreen{
	private static final String TAG = BaseScreen.class.getSimpleName();

	private String name;
	
	private GameLogic gameLogic;

	public BaseScreen(String name, GameLogic gameLogic) {
		this.name = name;
		this.gameLogic = gameLogic;
	}

	public String Name()
	{
		return this.name;
	}

	public GameLogic GetGameLogic()
	{
		return this.gameLogic;
	}

	@Override
	public void SwitchScreen(IScreen screen) {
		gameLogic.SetActiveScreen(screen);
	}

	@Override
	public void SwitchScreen(String name) {
		Log.i(TAG, "SwitchScreen to " + name);
		gameLogic.SetActiveScreen(name);
	}

	@Override
	public void OnDestroy() {
	}

	@Override
	public void OnShow() {
		Log.i(TAG, "Show:" + name);
	}

	@Override
	public void OnHide() {
		Log.i(TAG, "Hide:" + name);
	}

	@Override
	public void OnCreate() {
	}

	@Override
	public void Render(Canvas canvas) {
	}

	@Override
	public void Update() {
	}

	@Override
	public void Destroy() {
	}

	@Override
	public void OnTouch(float x, float y) {
	}

}
