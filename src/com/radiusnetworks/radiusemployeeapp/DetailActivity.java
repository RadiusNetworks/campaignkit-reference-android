package com.radiusnetworks.radiusemployeeapp;


import java.util.ArrayList;

import com.example.android.slidingtabscolors.SlidingTabsColorsFragment;
import com.radiusnetworks.campaignkit.Campaign;
import com.radiusnetworks.radiusemployeeapp.R;


import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

public class DetailActivity extends FragmentActivity {
	protected static final String TAG = "DetailActivity";
	public static final String KEY_CAMPAIGN_ID = "campaignId";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try{
			setContentView(R.layout.activity_detail);
			getActionBar().setDisplayHomeAsUpEnabled(true);

			
			refreshList(getIntent().getExtras());

			
		}catch(Exception e ){e.printStackTrace();}
	}
	
	@Override 
	public void onResume(){
		super.onResume();
		refreshList();
	}
	private void refreshList(){
		refreshList(null);
	}

	private void refreshList(Bundle b){
		ArrayList<Campaign> campaignArray =  ((MyApplication) this.getApplication()).getTriggeredCampaignArray();

		if (campaignArray != null){

	        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
	        SlidingTabsColorsFragment fragment = new SlidingTabsColorsFragment();

	        
	        //not working yet
	        if (b != null && b.getString(KEY_CAMPAIGN_ID,"") != ""){
	        	Log.d(TAG,"refreshing list with campaignId = "+b.getString(KEY_CAMPAIGN_ID,""));
	            Bundle args = new Bundle();
	            args.putString(KEY_CAMPAIGN_ID, b.getString(KEY_CAMPAIGN_ID,""));
	        	fragment.setArguments(args);
	        }
	        
	        transaction.replace(R.id.sample_content_fragment, fragment);
	        transaction.commit();
			
		} else Log.e(TAG,"CAMPAIGNARRAY == NULL!");

	}
	
	public ArrayList<Campaign> getCampaignArray(){
		return ((MyApplication) this.getApplication()).getTriggeredCampaignArray();
	}
}