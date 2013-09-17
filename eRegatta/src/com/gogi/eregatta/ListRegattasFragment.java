package com.gogi.eregatta;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.api.services.regattaendpoint.model.Regatta;

public class ListRegattasFragment extends Fragment
{
	MySQLiteHelper db = new MySQLiteHelper(MyApplication.getAppContext());
	OnRegattaClickedListener mCallback;
	public static final String IDS = "ids";
	
	public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallback = (OnRegattaClickedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnRegattaClickedListener");
        }
	}
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, 
        Bundle savedInstanceState) {
        
        return inflater.inflate(R.layout.regatta_list_view, container, false);
    }
	
    // Container Activity must implement this interface
    
    
    public void onActivityCreated(Bundle savedInstanceState){
    	super.onActivityCreated(savedInstanceState);
    	
    	Bundle arguments=getArguments(); 
    	int[] ids = arguments.getIntArray(IDS);
    	LinearLayout view = (LinearLayout) getView().findViewById(R.id.list_container);
    	
    	for (int i = 0; i < ids.length; i++){
    		int id = ids[i];
    		Regatta regatta = db.getRegatta(id);
    		
    		TextView rowView = new TextView(this.getActivity());
    		
    		rowView.setPadding(16, 16, 16, 16);
    		rowView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
    				LayoutParams.MATCH_PARENT));
    		//rowView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
    		rowView.setOnClickListener(onDescribeRegattaListener);
    		
    		rowView.setTag(regatta.getId());
    		rowView.setText(regatta.getName());
    		view.addView(rowView);
    	}
    }
    public interface OnRegattaClickedListener {
        public void onRegattaClicked(int id);
    }
    private OnClickListener onDescribeRegattaListener = new OnClickListener() {
	    public void onClick(View v) {
	    	mCallback.onRegattaClicked((Integer)v.getTag());
	    }
	};
}
