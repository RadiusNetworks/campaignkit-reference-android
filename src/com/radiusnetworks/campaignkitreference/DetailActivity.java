package com.radiusnetworks.campaignkitreference;


import com.radiusnetworks.campaignkitreference.R;
import com.radiusnetworks.campaignkit.Campaign;


import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;

public class DetailActivity extends Activity {
	protected static final String TAG = "DetailActivity";

	//	int beaconPosition;
	int dw = 0;
	int dh = 0;
	int height = 0;
	int width = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try{
			setContentView(R.layout.activity_detail);
			getActionBar().setDisplayHomeAsUpEnabled(true);

			// Getting campaignId
			Bundle b = getIntent().getExtras();
			String campaignId = b.getString("campaignId", "");
			
			Campaign thisCampaign = ((MyApplication) this.getApplication())._ckManager.getCampaign(campaignId);		

			if (thisCampaign != null){
				Log.d(TAG,"displaying campaign with id: "+campaignId);
				
				//Setting screen title
				this.setTitle((String) thisCampaign.getTitle());
				
				//Setting screen content
				WebView wv = (WebView) findViewById(R.id.contentWV);
				wv.loadData(thisCampaign.getContent(), "text/html", null);
			}
			/*
			beaconPosition = b.getInt("beaconPosition", 0);
			if (MainActivity.listAdapter.getItem(beaconPosition) != null){
				Log.d(TAG,"displaying beaconPosition = "+beaconPosition);

				this.setTitle((String) MainActivity.listAdapter.getItem(beaconPosition));
				if (MyApplication.sightedCampaignArray.get(beaconPosition) != null){
					Campaign thisCampaign = MyApplication.sightedCampaignArray.get(beaconPosition);

					displayHTMLContent(thisCampaign.getContent());

				}
			}
			 */
		}catch(Exception e ){e.printStackTrace();}
	}
}