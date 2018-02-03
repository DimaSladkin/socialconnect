package boy.yeahh.social_connect

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.os.IBinder
import android.support.v4.app.NotificationCompat
import android.util.Log
import com.polidea.rxandroidble.RxBleClient
import com.polidea.rxandroidble.scan.ScanFilter
import com.polidea.rxandroidble.scan.ScanResult
import com.polidea.rxandroidble.scan.ScanSettings
import rx.Subscription

/**
 * Created by Panda Eye on 03.02.2018.
 */
class ConnectService : Service() {
    companion object {
        val FILM_CATEGORY = "film"

        val BAG_CATEGORY = "bag"
    }

    var rxBleClient: RxBleClient? = null

    var deviceSubscription: Subscription? = null

    var currentBeaconId: String? = null

    var mNotifyMgr: NotificationManager? = null

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    private fun initBleScan() {
        deviceSubscription?.unsubscribe()
        deviceSubscription = rxBleClient?.scanBleDevices(ScanSettings.Builder()
                .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
                .setCallbackType(ScanSettings.CALLBACK_TYPE_ALL_MATCHES)
                .build(), ScanFilter.Builder().build())
                ?.doOnSubscribe { Log.d("onXscan", "lol") }
                ?.subscribe(
                        {
                            Log.i("onxScan", it.toString())
                            if (it.bleDevice.name == "FIND-E") {
                                outputDevice(it)
                                val extra =
                                        if (it.bleDevice.macAddress == "65:B2:02:10:01:61")
                                            MainActivity.DISTANCE_EXTRA
                                        else
                                            MainActivity.DISTANCE_SECOND_EXTRA
                                sendDistanceBroadcast(it.rssi.toString(), extra)
                            }
                        },
                        {
                            Log.i("onxScanErr", it.toString())
                        }
                )
        Log.d("onXscan", "deviceSubscription is $deviceSubscription")
    }

    private fun outputDevice(scanResult: ScanResult) {
        //textView.text = scanResult.bleDevice.name + scanResult.rssi + "  " + scanResult.bleDevice.macAddress
        if (scanResult.rssi > -70) {
            Log.i("onxCheck", " > -40")
            if (currentBeaconId != scanResult.bleDevice.macAddress) {
                Log.i("onxCheck", "found new")
                triggerBeaconInfo(scanResult.bleDevice.macAddress)
            }
            currentBeaconId = scanResult.bleDevice.macAddress
        }

    }

    private fun triggerBeaconInfo(beaconId: String) {
        Database().getBeaconInfo(beaconId)
                .subscribe(
                        {
                            Log.i("onxCheck", "get beacon model")
                            showNotification(it.name, it.description, it.category)
                        },
                        {}
                )

    }

    private fun showNotification(title: String, description: String, category: String) {
        val ii = Intent(applicationContext, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(applicationContext, 0, ii, 0)
        val alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val notificationBuilder = NotificationCompat.Builder(this, "notify-001")
                .setContentTitle(title)
                .setContentText(description)
                .setContentIntent(pendingIntent)
                .setSound(alarmSound)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setSmallIcon(
                        if (category == FILM_CATEGORY) R.drawable.ic_clapperboard
                        else R.drawable.ic_shopping_bag
                )
        mNotifyMgr?.notify(43443, notificationBuilder.build())
    }

    override fun onCreate() {
        super.onCreate()
        //ButterKnife.bind(this)
        rxBleClient = RxBleClient.create(this)
        initBleScan()

        mNotifyMgr = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mNotifyMgr?.createNotificationChannel(NotificationChannel("notify_001",
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT))
        }
    }

    private fun sendDistanceBroadcast(distance: String, extra: String) {
        val intent = Intent(MainActivity.DISTANCE_ACTION)
        intent.putExtra(extra, distance)
        sendBroadcast(intent)
    }
}