# cordova-plugin-google-play-age
Google Play Age Signals API
https://developer.android.com/google/play/age-signals/use-age-signals-api

### Response Codes
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
