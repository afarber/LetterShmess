package game.lettershmess.models;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;

public class Card implements IAnimation {
	private int x;//cell X position
	private int y;//cell Y position
	private int realX;//screen real X position
	private int realY;//screen real Y position
	private int width;
	private LettersField _lettersField;
	private int _cardSize;
	
	private int type;//0-free, 1-player 1, 2-player 2, 3-player 1 lock, 4-player 2 lock
	
	private int letterPosition;
	
	private int X1;
	private int Y1;
	private int X2;
	private int Y2;
	private float angle;
	
	private boolean moving;
	private int step;
	private int steps;
	
	private String letter;
	private Rect rect;
	private Rect _original;
	private boolean selected;
	private boolean inField;
	private boolean inWord;
	
	private boolean touched;
	
	private boolean lastUsed;//hot card in field
	
	public Card(LettersField lettersField, String letter, int x, int y, int size)
	{
		this._lettersField = lettersField;
		this._cardSize = size;
		this.inField = true;
		this.inWord = false;
		this.letter = letter;
		this.x = x;
		this.y = y;
		this.touched = false;
		this.angle = 0;

		type = 0;
		rect = getCardRealFieldPosition(x, y); 
		
		this.width = rect.right - rect.left; 
		
		setOriginal(rect.left, rect.top, rect.right, rect.bottom);
	}
	
	public void setCellX(int x)
	{
		this.x = x;
	}

	public void setCellY(int y)
	{
		this.y = y;
	}
	
	public int getCellX()
	{
		return this.x;
	}

	public int getCellY()
	{
		return this.y;
	}
	
	public void SetAngle(float a)
	{
		angle = a;
	}
	
	public float Angle()
	{
		return angle;
	}
	
	public void SetLetterPosition(int position)
	{
		this.letterPosition = position;
	}
	
	public int GetLetterPosition()
	{
		return this.letterPosition;
	}
	
	public void ChangeWordPosition(int newPosition)
	{
		SetMoving(true);
		//setXY1(x1, y1)
		//setXY2(x2, y2, newPosition)
		SetInWord(true);
	}
	
	public void setRealX(int x)
	{
		this.realX = x;
		this.rect.right = x + this.width;
		this.rect.left = x;
	}

	public void setRealY(int y)
	{
		this.realY = y;
		this.rect.bottom = this.rect.bottom - this.rect.top + y;
		this.rect.top = y;
	}
	
	public void setXY1(int x1, int y1)
	{
		this.X1 = x1;
		this.Y1 = y1;
	}

	public void setXY2(int x2, int y2, int newWidth, int steps)
	{
		step = 0;
		this.X2 = x2;
		this.Y2 = y2;
		this.steps = steps;
		this.width = newWidth;
	}

	public void updateXY2(int x2, int y2, int newWidth)
	{
		this.X2 = x2;
		this.Y2 = y2;
		this.width = newWidth;
	}

	public void Move()
	{
		step++;

		double dx = X2-X1;
		double dy = Y2-Y1;
		double d = Math.sqrt(dx*dx+dy*dy);
		
		double speed = d/steps;
		
		this.realX = (int)(this.X1 + speed * step * dx/d);
		this.realY = (int)(this.Y1 + speed * step * dy/d);
		
		setRealX(this.realX);
		setRealY(this.realY);
		angle = 10;
		
		//moving = realY > Y2;// Math.abs(realX-X2)<1 && Math.abs(realY-Y2)<1;
		moving = step < steps;//Math.abs(realY-Y2)<10;
		if(realY <= Y2) 
		{
			step = 0;
			this.inWord = true;
			this.inField = false;
			this.angle = 0;
		}
		else
		{
			this.inWord = false;
		}
	}

	public void Move2()
	{
		step++;

		double dx = X2-X1;
		double dy = Y2-Y1;
		double d = Math.sqrt(dx*dx+dy*dy);
		
		double speed = d/steps;
		
		this.realX = (int)(this.X1 + speed * step * dx/d);
		this.realY = (int)(this.Y1 + speed * step * dy/d);
		
		setRealX(this.realX);
		setRealY(this.realY);
		
		moving = realY > Y2;// Math.abs(realX-X2)<1 && Math.abs(realY-Y2)<1;
		if(step>=steps) 
		{
			step = 0;
			this.inWord = false;
			this.inField = true;
		}
		else
		{
			this.inWord = true;
		}
	}
	public boolean IsInField()
	{
		return inField;
	}
	
	public void SetInField(boolean isInField)
	{
		inField = isInField;
	}
	
	public boolean IsInWord()
	{
		return inWord;
	}
	
