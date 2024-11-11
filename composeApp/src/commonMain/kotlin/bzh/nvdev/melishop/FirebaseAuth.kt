package bzh.nvdev.melishop

import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.FirebaseOptions
import dev.gitlive.firebase.apps
import dev.gitlive.firebase.auth.FirebaseAuth
import dev.gitlive.firebase.auth.auth
import dev.gitlive.firebase.initialize


fun getFirebaseAuth(context: Any): FirebaseAuth {
    val firebaseOptions = FirebaseOptions(
        projectId = "melishop-c250f",
        apiKey = "AIzaSyBBrxZbjELKExsIBlir6R6NinX61y4kLoA",
        authDomain = "melishop-c250f.firebaseapp.com",
        storageBucket = "melishop-c250f.firebasestorage.app",
        gcmSenderId = "676713442914",
        applicationId = "1:676713442914:web:51f88e309baea7abc0ab3b"
    )
    val firebaseApp =
        Firebase.apps(context).firstOrNull()
            ?: Firebase.initialize(context = context, options = firebaseOptions)
    return Firebase.auth(firebaseApp)
}

suspend fun signInAnonymouslyIfNeeded(context: Any): String? {
    val auth = getFirebaseAuth(context)
    return auth.currentUser?.getIdTokenResult(forceRefresh = false)?.token
        ?: auth.signInAnonymously().user?.getIdTokenResult(forceRefresh = true)?.token
}


