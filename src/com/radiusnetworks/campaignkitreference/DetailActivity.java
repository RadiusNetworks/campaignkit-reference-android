package com.radiusnetworks.campaignkitreference;


import com.radiusnetworks.campaignkitreference.R;
import com.radiusnetworks.campaignkit.Campaign;


import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;

public class DetailActivity extends Activity {
	protected static final String TAG = "DetailActivity";

	int beaconPosition;
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

			// Getting position within ListView in MainActivity Activity, 
			//		to find out what campaign this is associated with.
			Bundle b = getIntent().getExtras();
			beaconPosition = b.getInt("beaconPosition", 0);
			if (MainActivity.listAdapter.getItem(beaconPosition) != null){
				Log.d(TAG,"displaying beaconPosition = "+beaconPosition);

				this.setTitle((String) MainActivity.listAdapter.getItem(beaconPosition));
				if (MyApplication.sightedCampaignArray.get(beaconPosition) != null){
					Campaign thisCampaign = MyApplication.sightedCampaignArray.get(beaconPosition);

					Log.i(TAG,"campaignid = "+thisCampaign.getId()+". content = "+ thisCampaign.getContent());
					displayHTMLContent(thisCampaign.getContent());

				}
			}
		}catch(Exception e ){e.printStackTrace();}
	}

	private void displayHTMLContent(String content){
		WebView wv = (WebView) findViewById(R.id.contentWV);
		wv.loadData(content, "text/html", null);
	}
}