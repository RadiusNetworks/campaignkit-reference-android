campaignkit-reference-android
=============================

This is a reference app for the Campaign Kit Library that is structured to run using the Eclipse Android Bundle. If you don't have Eclipse, or are using the Android Studio bundle, please use [this reference app instead](https://github.com/RadiusNetworks/campaignkit-reference-android-studio). 




#Requirements for use: 

* Minimum Android API Level 7.

* Android API Level 18 or higher to use AltBeacon features.

* Android API Level 9 or higher to use Geofencing features.

* Google Play Services library set as a dependency. The library version must be within the range of 4.2 and 6.5.87.

* CampaignKit.properties file downloaded from campaignkit.radiusnetworks.com.




#Setup Instructions

* Double click the campaignkit-android.tar.gz file and place the 'campaignkit-android' folder into the same location as the campaignkit-reference-android project.

* Open a current version of Eclipse ADT with Android SDK.

* Import the campaignkit-reference-android project, the campaignkit-android library, and the google-play-services_lib as 'Existing Android Code into Workspace'. If you don't have google-play-services_lib set up already, please refer to the [Google Setup docs](https://developer.android.com/google/play-services/setup.html) for the proper instructions.

* Right click on the CKReference project in the Eclipse Package Explorer and open of the Properties menu. Select the Android tab on the left, and click the 'Add...' button on the bottom right of the menu to add in both the campaignkit-android library and the google-play-services_lib as dependencies to the campaignkit-reference-android project.

* Go to campaignkit.radiusnetworks.com and set up some active Campaigns in your Campaign Kit. Once completed, from the overview page of your Campaign Kit click the small button with the Android robot icon at the top right. This will download your CampaignKit.properties file. Please copy that into the src folder of CKReference project in Eclipse. There is already one in the right spot, please just overwrite that one.

At this point the project will run, granted you have the proper Android build tools and APIs installed into your Android SDK Manager. This project is currently set to Android API level 19, so please install that through your SDK Manager.
