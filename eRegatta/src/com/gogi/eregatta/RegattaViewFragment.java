package com.gogi.eregatta;

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

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gogi.eregatta.ListRegattasFragment.OnRegattaClickedListener;
import com.google.api.services.bookingendpoint.model.Booking;
import com.google.api.services.regattaendpoint.model.Regatta;

public class RegattaViewFragment extends Fragment {
	MySQLiteHelper db = new MySQLiteHelper(MyApplication.getAppContext());
	Activity activity;
	OnRegattaClickedListener mCallback;
	public int id;
	public static final String ID = "id";
	
	public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity=activity;
       /* try {
            mCallback = (OnRegattaClickedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnRegattaClickedListener");
        }*/
	}
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, 
        Bundle savedInstanceState) {
		
        return inflater.inflate(R.layout.regatta_view, container, false);
    }
	private OnClickListener refreshWeatherButtonListener = new OnClickListener() {
	    public void onClick(View v) {
	    	Regatta regatta = (Regatta)v.getTag();
	    	Regatta[] regattas = {regatta};
	    	((SearchActivity)activity).new WeatherGetter().execute(regattas);
	    	
	    }
	};
	public void onActivityCreated(Bundle savedInstanceState){
    	super.onActivityCreated(savedInstanceState);        
    	
    	// inicializacije vseh View elementov
    	Bundle arguments=getArguments(); 
    	int id = arguments.getInt(ID);
    	this.id = id;
    	TextView regatta_name_textview = (TextView) getView().findViewById(R.id.regatta_name_textview);
    	TextView venue_textview = (TextView) getView().findViewById(R.id.venue_textview);
    	TextView date_textview = (TextView) getView().findViewById(R.id.date_textview);
    	TextView discipline_textview = (TextView) getView().findViewById(R.id.discipline_textview);
    	TextView class_textview = (TextView) getView().findViewById(R.id.class_textview);
    	TextView grade_textview = (TextView) getView().findViewById(R.id.grade_textview);
    	TextView type_textview = (TextView) getView().findViewById(R.id.type_textview);
    	FrameLayout web_icon_container = (FrameLayout) getView().findViewById(R.id.web_icon_container);
    	FrameLayout phone_icon_container = (FrameLayout) getView().findViewById(R.id.phone_icon_container);
    	FrameLayout mail_icon_container = (FrameLayout) getView().findViewById(R.id.mail_icon_container);
    	FrameLayout map_icon_container = (FrameLayout) getView().findViewById(R.id.map_icon_container);
    	
    	// Regatta ki jo prikazujemo
		Regatta regatta = db.getRegatta(id);
		
		// podatki iz RegattaClass tabele
		String[] classes = db.getRegattaClasses(id);
		String[] disciplines = db.getDisciplines(id);
		String[] grades = db.getGrades(id);
		String[] types = db.getTypes(id);
		
		// prikazovanje osnovnih podatkov
		regatta_name_textview.setText(regatta.getName());
		boolean setComma = false;
		if (regatta.getVenue()!=null && regatta.getVenue()!=""){
			venue_textview.setText(regatta.getVenue()+", ");
		}
		venue_textview.setText(venue_textview.getText()+countryTagToName(regatta.getCountry()));
		date_textview.setText(sqlToReadableDate(regatta.getStartDate())+" - "+sqlToReadableDate(regatta.getEndDate()));
		
		// vpisovanje vseh disciplin
		int validResults = 0;
		for (int i = 0; i<disciplines.length; i++){
			if (disciplines[i] != null && disciplines[i] !=""){
				validResults++;
				if (i > 0){
					discipline_textview.setText(discipline_textview.getText()+", ");
				}
				discipline_textview.setText(discipline_textview.getText()+disciplines[i]);
			}
		}
		if (validResults == 0){
			((LinearLayout) getView().findViewById(R.id.discipline_layout)).setVisibility(View.GONE);
		}
		
		// vpisovanje vseh classov
		validResults = 0;
		for (int i = 0; i<classes.length; i++){
			if (classes[i] != null && classes[i] != ""){
				validResults++;
				if (validResults > 1){
					class_textview.setText(class_textview.getText()+", ");
				}
				class_textview.setText(class_textview.getText()+classes[i]);
			}
		}
		if (validResults == 0){
			((LinearLayout) getView().findViewById(R.id.class_layout)).setVisibility(View.GONE);
		}
		
		// vpisovanje vseh gradov
		validResults = 0;
		for (int i = 0; i<grades.length; i++){
			if (grades[i]!=null && grades[i]!=""){
				validResults++;
				if (validResults > 1){
					grade_textview.setText(grade_textview.getText()+", ");
				}
				grade_textview.setText(grade_textview.getText()+grades[i]);
			}
		}
		if (validResults == 0){
			((LinearLayout) getView().findViewById(R.id.grade_layout)).setVisibility(View.GONE);
		}
		
		// vpisovanje vseh typov
		validResults = 0;
		for (int i = 0; i<types.length; i++){
			if (types[i]!=null){
				validResults++;
				if (validResults > 1){
					type_textview.setText(type_textview.getText()+", ");
				}
				type_textview.setText(type_textview.getText()+types[i]);
			}
		}
		if (validResults == 0){
			((LinearLayout) getView().findViewById(R.id.type_layout)).setVisibility(View.GONE);
		}
		
		if (regatta.getTelephone()!= null && !regatta.getTelephone().isEmpty()){
			ImageView phone_icon_imageview = new ImageView(getActivity());
			phone_icon_imageview.setBackgroundResource(R.drawable.voice_icon);
			phone_icon_imageview.setTag(regatta.getTelephone());
			phone_icon_imageview.setOnClickListener(new OnClickListener() {
			    public void onClick(View v) {
			    	if (v.getTag()!=null){
			    		String phoneNumber = v.getTag().toString();
			    		Intent intent = new Intent(Intent.ACTION_DIAL);
			    		intent.setData(Uri.parse("tel:"+phoneNumber));
			    		
			    		if (ViewActivity.isIntentAvailable(MyApplication.getAppContext(), Intent.ACTION_DIAL)){
			    			startActivity(intent);
			    		}
			    	}
			    }
			});
			phone_icon_container.addView(phone_icon_imageview);
			phone_icon_container.setVisibility(View.VISIBLE);
		}
		
		if (regatta.getWebsite()!= null && !regatta.getWebsite().isEmpty()){
			ImageView web_icon_imageview = new ImageView(getActivity());
			web_icon_imageview.setBackgroundResource(R.drawable.sites_icon);
			web_icon_imageview.setTag(regatta.getWebsite());
			web_icon_imageview.setOnClickListener(new OnClickListener() {
			    public void onClick(View v) {
			    	if (v.getTag()!=null){
			    		String url = v.getTag().toString();
			    		if (!url.startsWith("http://") && !url.startsWith("https://"))
		    			   url = "http://" + url;
			    		Intent intent = new Intent(Intent.ACTION_VIEW);
			    		intent.setData(Uri.parse(url));
			    		
			    		if (ViewActivity.isIntentAvailable(MyApplication.getAppContext(), Intent.ACTION_VIEW)){
			    			startActivity(intent);
			    		}
			    	}
			    }
			});
			web_icon_container.addView(web_icon_imageview);
			web_icon_container.setVisibility(View.VISIBLE);
		}
		
		if (regatta.getEmail()!= null && !regatta.getEmail().isEmpty()){
			ImageView mail_icon_imageview = new ImageView(getActivity());
			mail_icon_imageview.setBackgroundResource(R.drawable.googlemail_offline_icon);
			mail_icon_imageview.setTag(regatta.getEmail());
			mail_icon_imageview.setOnClickListener(new OnClickListener() {
			    public void onClick(View v) {
			    	if (v.getTag()!=null){
			    		String email = v.getTag().toString();
			    		
			    		Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
	    	            "mailto",email, null));
			    		
			    		startActivity(Intent.createChooser(emailIntent, "Send email..."));

			    	}
			    }
			});
			mail_icon_container.addView(mail_icon_imageview);
			mail_icon_container.setVisibility(View.VISIBLE);
		}
		
		if (regatta.getLatitude()!= null && regatta.getLatitude()!= 0.0){
			ImageView map_icon_imageview = new ImageView(getActivity());
			map_icon_imageview.setBackgroundResource(R.drawable.enterprise_earth_maps_icon);
			map_icon_imageview.setTag(regatta.getLatitude()+","+regatta.getLongitude());
			map_icon_imageview.setOnClickListener(new OnClickListener() {
			    public void onClick(View v) {
			    	if (v.getTag()!=null){
			    		String coordinates = v.getTag().toString();
			    		
			    		Intent intent = new Intent(Intent.ACTION_VIEW,
			    								   Uri.parse("geo:0,0?q="+coordinates+"(" + "Venue"  + ")"));
			    		if (ViewActivity.isIntentAvailable(MyApplication.getAppContext(), Intent.ACTION_VIEW)){
			    			try{
			    				startActivity(intent);
			    			}
			    			catch (Exception e){
			    				// ni mapsov
			    			}
			    		}
			    	}
			    }
			});

			map_icon_container.addView(map_icon_imageview);
			map_icon_container.setVisibility(View.VISIBLE);
		}
		
		// booking del
		Booking booking = db.getBooking(id);
		if (booking != null){
			TextView bookingPriceView = new TextView(getActivity());
			TextView bookingMoreView = new TextView(getActivity());
			bookingPriceView.setTextColor(Color.WHITE);
			bookingMoreView.setTextColor(getResources().getColor(R.color.android_blue));
			
			FrameLayout booking_price_container = (FrameLayout) getView().findViewById(R.id.booking_price_container);
			bookingPriceView.setText("$"+booking.getPrice());
			booking_price_container.addView(bookingPriceView);
			
			FrameLayout booking_more_container = (FrameLayout) getView().findViewById(R.id.booking_more_container);
			bookingMoreView.setText("See on booking.com");
			bookingMoreView.setTag(booking.getUrl());
			bookingMoreView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                	String url = v.getTag().toString();		    		
		    		Intent bookingIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
		    		startActivity(bookingIntent);
                }
            });
			booking_more_container.addView(bookingMoreView);
		}
		else {
			LinearLayout bookingLayout = (LinearLayout) getView().findViewById(R.id.booking_layout);
			bookingLayout.setVisibility(View.GONE);
		}
		
		
		// Weather del
		TextView weather_notification_textview = (TextView) getView().findViewById(R.id.weather_notification_textview);
		if (regatta.getLatitude() != null && regatta.getLatitude() != 0.0){
			DailyWeather[] dailyWeather = db.getDailyWeather(regatta.getId());
			
			// gumb za refreshat vreme
			LinearLayout refreshButtonLayout = (LinearLayout) getView().findViewById(R.id.refresh_weather_button_container);
			Button refreshWeatherButton = (Button)getView().findViewById(R.id.weather_refresh_button);
			refreshWeatherButton.setTag(regatta);
			refreshWeatherButton.setOnClickListener(refreshWeatherButtonListener);
			refreshButtonLayout.setVisibility(View.VISIBLE);
			
			GridView gridview = (GridView) getView().findViewById(R.id.daily_weather_gridview);
						
			if (dailyWeather != null){
				LinearLayout weather_notification_container = (LinearLayout)getView().findViewById(R.id.weather_notification_container);
				
			    gridview.setAdapter(new WeatherImageAdapter(this.getActivity(), dailyWeather));
			    gridview.setVisibility(View.VISIBLE);
			    gridview.setOnItemClickListener(new OnItemClickListener() {
			        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
			          
			        }
			    });
			    
				// prikaži vreme
			}				
			else{
				
			}
			
			// prikaži vreme
			
		}
		else {
			LinearLayout refreshButtonLayout = (LinearLayout) getView().findViewById(R.id.refresh_weather_button_container);
			refreshButtonLayout.setVisibility(View.GONE);
			
			LinearLayout weather_notification_container = (LinearLayout)getView().findViewById(R.id.weather_notification_container);
			weather_notification_textview.setText(R.string.location_not_found);
			weather_notification_container.setVisibility(View.VISIBLE);
			// print location not found
		}
    }
	
	public String countryTagToName(String tag){
		Resources res = getResources();
		String[] tags = res.getStringArray(R.array.country_tags_array);
		Log.d("DEBUG", "Tag: "+ tag);
		String[] countries = res.getStringArray(R.array.countries_array);
		String country = "";
		for (int i = 0; i<tags.length; i++){
			if (tags[i].equals(tag)){
				country = countries[i].split(" - ")[0];
				break;
			}
		}
		
		return country;
	}
	
	public String sqlToReadableDate (String dateStamp){
		final String NEW_FORMAT = "d.M.yyyy";
		final String OLD_FORMAT = "yyyy-MM-dd";

		// August 12, 2010
		String readableDate = "";

		SimpleDateFormat sdf = new SimpleDateFormat(OLD_FORMAT);
		try{
			Date d = sdf.parse(dateStamp);
			sdf.applyPattern(NEW_FORMAT);
			readableDate = sdf.format(d);
		}
		catch (Exception e){
			e.getMessage();
		}
		
		return readableDate;
		
	}

	
}
