package ca.josue_lubaki.chatapp

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import ca.josue_lubaki.chatapp.navigation.NavGraph
import ca.josue_lubaki.chatapp.ui.theme.ChatAppTheme
import ca.josue_lubaki.chatapp.service.FirebaseService
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            // get "route" into Bundle
            val route = intent.getStringExtra("route")
            Log.d("xxxx", "route on MainActivity : $route")

            val navController = rememberNavController()
            val windowSize = calculateWindowSizeClass(activity = this)

            FirebaseService.sharedPref = getSharedPreferences("sharedPref", Context.MODE_PRIVATE)

            ChatAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    NavGraph(
                        navController = navController,
                        windowSize = windowSize.widthSizeClass,
                        route = route
                    )
                }
            }
        }
    }
}
