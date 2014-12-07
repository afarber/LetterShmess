package game.lettershmess.models;

import android.graphics.Canvas;

public interface IScreen {
	public void OnCreate();
	public void OnShow();
	public void OnHide();
	public void Render(Canvas canvas);
	public void Update();
	public void Destroy();
	public void OnTouch(float x, float y);
	public void SwitchScreen(IScreen screen);
	public void SwitchScreen(String name);
	public void OnDestroy();
}