	public void SetInWord(boolean isInWord)
	{
		inWord = isInWord;
	}
	
	public void setRect(Rect rect)
	{
		this.realX = rect.left;
		this.realY = rect.top;
		this.rect = rect;
		this.width = rect.right - rect.left;
	}
	
	public void setWidth(int w)
	{
		this.width = w;
	}
	
	public Rect getRect()
	{
		return this.rect;
	}
	
	public int getWidth()
	{
		return this.width;
	}
	
	public int getFontSize()
	{
		return getWidth()/2;
	}
	
	public int getHeight()
	{
		return this.rect.bottom-this.rect.top;
	}
	
	public Rect getOriginal()
	{
		return this._original;
	}
	
	public void setOriginal(int left, int top, int right, int bottom)
	{
		_original = new Rect(left, top, right, bottom);
	}
	
	public void Select()
	{
		selected = true;
	}
	
	public void Unselect()
	{
		selected = false;
	}
	
	public boolean IsSelected()
	{
		return selected;
	}
	
	public boolean IsMoving()
	{
		return moving;
	}
	
	public void SetMoving(boolean moving)
	{
		this.moving = moving;
	}

	public Rect getCardRealFieldPosition(int x, int y)
	{
		int x0 = (_lettersField.Width() - _cardSize*5)/2;
		int y0 = _lettersField.Height() - _cardSize*5; 

		return new Rect(
				x*_cardSize+x0, 
				y*_cardSize+y0, 
				(x+1)*_cardSize+x0, 
				(y+1)*_cardSize+y0);
	}
	
	public String GetLetter()
	{
		return this.letter;
	}
	
	public int Type()
	{
		return type;
	}
	
	public void SetType(int type)
	{
		this.type = type;
	}
	
	public void SetHot(boolean isHot)
	{
		lastUsed = isHot;
	}
	
	public boolean IsHot()
	{
		return lastUsed;
	}
	
	public void Draw(Canvas canvas)
	{
		//canvas.drawBitmap(bitmap, x, y, null);
	}
	
	public void SetTouched(boolean touched)
	{
		this.touched = touched;
	}
	
	public boolean Touched()
	{
		return this.touched;
	}

	@Override
	public int Step() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int Steps() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int State() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void Started() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Update() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Finished() {
		// TODO Auto-generated method stub
		
	}

	public void MoveToWord() {
		_lettersField.AddLetter(this.GetLetter());
		this.Select();
		
		this.SetLetterPosition(_lettersField.GetCurrentWord().length()-1);
		this.setXY1(this.getRect().left, this.getRect().top);
		this.SetHot(false);
		
		_lettersField.ResizeWordIfNeed();
		
		int x2 = (_lettersField.GetCurrentWord().length()-1) * _lettersField.getWordCardSize();
		int y2 = (int)((this.getRect().bottom-this.getRect().top)*1.5);
		
		int steps = (int)(Math.random()*20+10);
		
		this.SetAngle(-10);
		this.setXY2(x2, y2, _lettersField.getWordCardSize(), steps);
		
		this.SetMoving(true);
		this.SetInField(false);
	}

	public void ReturnToField() {
		this.Unselect();
		
		Log.i("SELECT", _lettersField.GetCurrentWord() + " - card.IsInWord, but not in IsInField");
		Log.i("SELECT", "IsInWord");
		Log.i("card.LetterPosition", this.GetLetterPosition() + "->");
		
		this.setXY1(this.getRect().left, this.getRect().top);
		
		int steps = (int)(Math.random()*30+20);

		Log.i("InWord -> InField", this.getOriginal().left + "," + this.getOriginal().top);

		//card.setRect(card.getCardRealFieldPosition(card.getCellX(), card.getCellY()));
		Rect rectTo = this.getCardRealFieldPosition(this.getCellX(), this.getCellY());
		this.setXY2(rectTo.left, rectTo.top, this.getWidth(), steps);
		
		this.SetMoving(true);
		this.SetInField(true);
		this.SetInWord(false);
		
		_lettersField.RemoveLetter(this.GetLetterPosition());
		//card.SetLetterPosition(-1);	
	}

	public void PlayerMove(int currentPlayer) {
		this.Unselect();
		
		if((currentPlayer == 1 && this.Type() < 3)
				|| (currentPlayer == 2 && this.Type() < 3))
			this.SetType(currentPlayer);
		
		this.setRect(this.getCardRealFieldPosition(this.getCellX(), this.getCellY()));
		this.SetInField(true);
		this.SetInWord(false);
		this.SetHot(true);
	}
}
