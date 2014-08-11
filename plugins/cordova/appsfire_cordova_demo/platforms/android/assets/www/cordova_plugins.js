cordova.define('cordova/plugin_list', function(require, exports, module) {
module.exports = [
    {
        "file": "plugins/com.appsfire.cordova/www/appsfire.js",
        "id": "com.appsfire.cordova.AppsfireSDK",
        "clobbers": [
            "AppsfireSDK"
        ]
    }
];
module.exports.metadata = 
// TOP OF METADATA
{
    "com.appsfire.cordova": "1.0.4"
}
// BOTTOM OF METADATA
});