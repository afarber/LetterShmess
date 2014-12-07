package game.lettershmess.models;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.TargetApi;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.preference.PreferenceManager;
import android.util.Log;

import game.lettershmess.R;
import game.lettershmess.SoundManager;

public class MenuScreen extends BaseScreen {
	private static final String TAG = MenuScreen.class.getSimpleName();
	
	ArrayList<MenuButton> menuButtons;

	public MenuScreen(String name, GameLogic gameLogic) {
		super(name, gameLogic);
		menuButtons = new ArrayList<MenuButton>();
		Log.i(TAG, "Created");
	}
	
	@Override 
	public void OnShow()
	{
		menuButtons.clear();
		MenuButton mb;
		int k = 0;
		int sz = GetGameLogic().width()/5;
		int x = GetGameLogic().width()/7;
		
		Log.i(TAG, "Games:");

		if(GetGameLogic().GetGames().size()==0)
			GetGameLogic().LoadGames();
		
		if(false)
		{
			//получаем список открытых игр с сервера
			//TODO:сервер может быть недоступен
			//Тогда либо без списка игр, либо - подгрузка через облако
		
			String gamesJson = Utils.GetPage("http://samodum.ru/lsgame/openGames/?deviceId=", "");
			Log.i(TAG, "Open Games:" + gamesJson);
			try{
				JSONArray jsonArray = new JSONArray(gamesJson);
			      Log.i(TAG, "Number of entries " + jsonArray.length());
			      for (int i = 0; i < jsonArray.length(); i++) {
			        JSONObject jsonObject = jsonArray.getJSONObject(i);
			        String id = jsonObject.getString("Id");
			        String state = jsonObject.getString("State");
			        String letters = jsonObject.getString("Letters");
			        String player1 = jsonObject.getString("Player1");
			        String player2 = jsonObject.getString("Player2");
			        Log.i(TAG, id + ";" + state + ";" + letters + ";" + player1 + ";" + player2);
			        if(state.equals("0"))
			        {
				        Game game = new Game(GetGameLogic(), "game"+System.currentTimeMillis(), letters);
						GetGameLogic().AddGame(game);
						game.Init(0);
						IPlayer botOpponent = game.RandomBot();
						game.InitPlayers(botOpponent);
						
						IScreen gameScr = new GameScreen(game.Name(), GetGameLogic());
						((GameScreen)gameScr).RegisterGame(game);
						GetGameLogic().AddScreen(game.Name(), gameScr);
				        
						mb = new MenuButton(game.Name());
						mb.title = letters;
						int y = ((sz+sz/10) * k) + sz;
						mb.rect = new Rect(x, y, x+sz, y+sz);
			
						menuButtons.add(mb);
						k++;
			        }
			      }
				}catch(Exception e){
					Log.e(TAG, "ERROR in json:"+e.getMessage());
				}
		}
		
		k=0;
		for (Game game : GetGameLogic().GetGames()) {
			if(!game.GameOver())
			{
				GameScreen gameScr = new GameScreen(game.Name(), GetGameLogic());
				GetGameLogic().AddScreen(((GameScreen)gameScr).Name(), gameScr);
				gameScr.CreateSurface(GetGameLogic().width(), GetGameLogic().height());
			}
		}
		
		for (Game game : GetGameLogic().GetGames()) {
			if(!game.GameOver())
			{
				IPlayer opponent = game.GetPlayer(2);
				mb = new MenuButton(game.Name());
				mb.title = opponent.GetName();
				int y = ((sz+sz/10) * k) + sz;
				mb.rect = new Rect(x, y, x+sz, y+sz);
	
				menuButtons.add(mb);
				k++;
			}
		}
		
		mb = new MenuButton("NewGame");
		mb.title = GetGameLogic().Resources().getString(R.string.ru_new_game);
		int y = ((sz+sz/10) * k) + sz;
		mb.rect = new Rect(x, y, x+sz, y+sz);
		menuButtons.add(mb);
		k++;

		mb = new MenuButton("Settings");
		mb.title = GetGameLogic().Resources().getString(R.string.ru_settings);
		y = ((sz+sz/10) * k) + sz;
		mb.rect = new Rect(x, y, x+sz, y+sz);
		menuButtons.add(mb);	
	}

