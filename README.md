# Valtech Contact Sync Android

This android app syncs contact details for Valtech employees to your phone.
It is connected to Valtech's identity provider (Valtech IDP).

This app also serves to demonstrate how easy it is to integrate with Valtech IDP and its OAuth 2 interface.
It also serves as a public, free and open source (MIT) example of how to write a Contact Provider for Android.

# Local development

1. Download the [Android SDK](http://developer.android.com/sdk/index.html)
2. Install IntelliJ IDEA
3. Create the file `<repo-root>/local.properties` with the contents `sdk.dir=/path/to/android/sdk` 
4. Import the project in IntelliJ
	1. Open IntelliJ
  2. Import project
  3. Select the valtech-contactsync-android folder
  4. Import project from external Model: Gradle
  5. Select "User gradle default wrapper (recommended)"
  6. Finish
5. Add the Android SDK
	1. File -> Project structure -> SDKs
	2. Click +, choose Android SDK
	3. Select path to the Android SDK
	4. Click OK
	5. Go to Project in Project Structure
	6. Choose Android as the Project SDK