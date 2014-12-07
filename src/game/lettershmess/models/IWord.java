package game.lettershmess.models;

public interface IWord {
	void Set(String word);
	int GetLength();
	void Clear();
	void RemoveAt(int position);
	void Append(String letter);
	String GetWord();
}