	@Override
	public void OnTouch(float x, float y) {
		// TODO Auto-generated method stub
		Log.i(TAG, "Menu touched at " + x + ", " + y);

		for (MenuButton mb : menuButtons) {
			if(mb.Clicked(x, y))
			{
				Log.i(TAG, "mb.title: " + mb.title);
				
				if(mb.ScreenName().equals("NewGame"))
				{
					if(GetGameLogic().GetGames().size()<3)
					{
				        SoundManager.playSound(SoundManager.SOUND_PS2TV, 1);
				        
						Game game = new Game(GetGameLogic(), "game"+System.currentTimeMillis(), null);
						GetGameLogic().AddGame(game);
						game.Init(0);
						IPlayer botOpponent = game.RandomBot();
						game.InitPlayers(botOpponent);

						SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(GetGameLogic().Context());
					    String regId = prefs.getString("deviceId", "");

					    if(false)
					    	Utils.GetPage("http://samodum.ru/lsgame/registerGame/?letters=" + game.Field().Letters() + "&deviceId=" + regId, regId);
						
						IScreen gameScr = new GameScreen(game.Name(), GetGameLogic());
						((GameScreen)gameScr).RegisterGame(game);
						GetGameLogic().AddScreen(game.Name(), gameScr);
						//gameLogic.SetActiveScreen(loadingScreen);
	
						SwitchScreen(game.Name());
						return;//итератор по кнопкам меняется при добавлении новой игры, поэтому выходим из цикла
					}
					else
				        SoundManager.playSound(SoundManager.SOUND_BUZZ, 1);
				}
				else
				if(mb.ScreenName().startsWith("game"))
				{
					Log.i(TAG, "Switch to game " + mb.ScreenName());
					Game game = GetGameLogic().GetGame(mb.ScreenName());

					IPlayer p1 = game.GetPlayer(1);
					IPlayer p2 = game.GetPlayer(2);
					
					//SwitchScreen(game.Name());
					super.GetGameLogic().SetActiveScreen(game.Name());
					//super.GetGameLogic().SetActiveScreen("Menu");
				}
			}
		}
		//find clicked item: new game, options or any player
		//SwitchScreen("NewGame");
	}

	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	@Override
	public void Render(Canvas canvas) {
		canvas.drawColor(Color.argb(255, 240, 240, 240));

		ColorTheme dt = ColorTheme.Pop();
		
		canvas.drawColor(dt.BackgroundColor);

		Paint paint = new Paint();
		paint.setColor(Color.BLACK);
		paint.setAntiAlias(true);
		//canvas.drawText("This is Menu", 10, 50, paint);
		//canvas.drawText("Click for New Game", (float) (100 + Math.random()*20-10), (float) (100 + Math.random()*20-10), paint);
		
		//for (MenuButton mb : menuButtons) 
		for(int m=0; m<menuButtons.size(); m++)
		{
			MenuButton mb = menuButtons.get(m);
			paint.setColor(Color.argb(255, 236, 236, 236));
			canvas.drawRect(mb.rect, paint);

			int sz = mb.rect.right - mb.rect.left;
			if(mb.ScreenName().equals("NewGame"))
			{
				paint.setColor(dt.BackgroundColor);
				Rect r = new Rect(sz/2-sz/8 + mb.rect.left, mb.rect.top + sz/8, sz/2+sz/8 + mb.rect.left, mb.rect.bottom-sz/8);
				canvas.drawRect(r, paint);
				r = new Rect(mb.rect.left + sz/8, sz/2-sz/8 + mb.rect.top, mb.rect.right-sz/8, sz/2+sz/8 + mb.rect.top);
				canvas.drawRect(r, paint);
			}
			else
				if(mb.ScreenName().equals("Settings"))
				{
					paint.setColor(dt.BackgroundColor);
					canvas.drawCircle(mb.rect.left + sz/4, mb.rect.top+sz*2/3, sz/9, paint);
					canvas.drawCircle(mb.rect.left + sz/2, mb.rect.top+sz*2/3, sz/9, paint);
					canvas.drawCircle(mb.rect.left + sz*3/4, mb.rect.top+sz*2/3, sz/9, paint);
				}
				else
					{
						try{
							Game game = GetGameLogic().GetGame(mb.ScreenName());
							LettersField field = game.Field();
									
							for(int j=0; j<5;j++)
							{
								for(int i=0; i<5;i++)
								{
									int k = field.GetCard(i, j).Type();
											
									if(k == 0)
										if((i+j)%2==0)
											//p1.setColor(Color.argb(255, 230, 230, 230));
											paint.setColor(game.Theme().ItemColor);
										else
											//p1.setColor(Color.argb(255, 231, 231, 231));
											paint.setColor(game.Theme().ItemColorOdd);
									if(k == 1)
										//p1.setColor(Color.argb(255, 255, 192, 203));
										paint.setColor(game.Theme().ItemPlayer1Color);
									if(k == 2)
										//p1.setColor(Color.argb(255, 128, 192, 255));
										paint.setColor(game.Theme().ItemPlayer2Color);
									if(k == 3)
										//p1.setColor(Color.argb(255, 200, 70, 70));
										paint.setColor(game.Theme().ItemPlayer1LockedColor);
									if(k == 4)
										//p1.setColor(Color.argb(255, 70, 70, 200));
										paint.setColor(game.Theme().ItemPlayer2LockedColor);
	
									canvas.drawRect(mb.rect.left+i*sz/5, mb.rect.top+j*sz/5, mb.rect.left+(i+1)*sz/5, mb.rect.top+(j+1)*sz/5, paint);
								}
							}
						}
						catch(Exception e){}
					}
			
			paint.setColor(Color.argb(255, 236, 236, 236));
			paint.setTextSize(sz/4);

			Game game = GetGameLogic().GetGame(mb.ScreenName());

			String info1 = mb.title;
			if(game!=null)
				if(game.CurrentPlayer()==1)
					info1 = "Ваш ход";
				else
				{
					IPlayer p = game.GetPlayer(3-game.CurrentPlayer());
					info1 = "Ждём ответа соперника";
				}
			
			//read prefs data for current user
			SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(GetGameLogic().Context());
		    String prefMessage = prefs.getString("MESSAGE", "");

			canvas.drawText(info1, mb.rect.right+sz/20, (mb.rect.top+mb.rect.bottom)/2-sz/10, paint);
			//show additional info about this game
			if(mb.ScreenName().startsWith("game"))
			{
				IPlayer p = game.GetPlayer(3-game.CurrentPlayer());
				if(p!= null && p.LastWord() != null)
				{
					String info = p.GetName() + ":" + p.LastWord();
					paint.setTextSize(sz/5);

					canvas.drawText(info + prefMessage, mb.rect.right+sz/20, (mb.rect.top+mb.rect.bottom)/2+sz/5, paint);
				}
			}
		}
	}

	@Override
	public void Update() {
/*		for (Game game : GetGameLogic().GetGames()) {
			if(game.GameOver())
			{
				GetGameLogic().RemoveScreen(game.Name());
			}
		}*/
	}
	
	
}
