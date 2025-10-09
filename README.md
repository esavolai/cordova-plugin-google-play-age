# cordova-plugin-google-play-age
Google Play Age Signals Cordova Plugin
https://developer.android.com/google/play/age-signals/use-age-signals-api

### Add the plugin 
> cordova plugin add @globules-io/cordova-plugin-google-play-age

### Remove the plugin 
> cordova plugin rm @globules-io/cordova-plugin-google-play-age

### Usage
    cordova.plugins.agesignals.checkAgeSignals(
        function(result) {
            console.log('Age Status:', result.userStatus); // e.g., "VERIFIED"
            if (result.userStatus === 'SUPERVISED_APPROVAL_DENIED') {
                // Handle denial, e.g., restrict access
            }
            // Use ageLower/ageUpper for supervised users
        },
        function(error) {
            var code = parseInt(error);
            // Handle errors (see table below)
            if (code === -9) {
                alert('Please install the app from Google Play.');
            }
        }
    );


## Response Codes
> Values for userStatus

#### VERIFIED	
> The user is over 18. Google verified the user's age using a commercially reasonable method such as a government-issued ID, credit card, or facial age estimation.

#### SUPERVISED	
> The user has a supervised Google Account managed by a parent who sets their age. Use ageLower and ageUpper to determine the user's age range.

#### SUPERVISED_APPROVAL_PENDING	
> The user has a supervised Google Account, and their supervising parent has not yet approved one or more pending significant changes. Use ageLower and ageUpper to determine the user's age range. Use mostRecentApprovalDate to determine the last significant change that was approved.

#### SUPERVISED_APPROVAL_DENIED	
> The user has a supervised Google Account, and their supervising parent denied approval for one or more significant changes. Use ageLower and ageUpper to determine the user's age range. Use mostRecentApprovalDate to determine the last significant change that was approved.

#### UNKNOWN	
> The user is not verified or supervised in applicable jurisdictions and regions. These users could be over or under 18. To obtain an age signal from Google Play, ask the user to visit the Play Store to resolve their status.

#### Empty (a blank value)	
> userStatus is unknown or empty.

#### ageLower	
> 0 to 18	The (inclusive) lower bound of a supervised user's age range. Use the ageLower and ageUpper to determine the user's age range.
> Empty (a blank value) userStatus is unknown or empty.

#### ageUpper	
> 2 to 18	The (inclusive) upper bound of a supervised user's age range. Use the ageLower and ageUpper to determine the user's age range.
> Empty (a blank value)	Either the userStatus is supervised and the user's parent attested age is over 18. Or the userStatus is verified, unknown, or empty.

#### mostRecentApprovalDate	Datestamp	
> The effective from date of the most recent significant change that was approved. When an app is installed, the date of the most recent significant change prior to install is used.
> Empty (a blank value)	Either the userStatus is supervised and no significant change has been submitted. Or userStatus is verified, unknown, or empty.

#### installID	Play-generated alphanumeric ID.	
> An ID assigned to supervised user installs by Google Play, used for the purposes of notifying you of revoked app approval. Review the documentation for revoked app approvals.
> Empty (a blank value)	userStatus is verified, unknown, or empty.

## Error Codes
#### API_NOT_AVAILABLE : -1	
> The Play Age Signals API is not available. The Play Store app version installed on the device might be old.

#### PLAY_STORE_NOT_FOUND : -2	
> No Play Store app is found on the device. Ask the user to install or enable the Play Store.

#### NETWORK_ERROR : -3	
> No available network is found. Ask the user to check for a connection.

#### PLAY_SERVICES_NOT_FOUND : -4
> Play Services is not available or its version is too old. Ask the user to install, update, or enable Play Services.

#### CANNOT_BIND_TO_SERVICE : -5
> Binding to the service in the Play Store has failed. This can be due to having an old Play Store version installed on the device or device memory is overloaded. Ask the user to update the Play Store app. Retry with an exponential backoff.

#### PLAY_STORE_VERSION_OUTDATED : -6	
> The Play Store app needs to be updated. Ask the user to update the Play Store app.

#### PLAY_SERVICES_VERSION_OUTDATED : -7
> Play Services needs to be updated. Ask the user to update Play Services.

#### CLIENT_TRANSIENT_ERROR : -8	
> 	There was a transient error in the client device. Implement a retry strategy with a maximum number of attempts as an exit condition. If the issue still doesn't resolve, ask the user to try again later.

#### APP_NOT_OWNED : -9
> The app was not installed by Google Play. Ask the user to get your app from Google Play.

#### INTERNAL_ERROR : -100
> Unknown internal error. Implement a retry strategy with a maximum number of attempts as an exit condition. If the issue still doesn't resolve, ask the user to try again later. If it fails consistently, contact Google Play Developer support, include Play Age Signals API in the subject, and include as much technical detail as possible (such as a bug report).
