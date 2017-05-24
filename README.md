# ArduinoGlassAssistant
Android app to interface an Android device and a google glass inspired device powered by an Arduino.

Features:
* Catch any notifications received on the device and tranmit as string via bluetooth
* Basic navigation - transmit the distance and compass bearing of the user to the specified destination
* Send a plain text string

Hardware schematics are in the documentation file.

**Replace the value of 'private final String address' in MainActivity.java with your own HC-05 module's MAC address.**

Note: Notifications may not be received unless you go to Device Settings -> Privacy -> Notification access (or similar) and revoke and enable permissions for the app. You may have to do this every time you run the app. This is known bug in Android itself.
