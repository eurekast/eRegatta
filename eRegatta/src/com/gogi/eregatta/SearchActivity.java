package com.gogi.eregatta;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.json.jackson.JacksonFactory;
import com.google.api.services.bookingendpoint.Bookingendpoint;
import com.google.api.services.bookingendpoint.model.Booking;
import com.google.api.services.regattaclassendpoint.Regattaclassendpoint;
import com.google.api.services.regattaclassendpoint.model.RegattaClass;
import com.google.api.services.regattaendpoint.Regattaendpoint;
import com.google.api.services.regattaendpoint.model.Regatta;

public class SearchActivity extends FragmentActivity implements SearchFragment.OnSearchExecutedListener, ListRegattasFragment.OnRegattaClickedListener{ 
	private static final String DEBUG_TAG= "Debug";  
	private static final String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		

		
		if (findViewById(R.id.fragment_container) != null) {

            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
    
            // Create an instance of ExampleFragment
            SearchFragment searchFragment = new SearchFragment();
            
            // In case this activity was started with special instructions from an Intent,
            // pass the Intent's extras to the fragment as arguments
            searchFragment.setArguments(getIntent().getExtras());
            
            // Add the fragment to the 'fragment_container' FrameLayout
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, searchFragment).commit();
        }
		MySQLiteHelper db = new MySQLiteHelper(this);
		if (db.isDatabaseEmpty()){
			new RegattaGetter().execute();
		}
		
		//setSpinners();
		
		//Button searchButton = (Button)findViewById(R.id.search_button);
		//searchButton.setOnClickListener(searchButtonListener);
	    //Button refreshRegattasButton = (Button)findViewById(R.id.refreshRegattas_button);
	    //refreshRegattasButton.setOnClickListener(refreshRegattasButtonListener);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.first_tab, menu);
		return true;
	}
	
	@Override
	public void onSearchExecuted(int[] ids) {
		ListRegattasFragment listFragment = new ListRegattasFragment();
		Bundle args = new Bundle();
        args.putIntArray(ListRegattasFragment.IDS, ids);
        listFragment.setArguments(args);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, listFragment);
        transaction.addToBackStack(null);
        transaction.commit();
	}
	
	@Override
	public void onRegattaClicked(int id){
		RegattaViewFragment regattaViewFragment = new RegattaViewFragment();
		Bundle args = new Bundle();
        args.putInt(RegattaViewFragment.ID, id);
        regattaViewFragment.setArguments(args);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, regattaViewFragment);
        transaction.addToBackStack(null);
        transaction.commit();
	}
	
	private String toSQLdate(String strDate){
		SimpleDateFormat longFormat = new SimpleDateFormat("d MMMMM yyyy");
		String formattedDate = null;
        try {
        	Date date = longFormat.parse(strDate);
        	SimpleDateFormat SQLformat = new SimpleDateFormat("yyyy-MM-dd");
            formattedDate = SQLformat.format(date);
        }
        catch(Exception e){
        	e.printStackTrace();
        }
        if (formattedDate != null){
        	return formattedDate;
        }
        else {
        	return "";
        }
	}
	
	protected class RegattaGetter extends AsyncTask<Void, Void, ArrayList<?>[]> {
		MySQLiteHelper db = new MySQLiteHelper(MyApplication.getAppContext());
		
		ProgressDialog progressDialog = new ProgressDialog(SearchActivity.this);
		
		protected void onPreExecute(){
			progressDialog.setMessage("Refreshing regattas.. This can take a minute.");
			progressDialog.show();
		}
	    protected ArrayList<?>[] doInBackground(Void... unused)
	    {   
	    	ArrayList<?>[] result = new ArrayList<?>[3];
	    	
	    	// downloadanje regat
	    	Regattaendpoint.Builder regattaEndpointBuilder = new Regattaendpoint.Builder(
	            AndroidHttp.newCompatibleTransport(),
	            new JacksonFactory(),
	            new HttpRequestInitializer() {
	            	public void initialize(HttpRequest httpRequest) { }
	            });
	    	Regattaendpoint regattaEndpoint = CloudEndpointUtils.updateBuilder(regattaEndpointBuilder).build();
	        
	        ArrayList<Regatta> regattas = null;
			
	        try	{
	        	regattas = (ArrayList<Regatta>) regattaEndpoint.regattaEndpoint().downloadRegattas().execute().getItems();
				Log.d("DEBUG", "Regatta no. 1 name: "+regattas.get(0).getName());
			}
			catch (IOException e) {
				e.printStackTrace();
				Log.d("DEBUG", "Regattas: "+e.getMessage());
			}
	        
	    	// downloadanje klas regate
	    	Regattaclassendpoint.Builder regattaClassEndpointBuilder = new Regattaclassendpoint.Builder(
	            AndroidHttp.newCompatibleTransport(),
	            new JacksonFactory(),
	            new HttpRequestInitializer() {
	            	public void initialize(HttpRequest httpRequest) { }
	            });
	    	Regattaclassendpoint regattaClassEndpoint = CloudEndpointUtils.updateBuilder(regattaClassEndpointBuilder).build();
	        
	        ArrayList<RegattaClass> regattaClasses = null;
			
	        try	{
	        	regattaClasses = (ArrayList<RegattaClass>) regattaClassEndpoint.regattaClassEndpoint().downloadRegattaClasses().execute().getItems();
				Log.d("DEBUG", "RegattaClass size: "+regattaClasses.size());
			}
			catch (IOException e) {
				e.printStackTrace();
			}
	        
	        // downloadanje bookingov
	        Bookingendpoint.Builder bookingEndpointBuilder = new Bookingendpoint.Builder(
	            AndroidHttp.newCompatibleTransport(),
	            new JacksonFactory(),
	            new HttpRequestInitializer() {
	            	public void initialize(HttpRequest httpRequest) { }
	            });
	    	Bookingendpoint bookingEndpoint = CloudEndpointUtils.updateBuilder(bookingEndpointBuilder).build();

	        ArrayList<Booking> bookings = null;
			
	        try	{
	        	bookings = (ArrayList<Booking>) bookingEndpoint.bookingEndpoint().downloadBookings().execute().getItems();
				Log.d("DEBUG", "Booking no. 1 price: "+bookings.get(0).getPrice());
			}
			catch (IOException e) {
				e.printStackTrace();
			}
	        
	        result[0] = regattas;
	        result[1] = regattaClasses;
	        result[2] = bookings;
	        
			return result;
			
	    }
	    /*private String JSONencode()
	    {	
			// TODO Auto-generated method stub
	    	JSONObject obj = new JSONObject();
	    	try {
				obj.put("Name", ((TextView) findViewById(R.id.search_name)).getText().toString());
				obj.put("Venue", ((TextView)  findViewById(R.id.search_venue)).getText().toString());
				obj.put("Country", ((Spinner)  findViewById(R.id.search_country)).getSelectedItem().toString());
				obj.put("Discipline", ((Spinner)  findViewById(R.id.search_discipline)).getSelectedItem().toString());
				obj.put("Type", ((Spinner)  findViewById(R.id.search_type)).getSelectedItem().toString());
				obj.put("Class", ((Spinner)  findViewById(R.id.search_class)).getSelectedItem().toString());
				obj.put("Grade", ((Spinner)  findViewById(R.id.search_grade)).getSelectedItem().toString());
				obj.put("Month", ((Spinner)  findViewById(R.id.search_month)).getSelectedItem().toString());
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	String jsonEncodedData = obj.toString();
			return jsonEncodedData;
		}*/
	    
		protected void onPostExecute(ArrayList<?>[] result)
		{
			@SuppressWarnings("unchecked")
			ArrayList<Regatta> regattas = (ArrayList<Regatta>) result[0];
			@SuppressWarnings("unchecked")
			ArrayList<RegattaClass> regattaClasses = (ArrayList<RegattaClass>) result[1];
			@SuppressWarnings("unchecked")
			ArrayList<Booking> bookings = (ArrayList<Booking>) result[2];
			
	    	String debug_string = "";
	    	int count = 0;
	    	
	    	// pisanje v DB
	    	//Log.d("DEBUG", "DEBUG: num of regattas: "+ regattas.size());
	    	if (regattas!=null){
				db.refreshRegattas(regattas);
				if (regattaClasses != null){
					db.refreshRegattaClasses(regattaClasses);
				}
				if (bookings!=null){
					db.refreshBookings(bookings);
				}
				TextView refreshTime = (TextView)findViewById(R.id.refreshTime_textview);
				refreshTime.setText("Last refresh: "+db.getRefresh());
				
				progressDialog.dismiss();
				Context context = getApplicationContext();
	    		CharSequence text = "Regattas are now up to date.";
	    		int duration = Toast.LENGTH_SHORT;

	    		Toast toast = Toast.makeText(context, text, duration);
	    		toast.show();
	    	}
	    	else {
	    		progressDialog.dismiss();
	    		Context context = getApplicationContext();
	    		CharSequence text = "An error occured. Regattas not refreshed.";
	    		int duration = Toast.LENGTH_SHORT;

	    		Toast toast = Toast.makeText(context, text, duration);
	    		toast.show();
	    		
	    		// prikaži napako
	    	}
			
			db.close();
		}
	    
	}

	protected class WeatherGetter extends AsyncTask<Regatta, Void, ArrayList<?>[]> {
		MySQLiteHelper db = new MySQLiteHelper(MyApplication.getAppContext());
	   
		private int regattaId = 0;
		ProgressDialog progressDialog = new ProgressDialog(SearchActivity.this);
	    
		private static final String API_KEY = "fd0ad8950974365bc8f83a98a48e3a52";
		private static final String FORECAST_URL = "https://api.forecast.io/forecast/";
		
		// view v katerega gre vreme
		/*public View view; 
		
		public WeatherGetter(){

		   this.view = view;

		}*/
		
		protected void onPreExecute(){
			progressDialog.setMessage("Refreshing weather data..");
			progressDialog.show();
		}
	    protected ArrayList<?>[] doInBackground(Regatta... regattas)
	    {   
	    	
	    	Regatta regatta = regattas[0];
	    	String url = FORECAST_URL+API_KEY+"/"+regatta.getLatitude().toString()+","+regatta.getLongitude().toString()+"?units=si";
	    	String json = null;
			try {
				json = runURL(url);
			} catch (Exception e) {
				Log.d("[GET REQUEST]", "Network exception: "+ e.getMessage());
			}
			
			regattaId = regatta.getId();
			
			ArrayList<?>[] weather = parseWeather(json, regatta.getId());
			
			return weather;
	    }
	    
		protected void onPostExecute(ArrayList<?>[] weather)
		{	
			// Daily Weather se zapiše v bazo
			@SuppressWarnings("unchecked")
			ArrayList<DailyWeather> dailyArray = (ArrayList<DailyWeather>) weather[0];
			db.addDailyWeather(dailyArray);
			
			// Hourly Weather se zapiše v bazo
			@SuppressWarnings("unchecked")
			ArrayList<HourlyWeather> hourlyArray = (ArrayList<HourlyWeather>) weather[1];
			db.addHourlyWeather(hourlyArray);
			
			GridView gridview = (GridView) findViewById(R.id.daily_weather_gridview);
			WeatherImageAdapter adapter = (WeatherImageAdapter)gridview.getAdapter();
			adapter = new WeatherImageAdapter(SearchActivity.this, db.getDailyWeather(regattaId));
			gridview.setAdapter(adapter);
			gridview.setVisibility(View.VISIBLE);
				
			db.close();
			progressDialog.dismiss();
		}
		
		// parsa JSON iz forecast.io in vrne Weather objekt
		private ArrayList<?>[] parseWeather(String json, int regattaId){
			
			ArrayList<?>[] weatherObjects = new ArrayList<?>[2];
			try {
				
				JSONObject root = new JSONObject(json);
				JSONArray hourlyJSONarray = (JSONArray) ((JSONObject) root.get("hourly")).get("data");
				JSONArray dailyJSONarray = (JSONArray) ((JSONObject) root.get("daily")).get("data");
				
				// Daily del
				ArrayList<DailyWeather> dailyArray = new ArrayList<DailyWeather>();
				for (int i=0; i<dailyJSONarray.length(); i++)
				{
					JSONObject dailyJSON = (JSONObject) dailyJSONarray.get(i);
					DailyWeather daily = new DailyWeather();
					
					daily.regattaId = regattaId;
					daily.time = Long.parseLong(dailyJSON.get("time").toString());
					Log.d("DEBUG","Parsed: "+daily.time+", raw: "+dailyJSON.get("time")); 
					daily.icon = dailyJSON.get("icon").toString();
					daily.temperatureMin = Float.parseFloat(dailyJSON.get("temperatureMin").toString());
					daily.temperatureMax = Float.parseFloat(dailyJSON.get("temperatureMax").toString());
					if (dailyJSON.isNull("windBearing")){
						daily.windDir = 0;
					}
					else{
						daily.windDir = Integer.parseInt(dailyJSON.get("windBearing").toString());
					}
					daily.windSpeed = Float.parseFloat(dailyJSON.get("windSpeed").toString())*1.94f;
					daily.mm = Float.parseFloat(dailyJSON.get("precipIntensity").toString());
					
					dailyArray.add(daily);
				}
				
				// zapiše v bazo, da tudi naprej za prikaz na zaslon èe je treba
				db.addDailyWeather(dailyArray);
				weatherObjects[0] = dailyArray;
				Log.d("DEBUG", "ParseWeather: size of dailyArray: "+dailyArray.size());
				
				
				// Hourly del
				ArrayList<HourlyWeather> hourlyArray = new ArrayList<HourlyWeather>();
				for (int i=0; i<hourlyJSONarray.length(); i++)
				{
					JSONObject hourlyJSON = (JSONObject) hourlyJSONarray.get(i);
					HourlyWeather hourly = new HourlyWeather();
					
					hourly.regattaId = regattaId;
					hourly.time = Integer.parseInt(hourlyJSON.get("time").toString())*1000;
					hourly.icon = hourlyJSON.get("icon").toString();
					hourly.temperature = Float.parseFloat(hourlyJSON.get("temperature").toString());
					hourly.windDir = Integer.parseInt(hourlyJSON.get("windBearing").toString());
					hourly.windSpeed = Float.parseFloat(hourlyJSON.get("windSpeed").toString())*1.94f;
					hourly.mm = Float.parseFloat(hourlyJSON.get("precipIntensity").toString());
					
					hourlyArray.add(hourly);
				}
				
				// zapiše v bazo, da tudi naprej za prikaz na zaslon èe je treba
				db.addHourlyWeather(hourlyArray);
				weatherObjects[1] = hourlyArray;

				Log.d("DEBUG", "ParseWeather: size of hourlyArray: "+hourlyArray.size());
				
			}
			catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Log.d("DEBUG", "Error parsing JSON: "+e.getMessage());
			}
			
			return weatherObjects;
		}
		
		// vrne teskt, ki ga dobi iz URLja;
		private String runURL(String url) {   //src="http://www.example.com/trip/details/"
		    HttpClient httpclient = new DefaultHttpClient();   
		    HttpGet httpget = new HttpGet(url); 
		    String responseBody="";
	        
		    BasicHttpParams params=new BasicHttpParams();
	        params.setParameter("format", "json");

	        try {
	                //httpget.setParams(params);
	                HttpResponse response = httpclient.execute(httpget);
	                responseBody = EntityUtils.toString(response.getEntity());
	                Log.d("runURL", "response " + responseBody); //prints the complete HTML code of the web-page
	            } catch (Exception e) {
	                e.printStackTrace();
	        } 
	        return responseBody;
		}
	}
}
