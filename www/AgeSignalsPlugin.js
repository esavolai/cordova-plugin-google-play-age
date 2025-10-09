var exec = require('cordova/exec');

var AgeSignals = {
    checkAgeSignals: function(successCallback, errorCallback) {
        exec(successCallback, errorCallback, 'AgeSignalsPlugin', 'checkAgeSignals', []);
    }
};

module.exports = AgeSignals;