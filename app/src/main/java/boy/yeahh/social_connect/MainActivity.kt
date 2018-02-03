package boy.yeahh.social_connect

/*import android.Manifest
import android.app.*
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.RingtoneManager
import android.os.Build*/
import android.os.Bundle
/*import android.support.v4.app.ActivityCompat
import android.support.v4.app.NotificationCompat
import android.support.v4.content.ContextCompat*/
import android.support.v7.app.AppCompatActivity
/*import android.util.Log
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.polidea.rxandroidble.RxBleClient
import com.polidea.rxandroidble.scan.ScanFilter
import com.polidea.rxandroidble.scan.ScanResult
import com.polidea.rxandroidble.scan.ScanSettings
import rx.Subscription*/


class MainActivity : AppCompatActivity() {

    /*companion object {
        val FILM_CATEGORY = "film"

        val BAG_CATEGORY = "bag"
    }

    var rxBleClient: RxBleClient? = null

    var deviceSubscription: Subscription? = null

    var currentBeaconId: String? = null

    var mNotifyMgr: NotificationManager? = null

    @BindView(R.id.text)
    lateinit var textView: TextView*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        /*ButterKnife.bind(this)
        rxBleClient = RxBleClient.create(this)
        checkPermission()
        initBleScan()

        mNotifyMgr = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mNotifyMgr?.createNotificationChannel(NotificationChannel("notify_001",
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT))
        }*/
    }


    /*private fun initBleScan() {
        deviceSubscription?.unsubscribe()
        deviceSubscription = rxBleClient?.scanBleDevices(ScanSettings.Builder()
                .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
                .setCallbackType(ScanSettings.CALLBACK_TYPE_ALL_MATCHES)
                .build(), ScanFilter.Builder().build())
                ?.doOnSubscribe { Log.d("onXscan", "lol") }
                ?.subscribe(
                        {
                            Log.i("onxScan", it.toString())
                            if (it.bleDevice.name == "FIND-E")
                                outputDevice(it)
                        },
                        {
                            Log.i("onxScanErr", it.toString())
                        }
                )
        Log.d("onXscan", "deviceSubscription is $deviceSubscription")
    }

    private fun outputDevice(scanResult: ScanResult) {
        textView.text = scanResult.bleDevice.name + scanResult.rssi + "  " + scanResult.bleDevice.macAddress
        if (scanResult.rssi > -50) {
            Log.i("onxCheck", " > -40")
            if (currentBeaconId != scanResult.bleDevice.macAddress) {
                Log.i("onxCheck", "found new")
                triggerBeaconInfo(scanResult.bleDevice.macAddress)
            }
            currentBeaconId = scanResult.bleDevice.macAddress
        }

    }

    private fun checkPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION)) {

            } else {
                ActivityCompat.requestPermissions(this,
                        arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                        1)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            initBleScan()
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
                .setPriority(Notification.PRIORITY_MAX)
                .setSmallIcon(
                        if (category == FILM_CATEGORY) R.drawable.ic_clapperboard
                        else R.drawable.ic_shopping_bag
                )
        mNotifyMgr?.notify(43443, notificationBuilder.build())
    }*/
}
