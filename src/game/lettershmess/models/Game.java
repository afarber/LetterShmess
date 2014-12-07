package game.lettershmess.models;

import game.lettershmess.R;
import game.lettershmess.SoundManager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.TargetApi;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Build;
import android.preference.PreferenceManager;
import android.util.Log;
import android.util.TypedValue;

//TODO: Известные баги:
//1. если комп долго думает и сделать ход, то происходит сбой в очерёдности ходов игроков
//2. кнопки "отмена" и "отправить" работают всегда: и когда невидимы, и когда фишки двигаются

//TODO: сделать анимацию при убирании фишек
//сделать возможность убирать произвольную фишку (букву) из слова

@TargetApi(Build.VERSION_CODES.GINGERBREAD)
public class Game implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8326065952389292265L;
	private static final String TAG = Game.class.getSimpleName();
	private String name;
	private IamPlayer me;
	private BotPlayer opponent;
	private ColorTheme theme;
	private int _lastLetter;
	
	private GameLogic gameLogic;
	
	private Resources resources;
	private int width;
	private int height;
	
	public static final int FIRST_MOVE_IS_MINE  = 0;
	public static final int FIRST_MOVE_OPPONENT = 1;
	public static final int GAME_PLAY = 0;
	public static final int GAME_OVER = 1;
	
	private IPlayer[] players = new IPlayer[2];
	private int currentPlayer;
	
	//private Typeface chops;
	
	private LettersField mField;

	private int GameState = GAME_PLAY;
	
	private boolean NeedShake;
	private boolean ShakeHot;
	private int shakes1 = 0;
	private int shakes2 = 0;
	private int ticks;
	
	private Message msg;
	
	private boolean visible = false;
	
	private String helpWord = "";
	
	public Game(GameLogic gameLogic, String name, String letters)
	{
		this.gameLogic = gameLogic;
		this.name = name;
		this.me = me;
		this.opponent = opponent;
		visible = false;

		visible = false;
		resources = gameLogic.Resources();
		width = gameLogic.width();

		float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, resources.getDisplayMetrics());
		height = (int)(gameLogic.height() - px);//для AdMob
		
		mField = new LettersField(this, resources, width, height);
		mField.Generate(gameLogic.WordsDictionary(), letters);

		long seed = System.nanoTime();
		Random rnd = new Random(seed);
		double k = rnd.nextDouble();
		
		if(k<0.33)
			theme = ColorTheme.Pop();
		else if(k<0.67)
			theme = ColorTheme.Light();
		else
			theme = ColorTheme.YellowGreen();

		Init(FIRST_MOVE_IS_MINE);
		IPlayer botOpponent = RandomBot();
		InitPlayers(botOpponent);
		
		//CheckVictory(0);
	}
	
	public ColorTheme Theme()
	{
		return theme;
	}
	
	public LettersField Field()
	{
		return this.mField;
	}
	
	public IPlayer GetPlayer(int index)
	{
		if (index == 1) return players[0];// me;
		else return players[1];// opponent;
	}
	
	public String Name()
	{
		return name;
	}
	
	public void Init(int firstPlayer){
		visible = false;
		Log.i(TAG, "Init, player="+firstPlayer+", dictionary size=" + gameLogic.WordsDictionary().size());
		this.currentPlayer = firstPlayer + 1;
		
		this.resources = gameLogic.Resources();
		
		Log.i(TAG, "Init, WordsDictionary size=" + gameLogic.WordsDictionary().size());
		NeedShake = false;
	}
	
	public void SetIamPlayer(IamPlayer me)
	{
		Log.i(TAG, "SetIamPlayer:" + me);
		players[FIRST_MOVE_IS_MINE] = me;
		this.me = me;
	}
	
	public void SetOpponent(IPlayer opponent)
	{
		Log.i(TAG, "SetOpponent:" + opponent);
		players[FIRST_MOVE_OPPONENT] = opponent;
		this.opponent = (BotPlayer) opponent;
	}
	
	public int CurrentPlayer()
	{
		return currentPlayer;
	}
	
	public void TogglePlayer()
	{
		currentPlayer = 3 - currentPlayer;
	}
	
	public void Render(Canvas canvas)
	{
		if(!visible) return;
		
		ticks++;
		if(ticks%100 == 0)
		{
			//Log.i(TAG, "Render() " + Name());
		}

		canvas.drawColor(theme.BackgroundColor);

		//TODO:Andimation effects, elements -> IAnimation
		if(NeedShake) shakes1++;
		if(shakes1>50) NeedShake = false;
			
		if(ShakeHot) shakes2++;
		if(shakes2>100) ShakeHot = false;
			
		//background
		Paint p1 = new Paint();
		p1.setAntiAlias(true);
		p1.setColor(theme.BackgroundColor);

		//draw buttons
		if(mField.currentWord.GetLength()>0)
		{
			Paint btp = new Paint();
			btp.setColor(theme.TextColor);
			btp.setAntiAlias(true);
	
			Paint pb = new Paint();
			pb.setAntiAlias(true);
			pb.setColor(theme.ItemColor);
			for (GameButton btn : mField.GetButtons()) {
				canvas.drawRect(btn.Rectangle(), pb);
				btp.setTextSize(btn.Height()/2);
				canvas.drawText(btn.Text(), 
						(int)(btn.Rectangle().left+btn.Height()*0.3), 
						(int)(btn.Rectangle().top+btn.Height()*0.65), btp);
			}
		}
		
		double r = Math.cos(ticks*3.14/30)*0.05 + 0.6;
		//select active user
		Paint pu0 = new Paint();
		pu0.setAntiAlias(true);
		//pu0.setColor(Color.argb(255, 0, 100, 0));
		
		//me
		if(currentPlayer == 1)
		{
			pu0.setColor(theme.ItemPlayer1Color);
			canvas.drawCircle(width/2-mField.getCardSize()*2/3, (float)(mField.getCardSize()*2/3), 
					(float) (mField.getCardSize()*r), pu0);
		}
		//opponent
		else if(currentPlayer == 2)
		{
			pu0.setColor(theme.ItemPlayer2Color);
			canvas.drawCircle(width/2+mField.getCardSize()*2/3, (float)(mField.getCardSize()*2/3), 
					(float) (mField.getCardSize()*r), pu0);
		}
		
		//player 1
		players[0].draw(canvas, mField);
		
		//player 2
		players[1].draw(canvas, mField);

		//SCORE
		Paint ps = new Paint();
		ps.setAntiAlias(true);
		ps.setColor(theme.TextColor);
		ps.setTextSize((int)(mField.getCardSize()*0.44));
		float dx1 = mField.Score(1)<10 ? 0 : 0.14f;
		float dx2 = mField.Score(2)<10 ? 0 : -0.14f;
		canvas.drawText(""+mField.Score(1), (float)(width/2-mField.getCardSize()*(0.79 + dx1)), (float)(mField.getCardSize()*0.815), ps);
		canvas.drawText(""+mField.Score(2), (float)(width/2+mField.getCardSize()*(0.54 + dx2)), (float)(mField.getCardSize()*0.815), ps);
		
		//TODO:переделать!
		players[0].SetScore(mField.Score(1));
		players[1].SetScore(mField.Score(2));
		
		Paint tp = new Paint();
		tp.setColor(theme.TextColor);
		tp.setAntiAlias(true);
		//tp.setTypeface(chops);
		    
		//draw field
		for(int j=0; j<5; j++)
		{
			for(int i=0; i<5; i++)
			{
				boolean odd = (i+j)%2 == 0;
				Card card = mField.GetCard(i, j);
				String letter = card.GetLetter();
				
				if(card.IsMoving())
				{
					card.Move();
				}
				else
					card.SetAngle(0);
				
				Rect rect = card.getRect();

				card.setRect(rect);

				if(card.Type() == 0)
					if(odd)
						p1.setColor(theme.ItemColor);
					else
						p1.setColor(theme.ItemColorOdd);
				if(card.Type() == 1)
					p1.setColor(theme.ItemPlayer1Color);
				if(card.Type() == 2)
					p1.setColor(theme.ItemPlayer2Color);
				if(card.Type() == 3)
					p1.setColor(theme.ItemPlayer1LockedColor);
				if(card.Type() == 4)
					p1.setColor(theme.ItemPlayer2LockedColor);
				
				canvas.save();
				canvas.rotate(card.Angle(), (rect.left+rect.right)/2, (rect.top+rect.bottom)/2);
				if(ShakeHot && card.IsHot())
				{
					int angle = (int)(Math.sin(2 * Math.PI * shakes2/50.0)*10 * (odd?1:-1));
					canvas.rotate(angle, (rect.left+rect.right)/2, (rect.top+rect.bottom)/2);
				}
				canvas.drawRect(rect, p1);

				Typeface tf = Typeface.create("Tahoma", Typeface.BOLD);
				tp.setTypeface(tf);
				if(card.IsInWord())
				{
					card.setWidth(mField.getWordCardSize());
					tp.setTextSize(card.getWidth()/2);
				}
				else
				{
					tp.setTextSize(card.getFontSize());
				}
					
				//cards font color 
				if(card.Type() == 3)
				    tp.setColor(theme.ItemPlayerFontLockedColor1);
				else
					if(card.Type() == 4)
					    tp.setColor(theme.ItemPlayerFontLockedColor2);
					else
						tp.setColor(theme.ItemPlayerFontColor);
				
				canvas.drawText(letter, 
						rect.left+card.getWidth()/3 
							+ ((ShakeHot && card.IsHot()) ? 0 : 0)
							+ ((card.IsInWord() && NeedShake) ? (int)(Math.random()*9-4) : 0), 
						rect.top+card.getHeight()*2/3 
							+ ((false && ShakeHot && card.IsHot()) ? (int)(Math.sin(2 * Math.PI * shakes2/50.0)*mField.getCardSize()/8) : 0)
							+ ((card.IsInWord() && NeedShake) ? (int)(Math.random()*9-4) : 0), tp);

				canvas.restore();
			}
		}

		//canvas.drawRect(new Rect(0, getHeight()-50, getWidth(), getHeight()), p0);

		Paint paint = new Paint();
		paint.setColor(theme.TextColor);
		paint.setTextSize(mField.getCardSize()/4);
		paint.setAntiAlias(true);
		canvas.drawText("<<<", mField.getCardSize()/4, mField.getCardSize()/3, paint);
		//paint.setAlpha(100);

		//last word of 1st player
		String w1 = GetPlayer(1).LastWord();
		int xw1 = (int)paint.measureText(w1);
		int wx11 = (width/2-mField.getCardSize()*2/3-xw1/2);
		int wx12 = xw1/2 + width/2-mField.getCardSize()*2/3;
		if(wx12>width/2)
			wx11 = width/2-10-xw1;
		canvas.drawText(w1, wx11, (int)(mField.getCardSize()*1.5), paint);

		//second player's word
		String w2 = GetPlayer(2).LastWord();
		int xw2 = (int)paint.measureText(w2);
		int wx21 = (width/2+mField.getCardSize()*2/3);
		int wx22 = xw2/2 + width/2-mField.getCardSize()*2/3;
		if(wx22<width/2)
			wx21 = width/2+10;
		canvas.drawText(w2, wx21, (int)(mField.getCardSize()*1.5), paint);

		if(msg!=null && msg.IsVisible())
		{
			msg.Update();
			//if(msgTick<msgTicks && ScreenMessage.length()>0)
			
			paint = new Paint();
			//int alpha = (int)(255 * Math.sin(Math.PI * msgTick/msgTicks));
			paint.setColor(msg.Color());
			paint.setTextSize(mField.getCardSize()*5/12);
			paint.setAntiAlias(true);
			canvas.drawText(msg.Text(), msg.X(), msg.Y(), paint);
		}
		
		if(GameState == 1)
		{
			currentPlayer = -1;
		}
		//paddle.draw(canvas);
		//ball.draw(canvas);
	}
	
	public boolean nextMove() {
		return false;
	}

	public int CardSize()
	{
		return mField.getCardSize();
	}

	public void Show()
	{
		int bH = mField.getCardSize()/2;
		int bW = width/4;
		
		mField.AddButton(new GameButton(5, mField.cards[0].getCardRealFieldPosition(0, 0).top-bH-5, 
				resources.getString(R.string.ru_cancel), bW, bH));
		mField.AddButton(new GameButton(width-bW-5, mField.cards[0].getCardRealFieldPosition(0, 0).top-bH-5, 
				resources.getString(R.string.ru_send), bW, bH));
		
		visible = true;
	}
	
	public void Touched(float x, float y)
	{
		Card card = mField.FindCard(x, y);
		if(card != null && GameState == GAME_PLAY)
		{
			card.SetTouched(true);
			if(card.IsSelected())
			{
				//card.ReturnToField();
			}
			else
			{
		        SoundManager.playSound(SoundManager.SOUND_ARCADE_BEAT, 1);

		        card.MoveToWord();
			}
		}//is a card
		
		for (GameButton b : mField.GetButtons()) {
			if(b.Visible() && b.Clicked(x, y))
			{
				Log.i("CLICK", "button '" + b.Text()+ "' clicked");

				//нажатие на кнопку отмены хода - возврат всех карт на игровое поле
				if(b.Text() == resources.getString(R.string.ru_cancel))
				{
			        SoundManager.playSound(SoundManager.SOUND_BUZZ, 1);

					mField.CancelPlayerMove();
				}
				
				//нажатие на кнопку отправки хода
				if(b.Text() == resources.getString(R.string.ru_send))
				{
					//слово уже использовалось
					if(mField.AlreadyUsed(mField.currentWord.GetWord()))
					{
						NeedShake = true;
						shakes1 = 0;
						msg = new Message(resources.getString(R.string.ru_already_used), 
								width/2-resources.getString(R.string.ru_already_used).length()*mField.getCardSize()/10, 
								mField.cards[0].getCardRealFieldPosition(0, 0).top-mField.getCardSize()/2-5, Color.RED, 150);
					}
					else
					if(gameLogic.WordsDictionary().contains(mField.currentWord.GetWord()))
					{
						//если слово есть в словаре, то делаем ход
						if(currentPlayer == 1)
						{
					        SoundManager.playSound(SoundManager.SOUND_ENERGY, 1);

					        MyMove();
						}
						else{
					        SoundManager.playSound(SoundManager.SOUND_BELL_107, 1);

					        msg = new Message(resources.getString(R.string.ru_not_your_move), 
									width/2-resources.getString(R.string.ru_not_your_move).length()*mField.getCardSize()/10, 
									mField.cards[0].getCardRealFieldPosition(0, 0).top-mField.getCardSize()/2-5, 
									Color.RED, 150);
						}
						
						if(GameState == GAME_PLAY)
						{
							//toggle player
							if(currentPlayer == 2)
							{
								//TODO:Async http://www.vogella.com/articles/AndroidBackgroundProcessing/article.html
								Runnable runnable = new Runnable() {
								      @Override
								      public void run() {
								          BotMove();
								      }
								    };
								    new Thread(runnable).start();
							}
						}
					}
					else
					{
						//слова нет в словаре!
						if(mField.currentWord.GetWord().length()>1)
						{
							NeedShake = true;
							shakes1 = 0;

					        SoundManager.playSound(SoundManager.SOUND_BEEP_SIN_NEO, 1);

							msg = new Message(resources.getString(R.string.ru_not_dictionary), 
									width/2-resources.getString(R.string.ru_not_dictionary).length()*mField.getCardSize()/10, 
									mField.cards[0].getCardRealFieldPosition(0, 0).top-mField.getCardSize()/2-5, 
									Color.RED, 150);
						}
					}
				}
			}
		}	

	}
	
	public BotPlayer MakeBotPlayer(String name, double IQ)
	{
		BotPlayer bot = new BotPlayer(name, 2, theme, IQ, 
				(int)(width/2+CardSize()*2/3), (int)(CardSize()*2/3), 
				(int) (width/2+CardSize()*1.3), (int)(CardSize()*2/3));
		return bot;
	}

	public BotPlayer RandomBot()
	{
		//set up bot parameters: name, color, position, text position
		BotPlayer wasserDroid = MakeBotPlayer("Вассердроид", 0.99);
		BotPlayer andruz = MakeBotPlayer("Андрузь", 0.05);
		
		//opponent is selected from main menu: droids or carbonbased lifeform :)
		Random rnd = new Random(System.nanoTime());
		BotPlayer botOpponent = rnd.nextDouble()<0.5 ? wasserDroid : andruz;

		return botOpponent;
	}
	
	public void InitPlayers(IPlayer botOpponent) {
		
		//or facebook user via GCM
		CarbonBasedLifeformPlayer aliveOpponent = new CarbonBasedLifeformPlayer("Алёнка", 2, theme,
				(int)(width/2-CardSize()*2/3), (int)(CardSize()*2/3), 
				(int) (width/2-CardSize()*1.7), (int)(CardSize()*2/3));

		me = new IamPlayer("Вы", 1, theme, 
				(int)(width/2-CardSize()*2/3), (int)(CardSize()*2/3), 
				(int) (width/2-CardSize()*1.7), (int)(CardSize()*2/3));

		SetIamPlayer(me);
		SetOpponent(botOpponent);	
	}	
	
	public boolean GameOver()
	{
		return GameState == GAME_OVER;
	}

	public int LastLetter()
	{
		return _lastLetter;
	}
	
	public void SetLastLetter(int letterIndex)
	{
		_lastLetter = letterIndex;
	}
	
	private void MyMove()
	{
		int oldScore = mField.Score(currentPlayer);
		GetPlayer(currentPlayer).AddWord(mField.currentWord.GetWord());
		
		ReturnCardsToField();
		
		CheckVictory(oldScore);
		
		TogglePlayer();
	}
	
	private void ReturnCardsToField() {
		//возврат всех карт на игровое поле, пометка горячих карт, карт игрока, подсчёт очков
		int empties = 0;
		for(int i=0; i<mField.cards.length; i++)
		{
			mField.cards[i].SetHot(false);

			if(mField.cards[i].IsInWord())
			{
				mField.cards[i].PlayerMove(currentPlayer);
			}
			
			if(mField.cards[i].Type() == 0)
			{
				empties++;
				SetLastLetter(i); 
			}
		}
		if(empties>1) 
			SetLastLetter(-1);
	}

	private void BotMove()
	{
		int tries = 100000;
		if(LastLetter()>=0) 
			tries = 200000;
		
		int oldScore = mField.Score(currentPlayer);
		
		ArrayList<Integer> answerChain = mField.FindDeepAnswerInCardsChain(
				tries, ((BotPlayer)players[1]).IQ(), LastLetter());
		
		helpWord = mField.ChainToWord(answerChain);
		
		ReturnCardsToField();
		
		if(answerChain!=null)
		{
	        SoundManager.playSound(SoundManager.SOUND_ENERGY, 1);

	        Log.i("Computer move", "answer chain: " + answerChain);
			
			for(int i=0; i<25; i++)
				mField.cards[i].SetHot(false);
			
			for (Integer k : answerChain) {
				mField.cards[k].PlayerMove(currentPlayer);
			}
			
			GetPlayer(currentPlayer).AddWord(helpWord);
			
			CheckVictory(oldScore);
			
		}
		TogglePlayer();
	}
	
	private void CheckVictory(int oldScore){
		mField.currentWord.Clear();
		
		mField.CheckCardStatuses();

		int score = mField.Score(currentPlayer);
		int ds = score - oldScore;
		
		msg = new Message("+" + ds, 
				width/2-("+" + ds).length()*mField.getCardSize()/10, 
				mField.cards[0].getCardRealFieldPosition(0, 0).top-mField.getCardSize()/2-5, 
				currentPlayer == 0 ? theme.ItemPlayer1Color : theme.ItemPlayer2Color, 100);

		ShakeHot = true;
		shakes2 = 0;

		if(mField.Score(1) + mField.Score(2) == 25)
		{
			SoundManager.playSound(SoundManager.SOUND_ENERGY, 1);
			
			GameState = GAME_OVER;
			boolean IWON = mField.Score(1) > mField.Score(2);
			String winner = IWON ? resources.getString(R.string.ru_you_won) : resources.getString(R.string.ru_you_lose);
			String s = "" + winner;
			msg = new Message(s, width/2-s.length()*mField.getCardSize()/10, 
							  mField.cards[0].getCardRealFieldPosition(0, 0).top-mField.getCardSize()/2-5, 
							  theme.TextColor, 150);
		}
	}

}
