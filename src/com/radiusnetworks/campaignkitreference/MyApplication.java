package com.radiusnetworks.campaignkitreference;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.example.campaignkitreference.R;
import com.radiusnetworks.campaignkit.Campaign;
import com.radiusnetworks.campaignkit.CampaignKitNotifier;
import com.radiusnetworks.campaignkit.CampaignKitManager;
import com.radiusnetworks.campaignkitreference.MainActivity;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by dyoung on 4/2/14.
 */
public class MyApplication extends Application implements CampaignKitNotifier {
	public static final String TAG = "MyApplication";


	static ArrayList<Campaign> sightedCampaignArray = new ArrayList<Campaign>(); //all campaigns with their beacon within range right now
	static ArrayList<String> sightedCampaignTitles = new ArrayList<String>(); //titles of all campaigns with their beacon within range, in same order

	private CampaignKitManager _ckManager;


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



	private void addToSightedCampaignArrayAndShowNotification(int[] campaignIDs){
		//adding all campaigns associated with this beacon to the sightedCampaignArray
		for (int campaignID : campaignIDs){
			addToSightedCampaignArrayAndShowNotification(campaignID);

		}

	}
	private void addToSightedCampaignArrayAndShowNotification(int campaignID){
		//adding all campaigns associated with this beacon to the sightedCampaignArray
		Campaign c = _ckManager.getCampaign(campaignID);
		Log.d(TAG,"addToSightedCampaignArrayAndShowNotification. campaignID="+campaignID+". c.getid="+c.getId());
		if (c.getIdAsInt() == campaignID){
			Log.d(TAG,"campaign id matched to campaign in Array. adding to sightedCampaignArray");
			addToSightedCampaignArrayAndShowNotification(c);

		}

	}
	private void addToSightedCampaignArrayAndShowNotification(Campaign campaign){
		//adding Campaign to sightedCampaignArray, this will force it to be shown on the SightedCampaignList
		sightedCampaignArray.add(campaign);

		//sending notification or alert, depending on whether app is in background or foreground
		notifyUser(campaign);
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
	ArrayList<String> getSightedCampaignTitlesList(){
		if (sightedCampaignArray != null){
			sightedCampaignTitles.clear();
			for (Campaign d : sightedCampaignArray){
				sightedCampaignTitles.add(d.getTitle());
			}
		}
		return sightedCampaignTitles;
	}

	/**
	 * Creates a local <code>Notification</code> or <code>Alert</code> to notify user of a new nearby
	 * <code>Campaign</code>. If the app is currently in the background it will create a <code>Notification</code>,
	 * otherwise, it will create an <code>Alert</code>.
	 *  
	 *  
	 * @param d 	The <code>Campaign</code> the user has encountered.
	 */
	void notifyUser(final Campaign d){
		Log.d(TAG,"notifyUser. campaignid ="+d.getId());

		if((_mainActivity == null) || (!_mainActivity.isInForeground())){
			sendNotification(d);
		}
		else{
			_mainActivity.showAlert(d);
		}
	}

	public void sendNotification(Campaign c) {
		Log.e(TAG,"sendingNotification from MyApplication");
		final String notificationText = c.getMessage();

		NotificationCompat.Builder mBuilder =
				new NotificationCompat.Builder(this)
		.setSmallIcon(R.drawable.ic_launcher)
		.setTicker(notificationText) 

		.setContentTitle("District Taco")
		.setContentText(notificationText)
		//.setLargeIcon(R.drawable.ic_launcher)
		.setStyle(new NotificationCompat.BigTextStyle()
		.bigText(notificationText));



		// Creates an explicit intent for an Activity in your app
		Intent resultIntent = new Intent(this, MainActivity.class);

		resultIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);         

		// The stack builder object will contain an artificial back stack for the
		// started Activity.
		// This ensures that navigating backward from the Activity leads out of
		// your application to the Home screen.
		TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
		// Adds the back stack for the Intent (but not the Intent itself)
		stackBuilder.addParentStack(MainActivity.class);//  this);
		// Adds the Intent that starts the Activity to the top of the stack
		stackBuilder.addNextIntent(resultIntent);
		PendingIntent resultPendingIntent =
				stackBuilder.getPendingIntent(
						0,
						PendingIntent.FLAG_UPDATE_CURRENT
						);

		mBuilder.setContentIntent(resultPendingIntent);

		mBuilder.setAutoCancel(true);


		NotificationManager mNotificationManager =
				(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

		Notification notification = mBuilder.build();
		notification.defaults |= Notification.DEFAULT_SOUND;
		notification.defaults |= Notification.DEFAULT_LIGHTS;
		notification.flags |= Notification.FLAG_SHOW_LIGHTS;
		notification.ledARGB = 0xff00ff00;
		notification.ledOnMS = 300;
		notification.ledOffMS = 1000;

		mNotificationManager.notify(c.getIdAsInt(),notification);

	}



	@Override
	public void didFindCampaign(Campaign campaign) {
		addToSightedCampaignArrayAndShowNotification(campaign);		
		refreshLizardman();
	}

	@Override
	public void didFindCampaign(int campaignID) {
		addToSightedCampaignArrayAndShowNotification(campaignID);			
		refreshLizardman();
	}

	@Override
	public void didFindCampaign(int[] campaignIDs) {
		Log.d(TAG,"didFindCampaign int[]");
		addToSightedCampaignArrayAndShowNotification(campaignIDs);
		refreshLizardman();
	}





}
