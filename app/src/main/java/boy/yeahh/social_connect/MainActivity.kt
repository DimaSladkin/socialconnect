package boy.yeahh.social_connect

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.NotificationCompat
import android.support.v4.content.ContextCompat
import android.util.Log
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.polidea.rxandroidble.RxBleClient
import com.polidea.rxandroidble.scan.ScanFilter
import com.polidea.rxandroidble.scan.ScanResult
import com.polidea.rxandroidble.scan.ScanSettings
import rx.Subscription
import android.app.NotificationManager
import android.content.Context


class MainActivity : AppCompatActivity() {

    companion object {
        val FILM_CATEGORY = "film"

        val BAG_CATEGORY = "bag"
    }

    var rxBleClient: RxBleClient? = null

    var deviceSubscription: Subscription? = null

    var currentBeaconId: String? = null

    @BindView(R.id.text)
    lateinit var textView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ButterKnife.bind(this)
        rxBleClient = RxBleClient.create(this)
        checkPermission()
        initBleScan()
    }


    private fun initBleScan() {
        deviceSubscription?.unsubscribe()
        deviceSubscription = rxBleClient?.scanBleDevices(ScanSettings.Builder()
                .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
                .setCallbackType(ScanSettings.CALLBACK_TYPE_ALL_MATCHES)
                .build(), ScanFilter.Builder().build())
                ?.doOnSubscribe { }
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
    }

    private fun outputDevice(scanResult: ScanResult) {
        textView.text = scanResult.bleDevice.name + scanResult.rssi + "  " + scanResult.bleDevice.macAddress
        if (scanResult.rssi > -40) {
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
        val notificationBuilder = NotificationCompat.Builder(this)
                .setContentTitle(title)
                .setContentText(description)
                .setSmallIcon(
                        if (category == FILM_CATEGORY) R.drawable.ic_clapperboard
                        else R.drawable.ic_shopping_bag
                )
        val mNotifyMgr = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        mNotifyMgr.notify(43443, notificationBuilder.build())
    }
}
