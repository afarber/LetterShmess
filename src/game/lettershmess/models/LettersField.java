package game.lettershmess.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import game.lettershmess.R;

import android.annotation.TargetApi;
import android.content.res.Resources;
import android.os.Build;
import android.util.Log;

@TargetApi(Build.VERSION_CODES.GINGERBREAD)
public class LettersField {
	private Game game;
	public Card[] cards;
	public ArrayList<GameButton> buttons;
	
	private Resources resources;
	private int score1;
	private int score2;
	
	//private ArrayList<String> words1;
	//private ArrayList<String> words2;
	
	private int cardsSize;
	private int wordCardsSize;
	private int _winW;
	private int _winH;
	
	//public String currentWord;
	public Word currentWord;
	
	private ArrayList<String> wordsDictionary;
	private String gameLetters = "";

	public LettersField(Game game, Resources resources, int windowWidth, int windowHeight)
	{
		Log.i("LettersField", "constructor");
		this.game = game;
		this.resources = resources;
		_winW = windowWidth;
		_winH = windowHeight;
		cardsSize = (int)((_winH*0.6)/5);
		buttons = new  ArrayList<GameButton>();
		wordCardsSize = cardsSize;
		cards = new Card[25];
		currentWord = new Word();
		currentWord.Clear();
	}

	/*
	public void AddAnswer(int player, String word)
	{
		if(player == 1)
			words1.add(word);
		else
			words2.add(word);
	}
	*/
	public boolean AlreadyUsed(String word)
	{
		if(game.GetPlayer(1).Words().contains(word)) 
			{
				return true;
			}
		else
			if(game.GetPlayer(2).Words().contains(word)) 
				{
					return true;
				}
		return false;
	}
	
	
	private int CardTypeLeft(int k)
	{
		int x = k%5;
		int y = (int)(k/5);
		if(x>0)
			return GetCard(x-1, y).Type();
		else
			return -1;
	}
	
	private int CardTypeRight(int k)
	{
		int x = k%5;
		int y = (int)(k/5);
		if(x<4)
			return GetCard(x+1, y).Type();
		else
			return -1;
	}
	
	private int CardTypeUp(int k)
	{
		int x = k%5;
		int y = (int)(k/5);
		if(y>0)
			return GetCard(x, y-1).Type();
		else
			return -1;
	}
	
	private int CardTypeDown(int k)
	{
		int x = k%5;
		int y = (int)(k/5);
		if(y<4)
			return GetCard(x, y+1).Type();
		else
			return -1;
	}
	
	private int IsMine(int player, int cardType)
	{
		if(player == 1)
			return (cardType == 1 || cardType == 3 || cardType == -1) ? 1 : 0;
		else
		if(player == 2)
			return (cardType == 2 || cardType == 4 || cardType == -1) ? 1 : 0;
		else return 0;
	}
	
	public void CheckCardStatuses()
	{
		int oldScore1 = score1; 
		int oldScore2 = score2; 
		score1 = 0;
		score2 = 0;
		for(int i=0; i<25; i++)
		{
			int cl = CardTypeLeft(i);
			int cr = CardTypeRight(i);
			int cu = CardTypeUp(i);
			int cd = CardTypeDown(i);
			
			int cnt = 0;
			if(cards[i].Type() == 1)
			{	
				cnt = cnt + IsMine(1,cl) + IsMine(1,cr) + IsMine(1,cu) + IsMine(1,cd);
				if(cnt == 4)
					cards[i].SetType(3);
			}

			if(cards[i].Type() == 2)
			{	
				cnt = cnt + IsMine(2,cl) + IsMine(2,cr) + IsMine(2,cu) + IsMine(2,cd);
				if(cnt == 4)
					cards[i].SetType(4);
			}
			
			if(cards[i].Type() == 1 || cards[i].Type() == 3)
				score1++;
			if(cards[i].Type() == 2 || cards[i].Type() == 4)
				score2++;
		}
	}
	
	public int Score(int player)
	{
		if(player == 1)
			return score1;
		else return score2;
	}
	
	public void AddButton(GameButton button)
	{
		buttons.add(button);
	}
	
	public ArrayList<GameButton> GetButtons()
	{
		return buttons;
	}
	
	public int Width()
	{
		return _winW;
	}
	
	public int Height()
	{
		return _winH;
	}
	
	public void RemoveLetter(int position)
	{
		currentWord.RemoveAt(position);
	}
	
	public void ResizeWordIfNeed()
	{
		
		int csize = _winW / currentWord.GetLength();

		wordCardsSize = Math.min(csize, cardsSize);
		
		for(int i=0; i<25; i++)
		{
			if(cards[i].IsInWord() || cards[i].IsMoving())
			{
				cards[i].setWidth(getWordCardSize());
				cards[i].setRealX((cards[i].GetLetterPosition()) * wordCardsSize);
			}

			if(cards[i].IsMoving())
				cards[i].updateXY2((cards[i].GetLetterPosition()) * wordCardsSize, 
						(int)((cards[i].getRect().bottom-cards[i].getRect().top)*1.5), wordCardsSize);

		}
	}
	
	public void AddLetter(String letter)
	{
		currentWord.Append(letter);
	}

	public void SetCurrentWord(String word)
	{
		this.currentWord.Set(word);
	}
	
	public String GetCurrentWord()
	{
		return this.currentWord.GetWord();
	}

