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
        const val FILM_CATEGORY = "Cinema"
    }

    private var rxBleClient: RxBleClient? = null

    private var deviceSubscription: Subscription? = null

    private var currentBeaconId: String? = null

    private var mNotifyMgr: NotificationManager? = null

    override fun onBind(p0: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int) = START_STICKY

    private fun initBleScan() {
        deviceSubscription?.unsubscribe()
        deviceSubscription = rxBleClient?.scanBleDevices(ScanSettings.Builder()
                .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
                .setCallbackType(ScanSettings.CALLBACK_TYPE_ALL_MATCHES)
                .build(), ScanFilter.Builder().build())
                ?.doOnSubscribe { Log.d("onxScan", "lol") }
                ?.subscribe(
                        {
                            Log.i("onxScan", it.toString())
                            if (it.bleDevice.name == "FIND-E") {
                                outputDevice(it)
                            }
                        },
                        {
                            Log.i("onxScanErr", it.toString())
                        }
                )
        Log.d("onXscan", "deviceSubscription is $deviceSubscription")
    }

    private fun outputDevice(scanResult: ScanResult) {
        if (scanResult.rssi > -70) {
            Log.i("onxCheck", " > -40")
            if (currentBeaconId != scanResult.bleDevice.macAddress) {
                Log.i("onxCheck", "found new")
                triggerBeaconInfo(scanResult.bleDevice.macAddress)
                val extra =
                        if (scanResult.bleDevice.macAddress == "65:B2:02:10:01:61")
                            MainActivity.DISTANCE_EXTRA
                        else
                            MainActivity.DISTANCE_SECOND_EXTRA
                sendDistanceBroadcast(scanResult.rssi.toString(), extra)
            }
            currentBeaconId = scanResult.bleDevice.macAddress
        }

    }

    private fun triggerBeaconInfo(beaconId: String) {
        Database().getBeaconInfo(beaconId)
                .subscribe(
                        {
                            Log.i("onxCheck", "get beacon model")
                            showNotification(it)
                        },
                        {}
                )

    }

    private fun showNotification(beaconModel: BeaconModel) {
        val ii = Intent(applicationContext, ShopInfoActivity::class.java)
        ii.addCategory("2")
        ii.putExtra(MainActivity.BEACON_ACTION, MainActivity.BEACON_ACTION)
        ii.putParcelableArrayListExtra(MainActivity.BEACON_EXTRA, arrayListOf(beaconModel))
        ii.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        val pendingIntent = PendingIntent.getActivity(applicationContext, 0, ii, PendingIntent.FLAG_UPDATE_CURRENT)
        val alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val notificationBuilder = NotificationCompat.Builder(this, "notify-001")
                .setContentTitle(beaconModel.name)
                .setContentText(beaconModel.description)
                .setContentIntent(pendingIntent)
                .setSound(alarmSound)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setSmallIcon(
                        if (beaconModel.category == FILM_CATEGORY) R.drawable.ic_clapperboard
                        else R.drawable.ic_shopping_bag
                )
        mNotifyMgr?.notify(43443, notificationBuilder.build())
    }

    override fun onCreate() {
        super.onCreate()
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
        Log.i("onxService", "send")
        val intent = Intent(MainActivity.DISTANCE_ACTION)
        intent.putExtra(extra, distance)
        sendBroadcast(intent)
    }
}