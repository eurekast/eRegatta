package com.gogi.eregatta;

import java.util.Calendar;
import java.util.HashMap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;

public class WeatherImageAdapter extends BaseAdapter {
	private Context mContext;
	private DailyWeather[] weather;
	
	public WeatherImageAdapter(Context c, DailyWeather[] weather) {
		this.weather = weather;
        mContext = c;
    }
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 36;
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {	
		TextView textView;
		
		
		HashMap<String, Integer> iconLinks = new HashMap<String, Integer>();
		iconLinks.put("clear-day", R.drawable.clear_day);
		iconLinks.put("clear-night", R.drawable.clear_day);
		iconLinks.put("cloudy", R.drawable.cloudy);
		iconLinks.put("fog", R.drawable.fog);
		iconLinks.put("partly-cloudy-day", R.drawable.partly_cloudy_day);
		iconLinks.put("partly-cloudy-night", R.drawable.partly_cloudy_day);
		iconLinks.put("rain", R.drawable.rain);
		iconLinks.put("sleet", R.drawable.sleet);
		iconLinks.put("snow", R.drawable.snow);
		iconLinks.put("wind", R.drawable.wind);
		
		
		
		// first column
		if (position%6 == 0){
			if (convertView == null) {  // if it's not recycled, initialize some attributes
				textView = new TextView(mContext);
				textView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
		                LayoutParams.WRAP_CONTENT));
				textView.setText(""+position);
			}
			else {
				textView = (TextView) convertView;
	        }
			switch(position){
			case 12:
				textView.setText(R.string.celsiumMin);
				break;
			case 18:
				textView.setText(R.string.celsiumMax);
				break;
			case 24:
				textView.setText(R.string.kt);
				break;
			case 36:
				textView.setText(R.string.degree);
				break;
			default:
				textView.setText("");
			}
				
			return textView;
		}
		// date
		else if (position<6){
			if (convertView == null) {  // if it's not recycled, initialize some attributes
				textView = new TextView(mContext);
				textView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
		                LayoutParams.WRAP_CONTENT));
				textView.setText(""+position);
			}
			else {
				textView = (TextView) convertView;
	        }
			Calendar mydate = Calendar.getInstance();
			Log.d("DEBUG", "weather size: "+weather.length);
			mydate.setTimeInMillis(weather[position-1].time*1000);
			textView.setText(mydate.get(Calendar.DAY_OF_MONTH)+"."+(mydate.get(Calendar.MONTH)+1)+".");
				
			return textView;
		}
		// icons
		else if (position<12){
			ImageView imageView;
			if (convertView == null) {  // if it's not recycled, initialize some attributes
				imageView = new ImageView(mContext);
				imageView.setLayoutParams(new GridView.LayoutParams(80, 80));
				imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
		    	imageView.setPadding(8, 25, 8, 25);
			}
			else {
	            imageView = (ImageView) convertView;
	        }
			String icon = weather[position-7].icon;
	        imageView.setImageResource(iconLinks.get(icon));
	        return imageView;
		}
		// temperature
		else if (position<18){	
			if (convertView == null) {  // if it's not recycled, initialize some attributes
				textView = new TextView(mContext);
				textView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
		                LayoutParams.WRAP_CONTENT));
				textView.setText(""+position);
			}
			else {
				textView = (TextView) convertView;
	        }
			textView.setText(String.format("%.2g%n", weather[position-13].temperatureMin));
			return textView;
		}
		else if (position<24){
			if (convertView == null) {  // if it's not recycled, initialize some attributes
				textView = new TextView(mContext);
				textView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
		                LayoutParams.WRAP_CONTENT));
				textView.setText(""+position);
			}
			else {
				textView = (TextView) convertView;
	        }
			textView.setText(String.format("%.2g%n", weather[position-19].temperatureMax));
			return textView;
		}
		// windSpeed
		else if (position<30){
			if (convertView == null) {  // if it's not recycled, initialize some attributes
				textView = new TextView(mContext);
				textView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
		                LayoutParams.WRAP_CONTENT));
				textView.setText(""+position);
			}
			else {
				textView = (TextView) convertView;
	        }
			textView.setText(String.format("%.2g%n", weather[position-25].windSpeed));
			return textView;
		}
		// windDir
		else if (position<36){
			ImageView imageView;
			if (convertView == null) {  // if it's not recycled, initialize some attributes
				imageView = new ImageView(mContext);
				imageView.setLayoutParams(new GridView.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
				imageView.setImageResource(R.drawable.arrow);
				imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
				
		    	//imageView.setPadding(8, 25, 8, 25);
			}
			else {
	            imageView = (ImageView) convertView;
	        }
			BitmapFactory.Options dimensions = new BitmapFactory.Options(); 
			dimensions.inJustDecodeBounds = true;
			Bitmap mBitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.arrow, dimensions);
			int height = dimensions.outHeight;
			int width =  dimensions.outWidth;
			
			/*Matrix matrix=new Matrix();
			imageView.setScaleType(ScaleType.MATRIX);   //required
			Log.d("DEBUG", "arrow params: "+(height/2)+", "+(width/2));
			matrix.postRotate((float) weather[position-31].windDir, height/2, width/2);
			imageView.setImageMatrix(matrix);*/
			imageView.setRotation(weather[position-31].windDir-90);
			
			return imageView;
		}
		else{
			if (convertView == null) {  // if it's not recycled, initialize some attributes
				textView = new TextView(mContext);
				textView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
		                LayoutParams.WRAP_CONTENT));
				textView.setText(""+position);
			}
			else {
				textView = (TextView) convertView;
	        }
			return textView;
		}
	}
	
}
