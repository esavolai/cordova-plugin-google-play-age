var exec = require('cordova/exec');

var AgeSignals = {
    checkAgeSignals: function(successCallback, errorCallback) {
        exec(successCallback, errorCallback, 'AgeSignals', 'checkAgeSignals', []);
    }
};

module.exports = AgeSignals;