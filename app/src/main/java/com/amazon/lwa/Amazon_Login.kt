package com.amazon.lwa

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.amazon.identity.auth.device.AuthError
import com.amazon.identity.auth.device.api.Listener
import com.amazon.identity.auth.device.api.authorization.AuthCancellation
import com.amazon.identity.auth.device.api.authorization.AuthorizationManager
import com.amazon.identity.auth.device.api.authorization.AuthorizeListener
import com.amazon.identity.auth.device.api.authorization.AuthorizeRequest
import com.amazon.identity.auth.device.api.authorization.AuthorizeResult
import com.amazon.identity.auth.device.api.authorization.ProfileScope
import com.amazon.identity.auth.device.api.authorization.User
import com.amazon.identity.auth.device.api.workflow.RequestContext

class Amazon_Login : AppCompatActivity() {

    private val TAG: String = Amazon_Login::class.java.getName()

    private lateinit var profileText: TextView
    private lateinit var logoutBtn: Button
    private lateinit var logInProgress: ProgressBar
    private lateinit var requestContext: RequestContext
    private var isLoggedIn = false
    private lateinit var loginButton: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestContext = RequestContext.create(this)
        requestContext.registerListener(object : AuthorizeListener() {
            /* Authorization was completed successfully. */
            override fun onSuccess(authorizeResult: AuthorizeResult) {
                runOnUiThread { // At this point we know the authorization completed, so remove the ability to return to the app to sign-in again
                    setLoggingInState(true)
                }
                fetchUserProfile()
            }

            /* There was an error during the attempt to authorize the application */
            override fun onError(authError: AuthError) {
                Log.e(TAG, "AuthError during authorization", authError)
                runOnUiThread {
                    showAuthToast("Error during authorization.  Please try again.")
                    resetProfileView()
                    setLoggingInState(false)
                }
            }

            /* Authorization was cancelled before it could be completed. */
            override fun onCancel(authCancellation: AuthCancellation) {
                Log.e(TAG, "User cancelled authorization")
                runOnUiThread {
                    showAuthToast("Authorization cancelled")
                    resetProfileView()
                }
            }
        })
        setContentView(R.layout.activity_amazon_login)
        initializeUI()
    }


    override fun onResume() {
        super.onResume()
        requestContext.onResume()
    }

    override fun onStart() {
        super.onStart()
        val scopes = arrayOf(ProfileScope.profile(), ProfileScope.postalCode())
        AuthorizationManager.getToken(this, scopes, object : Listener<AuthorizeResult, AuthError?> {
            override fun onSuccess(result: AuthorizeResult) {
                if (result.accessToken != null) {
                    /* The user is signed in */
                    fetchUserProfile()
                } else {
                    /* The user is not signed in */
                    Log.e(TAG, "User not signed in")
                }
            }

            override fun onError(ae: AuthError?) {
                /* The user is not signed in */
                Log.e(TAG, "User not signed in")
            }
        })
    }


    private fun fetchUserProfile() {
        User.fetch(this, object : Listener<User, AuthError?> {
            /* fetch completed successfully. */
            override fun onSuccess(user: User) {
                val name = user.userName
                val email = user.userEmail
                val account = user.userId
                val zipCode = user.userPostalCode
                val dummyZipCode="11111" // Added to prevent crash if in Amazon profile zip not updated
                runOnUiThread { updateProfileData(name, email, account, dummyZipCode) }
            }

            /* There was an error during the attempt to get the profile. */
            override fun onError(ae: AuthError?) {
                runOnUiThread {
                    setLoggedOutState()
                    val errorMessage = "Error retrieving profile information.\nPlease log in again"
                    val errorToast =
                        Toast.makeText(applicationContext, errorMessage, Toast.LENGTH_LONG)
                    errorToast.setGravity(Gravity.CENTER, 0, 0)
                    errorToast.show()
                }
            }
        })
    }

    private fun updateProfileData(name: String, email: String, account: String, zipCode: String) {
        try {
            val profileBuilder = StringBuilder()
            profileBuilder.append(String.format("Welcome, %s!\n", name))
            profileBuilder.append(String.format("Your email is %s\n", email))
            profileBuilder.append(String.format("Your zipCode is %s\n", zipCode))
            val profile = profileBuilder.toString()
            Log.d(TAG, "Profile Response: $profile")
            runOnUiThread {
                updateProfileView(profile)
                setLoggedInState()
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error updating profile", e)
        }
    }

    /**
     * Initializes all of the UI elements in the activity
     */
    private fun initializeUI() {
        loginButton = findViewById(R.id.login_with_amazon)
        loginButton.setOnClickListener(View.OnClickListener {
            AuthorizationManager.authorize(
                AuthorizeRequest.Builder(requestContext)
                    .addScopes(ProfileScope.profile(), ProfileScope.postalCode())
                    .build()
            )
        })

        // Find the button with the logout ID and set up a click handler
        val logoutButton = findViewById<View>(R.id.logout)
        logoutButton.setOnClickListener {
            AuthorizationManager.signOut(
                applicationContext,
                object : Listener<Void?, AuthError?> {
                    override fun onSuccess(response: Void?) {
                        runOnUiThread { setLoggedOutState() }
                    }

                    override fun onError(authError: AuthError?) {
                        Log.e(TAG, "Error clearing authorization state.", authError)
                    }
                })
        }
        val logoutText = getString(R.string.logout)
        profileText = findViewById<View>(R.id.profile_info) as TextView
        logoutBtn = logoutButton as Button
        logoutBtn.text = logoutText
        logInProgress = findViewById<View>(R.id.log_in_progress) as ProgressBar
    }

    /**
     * Sets the text in the mProfileText [TextView] to the value of the provided String.
     *
     * @param profileInfo the String with which to update the [TextView].
     */
    private fun updateProfileView(profileInfo: String) {
        Log.d(TAG, "Updating profile view")
        profileText!!.text = profileInfo
    }

    /**
     * Sets the text in the mProfileText [TextView] to the prompt it originally displayed.
     */
    private fun resetProfileView() {
        setLoggingInState(false)
        profileText!!.text = getString(R.string.default_message)
    }

    /**
     * Sets the state of the application to reflect that the user is currently authorized.
     */
    private fun setLoggedInState() {
        loginButton!!.visibility = Button.GONE
        setLoggedInButtonsVisibility(Button.VISIBLE)
        isLoggedIn = true
        setLoggingInState(false)
    }

    /**
     * Sets the state of the application to reflect that the user is not currently authorized.
     */
    private fun setLoggedOutState() {
        loginButton!!.visibility = Button.VISIBLE
        setLoggedInButtonsVisibility(Button.GONE)
        isLoggedIn = false
        resetProfileView()
    }

    /**
     * Changes the visibility for both of the buttons that are available during the logged in state
     *
     * @param visibility the visibility to which the buttons should be set
     */
    private fun setLoggedInButtonsVisibility(visibility: Int) {
        logoutBtn!!.visibility = visibility
    }

    /**
     * Turns on/off display elements which indicate that the user is currently in the process of logging in
     *
     * @param loggingIn whether or not the user is currently in the process of logging in
     */
    private fun setLoggingInState(loggingIn: Boolean) {
        if (loggingIn) {
            loginButton!!.visibility = Button.GONE
            setLoggedInButtonsVisibility(Button.GONE)
            logInProgress!!.visibility = ProgressBar.VISIBLE
            profileText!!.visibility = TextView.GONE
        } else {
            if (isLoggedIn) {
                setLoggedInButtonsVisibility(Button.VISIBLE)
            } else {
                loginButton!!.visibility = Button.VISIBLE
            }
            logInProgress!!.visibility = ProgressBar.GONE
            profileText!!.visibility = TextView.VISIBLE
        }
    }

    private fun showAuthToast(authToastMessage: String) {
        val authToast = Toast.makeText(applicationContext, authToastMessage, Toast.LENGTH_LONG)
        authToast.setGravity(Gravity.CENTER, 0, 0)
        authToast.show()
    }
}
