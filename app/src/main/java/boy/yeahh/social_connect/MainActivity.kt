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
import android.widget.Toast
import butterknife.BindView
import butterknife.ButterKnife


class MainActivity : AppCompatActivity() {

    companion object {
        const val DISTANCE_EXTRA = "distance_extra"
        const val DISTANCE_ACTION = "distance_action"
        const val DISTANCE_SECOND_EXTRA = "distance_second_extra"
        const val BEACON_EXTRA = "beacon_extra"
        const val BEACON_ACTION = "beacon_action"
    }

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

        LocalBroadcastManager.getInstance(this@MainActivity).registerReceiver(broadcastReceiver, IntentFilter(DISTANCE_ACTION))

        runService()
    }

    override fun onDestroy() {
        LocalBroadcastManager.getInstance(this@MainActivity).unregisterReceiver(broadcastReceiver)
        super.onDestroy()
    }

    private fun runService() {
        if (BluetoothAdapter.getDefaultAdapter().isEnabled) {
            startService(Intent(this@MainActivity, ConnectService::class.java))
            Toast.makeText(this@MainActivity, R.string.service_started, Toast.LENGTH_LONG).show()
        } else Toast.makeText(this@MainActivity, R.string.service_disabled, Toast.LENGTH_LONG).show()
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
}
