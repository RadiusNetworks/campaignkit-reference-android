package com.radiusnetworks.campaignkitreference;


import java.util.ArrayList;
import java.util.List;

import com.example.android.expandingcells.CustomArrayAdapter;
import com.example.android.expandingcells.ExpandableListItem;
import com.example.android.expandingcells.ExpandingListView;
import com.radiusnetworks.campaignkitreference.R;
import com.radiusnetworks.campaignkit.Campaign;


import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.widget.ArrayAdapter;

public class DetailActivity extends Activity {
	protected static final String TAG = "DetailActivity";


	private final int CELL_DEFAULT_HEIGHT = 200;

	private ExpandingListView mListView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try{
			setContentView(R.layout.activity_detail);
			getActionBar().setDisplayHomeAsUpEnabled(true);

			// Getting campaignId
			Bundle b = getIntent().getExtras();
			String campaignId = b.getString("campaignId", "");

			if (campaignId != ""){
				Campaign thisCampaign = ((MyApplication) this.getApplication())._ckManager.getCampaign(campaignId);		
				if (thisCampaign != null){

					//TODO: expand list at thisCampaign and autoScroll to it
				}
			}

			refreshList();

			

		}catch(Exception e ){e.printStackTrace();}
	}
	
	@Override 
	public void onResume(){
		super.onResume();
		refreshList();
	}
	
	private void refreshList(){
		List<ExpandableListItem> mData = new ArrayList<ExpandableListItem>();
		ArrayList<Campaign> campaignArray =  ((MyApplication) this.getApplication()).getSightedCampaignArray();

		if (campaignArray != null){
			Log.d(TAG,"campaignArray ="+campaignArray.toString());
			for (Campaign c : campaignArray) {
				mData.add(new ExpandableListItem(c.getTitle(), c.getContent(),
						CELL_DEFAULT_HEIGHT, c.getMessage()));
			}

			CustomArrayAdapter adapter = new CustomArrayAdapter(this, R.layout.list_view_item, mData);

			mListView = (ExpandingListView)findViewById(R.id.detail_list_view);
			mListView.setAdapter(adapter);
			ColorDrawable blue = new ColorDrawable(this.getResources().getColor(R.color.radius_blue));
			mListView.setDivider(blue);
			mListView.setDividerHeight(2);
		} else Log.e(TAG,"CAMPAIGNARRAY == NULL!");

	}
}