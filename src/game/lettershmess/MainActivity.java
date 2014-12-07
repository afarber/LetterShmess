package game.lettershmess;

import game.lettershmess.models.GameLogic;
import game.lettershmess.models.GameScreen;
import game.lettershmess.models.Utils;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import game.lettershmess.R;
import game.lettershmess.R.drawable;

@TargetApi(Build.VERSION_CODES.GINGERBREAD)
public class MainActivity extends Activity {

	private static final String TAG = MainActivity.class.getSimpleName();
	private SurfaceView mainGameView;//all game logic on one canvas: menu, game, settings, game over, etc
	static Bitmap bitmap;
    static Canvas canvas;
    private GameLogic gameLogic;
    private ArrayList<String> wordsDictionary;
    private Context context;
    private MyTask mt;
    private boolean dictionaryLoaded;
    private ImageView image;
    private Activity activity;

	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i(TAG, "onCreate");

		activity = this;
		
		//дл€ работы с сетью в API>10
		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build(); 
			StrictMode.setThreadPolicy(policy); 
		}
		
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        SoundManager.getInstance();
        SoundManager.initSounds(this);
        SoundManager.loadSounds();

        SoundManager.playSound(SoundManager.SOUND_WELCOME, 1);

        if(true)
            //используем экран на максимум
        	getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        else
            //оставл€ем сверху служебную информацию - врем€, сеть и пр. „тобы было видно вход€щие сообщени€
        	getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);

        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN, WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        
        context = this;
		gameLogic = new GameLogic(context, getResources());

		dictionaryLoaded = false;
		
        SharedPreferences sPref = getPreferences(MODE_PRIVATE);
	    String data1 = sPref.getString("data1", "");

	    //TODO: —охранение игр!
	    //TODO:сделать загрузчик словар€ на этой странице
	    //плюсы: 1. задержка дл€ рекламы; 2. јнимаци€ на первой странице 3. Ќе надо будет ждать при клике на вторую страницу
	    //http://www.vogella.com/articles/AndroidBackgroundProcessing/article.html
	    
		//useExtras();
		
		//SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
	    //String xxx = prefs.getString("MESSAGE", "n/a");
	    //Log.w(TAG, "auth:" + auth);
	    //Toast.makeText(this, "Last Message from GAME:" + auth, Toast.LENGTH_LONG).show();

	    mainGameView = new MainGameView(this, gameLogic);
        //mainGameView = (MainGameView) findViewById(R.id -or- layout.game);

		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
			    RelativeLayout.LayoutParams.WRAP_CONTENT, 
			    RelativeLayout.LayoutParams.WRAP_CONTENT);
			lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
			lp.addRule(RelativeLayout.CENTER_HORIZONTAL);
