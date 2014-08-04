package com.radiusnetworks.campaignkitreference;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListFragment;
import android.os.Bundle;
import android.widget.TableRow;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.radiusnetworks.campaignkitreference.DetailActivity;
import com.radiusnetworks.campaignkitreference.MyApplication;
import com.radiusnetworks.campaignkitreference.R;
import com.radiusnetworks.campaignkit.Campaign;
import com.radiusnetworks.proximity.ibeacon.IBeaconManager;

/**
 * The Main <code>Activity</code> for the CampaignKit's Demo Client.
 * 
 * <p>
 * A <code>ListFragment</code> is utilized in this class to display campaigns sent in from the
 * CampaignKitManager.getCurrentCampaigns() method.
 * 
 * 
 * 
 * @author Matt Tyler
 *
 */
public class MainActivity extends Activity {
	public static final String TAG = "MainActivity";
	Map<String,TableRow> rowMap = new HashMap<String,TableRow>();

	private boolean _visible = false;
	private static MyApplication _application;
	private Context _context;

	public static ArrayAdapter<String> listAdapter;
	static final String RADIUS_UUID = "842AF9C4-08F5-11E3-9282-F23C91AEC05E";


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (_application == null){
			Log.d(TAG,"_application was null. initializing _application value");
			_application = (MyApplication) this.getApplication();
		}
		_application.setMainActivity(this);
		_context = this;
		setContentView(R.layout.activity_main);

		verifyBluetooth();
		
		findViewById(R.id.campaignsButton).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				//Sending to DetailActivity
				Intent intent = new Intent();
				intent.setClass(_context, DetailActivity.class);
				startActivity(intent);
				
			}
		});
		findViewById(R.id.campaignsButton).setVisibility((areCampaignsSightedNow())? View.VISIBLE : View.GONE);

	}

	/**
	 * Refreshes <code>Listview</code> with current campaign titles.
	 */
	public void refreshVisibleList() {
		runOnUiThread(new Runnable() {
			public void run() {
				findViewById(R.id.campaignsButton).setVisibility((areCampaignsSightedNow())? View.VISIBLE : View.GONE);
			}
		});
	}

	private boolean areCampaignsSightedNow(){
		if (_application == null){
			Log.d(TAG,"_application was null. initializing _application value");
			_application = (MyApplication) this.getApplication();
			_application.setMainActivity(this);
		}
		
		if (_application.getSightedCampaignArray() != null && _application.getSightedCampaignArray().size() >0){
			Log.d(TAG,"_application.getSightedCampaignArray().size() = "+_application.getSightedCampaignArray().size());
			return true;
		}
		
		return false;
	}
	
	
	
	

	private void verifyBluetooth() {

		try {
			if (!IBeaconManager.getInstanceForApplication(this).checkAvailability()) {
				Log.e(TAG,"Bluetooth not enabled.");
				final AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setTitle("Bluetooth not enabled");			
				builder.setMessage("Please enable bluetooth in settings and restart this application.");
				builder.setPositiveButton(android.R.string.ok, null);
				builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
					@Override
					public void onDismiss(DialogInterface dialog) {
						dialog.dismiss();

						//closing application
						//finish();
						//System.exit(0);					
					}					
				});
				builder.show();
			}			
		}
		catch (RuntimeException e) {
			Log.e(TAG,"Bluetooth LE not available.");
			final AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("Bluetooth LE not available");			
			builder.setMessage("Sorry, this device does not support Bluetooth LE.");
			builder.setPositiveButton(android.R.string.ok, null);
			builder.setOnDismissListener(new DialogInterface.OnDismissListener() {

				@Override
				public void onDismiss(DialogInterface dialog) {
					dialog.dismiss();

					//closing application
					//finish();
					//System.exit(0);
				}

			});
			builder.show();

		}

	}
	
	
	/*

	public static class SightedCampaignList extends ListFragment {

		@Override
		public void onActivityCreated(Bundle savedInstanceState) {
			super.onActivityCreated(savedInstanceState);

			setListAdapter(listAdapter);

		}


		@Override
		public void onListItemClick(ListView l, View v, int position, long id) {
			Log.i(TAG, "onListItem clicked. id: " + id+ ". position: "+position);
			try {

				((MainActivity) getActivity()).sendToDetailsScreen(position);

			}catch(Exception e){e.printStackTrace();}
		}
	}

	private void sendToDetailsScreen(int positionOnList){
		sendToDetailsScreen( _application.getCampaignFromList(positionOnList));
	}
	
	private void sendToDetailsScreen(Campaign c){
		if (c != null){
			Intent intent = new Intent();
			intent.setClass(this, DetailActivity.class);
			intent.putExtra("campaignId", c.getId());
			startActivity(intent);
		}
	}


	public void showRegistrationFailureNotification(final int status) {
		Log.e(TAG,"didRegister. Error in Registration. status code = "+status);

		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
				builder.setTitle("Registration Error");			
				builder.setMessage("status code = "+status);
				builder.setPositiveButton(android.R.string.ok, null);
				builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
					@Override
					public void onDismiss(DialogInterface dialog) {
						dialog.dismiss();
					}					
				});
				builder.show();					
			}
		});			
	}

	
*/
}
