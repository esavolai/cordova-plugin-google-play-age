package io.globules.agesignals;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.PluginResult;
import org.apache.cordova.PluginResult.Status;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Date;

import android.content.Context;
import android.util.Log;
import androidx.annotation.NonNull;

import com.google.android.play.agesignals.testing.FakeAgeSignalsManager;
import com.google.android.play.agesignals.AgeSignalsManager;
import com.google.android.play.agesignals.AgeSignalsManagerFactory;
import com.google.android.play.agesignals.AgeSignalsRequest;
import com.google.android.play.agesignals.AgeSignalsResult;
import com.google.android.play.agesignals.AgeSignalsException;
import com.google.android.play.agesignals.model.AgeSignalsErrorCode;
import com.google.android.play.agesignals.model.AgeSignalsVerificationStatus;
public class AgeSignalsPlugin extends CordovaPlugin {
    private static final String TAG = "AgeSignalsPlugin";
    private CallbackContext context;

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        context = callbackContext;
        if ("checkAgeSignals".equals(action)) {
            String testNumber = args.optString(0, "");
            if (testNumber.isEmpty()) {
                checkAgeSignals();
            } else {
                testAgeSignals(Integer.parseInt(testNumber));
            }
            return true;
        }
        return false;
    }

    private void checkAgeSignals() {
        Context cordovaContext = cordova.getActivity().getApplicationContext();
        AgeSignalsManager manager = AgeSignalsManagerFactory.create(cordovaContext);

        manager.checkAgeSignals(AgeSignalsRequest.builder().build())
            .addOnSuccessListener(result -> {
                onSuccess(result);
            })
            .addOnFailureListener(throwable -> {
                onFailure(throwable);
            });
    }

    private void testAgeSignals(int testNumber) {
        AgeSignalsResult fakeUser;
        FakeAgeSignalsManager manager = new FakeAgeSignalsManager();
        String fakeInstallId = "fake_install_id";

        switch (testNumber) {
            case 1:
                /* 
                        simulated response for a verified adult
                */
                fakeUser = AgeSignalsResult.builder()
                    .setUserStatus(AgeSignalsVerificationStatus.VERIFIED)
                    .build();
                manager.setNextAgeSignalsResult(fakeUser);
                break;

            case 2:
                /* 
                        simulated response for a supervised user between 13 and 17 years old
                */
                fakeUser = AgeSignalsResult.builder()
                    .setUserStatus(AgeSignalsVerificationStatus.SUPERVISED)
                    .setAgeLower(13)
                    .setAgeUpper(17)
                    .setInstallId(fakeInstallId)
                    .build();
                manager.setNextAgeSignalsResult(fakeUser);
                break;

            case 3:
                /* 
                        simulated response for a declared user with a custom age range of 13 to 15
                */
                fakeUser = AgeSignalsResult.builder()
                    .setUserStatus(AgeSignalsVerificationStatus.DECLARED)
                    .setAgeLower(13)
                    .setAgeUpper(15)
                    .setInstallId(fakeInstallId)
                    .build();
                manager.setNextAgeSignalsResult(fakeUser);
                break;

            case 4:
                /*
                        simulated response for a pending significant change approval for a supervised 
                        user between 13 and 17 years old with no previous significant change 
                        having been approved
                */
                fakeUser = AgeSignalsResult.builder()
                    .setUserStatus(AgeSignalsVerificationStatus.SUPERVISED_APPROVAL_PENDING)
                    .setAgeLower(13)
                    .setAgeUpper(17)
                    .setInstallId(fakeInstallId)
                    .build();
                manager.setNextAgeSignalsResult(fakeUser);
                break;

            case 5:
                /*
                        simulated response for a pending significant change approval for a supervised
                        user between 13 and 17 years old with all significant changes approved up to 
                        and including the significant change that was effective from 2025-03-01
                */
                fakeUser = AgeSignalsResult.builder()
                    .setUserStatus(AgeSignalsVerificationStatus.SUPERVISED_APPROVAL_PENDING)
                    .setAgeLower(13)
                    .setAgeUpper(17)
                    .setMostRecentApprovalDate(
                        Date.from(LocalDate.of(2025, 3, 1).atStartOfDay(ZoneOffset.UTC).toInstant())
                    )
                    .setInstallId(fakeInstallId)
                    .build();
                manager.setNextAgeSignalsResult(fakeUser);
                break;

            case 6:
                /*
                        simulated response for a supervised user between 13 and 17 years old with all 
                        significant changes approved up to and including the significant change that 
                        was effective from 2025-02-01
                */
                fakeUser = AgeSignalsResult.builder()
                    .setUserStatus(AgeSignalsVerificationStatus.SUPERVISED_APPROVAL_DENIED)
                    .setAgeLower(13)
                    .setAgeUpper(17)
                    .setMostRecentApprovalDate(
                        Date.from(LocalDate.of(2025, 3, 1).atStartOfDay(ZoneOffset.UTC).toInstant())
                    )
                    .setInstallId(fakeInstallId)
                    .build();
                manager.setNextAgeSignalsResult(fakeUser);
                break;

            case 7:
                /*
                        simulated response for an unknown user status
                */
                fakeUser = AgeSignalsResult.builder()
                    .setUserStatus(AgeSignalsVerificationStatus.UNKNOWN)
                    .build();
                manager.setNextAgeSignalsResult(fakeUser);
                break;

            case 8:
                /*
                        simulated response for a null user status value
                */
                fakeUser = AgeSignalsResult.builder()
                    .setUserStatus(null)
                    .build();
                manager.setNextAgeSignalsResult(fakeUser);
                break;

            case 9:
                /*
                        simulated response for a network error code
                */
                manager.setNextAgeSignalsException(
                    new AgeSignalsException(AgeSignalsErrorCode.NETWORK_ERROR)
                );
                break;


            default:
                break;
            }
            manager.checkAgeSignals(AgeSignalsRequest.builder().build())
                .addOnSuccessListener(result -> {
                    onSuccess(result);
                })
                .addOnFailureListener(throwable -> {
                    onFailure(throwable);
                });

    }

    private int extractErrorCode(Throwable throwable) {
        if (throwable instanceof AgeSignalsException) {
            return ((AgeSignalsException) throwable).getErrorCode();
        }
        return -100;
    }

    private void onSuccess (AgeSignalsResult result) {
        JSONObject response = new JSONObject();
        try {
            Log.d(TAG, "check returned: " + result.toString());
            response.put("userStatus", result.userStatus());
            response.put("ageLower", result.ageLower());
            response.put("ageUpper", result.ageUpper());
            response.put("mostRecentApprovalDate", result.mostRecentApprovalDate());
            response.put("installId", result.installId());
            PluginResult pluginResult = new PluginResult(Status.OK, response);
            pluginResult.setKeepCallback(true);
            context.sendPluginResult(pluginResult);
        } catch (JSONException e) {
            PluginResult pluginResult = new PluginResult(Status.ERROR, e.getMessage());
            pluginResult.setKeepCallback(true);
            context.sendPluginResult(pluginResult);
        }
        return;
    }

    private void onFailure (Throwable throwable) {
        int errorCode = extractErrorCode(throwable);
        PluginResult pluginResult = new PluginResult(Status.ERROR, errorCode);
        pluginResult.setKeepCallback(true);
        context.sendPluginResult(pluginResult);
        return;
    }
}
