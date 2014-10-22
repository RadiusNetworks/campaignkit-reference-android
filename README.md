campaignkit-reference-android
=============================

This is a reference app for the Campaign Kit Library that is structured to run using the Eclipse Android Bundle. If you are using the Android Studio bundle, please use [this reference app instead](https://github.com/RadiusNetworks/campaignkit-reference-android-studio).



#Requirements For Use

* Campaign Kit Library ([download the .tar.gz file](https://github.com/RadiusNetworks/campaignkit-android/releases))

* Android API Level 18 or higher

* Google Play Services library set as a dependency if using Geofencing features.

* CampaignKit.properties file downloaded from campaignkit.radiusnetworks.com.



#Setup Instructions

* Double click the campaignkit-android.tar.gz file and place the 'campaignkit-android' folder into the same location as the campaignkit-reference-android project.

* Open a current version of Eclipse ADT with Android SDK. If you don't have that yet, you can find it [here](https://developer.android.com/sdk/index.html).

* Import the campaignkit-reference-android project, the campaignkit-android library, and the google-play-services_lib as 'Existing Android Code into Workspace'. If you don't have google-play-services_lib set up already, please refer to the [Google Setup docs](https://developer.android.com/google/play-services/setup.html) for the proper instructions.

* Right click on the CKReference project in the Eclipse Package Explorer and open of the Properties menu. Select the Android tab on the left, and click the 'Add...' button on the bottom right of the menu to add in both the campaignkit-android library and the google-play-services_lib as dependencies to the campaignkit-reference-android project.

* Go to campaignkit.radiusnetworks.com and set up some active Campaigns in your Campaign Kit. Once completed, from the overview page of your Campaign Kit click the small button with the Android robot icon at the top right. This will download your CampaignKit.properties file. Please copy that into the src folder of CKReference project in Eclipse. There is already one in the right spot, please just overwrite that one.

At this point the project will run, granted you have the proper Android build tools and APIs installed into your Android SDK Manager. This project is currently set to Android API level 19, so please install that through your SDK Manager.
