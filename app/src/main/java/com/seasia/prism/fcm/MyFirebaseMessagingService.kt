package com.seasia.prism.fcm


import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.seasia.prism.App
import com.seasia.prism.MainActivity
import com.seasia.prism.R
import com.seasia.prism.util.PrefStore
import com.seasia.prism.util.PreferenceKeys.DEVECE_TOKEN
import org.json.JSONObject


class MyFirebaseMessagingService : FirebaseMessagingService() {
    var title: String? = null
    var message: String? = null
    var notification_code: String? = null
    var CHANNEL_ID = "" // The id of the channel.
    var CHANNEL_ONE_NAME = "Channel One"
    var notificationManager: NotificationManager? = null
    var notificationChannel: NotificationChannel? = null
    var notification: Notification? = null
    override fun onNewToken(refreshedToken: String) {
        super.onNewToken(refreshedToken)
        Log.d(TAG, "Refreshed token: $refreshedToken")
        sendRegistrationToServer(refreshedToken)
    }

    private fun sendRegistrationToServer(token: String) {
        //  AppController.getInstance().setDeviceToken(SyncStateContbract.Constants.DEVICE_TOKEN, token)
        var sharedPref = PrefStore(App.app)
        sharedPref.save(DEVECE_TOKEN,token)
        Log.e("device_token", token)
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        manager
        CHANNEL_ID = applicationContext.packageName
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel = NotificationChannel(CHANNEL_ID, CHANNEL_ONE_NAME, NotificationManager.IMPORTANCE_DEFAULT)
            notificationChannel!!.enableLights(true)
            notificationChannel!!.lightColor = Color.RED
            notificationChannel!!.setShowBadge(true)
            notificationChannel!!.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
        }
        Log.e(TAG, "Notification Message Body: " + remoteMessage.data)
        message = remoteMessage.data.get("message")
        title = remoteMessage.data.get("title")
        try {
            sendMessagePush(message!!);
        }catch (e:Exception){
            e.printStackTrace()
        }
    }

//    private fun popupExtraWorkOrder(id: String, orderId: String, extraTitle: String, extraDescription: String, imageVideoModels: ArrayList<ImageVideoModel2>) {
//        val mainHandler = Handler(applicationContext.mainLooper)
//        val myRunnable = Runnable {
//            val handler = Handler(Looper.getMainLooper())
//            handler.post {
//                val intent = Intent(this@MyFirebaseMessagingService, HomeActivity::class.java)
//                intent.putExtra("id", id)
//                intent.putExtra("orderId", orderId)
//                intent.putExtra("extraTitle", extraTitle)
//                intent.putExtra("extraDescription", extraDescription)
//                intent.putParcelableArrayListExtra("imgArray", imageVideoModels)
//                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
//                startActivity(intent)
//            }
//        }
//        mainHandler.post(myRunnable)
//    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private fun sendMessagePush(message: String) {
        var intent: Intent? = null

        intent=Intent(applicationContext,MainActivity::class.java)
//        intent!!.putExtra("goto_to_notification_screen", "getnotification")
//        intent!!.putExtra("message", message)
        intent!!.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)
        val icon1 = BitmapFactory.decodeResource(resources, R.mipmap.ic_icon)
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = Notification.Builder(applicationContext)
                .setSmallIcon(notificationIcon).setLargeIcon(icon1)
                .setStyle(Notification.BigTextStyle().bigText(message))
                //   .setColor(ContextCompat.getColor(applicationContext, R.color.pre))
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationBuilder.setChannelId(CHANNEL_ID)
            notificationManager!!.createNotificationChannel(notificationChannel!!)
        }
        notification = notificationBuilder.build()
        notificationManager!!.notify(i++, notification)
    }

    private val manager: NotificationManager?
         get() {
            if (notificationManager == null) {
                notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            }
            return notificationManager
        }

    private val notificationIcon: Int
         get() {
            val useWhiteIcon = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
            return if (useWhiteIcon) R.mipmap.ic_icon else R.mipmap.ic_icon
        }

    companion object {
        private const val TAG = "MyFirebaseMsgService"
        private var i = 0
    }
}