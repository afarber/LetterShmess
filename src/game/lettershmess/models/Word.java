package game.lettershmess.models;

import java.util.LinkedList;


import android.util.Log;

public class Word implements IWord {
	private LinkedList<Card> lettersChain;
	//public ArrayList<Card> LettersChain2;
	private String _word;
	
	public Word()
	{
		lettersChain = new LinkedList<Card>();
		_word = "";
	}

	@Override
	public void Set(String word) {
		_word = word;
	}

	@Override
	public int GetLength() {
		//return LettersChain.size();
		return _word.length();
	}

	@Override
	public void Clear() {
		lettersChain.clear();
		_word = "";
	}

	@Override
	public void RemoveAt(int position) {
		Log.i("RemoveAt", "word:"+_word + " : " + position);
		//_word = _word.substring(0, position-1) + _word.substring(position, _word.length()-position);
	}

	@Override
	public void Append(String letter) {
		_word += letter;
	}

	@Override
	public String GetWord() {
		return _word;
	}
}
