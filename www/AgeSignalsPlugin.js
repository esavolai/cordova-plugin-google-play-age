var exec = require("cordova/exec");

var AgeSignals = {
    checkAgeSignals: function(testNumber, successCallback, errorCallback) {
        exec(successCallback, errorCallback, "AgeSignalsPlugin", "checkAgeSignals", [ testNumber ]);
    },
};

module.exports = AgeSignals;