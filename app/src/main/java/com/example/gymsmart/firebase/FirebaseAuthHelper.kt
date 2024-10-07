import android.app.Activity
import android.content.Intent
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.navigation.NavController
import com.example.gymsmart.R
import com.example.gymsmart.firebase.UserSession
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

public class FirebaseAuthHelper(
    private val activity: Activity,
    private val navController: NavController,
    private val signInLauncher: ActivityResultLauncher<Intent>
) {

    private lateinit var googleSignInClient: GoogleSignInClient
    private var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    init {
        // Configure Google Sign-In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(activity.getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(activity, gso)
    }
    fun handleSignInResult(data: Intent?) {
        val task = GoogleSignIn.getSignedInAccountFromIntent(data)
        try {
            val account = task.getResult(ApiException::class.java)!!
            Log.d("FirebaseAuthHelper", "firebaseAuthWithGoogle:" + account.id)
            firebaseAuthWithGoogle(account.idToken!!)
        } catch (e: ApiException) {
            Log.w("FirebaseAuthHelper", "Google sign in failed", e)
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener(activity) { task ->
                if (task.isSuccessful) {
                    // Sign in success
                    Log.d("FirebaseAuthHelper", "signInWithCredential:success")
                    val user = firebaseAuth.currentUser
                    user?.let {
                        // Set user data in the singleton for global access
                        UserSession.setUserData(it.email, it.displayName, it.photoUrl.toString())

                        // Navigate to the next screen after successful login
                        navController.navigate("home") // Assuming 'home' is the destination
                    }
                } else {
                    Log.w("FirebaseAuthHelper", "signInWithCredential:failure", task.exception)
                }
            }
    }

    fun signOut() {
        googleSignInClient.signOut().addOnCompleteListener {
            Log.d("FirebaseAuthHelper", "User signed out")
        }
    }
    fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        signInLauncher.launch(signInIntent)
    }
}
