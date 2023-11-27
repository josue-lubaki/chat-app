package ca.josue_lubaki.chatapp

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import ca.josue_lubaki.chatapp.navigation.NavGraph
import ca.josue_lubaki.chatapp.ui.theme.ChatAppTheme
import ca.josue_lubaki.common.data.datasource.DataStoreOperationsImpl
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            // initialize DataStoreOperations
            val dataStore = DataStoreOperationsImpl(context = this@MainActivity)

            FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w("FCM", "Fetching FCM registration token failed", task.exception)
                    return@OnCompleteListener
                }

                // Get new FCM registration token
                val token = task.result

                runBlocking(Dispatchers.IO) {
                    dataStore.saveFirebaseToken(token = token)
                }
            })

            // get "senderId" into Bundle when user receive a notification
            val senderId = intent.getStringExtra("senderId")

            val navController = rememberNavController()
            val windowSize = calculateWindowSizeClass(activity = this)

            ChatAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavGraph(
                        navController = navController,
                        windowSize = windowSize.widthSizeClass,
                        senderId = senderId
                    )
                }
            }
        }
    }
}
