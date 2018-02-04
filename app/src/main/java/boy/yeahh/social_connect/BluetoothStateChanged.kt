package boy.yeahh.social_connect

import android.bluetooth.BluetoothAdapter
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

/**
 * Created by Panda Eye on 03.02.2018.
 */
class BluetoothStateChanged: BroadcastReceiver() {
    override fun onReceive(p0: Context?, p1: Intent?) {
        if (p1?.action == "android.bluetooth.adapter.action.STATE_CHANGED") {
            if(p1.getIntExtra(BluetoothAdapter.EXTRA_STATE, -1) == BluetoothAdapter.STATE_OFF)
                p0?.stopService(Intent(p0, ConnectService::class.java))
            else p0?.startService(Intent(p0, ConnectService::class.java))
        }
    }
}