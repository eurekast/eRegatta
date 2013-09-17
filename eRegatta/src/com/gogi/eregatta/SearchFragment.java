package com.gogi.eregatta;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.gogi.eregatta.SearchActivity.WeatherGetter;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class SearchFragment extends Fragment {
		MySQLiteHelper db = new MySQLiteHelper(MyApplication.getAppContext());
		OnSearchExecutedListener mCallback;
		Activity activity;
		
	    // Container Activity must implement this interface
		public void onAttach(Activity activity) {
		        super.onAttach(activity);
		        try {
		            mCallback = (OnSearchExecutedListener) activity;
		        } catch (ClassCastException e) {
		            throw new ClassCastException(activity.toString()
		                    + " must implement OnHeadlineSelectedListener");
		        }
		        this.activity=activity;
		}
		@Override
	    public View onCreateView(LayoutInflater inflater, ViewGroup container, 
	        Bundle savedInstanceState) {
			
	        // Inflate the layout for this fragment
	        return inflater.inflate(R.layout.search_view, container, false);
		}
	    @Override
	    public void onActivityCreated(Bundle savedInstanceState){
	    	super.onActivityCreated(savedInstanceState);
	    	setSpinners();
	    	
	    	Button searchButton = (Button)getView().findViewById(R.id.search_button);
			searchButton.setOnClickListener(searchButtonListener);
			Button refreshRegattasButton = (Button)getView().findViewById(R.id.refreshRegattas_button);
			refreshRegattasButton.setOnClickListener(refreshRegattasButtonListener);
			
			TextView refreshTime = (TextView)getView().findViewById(R.id.refreshTime_textview);
			refreshTime.setText("Last refresh:\n"+db.getRefresh());
			
	    }
	    
	    public interface OnSearchExecutedListener {
	        public void onSearchExecuted(int[] ids);
	    }
    
	   
	  
	    private OnClickListener refreshRegattasButtonListener = new OnClickListener() {
		    public void onClick(View v) {
		    	((SearchActivity)activity).new RegattaGetter().execute();
		    }
		};
		private OnClickListener searchButtonListener = new OnClickListener() {
		    public void onClick(View v) {
		    	
		    	HashMap<String, String> regattaParams = new HashMap<String, String>();
		    	HashMap<String, String> regattaClassParams = new HashMap<String, String>();
		    	regattaParams.put(MySQLiteHelper.FIELD_NAME, ((EditText) getView().findViewById(R.id.search_name)).getText().toString());
		    	regattaParams.put(MySQLiteHelper.FIELD_VENUE, ((EditText) getView().findViewById(R.id.search_venue)).getText().toString());
		    	regattaParams.put(MySQLiteHelper.FIELD_COUNTRY, ((Spinner) getView().findViewById(R.id.search_country)).getSelectedItem().toString());
		    	regattaClassParams.put(MySQLiteHelper.FIELD_DISCIPLINE, ((Spinner) getView().findViewById(R.id.search_discipline)).getSelectedItem().toString());
		    	regattaClassParams.put(MySQLiteHelper.FIELD_TYPE, ((Spinner) getView().findViewById(R.id.search_type)).getSelectedItem().toString());
		    	regattaClassParams.put(MySQLiteHelper.FIELD_REGATTACLASS, ((Spinner) getView().findViewById(R.id.search_class)).getSelectedItem().toString());
		    	regattaClassParams.put(MySQLiteHelper.FIELD_GRADE, ((Spinner) getView().findViewById(R.id.search_grade)).getSelectedItem().toString());
		    	regattaParams.put(MySQLiteHelper.FIELD_MONTH, ((Spinner) getView().findViewById(R.id.search_month)).getSelectedItem().toString());
		    	
		    	ArrayList<HashMap<String, String>> params = new ArrayList<HashMap<String, String>>();
		    	params.add(regattaParams);
		    	params.add(regattaClassParams);
		    	
		    	int[] ids = db.searchRegattas(params);
		    	mCallback.onSearchExecuted(ids);
		    }
		};
		
		public void setSpinners(){				// populating spinners
			Spinner spinner1 = (Spinner) getView().findViewById(R.id.search_country);
			ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this.getActivity(), R.array.countries_array, android.R.layout.simple_spinner_item);
			adapter1.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
			spinner1.setAdapter(adapter1);
			
			spinner1 = (Spinner) getView().findViewById(R.id.search_discipline);
			adapter1 = ArrayAdapter.createFromResource(this.getActivity(), R.array.discipline_array, android.R.layout.simple_spinner_item);
			adapter1.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
			spinner1.setAdapter(adapter1);
			
			spinner1 = (Spinner) getView().findViewById(R.id.search_type);
			adapter1 = ArrayAdapter.createFromResource(this.getActivity(), R.array.type_array, android.R.layout.simple_spinner_item);
			adapter1.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
			spinner1.setAdapter(adapter1);
			
			spinner1 = (Spinner) getView().findViewById(R.id.search_class);
			adapter1 = ArrayAdapter.createFromResource(this.getActivity(), R.array.class_array, android.R.layout.simple_spinner_item);
			adapter1.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
			spinner1.setAdapter(adapter1);
			
			spinner1 = (Spinner) getView().findViewById(R.id.search_grade);
			adapter1 = ArrayAdapter.createFromResource(this.getActivity(), R.array.grade_array, android.R.layout.simple_spinner_item);
			adapter1.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
			spinner1.setAdapter(adapter1);
			
			spinner1 = (Spinner) getView().findViewById(R.id.search_month);		// populating Month search spinner with next 12 months
			Date current = new Date();  
			Calendar cal = Calendar.getInstance();  
			cal.setTime(current);  
			SimpleDateFormat format = new SimpleDateFormat("MMMMM yyyy");
			List<String> list=new ArrayList<String>();
			list.add("");
			list.add(format.format(current));
			for (int i = 0; i<11; i++){
				cal.set(Calendar.MONTH, (cal.get(Calendar.MONTH)+1));  
				current = cal.getTime(); 
				list.add(format.format(current));
			}
			ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_list_item_1, list);
			adapter2.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
			spinner1.setAdapter(adapter2);
		}
	
}
