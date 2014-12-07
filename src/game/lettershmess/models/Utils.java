package game.lettershmess.models;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import game.lettershmess.R;

public class Utils {
	public static final float AVG_WORD_LENGTH = 5.28f;//eng=4.24
	private static Map<String, Integer> LETTER_STATS = new HashMap<String, Integer>();

	private static LetterStats[] Letters = new LetterStats[33];

	private static Resources _resources;
	
	private static int totalDictionaryWords;
	private static int wordIndex;
	private static double percents;
	private long seed;
	private static Random rnd;
	
	public Utils(Resources resources)
	{
		Log.i("Utils", "Utils()");
		seed = System.nanoTime();
		rnd = new Random(seed);
		this._resources = resources;
		InitLetters();
	}

	public static int getTotalDictionaryWords()
	{
		return totalDictionaryWords;
	}

	public static double getPercents()
	{
		return percents;
	}
	
	public static ArrayList<String> ReadDictionary(Context context, String filename)
	{
		ArrayList<String> wordsDictionary = new ArrayList<String>();
		AssetManager am = context.getAssets();
		try {
			InputStream is = am.open(filename);
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			totalDictionaryWords = byteArrayOutputStream.size();
			Log.i("UTILS", "totalDictionaryWords="+totalDictionaryWords);
			wordIndex = 0;
			percents = 0.0;
			int i = is.read();
			while (i != -1)
			{
				byteArrayOutputStream.write(i);
				i = is.read();
				wordIndex++;
				percents = 100.0 * wordIndex / (804578 + 76182);//TODO: размер словаря
			}
			is.close();
			Log.i("UTILS", "words="+wordIndex);
			
			//TODO:java.lang.OutOfMemoryError - sometimes!!! GC!
			String content = byteArrayOutputStream.toString("cp1251");
			Pattern p = Pattern.compile("(\\w+):"); 
		   	Matcher m = p.matcher(content);
		   	wordsDictionary = new ArrayList<String>();
			Log.i("UTILS", "parsing...");
			//wordIndex = 0;
			//percents = 0.0;
		   	while (m.find()) {   
		   	     String w = m.group(1);
		   	     if(w.length()>1 
		   	    		 //TODO: убрать этот костыль, когда будет нормальный словарь
		   	    		 && !w.toUpperCase().endsWith("ЕЕ")
		   	    		 && !w.toUpperCase().endsWith("ЫЙ")
		   	    		 && !w.toUpperCase().endsWith("ИЙ")
		   	    		 && !w.toUpperCase().endsWith("НАЯ")
		   	    		 && !w.toUpperCase().endsWith("КАЯ")
		   	    		 && !w.toUpperCase().endsWith("ШАЯ")
		   	    		 && !w.toUpperCase().endsWith("ОЕ")
		   	    		 && !w.toUpperCase().endsWith("ЯЯ")
		   	    		 )
		   	    	 wordsDictionary.add(w);
				wordIndex++;
				percents = 100.0 * wordIndex / (804578 + 76182);
		   	} 

		   	byteArrayOutputStream.flush();
		   	
			Log.i("UTILS", "parsed "+wordIndex);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return wordsDictionary;
	}
	
	public void InitLetters()
	{
		Log.i("InitLetters", "InitLetters()");
		String ru_abc33_unsorted = _resources.getString(R.string.ru_abc33_unsorted);
		
		float[] pp = new float[] {
			0.0986766868158798f,
			0.0918782150991421f,
			0.0853639282865572f,
			0.0767312969951908f,
			0.0710489717860678f,
			0.0638660953680606f,
			0.0610160090829339f,
			0.0496321384296244f,
			0.0491694799141679f,
			0.0432084612966245f,
			0.0356288243119499f,
			0.0299231602459641f,
			0.0261326553166602f,
			0.0243671394386868f,
			0.0197940963675129f,
			0.0192037605762776f,
			0.0171581784159506f,
			0.0165403851460532f,
			0.0158251178269053f,
			0.0146554292358996f,
			0.0134006224610413f,
			0.0122556456008314f,
			0.0122213237525038f,
			0.0112191257813369f,
			0.00787755062815847f,
			0.00681357333000183f,
			0.00658567625710634f,
			0.00623010190843205f,
			0.00390857208755092f,
			0.00385640287809291f,
			0.00295030608224339f,
			0.00254805401984352f,
			0.000313015256748019f
		};
		
		for(int i=0; i<ru_abc33_unsorted.length(); i++)
		{
			Letters[i] = new LetterStats(ru_abc33_unsorted.substring(i, i+1), pp[i]);
		}
	}
	
	public float getLetterStats(String letter)
	{
		return LETTER_STATS.get(letter.toUpperCase());
	}
	
	public static String getRandomLetter()
	{
		double k = rnd.nextDouble();
		//Log.i("getRandomLetter", "" + k);
		
		double x = 1.0;
		int	i = 32;
		while(x>k && i>=0)
		{
			x = x-Letters[i].P;
			i--;
		}
		
		return Letters[i+1].Letter;
	}

	
	public void sendRegistrationIdToServer(String deviceId) {
		Log.d("C2DM", "Sending registration ID to my application server");
		try {
			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost("http://samodum.ru/lsgame/register");
		  
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		    // Get the deviceID
			nameValuePairs.add(new BasicNameValuePair("deviceid", deviceId));
		    //nameValuePairs.add(new BasicNameValuePair("registrationid", registrationId));

		    post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		    HttpResponse response = client.execute(post);
		    BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		    /*String line = "";
		    while ((line = rd.readLine()) != null) {
		      Log.e("HttpResponse", line);
		    }*/
		} catch (Exception e) {
			e.printStackTrace();
		}
	} 

	public static String GetPage(String url, String deviceId)
	{
		HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(url);
        //post.addHeader("Cookie", "cookie data");

    	String content = "";
        Log.i("CONTENT", content);

        try{
	        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
	        pairs.add(new BasicNameValuePair("deviceId", deviceId));
	        
	        post.setEntity(new UrlEncodedFormEntity(pairs));
	        HttpResponse response = client.execute(post);
	        HttpEntity resEntity = response.getEntity();
	        
        	if (resEntity != null) {
        		Hashtable<String, String> cookies = new Hashtable<String, String>();
        		Header[] headers = response.getAllHeaders();
	            for (int i=0; i < headers.length; i++) {
	                Header h = headers[i];
	                String hdrName = h.getName();
	                //content += ("Header name: -"+hdrName+"-\n ");
	                if(hdrName.equals("Set-Cookie"))
	                {
	                	String val = cookies.get(hdrName);
	                	if(val == null || val.equals(""))
	                	{
	                		cookies.put(h.getName(), h.getValue());
		                	//_cookie = h.getValue();
			                //content += ("Header name: "+hdrName+";\n ");
			                //content += ("Header value: "+h.getValue()+";\n ");
	                	}
	                }
	            }
	            content += EntityUtils.toString(resEntity);
	        	//Toast.makeText(this, "GetPage:" + content, Toast.LENGTH_LONG).show();
	        	return content;
	        }
        } catch (Exception e) {
        	//Toast.makeText(this, "GetPage Error in "+k+":" + e.getMessage(), Toast.LENGTH_LONG).show();
        }
		return content;  
	}
	

}
