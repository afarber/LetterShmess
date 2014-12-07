package game.lettershmess;

import java.util.HashMap;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

public class SoundManager {
	
	static private SoundManager _instance;
	private static SoundPool mSoundPool; 
	private static HashMap<Integer, Integer> mSoundPoolMap; 
	private static AudioManager  mAudioManager;
	private static Context mContext;

	public static final int SOUND_WELCOME = 1;
	public static final int SOUND_ARCADE_BEAT = 2;
	public static final int SOUND_BELL_105 = 3;
	public static final int SOUND_BELL_106 = 4;
	public static final int SOUND_BELL_107 = 5;
	public static final int SOUND_BEEP_SIN_NEO = 6;
	public static final int SOUND_IDG_BEEP_INTERMED = 7;
	public static final int SOUND_INTERMED = 8;
	public static final int SOUND_PS2TV = 9;
	public static final int SOUND_ENERGY = 10;
	public static final int SOUND_BUZZ = 11;

	private SoundManager()
	{   
	}
	
	/**
	 * Requests the instance of the Sound Manager and creates it
	 * if it does not exist.
	 * 
	 * @return Returns the single instance of the SoundManager
	 */
	static synchronized public SoundManager getInstance() 
	{
	    if (_instance == null) 
	      _instance = new SoundManager();
	    return _instance;
	 }
	
	/**
	 * Initialises the storage for the sounds
	 * 
	 * @param theContext The Application context
	 */
	public static  void initSounds(Context theContext) 
	{ 
		 mContext = theContext;
	     mSoundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 0);
	     mSoundPoolMap = new HashMap<Integer, Integer>(); 
	     mAudioManager = (AudioManager)mContext.getSystemService(Context.AUDIO_SERVICE); 	    
	} 
	
	/**
	 * Add a new Sound to the SoundPool
	 * 
	 * @param Index - The Sound Index for Retrieval
	 * @param SoundID - The Android ID for the Sound asset.
	 */
	public static void addSound(int Index,int SoundID)
	{
		mSoundPoolMap.put(Index, mSoundPool.load(mContext, SoundID, 1));
	}
	
	/**
	 * Loads the various sound assets
	 * Currently hardcoded but could easily be changed to be flexible.
	 */
	public static void loadSounds()
	{
		mSoundPoolMap.put(SOUND_WELCOME, mSoundPool.load(mContext, R.raw.welcome, 1));
		mSoundPoolMap.put(SOUND_ARCADE_BEAT, mSoundPool.load(mContext, R.raw.arcadebeat, 1));
		mSoundPoolMap.put(SOUND_BELL_105, mSoundPool.load(mContext, R.raw.bell_105, 1));
		mSoundPoolMap.put(SOUND_BELL_106, mSoundPool.load(mContext, R.raw.bell_106, 1));
		mSoundPoolMap.put(SOUND_BELL_107, mSoundPool.load(mContext, R.raw.bell_107, 1));
		mSoundPoolMap.put(SOUND_BEEP_SIN_NEO, mSoundPool.load(mContext, R.raw.beep_signeo, 1));
		mSoundPoolMap.put(SOUND_IDG_BEEP_INTERMED, mSoundPool.load(mContext, R.raw.idg_beep_intermed, 1));
		mSoundPoolMap.put(SOUND_INTERMED, mSoundPool.load(mContext, R.raw.intermed, 1));
		mSoundPoolMap.put(SOUND_PS2TV, mSoundPool.load(mContext, R.raw.ps2_tv, 1));
		mSoundPoolMap.put(SOUND_ENERGY, mSoundPool.load(mContext, R.raw.energy, 1));
		mSoundPoolMap.put(SOUND_BUZZ, mSoundPool.load(mContext, R.raw.buzz, 1));
	}
	
	/**
	 * Plays a Sound
	 * 
	 * @param index - The Index of the Sound to be played
	 * @param speed - The Speed to play not, not currently used but included for compatibility
	 */
	public static void playSound(int index,float speed) 
	{ 		
		     float streamVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC); 
		     streamVolume = streamVolume / mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		     mSoundPool.play(mSoundPoolMap.get(index), streamVolume, streamVolume, 1, 0, speed); 
	}
	
	/**
	 * Stop a Sound
	 * @param index - index of the sound to be stopped
	 */
	public static void stopSound(int index)
	{
		mSoundPool.stop(mSoundPoolMap.get(index));
	}
	
	public static void cleanup()
	{
		mSoundPool.release();
		mSoundPool = null;
	    mSoundPoolMap.clear();
	    mAudioManager.unloadSoundEffects();
	    _instance = null;
	    
	}

	
}