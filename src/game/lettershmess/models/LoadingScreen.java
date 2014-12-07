package game.lettershmess.models;

import java.util.ArrayList;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.util.Log;

import game.lettershmess.R;

public class LoadingScreen extends BaseScreen {
	private static final String TAG = LoadingScreen.class.getSimpleName();

	private LettersField mField;
	private Context context;
	private ArrayList<String> wordsDictionary;
	private MyTask mt;
	private Resources recources;
	private Typeface chops;

	public LoadingScreen(String name, GameLogic gameLogic) {
		super(name, gameLogic);
		recources = gameLogic.Resources();
		Log.i(TAG, "LoadingScreen()");
	}

	@Override
	public void OnCreate() {
		Log.i(TAG, "Create()");
	}
	
	public void LoadDictionary(Context context)
	{
		Log.i(TAG, "Loading dictionary...");
		this.context = context;
		chops = Typeface.createFromAsset(context.getAssets(), "fonts/Lobster.ttf");
		
		//TODO: true/false - используется ли процесс загрузки словаря или просто логотип с игрой
		if(true)
		{
			mt = new MyTask();
			mt.execute();
		}
		else
			super.GetGameLogic().SetActiveScreen("Menu");
	}

	public void OnLoadDictionary()
	{
		super.GetGameLogic().SetDictionary(wordsDictionary);
		super.GetGameLogic().SetActiveScreen("Menu");
	}
	
	@Override
	public void OnTouch(float x, float y) {
	}

	@Override
	public void OnDestroy() {
	}

	@Override
	public void SwitchScreen(IScreen screen) {
	}

	@Override
	public void Render(Canvas canvas) {
		double percents = Utils.getPercents();
		canvas.drawColor(Color.BLACK);

		//Рисуем на канве (взято из RayTracer)
		//upd: здесь не работает. Возможно, из-за того, что в потоке
        //Bitmap workingBitmap = BitmapFactory.decodeResource(recources, R.drawable.lettershmess);
        //Bitmap bitmap = workingBitmap.copy(Bitmap.Config.ARGB_8888, true);
        //canvas = new Canvas(bitmap);
		
		int w = super.GetGameLogic().width();
		int h = super.GetGameLogic().height();
		
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setTypeface(chops);

		paint.setTextSize(w/10);
		paint.setColor(Color.RED);
		int dxLS = (int)paint.measureText("Леттершмесс");
		canvas.drawText("ЛеттерШмесс", (w/2-dxLS/2), h/4, paint);
		
		paint.setTextSize(w/20);
		paint.setColor(Color.LTGRAY);
		int x = (int)((w-20)*percents/100.0); 
		String str = super.GetGameLogic().Resources().getString(R.string.ru_loading);
		int dxS = (int)paint.measureText(str);
		str = String.format(str, percents)+"%";
		canvas.drawText(str, 
				(float) (w/2-dxS/2),
				(float) (h/2+h/16-w/10-4), paint);

		canvas.drawRect(10, h/2, x+10, h/2+h/16, paint);
		
		//Log.i(TAG, x + "");
	}

	@Override
	public void Update() {
		//ball.update();
		//paddle.update();
	}

	@Override
	public void Destroy() {
	}
	
	class MyTask extends AsyncTask<Void, Void, Void> {

	    @Override
	    protected void onPreExecute() {
	      super.onPreExecute();
			Log.i(TAG, "onPreExecute");
	      //tvInfo.setText("Begin");
	    }

	    @Override
	    protected Void doInBackground(Void... params) {
			Log.i(TAG, "Dictionary loading...");
			wordsDictionary = Utils.ReadDictionary(context, "runouns.txt");
			Log.i(TAG, "Dictionary loaded!");

	    	//try {
	        //TimeUnit.SECONDS.sleep(2);
	        //Update();
	      //} catch (InterruptedException e) {
	      //  e.printStackTrace();
	      //}
	      return null;
	    }

	    @Override
	    protected void onPostExecute(Void result) {
	      super.onPostExecute(result);
	      OnLoadDictionary();
	    }
	  }
}
