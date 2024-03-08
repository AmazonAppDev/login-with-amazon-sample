# Login with Amazon (LwA) example for Kotlin apps
This project is a simple demonstration of [Login with Amazon (LwA) SDK](https://developer.amazon.com/docs/login-with-amazon/documentation-overview.html) implementation on for below devices:
- Amazon Fire tablets
- Amazon Fire TV's
- Non Amazon Devices

## Overview
This sample app demonstrates how to integrate 'Login with Amazon' in an Android application. Users can authenticate using their Amazon credentials and view their profile information within the app.

## Features
- **Amazon OAuth 2.0 Authentication**: Secure login using Amazon credentials.
- **Profile Retrieval**: Fetch and display the user's Amazon profile information post-login.
- **Responsive UI**: Optimized for both smartphones and tablets.

## Prerequisites
To run this app, you'll need to do the following:
- Install the latest version of [Android Studio](https://developer.android.com/studio).
- Sign up for [Amazon Developer account](https://developer.amazon.com/docs/app-submission/manage-account-and-permissions.html#create_account). 
- Download the [Amazon LwA Mobile SDK](https://developer.amazon.com/docs/apps-and-games/sdk-downloads.html#lwa).
- Download the [Login with Amazon button](https://developer.amazon.com/docs/login-with-amazon/button.html).

## Setup Instructions
### Step 1: Clone the Repository
Clone the project to your local machine: 

`git clone https://github.com/AmazonAppDev/login-with-amazon-sample.git`

### Step 2: Amazon Developer Account Setup
1. Sign in to the [Amazon Developer Console](https://developer.amazon.com/).
2. [Create a new security profile](https://developer.amazon.com/docs/login-with-amazon/register-web.html).
3. [Create an API key](https://developer.amazon.com/docs/login-with-amazon/register-android.html#add-android-settings).
4. Store the API key in a new text file named as **api_key.txt** for later use.

### Step 3: Configure the Project
1. Open the project in Android Studio.
2. Go into project view.
3. Navigate to app -> libs.
4. Right click jar file and add as library.
5. Navigate to app -> src -> assets.
6. Add **api_key.txt** file into the folder.
7. Add JAR file in project found in Amazon LwA Mobile SDK downloaded as prerequisite.
8. Extract the zip file libs->login-with-amazon-sdk.jar(copy from amazon LwA Mobile SDK).
9. Add **login-with-amazon-sdk.jar** file into the app -> libs folder in the project.
10. Add Login with Amazon button image into the app -> res -> drawable folder in the project.
11. Rename the image as **btnlwa_gold_loginwithamazon** to successfully load in layout.

### Step 4: Building and Running the App
1. In Android Studio, select 'Build' > 'Make Project'.
2. Choose 'Run' > 'Run app' to launch the app on your device or emulator.

## Usage
After launching the app, tap on the 'Login with Amazon' button. Enter your Amazon credentials, and upon successful authentication, the app will display your Amazon profile name and email.

## Troubleshooting
- **Login Failure**: Ensure your API key and package name in Amazon Developer Console match those in your project.
- **Network Issues**: Verify your internet connection and Amazon service availability.

## Get support
If you found a bug or want to suggest a new [feature/use case/sample], please [file an issue](../../issues).

If you have questions or need help with code, make sure to ask in our Appstore developer community [Q&A category](https://community.amazondeveloper.com/c/amazon-appstore/appstore-questions/20). In addition, we are available at 
- on X at [@AmazonAppDev](https://twitter.com/AmazonAppDev)
- on Stack Overflow at the [amazon-appstore](https://stackoverflow.com/questions/tagged/amazon-appstore) tag

### Stay updated
Get the most up to date Amazon Appstore developer news, product releases, tutorials, and more:

* 📣 Follow [@AmazonAppDev](https://twitter.com/AmazonAppDev)
* 📺 Subscribe to our [Youtube channel](https://www.youtube.com/amazonappstoredevelopers)
* 📧 Sign up for the [Developer Newsletter](https://m.amazonappservices.com/devto-newsletter-subscribe)

## Authors

- [Manu Kakkar](https://github.com/manukakk)