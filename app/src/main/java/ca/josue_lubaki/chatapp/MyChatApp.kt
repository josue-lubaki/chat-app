package ca.josue_lubaki.chatapp

import android.app.Application
import ca.josue_lubaki.chat.di.chatModules
import ca.josue_lubaki.common.di.commonModules
import ca.josue_lubaki.di.profileModules
import ca.josue_lubaki.users.di.dashboardModules
import ca.josue_lubaki.login.di.loginModules
import ca.josue_lubaki.register.di.registerModules
import com.google.firebase.Firebase
import com.google.firebase.initialize
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

/**
 * created by Josue Lubaki
 * date : 2023-10-18
 * version : 1.0.0
 */

class MyChatApp : Application() {

        override fun onCreate() {
            super.onCreate()

            Firebase.initialize(this)

            // modules
            startKoin {
                androidContext(this@MyChatApp)
                modules(
                    commonModules +
                    registerModules +
                    loginModules +
                    dashboardModules +
                    profileModules +
                    chatModules
                )
            }
        }
}
