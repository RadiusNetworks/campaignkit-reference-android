package com.radiusnetworks.campaignkitreference;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListFragment;
import android.os.Bundle;
import android.widget.TableRow;


import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.View;
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
 * The Main <code>Activity</code> for the CampaignKit's Demo Client. This implements <code>CampaignKitNotifier</code>
 * and uses the <code>CampaignKitManager</code> class.
 * <p>
 * This class implements two required methods for the <code>CampaignKitNotifier, campaignsDelivered</code>, 
 * and <code>didRegister</code>. The <code>CampaignKitManager</code> constructor is called within this class's
 * onCreate method, as is necessary for <code>CampaignKit</code> functionality.
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
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_main);

		verifyBluetooth();

		// Create the list fragment and add it as our sole content.
		if (getFragmentManager().findFragmentById(R.id.listLayout) == null) {//android.R.id.content
			SightedCampaignList list = new SightedCampaignList();
			getFragmentManager().beginTransaction().add(R.id.listLayout, list).commit();//android.R.id.content

			listAdapter = new ArrayAdapter<String>(this,
					android.R.layout.simple_list_item_1, _application.getSightedCampaignTitlesList());

		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		_visible = false;
	}
	@Override 
	protected void onResume() {
		super.onResume();
		_visible = true;
	}

	public boolean isInForeground(){
		return _visible;
	}


	/**
	 * Refreshes <code>Listview</code> with current campaign titles.
	 */
	void refreshVisibleList() {
		runOnUiThread(new Runnable() {
			public void run() {
				_application.getSightedCampaignTitlesList(); //this refreshes the titles list
				listAdapter.notifyDataSetChanged();
			}
		});
	}

	/**
	 * A <code>ListFragment</code> displaying all campaigns currently within range.
	 */
	public static class SightedCampaignList extends ListFragment {

		@Override
		public void onActivityCreated(Bundle savedInstanceState) {
			super.onActivityCreated(savedInstanceState);

			setListAdapter(listAdapter);

		}

		/**
		 * Moves to campaign details <code>Activity</code> to display campaign-specific information.
		 */
		@Override
		public void onListItemClick(ListView l, View v, int position, long id) {
			Log.i(TAG, "onListItem clicked. id: " + id+ ". position: "+position);
			try {

				((MainActivity) getActivity()).sendToDetailsScreen(position);

			}catch(Exception e){e.printStackTrace();}
		}
	}

	private void sendToDetailsScreen(int positionOnList){
		//Sending to Details Screen
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
		//create Alert instead of notification
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


	/*
	public void showAlert(final Campaign c){
		final String notificationText = "Welcome! "+c.getTitle();

		//create Alert as well
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
				builder.setTitle("District Taco");			
				builder.setMessage(notificationText);
				builder.setPositiveButton(android.R.string.ok, null);
				builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
					@Override
					public void onDismiss(DialogInterface dialog) {
						dialog.dismiss();
						sendToDetailsScreen(c);
					}					
				});
				builder.show();					
			}
		});			
	}
	 */
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

	PROXIMITY KIT STUFF

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ((MyApplication)getApplication()).setMainActivity(this);
    }

    public void displayTableRow(final IBeacon iBeacon, final String displayString, final boolean updateIfExists) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                TableLayout table = (TableLayout) findViewById(R.id.beacon_table);
                String key = iBeacon.getProximityUuid() + "-" + iBeacon.getMajor() + "-" + iBeacon.getMinor();
                TableRow tr = (TableRow) rowMap.get(key);
                if (tr == null) {
                    tr = new TableRow(MainActivity.this);
                    tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                    rowMap.put(key, tr);
                    table.addView(tr);
                }
                else {
                    if (updateIfExists == false) {
                        return;
                    }
                }
                tr.removeAllViews();
                TextView textView=new TextView(MainActivity.this);
                textView.setText(displayString);
                tr.addView(textView);

            }
        });

    }
	 */
}