/*
		//ставим рекламу на игровое поле (и в меню)
		AdView admobView = new AdView(activity, AdSize.BANNER, "a151d29f4706abf");
		admobView.setLayoutParams(lp);

		layout.addView(admobView);
		admobView.loadAd(new AdRequest());
*/
			final RelativeLayout layout = new RelativeLayout(context);
			layout.addView(mainGameView);

		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				RegisterDevice();
			}
		};
		new Thread(runnable).start();
		
		//true/false - показываем процесс загрузки словар€ или статичную фоновую картинку
	    if(true)
	    {
	        //show canvas game screen
	        setContentView(layout);
	    }
	    else
	    {
	        //show home screen with logo
			setContentView(R.layout.main_menu);
	
			mt = new MyTask();
		    mt.execute();
	
	        //find logo image for click -> game
	        image = (ImageView)findViewById(R.id.mainMenuImage);
	        
	        //–исуем на канве (вз€то из RayTracer)
	        Bitmap workingBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.lettershmess);
	        bitmap = workingBitmap.copy(Bitmap.Config.ARGB_8888, true);
	        canvas = new Canvas(bitmap);  
	
	        int w = canvas.getWidth();
	        int h = canvas.getHeight();
	        Log.i(TAG, w + "x" + h);
	
	        final Paint p = new Paint();   
	        p.setAntiAlias(true);  
	        p.setStyle(Paint.Style.FILL_AND_STROKE);  
	        p.setColor(Color.WHITE);
	        p.setTextSize(w/27);
	        String str = "ѕожалуйста, подождите. «агружаетс€ словарь...";
			int xw = (int)p.measureText(str);
	        canvas.drawText(str, w/2-xw/2, 3*h/5, p);
	        image.setImageBitmap(bitmap);
	
			image.setOnClickListener(new OnClickListener(){
				public void onClick(View v) {
					if(dictionaryLoaded)
					{
						setContentView(layout);
					}
				}
	        });
	    }

        Log.i(TAG, "onCreate.done");
	}

	private void RegisterDevice()
	{
        String regId = "n/a";
		try
		{
			//регистрируем это устройство в облаке
			//нужно будет здесь же при получении ID устройства (regId) передать его на сервер (который сейчас имитирует GCMtester)
			//там сохранить regID в базе данных и дать серверу знать, что мы в игре
			
			//вопрос: как передать данные в гугловское облако? Ќапример, сообщить о своЄм намерении сыграть в игру, закончить игру, о сделанном ходе?
			//необходимо будет получить список подключенных к игре пользователей, ожидающих игру
			//ћожно ли работать с облаком через Intent? „тобы устройство само передало в облако данные, когда будет доступ к интернету
			/*
			GCMRegistrar.checkDevice(this);
	        GCMRegistrar.checkManifest(this);
	        
	        regId = GCMRegistrar.getRegistrationId(this);
	        //GetPage("http://samodum.ru/lsgame/register/", regId);
        	//sendRegistrationIdToServer(regId);
	        if (regId.equals("")) { 
	        	GCMRegistrar.register(this, "611271756621" );
		        regId = GCMRegistrar.getRegistrationId(this);
	        	//sendRegistrationIdToServer(regId);
	        } else {
		        //Toast.makeText(this, "Already registered: " + regId, Toast.LENGTH_LONG).show();
	        	//sendRegistrationIdToServer(regId);
	        }
	        
	        if (!regId.equals(""))
	        {
		        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
	            Editor edit = prefs.edit();
	            edit.putString("deviceId", regId);
	            edit.commit();

	            Utils.GetPage("http://samodum.ru/lsgame/register/", regId);
	        }
	        */
		}
		catch(Exception e)
		{
			Log.e(TAG, "ERROR IN GCM:" + e.getMessage());
			//Toast.makeText(this, "ERROR IN GCM:" + e.getMessage(), Toast.LENGTH_LONG).show();
		}      

	}
	
	private void useExtras() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            //String title = extras.getString("title");
            String message = extras.getString("message");
            String url = extras.getString("url");

            if (url != null && !"".equalsIgnoreCase(url)) {
                Uri uri = Uri.parse(url);
                String text = message;
                if (text.length() != 1) {
                    Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
                }
                startActivity(new Intent(Intent.ACTION_VIEW, uri));
            }
        }
    }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if (keyCode == KeyEvent.KEYCODE_BACK) {
	        //saving game state
	    	Log.w(TAG, "KEY BACK PRESSED!");
	    	
	    	if(gameLogic.ActiveScreen() instanceof GameScreen)
	    	{
	    		gameLogic.SetActiveScreen("Menu");
	    		return super.onKeyDown(0, event);
	    	}
	    }
	    if (keyCode == KeyEvent.KEYCODE_HOME) {
	        //что-то делаем: завершаем Activity, открываем другую и т.д.
	    	Log.w(TAG, "KEY HOME PRESSED!");
	    }

		gameLogic.SaveGames();
	    
	    return super.onKeyDown(keyCode, event);
	}
	
	
	public void OnLoadDictionary()
	{
		Log.i(TAG, "Dictionary loaded");
		gameLogic.SetDictionary(wordsDictionary);
		
		//повтор€ем фон, чтобы затереть предыдущую надпись
		Bitmap workingBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.lettershmess);
        bitmap = workingBitmap.copy(Bitmap.Config.ARGB_8888, true);
        canvas = new Canvas(bitmap);  
        
        int w = canvas.getWidth();
        int h = canvas.getHeight();

        final Paint p = new Paint();   
        p.setAntiAlias(true);  
        p.setStyle(Paint.Style.FILL_AND_STROKE);  
        p.setColor(Color.WHITE);
        p.setTextSize(w/20);
        String str = "—ловарь загружен!";
		int xw = (int)p.measureText(str);
        canvas.drawText(str, w/2-xw/2, 3*h/5, p);
        image.setImageBitmap(bitmap);
		
		dictionaryLoaded = true;
	}
	
	class MyTask extends AsyncTask<Void, Void, Void> {

	    @Override
	    protected void onPreExecute() {
	      super.onPreExecute();
			Log.i(TAG, "onPreExecute");
	    }

	    @Override
	    protected Void doInBackground(Void... params) {
			Log.i(TAG, "Dictionary loading...");
			
			wordsDictionary = Utils.ReadDictionary(context, "runouns.txt");

	      return null;
	    }

	    @Override
	    protected void onPostExecute(Void result) {
	      super.onPostExecute(result);
	      OnLoadDictionary();			
	      Log.i(TAG, "Dictionary loaded!");
	    }
	  }
}
