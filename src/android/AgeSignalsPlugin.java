package io.globules.agesignals;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import androidx.annotation.NonNull;

import com.google.android.play.agesignals.AgeSignalsManager;
import com.google.android.play.agesignals.AgeSignalsManagerFactory;
import com.google.android.play.agesignals.AgeSignalsRequest;
import com.google.android.play.agesignals.model.AgeSignalsVerificationStatus;
import com.google.android.play.agesignals.AgeSignalsResult;

public class AgeSignalsPlugin extends CordovaPlugin {

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if ("checkAgeSignals".equals(action)) {
            checkAgeSignals(callbackContext);
            return true;
        }
        return false;
    }

    private void checkAgeSignals(CallbackContext callbackContext) {
        Context context = this.cordova.getActivity().getApplicationContext();
        AgeSignalsManager manager = AgeSignalsManagerFactory.create(context);

        manager.checkAgeSignals(AgeSignalsRequest.builder().build())
            .addOnSuccessListener(result -> {
                JSONObject response = new JSONObject();
                try {
                    response.put("userStatus", result.userStatus().toString());
                    response.put("ageLower", result.ageLower());
                    response.put("ageUpper", result.ageUpper());
                    response.put("mostRecentApprovalDate", result.mostRecentApprovalDate());
                    response.put("installId", result.installId());
                    callbackContext.success(response);
                } catch (JSONException e) {
                    callbackContext.error("JSON error: " + e.getMessage());
                }
            })
            .addOnFailureListener(throwable -> {
                // Extract error code from throwable if available; default to -100 for internal errors
                int errorCode = throwable.getMessage() != null ? extractErrorCode(throwable.getMessage()) : -100;
                callbackContext.error(String.valueOf(errorCode));
            });
    }

    // Helper to parse error codes (implement based on API docs; extend as needed)
    private int extractErrorCode(String message) {
        // Parse logic for codes like -1 (API_NOT_AVAILABLE), etc.
        // For simplicity, return -100 if not matched; in production, use regex or switch on known strings
        return -100;
    }
}