package boy.yeahh.social_connect

import android.Manifest
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.content.BroadcastReceiver
import android.content.Context

import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager

import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.content.LocalBroadcastManager
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import butterknife.BindView
import butterknife.ButterKnife


class MainActivity : AppCompatActivity() {

    companion object {
        val FILM_CATEGORY = "film"

        val BAG_CATEGORY = "bag"

        val DISTANCE_EXTRA = "distance_extra"

        val DISTANCE_ACTION = "distance_action"

        val DISTANCE_SECOND_EXTRA = "distance_second_extra"

        val BEACON_EXTRA = "beacon_extra"

        val BEACON_ACTION = "beacon_action"
    }

//    var rxBleClient: RxBleClient? = null
//
//    var deviceSubscription: Subscription? = null
//
//    var currentBeaconId: String? = null
//
//    var mNotifyMgr: NotificationManager? = null

//    @BindView(R.id.text1_dist)
//    lateinit var textFirst: TextView
//
//    @BindView(R.id.text2_dist)
//    lateinit var textSec: TextView

    @BindView(R.id.main_img)
    lateinit var mainImage: ImageView

    private var broadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(p0: Context?, p1: Intent?) {
            Log.i("onxService", "receive")
            val str = p1?.getStringExtra(DISTANCE_EXTRA)
            val str2 = p1?.getStringExtra(DISTANCE_SECOND_EXTRA)
            if (str != null) mainImage.setImageResource(R.drawable.banneren)
            if (str2 != null) mainImage.setImageResource(R.drawable.pull_and_bear_shop)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ButterKnife.bind(this)
        checkPermission()

        registerReceiver(broadcastReceiver, IntentFilter(DISTANCE_ACTION))

        runService()
    }

    override fun onDestroy() {
        LocalBroadcastManager.getInstance(this@MainActivity).unregisterReceiver(broadcastReceiver)
        super.onDestroy()
    }


//    private fun initBleScan() {
//        deviceSubscription?.unsubscribe()
//        deviceSubscription = rxBleClient?.scanBleDevices(ScanSettings.Builder()
//                .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
//                .setCallbackType(ScanSettings.CALLBACK_TYPE_ALL_MATCHES)
//                .build(), ScanFilter.Builder().build())
//                ?.doOnSubscribe { Log.d("onXscan", "lol") }
//                ?.subscribe(
//                        {
//                            Log.i("onxScan", it.toString())
//                            if (it.bleDevice.name == "FIND-E")
//                                outputDevice(it)
//                        },
//                        {
//                            Log.i("onxScanErr", it.toString())
//                        }
//                )
//        Log.d("onXscan", "deviceSubscription is $deviceSubscription")
//    }
//
//    private fun outputDevice(scanResult: ScanResult) {
//        textView.text = scanResult.bleDevice.name + scanResult.rssi + "  " + scanResult.bleDevice.macAddress
//        if (scanResult.rssi > -50) {
//            Log.i("onxCheck", " > -40")
//            if (currentBeaconId != scanResult.bleDevice.macAddress) {
//                Log.i("onxCheck", "found new")
//                triggerBeaconInfo(scanResult.bleDevice.macAddress)
//            }
//            currentBeaconId = scanResult.bleDevice.macAddress
//        }
//
//    }

    private fun runService() {
        if (BluetoothAdapter.getDefaultAdapter().isEnabled) {
            startService(Intent(this@MainActivity, ConnectService::class.java))
            Toast.makeText(this@MainActivity, "Service started", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(this@MainActivity, "Bluetooth disabled, service is offline", Toast.LENGTH_LONG).show()
        }
    }

    private fun checkPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            runService()
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION)) {
                runService()
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
            runService()
        }
    }
//
//    private fun triggerBeaconInfo(beaconId: String) {
//        Database().getBeaconInfo(beaconId)
//                .subscribe(
//                        {
//                            Log.i("onxCheck", "get beacon model")
//                            showNotification(it.name, it.description, it.category)
//                        },
//                        {}
//                )
//
//    }
//
//    private fun showNotification(title: String, description: String, category: String) {
//        val ii = Intent(applicationContext, MainActivity::class.java)
//        val pendingIntent = PendingIntent.getActivity(applicationContext, 0, ii, 0)
//        val defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
//        val yeahBoy = Uri.parse("android.resource://"
//                + applicationContext.packageName + "/" + R.raw.yeah_boy)
//
//        val notificationBuilder = NotificationCompat.Builder(this, "notify-001")
//                .setContentTitle(title)
//                .setContentText(description)
//                .setContentIntent(pendingIntent)
//                .setSound(
//                        if (category == FILM_CATEGORY) defaultSound
//                        else yeahBoy
//                )
//                .setPriority(Notification.PRIORITY_MAX)
//                .setSmallIcon(
//                        if (category == FILM_CATEGORY) R.drawable.ic_clapperboard
//                        else R.drawable.ic_shopping_bag
//                )
//        mNotifyMgr?.notify(43443, notificationBuilder.build())
//    }
}
