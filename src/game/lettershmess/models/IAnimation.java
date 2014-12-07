package game.lettershmess.models;

public interface IAnimation {
	public int Step();
	public int Steps();
	public int Type();
	public int State();
	public void Started();
	public void Update();
	public void Finished();
}
