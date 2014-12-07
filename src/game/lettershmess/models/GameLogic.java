package game.lettershmess.models;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.util.Base64OutputStream;
import android.util.Log;

//singleton
public class GameLogic implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5610446110733214508L;
	private static final String TAG = GameLogic.class.getSimpleName();
	private Map<String, IScreen> screens = new HashMap<String, IScreen>();
	private IScreen activeScreen;
	private int ticks;
	private int width, height;
	private Resources resources;
	private Context context;

	private ArrayList<String> wordsDictionary;
	//private ArrayList<IPlayer> players;
	private ArrayList<Game> games;

	public GameLogic(Context context, Resources resources)
	{
		Log.i(TAG, "GameLogic()");
		this.resources = resources;
		this.context = context;
		screens = new HashMap<String, IScreen>();
		games = new ArrayList<Game>();
	}
	
	public Context Context()
	{
		return this.context;
	}
	
	public Resources Resources()
	{
		return this.resources;
	}
	
	public void SetDictionary(ArrayList<String> wordsDictionary)
	{
		this.wordsDictionary = wordsDictionary;  
	}
	
	public ArrayList<String> WordsDictionary()
	{
		return wordsDictionary;
	}
	
	//RegisterScreen
	public void AddScreen(String name, IScreen screen)
	{
		if(screens.get(name) == null)
		{
			screens.put(name, screen);
			Log.i(TAG, "new screen added: " + name);
		}
	}
	
	public void RemoveScreen(String name)
	{
		screens.remove(name);
	}
	
	public IScreen FindScreen(String name)
	{
		IScreen s = screens.get(name);
		Log.i(TAG, "screen found: " + s);
		return s;
	}
	
	public void SetActiveScreen(IScreen screen)
	{
		activeScreen = screen;
		activeScreen.OnCreate();
	}
	
	public void SetActiveScreen(String name)
	{
		Log.i(TAG, "SetActiveScreen to " + name);
		//current screen becomes old
		activeScreen.OnHide();

		//new screen
		activeScreen = FindScreen(name);
		Log.i(TAG, "ActiveScreen=" + activeScreen);
		Log.i(TAG, "ActiveScreen is " + name);
		activeScreen.OnShow();
		Log.i(TAG, "ActiveScreen " + name + " shown");
	}
	
	public IScreen ActiveScreen()
	{
		return activeScreen;
	}

	public void CreateSurface(int width, int height) {
		this.width = width;
		this.height = height;
	}
	
	public int width()
	{
		return width;
	}
	
	public int height()
	{
		return height;
	}
	
	public void AddGame(Game game)
	{
		games.add(game);
	}
	
	public void RemoveGame(Game game)
	{
		games.remove(game);
	}
	
	public 	ArrayList<Game> GetGames()
	{
		return games;
	}
 
	public Game GetGame(String name)
	{
		//ïîìåíàòü íà HashMap. íåïğàâèëüíî òàê èñêàòü!
		for (Game game : games) {
			if(game.Name().equals(name))
				return game;
		}
		return null;
	}

	public void LoadGames()
	{
		SharedPreferences sPrefGame =  PreferenceManager.getDefaultSharedPreferences(context);

	    String strGames = sPrefGame.getString("games", "");
	    //strGames = "{\"game\":[{\"player\":[{\"score\":0,\"word\":\"[]\",\"name\":\"Âû\"},{\"score\":0,\"word\":\"[]\",\"name\":\"Âàññåğäğîèä\"}],\"letters\":\"ËÊÒÅĞËÒÏÇÑÖÒ×ÅÁßÅÒÀÓÊÅÀÍÈ\",\"name\":\"game1378726829110\"}]}";
	    //strGames = "{\"game\":[{\"player\":[{\"score\":4,\"word\":\"[ÊÎÑÀ]\",\"name\":\"Âû\"},{\"score\":0,\"word\":\"[]\",\"name\":\"Âàññåğäğîèä\"}],\"letters\":\"ÊÎÑÃÀÒÃÑËĞÑÓÈÍÈßĞÏÁÂÎÒÉÇÑ\",\"name\":\"game1378727023931\"},{\"player\":[{\"score\":0,\"word\":\"[]\",\"name\":\"Âû\"},{\"score\":0,\"word\":\"[]\",\"name\":\"Âàññåğäğîèä\"}],\"letters\":\"ÍÍÌĞÅÂÅ¨ËÅÑÀÅØÀÒĞÊÇÎÏÀÍÑÒ\",\"name\":\"game1378727028215\"},{\"player\":[{\"score\":2,\"word\":\"[ÆÈĞ]\",\"name\":\"Âû\"},{\"score\":4,\"word\":\"[ÌÀÈÑ]\",\"name\":\"Àíäğóçü\"}],\"letters\":\"ÆĞÈÊÅÓÃÏØËÏÎÍÅÅÑÎÅÀÎÌÎÅÂÎ\",\"name\":\"game1378727039366\"}]}";
	    //strGames = "{\"game\":[{\"player\":[{\"score\":6,\"word\":\"[ÊÎÑÀ, ÑÈËÎÑ]\",\"name\":\"Âû\"},{\"score\":6,\"word\":\"[ÏÀÊÒ, ÈÇ]\",\"name\":\"Âàññåğäğîèä\"}],\"letters\":\"ÊÎÑÃÀÒÃÑËĞÑÓÈÍÈßĞÏÁÂÎÒÉÇÑ\",\"cards\":[2,1,1,0,2,0,0,1,1,0,1,0,1,0,2,0,0,2,0,0,0,2,0,2,0],\"name\":\"game1378727023931\"},{\"player\":[{\"score\":2,\"word\":\"[ÌÅĞÊÀ]\",\"name\":\"Âû\"},{\"score\":8,\"word\":\"[ÍÀÂÀĞĞÊÀ]\",\"name\":\"Âàññåğäğîèä\"}],\"letters\":\"ÍÍÌĞÅÂÅ¨ËÅÑÀÅØÀÒĞÊÇÎÏÀÍÑÒ\",\"cards\":[0,0,1,2,1,2,0,0,0,0,0,2,0,0,2,0,2,2,0,0,0,2,2,0,0],\"name\":\"game1378727028215\"},{\"player\":[{\"score\":0,\"word\":\"[]\",\"name\":\"Âû\"},{\"score\":0,\"word\":\"[]\",\"name\":\"Àíäğóçü\"}],\"letters\":\"ÆĞÈÊÅÓÃÏØËÏÎÍÅÅÑÎÅÀÎÌÎÅÂÎ\",\"cards\":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],\"name\":\"game1378727039366\"}]}";
	    //{"game":[{"player":[{"score":8,"word":"[ÃĞÎÃ]","name":"Âû"},{"score":10,"word":"[ÍÀÃÓË]","name":"Âàññåğäğîèä"}],"letters":"ÊÎÑÃÀÒÃÑËĞÑÓÈÍÈßĞÏÁÂÎÒÉÇÑ","cards":[2,1,3,1,2,0,2,3,2,1,1,2,1,2,2,0,0,2,0,0,1,2,0,2,0],"name":"game1378727023931"},{"player":[{"score":5,"word":"[¨ĞØ]","name":"Âû"},{"score":8,"word":"[ÏÀÊ]","name":"Âàññåğäğîèä"}],"letters":"ÍÍÌĞÅÂÅ¨ËÅÑÀÅØÀÒĞÊÇÎÏÀÍÑÒ","cards":[0,0,1,1,1,2,0,1,0,0,0,2,0,1,2,0,2,2,0,0,2,4,2,0,0],"name":"game1378727028215"},{"player":[{"score":4,"word":"[ÊÓĞÀÆ]","name":"Âû"},{"score":8,"word":"[ÀÓË]","name":"Àíäğóçü"}],"letters":"ÆĞÈÊÅÓÃÏØËÏÎÍÅÅÑÎÅÀÎÌÎÅÂÎ","cards":[3,1,1,1,0,2,2,0,0,2,0,0,2,0,0,0,0,0,2,2,0,2,0,0,2],"name":"game1378727039366"}]}

	    Log.i(TAG + ":Load games", strGames );
	    
	    //TODO:Ğàáîòàòü ÷åğåç èíòåğôåéñ!
	    try{
	    	if(false)
	    	{
		    	JSONObject jsonGamesObj = new JSONObject(strGames);
		    	JSONArray jsonGames = new JSONArray(jsonGamesObj.getString("game"));
		    	for (int i = 0; i < jsonGames.length(); i++) {
			    	JSONObject jsonGame = jsonGames.getJSONObject(i);
			    	
			        String gameName = jsonGame.getString("name");
			        String letters = jsonGame.getString("letters");
	
			        Game game = new Game(this, gameName, letters);
					this.AddGame(game);
	
					IScreen gameScr = new GameScreen(game.Name(), this);
					((GameScreen)gameScr).RegisterGame(game);
					this.AddScreen(game.Name(), gameScr);
					
					IPlayer[] players = new IamPlayer[2];
					IPlayer botOpponent = game.MakeBotPlayer("wasser/andruz", 0.99);
					
			        Log.i(TAG, "-game:" + gameName + "; letters="+letters);
	
			        JSONArray jsonCards = jsonGame.getJSONArray("cards");
			        for(int m=0;m<jsonCards.length();m++)
			        {
			        	game.Field().cards[m].SetType(jsonCards.getInt(m));
			        }
			        
			        JSONArray jsonPlayers = jsonGame.getJSONArray("player");
			        for (int j = 0; j < jsonPlayers.length(); j++) {
			        	JSONObject jsonPlayer = jsonPlayers.getJSONObject(j);
				        int score = jsonPlayer.getInt("score");
				        String playerName = jsonPlayer.getString("name");
	
				        Log.i(TAG, "---player:" + playerName + "; score="+score);
	
				        if(j==1)
							botOpponent = game.MakeBotPlayer(playerName, 0.05);
	
				        JSONArray jsonWords = new JSONArray(jsonPlayer.getString("word"));
				        for (int k = 0; k < jsonWords.length(); k++) {
					        Log.i(TAG, "-----word:" + jsonWords.getString(k));
					        if(j==1)
					        	botOpponent.AddWord(jsonWords.getString(k));
				        }
			        }
			        
			        game.Init(0);
					game.InitPlayers(botOpponent);
		    	}
	    	}
		}
		catch(Exception e){
	        Log.e(TAG + ":Load games", e.getMessage() );
		}
	}
	
	public void SaveGames() {
		Log.i(TAG, "Save games");
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        Editor edit = prefs.edit();
        
        JSONObject jsonGames = new JSONObject();
        try{
            JSONArray jsonGameArr = new JSONArray();
        	for(int i=0;i<games.size();i++)
        	{
	            JSONObject jsonGame1 = new JSONObject();
	            jsonGame1.put("letters", games.get(i).Field().Letters());

	            JSONArray jsonCards = new JSONArray();
	            for(int jj=0;jj<5;jj++)
	            {
		            for(int ii=0;ii<5;ii++)
		            {
		            	Card card = games.get(i).Field().GetCard(ii, jj);
		            	jsonCards.put(jj*5+ii, card.Type());
		            }
	            }
	            jsonGame1.put("cards", jsonCards);

	            jsonGame1.put("name", games.get(i).Name());
	            
	            JSONArray jsonPlayers = new JSONArray();
	            
	            for(int j=0;j<2;j++)
	            {
	                JSONObject jsonPlayer = new JSONObject();
		            jsonPlayer.put("name", games.get(i).GetPlayer(j+1).GetName());
		            jsonPlayer.put("score", games.get(i).GetPlayer(j+1).Score());

		            //words
		            jsonPlayer.put("word", games.get(i).GetPlayer(j+1).Words());
		            jsonPlayers.put(j, jsonPlayer);
	            }
	            jsonGame1.put("player", jsonPlayers);
	            jsonGameArr.put(i, jsonGame1);
        	}
            jsonGames.put("game", jsonGameArr);
        Log.i("jsonGames", jsonGames.toString());
        }
        catch(Exception e){}

        edit.putString("games", jsonGames.toString());
        edit.commit();
    	
	}
	
	public void SaveObject(String key, Object object)
	{
		SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(context);

		SharedPreferences.Editor ed = mPrefs.edit();
	    ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();

	    ObjectOutputStream objectOutput;
	    try {
	        objectOutput = new ObjectOutputStream(arrayOutputStream);
	        objectOutput.writeObject(object);
	        byte[] data = arrayOutputStream.toByteArray();
	        objectOutput.close();
	        arrayOutputStream.close();

	        ByteArrayOutputStream out = new ByteArrayOutputStream();
	        Base64OutputStream b64 = new Base64OutputStream(out, Base64.DEFAULT);
	        b64.write(data);
	        b64.close();
	        out.close();

	        String savedStr = new String(out.toByteArray());
	        ed.putString(key, savedStr);
	        Log.i(TAG, "Saved:" + savedStr);

	        ed.commit();
	    } catch (IOException e) {
	        e.printStackTrace();
	        Log.e(TAG, "Saving ERROR:" + e.getMessage());
	    }
	}
}