	public String reverse(String word) {
	    char[] chs = word.toCharArray();

	    int i=0, j=chs.length-1;
	    while (i < j) {
	        char t = chs[i];
	        chs[i] = chs[j];
	        chs[j] = t;
	       i++; j--;
	    }
	    return new String(chs);
	}
	
	private ArrayList<Integer> FindCardsChain(String word)
	{
		ArrayList<Integer> cardsChain = new ArrayList<Integer>();
		String letters = gameLetters;
		
		//if need recalc p-index
		int z1 = 0;
		int z2 = 1;
		if(Math.random()<0.5)
		{
			letters = reverse(letters);
			z1 = 25-1;
			z2 = -1;
		}
		
		//Log.i("letters", letters + " - " + word);
		for(int i=0; i<word.length(); i++)
		{
			String wl = word.substring(i, i+1);
			//Log.i("letter", wl);
			
			if(letters.contains(wl))
			{
				int p = letters.indexOf(wl);
				String part1 = "";
				if(p>0)
					part1 = letters.substring(0, p);
				String part2 = "";
				if(p<letters.length())
					part2 = letters.substring(p+1, letters.length());
				letters = part1 + "." + part2;
				cardsChain.add(z1 + p*z2);
			}
			else
			{
				//Log.i("letters", "Слово не подходит. Совпадений "+(i+1)+"/"+word.length());
				return null;
			}
			//Log.i("letters", letters);
		}
		//Log.i("letters", "" + cardsChain);
		return cardsChain;
	}
	
	public String ChainToWord(ArrayList<Integer> cardsChain)
	{
		if(cardsChain == null || cardsChain.size() < 1) 
			return "СДАЮСЬ!";
		else
		{
			String answer = "";
			Log.i("cardsChain", "" + cardsChain);
			for (Integer i : cardsChain) {
				Log.i("card letter", i + " -> " + cards[i].GetLetter());
				answer += cards[i].GetLetter();
			}
			return answer;
		}
	}
	
	//probability - is probability of fast answer, without deep search. Complexity of game (0 - very hard, 1 - very simple game)
	public ArrayList<Integer> FindDeepAnswerInCardsChain(int maxTries, double complexity, int lastLetter)
	{
		Random randomGenerator = new Random();
		int k = 0;
		int maxLen = 0;
		ArrayList<Integer> foundCardsChain = null;
		String foundWord = "";
		
		double xx = Math.random();
		boolean deepSearch = xx<complexity;
		Log.i("deepSearch", xx + " -> " + deepSearch);
		
		String ru_abc33_unsorted = resources.getString(R.string.ru_abc33_unsorted);
		while(k < maxTries)
		{
			int index = randomGenerator.nextInt(wordsDictionary.size());
	        String word = wordsDictionary.get(index);
	
	        ArrayList<Integer> cardsChain = FindCardsChain(word);
	        
	        if(cardsChain != null && cardsChain.size()>0 && !AlreadyUsed(word))
	        {
	        	if(lastLetter<0 || (lastLetter>=0
	        			&& word.contains(cards[lastLetter].GetLetter())
	        			))
	        	{
		        	if(!deepSearch)
		        	{
		        		Log.i("FindDeepAnswerInCardsChain", "" + cardsChain);
			        	return cardsChain;//return first found correct unused word
		        	}
		        	else
		        	//finding max word
		        	if(cardsChain.size()>maxLen)
		        	{
		        		foundWord = word;
		        		foundCardsChain = cardsChain;
		        		maxLen = cardsChain.size();
		        	}
	        	}
	        }
	        k++;
		}
		Log.i("FindDeepAnswerInCardsChain", foundWord + " - " + foundCardsChain);
		return foundCardsChain;
	}
	
	//returns constant card cize in the field
	public int getCardSize()
	{
		return cardsSize;
	}
	
	//returns card size in word area
	public int getWordCardSize()
	{
		return wordCardsSize;
	}
	
	public String Letters()
	{
		return this.gameLetters;
	}
	
	public void Generate(ArrayList<String> wordsDictionary, String letters)
	{
		Log.i("LettersField", "Generate, " + wordsDictionary.size());
		this.wordsDictionary = wordsDictionary;

		Collections.sort(wordsDictionary);
		Utils utils = new Utils(resources);
		//utils.InitLetters();
		
		for(int i=0; i<25; i++)
		{
			String letter = utils.getRandomLetter();
			//TODO:для теста
			//if(i<1) letter = "Ъ";
			
			if(letters!=null)
				letter = letters.substring(i, i+1);
			
			gameLetters += letter;
			int x = i%5;
			int y = (int)(i/5);
			cards[i] = new Card(this, letter, x, y, cardsSize);
		}
	}

	public Card GetCard(int x, int y)
	{
		int n = y*5+x;
		return cards[n];
	}
	
	public Card FindCard(float mouseX, float mouseY)
	{
		for(int i=0; i<25; i++)
		{
			if(
					mouseX>=cards[i].getRect().left && 
					mouseX<=cards[i].getRect().right &&
					mouseY>=cards[i].getRect().top && 
					mouseY<=cards[i].getRect().bottom 
					)
				return cards[i];
		}
		
		return null;
	}

	public void CancelPlayerMove() {
		for(int i=0; i<this.cards.length; i++)
		{
			this.cards[i].Unselect();
			this.cards[i].setRect(this.cards[i].getCardRealFieldPosition(this.cards[i].getCellX(), this.cards[i].getCellY()));
			this.cards[i].SetInField(true);
			this.cards[i].SetInWord(false);
		}
		this.currentWord.Clear();
	}
	
}
