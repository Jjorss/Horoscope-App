package com.jackson.horoscopeapp;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity {
	
	TextView horoscope;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		horoscope = (TextView) findViewById(R.id.horoscope);
		new GetHoroscopeTask().execute("Aries");		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public class GetHoroscopeTask extends AsyncTask<String, Void, Void>{

		String horoscopeText = "";
		
		
		@Override
		protected Void doInBackground(String... params) {
			try {
				horoscopeText = parseHoroscope(getHoroscope(params[0]));
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
		
		 @Override
	     protected void onPostExecute(Void result) {
			 horoscope.setText(horoscopeText);
	     }

		
		public String getHoroscope(String sign) throws Exception {

	        //Establish a connection by reading a URL
	        HttpURLConnection aliveConnect = (HttpURLConnection) 
	        		(new URL("http://widgets.fabulously40.com/horoscope.json?sign=" + sign)).openConnection();
	        		// old URL = http://horoscope-api.herokuapp.com/horoscope/today/
	        
	        
	        //A string builder to contain all the information read using the url
	        StringBuilder jsonData = new StringBuilder();
	        InputStream rawInput = aliveConnect.getInputStream();
	        BufferedReader readData = new BufferedReader(new InputStreamReader(rawInput));
	        String currentLine = "";

	        //Iterate throughout the url contents and append to the string builder defined above
	        while ((currentLine = readData.readLine()) != null) {
	            jsonData.append(currentLine);
	        }

	        //Stop reading the file
	        rawInput.close();

	        //Kill the connection
	        aliveConnect.disconnect();

	        //Return the data converted into string
	        return jsonData.toString();
	    }
	    

	    public String parseHoroscope(String sign) {
			String hor = "";
			String newHor = "";
			// load in horoscope
	    	try {
				hor = getHoroscope(sign);
			} catch (Exception e) {
				e.printStackTrace();
			}
	    	
			int startPoint = sign.length() + 37;
			// eliminates backslashes
			//starts at 37 because it eliminates the beginning and - 3 because
			// it eliminates the end
			
	    	for (int i = startPoint; i < hor.length() - 3; i++ ) {
				if (hor.charAt(i) == '\\') {
					if (hor.charAt(i + 5) == '9') {
						i += 6;
						newHor += "'";
					} else if (hor.charAt(i + 5) == '4') {
						i += 6;
						newHor += sign;
					}
				}
				
	    		newHor += hor.charAt(i);
			}
	    	
	    	return newHor;
	    }
	}


	
}
