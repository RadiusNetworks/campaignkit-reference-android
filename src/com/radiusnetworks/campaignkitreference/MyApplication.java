package com.radiusnetworks.campaignkitreference;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo.DetailedState;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.radiusnetworks.campaignkitreference.MainActivity;
import com.radiusnetworks.campaignkitreference.R;
import com.radiusnetworks.campaignkit.Campaign;
import com.radiusnetworks.campaignkit.CampaignKitNotifier;
import com.radiusnetworks.campaignkit.CampaignKitManager;
import com.radiusnetworks.campaignkit.CampaignNotificationBuilder;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by dyoung on 4/2/14.
 */
public class MyApplication extends Application implements CampaignKitNotifier {
	public static final String TAG = "MyApplication";


	static ArrayList<Campaign> sightedCampaignArray = new ArrayList<Campaign>(); //all campaigns with their beacon within range right now
	static ArrayList<String> sightedCampaignTitles = new ArrayList<String>(); //titles of all campaigns with their beacon within range, in same order

	public CampaignKitManager _ckManager;


	private MainActivity _mainActivity = null;

	Date lastRefreshTime = new Date();

	@Override
	public void onCreate() {
		super.onCreate();

		_ckManager = CampaignKitManager.getInstanceForApplication(this);
		_ckManager.start();
		_ckManager.setNotifier(this);

	}




	public void setMainActivity(MainActivity _mainActivity) {
		this._mainActivity = _mainActivity;
	}

	/**
	 * Refreshes <code>Listview</code> on MainActivity <code>Activity</code> to properly display 
	 * campaigns associated with newly sighted beacons.
	 */
	private void refreshLizardman(){
		if (_mainActivity != null) {
			_mainActivity.refreshVisibleList();			
		}
		else {
			Log.d(TAG, "Main activity not started yet.");
		}
	}


	private void addToSightedCampaignArrayAndShowNotification(Campaign campaign){
		//adding Campaign to sightedCampaignArray, this will force it to be shown on the SightedCampaignList
		sightedCampaignArray.add(campaign);

		//sending notification or alert, depending on whether app is in background or foreground
		new CampaignNotificationBuilder(_mainActivity, campaign)
		.setSmallIcon(R.drawable.ic_launcher)
		.setOnClickActivity(DetailActivity.class)
		.show();
	}

	static void removeFromSightedCampaignArray(int[] campaignIDs){
		//Note: this uses odd logic to prevent a ConcurrentModificationException

		ArrayList <Campaign> campaignsToRemove = new ArrayList<Campaign>();
		for (Campaign d : sightedCampaignArray){
			for (int campaignID : campaignIDs){

				if (d.getIdAsInt() == campaignID){
					campaignsToRemove.add(d);
				}
			}
		}

		for (Campaign d : campaignsToRemove){
			sightedCampaignArray.remove(d);
		}
	}
	/**
	 * Refreshes sightedCampaignTitles <code>Arraylist</code>.
	 * @return refreshed sightedCampaignTitles <code>Arraylist</code>.
	 */
	public ArrayList<String> getSightedCampaignTitlesList(){
		if (sightedCampaignArray != null){
			sightedCampaignTitles.clear();
			for (Campaign c : sightedCampaignArray){
				sightedCampaignTitles.add(c.getTitle());
			}
		}
		return sightedCampaignTitles;
	}
	
	public Campaign getCampaignFromList(int positionOnList){
		if (sightedCampaignArray != null){
			return sightedCampaignArray.get(positionOnList);
		}
		return null;
	}


	@Override
	public void didFindCampaign(Campaign campaign) {
		addToSightedCampaignArrayAndShowNotification(campaign);		
		refreshLizardman();
	}
}
