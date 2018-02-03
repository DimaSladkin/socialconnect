package boy.yeahh.social_connect

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

/**
 * Created by Panda Eye on 03.02.2018.
 */
class BootBroadcast:BroadcastReceiver() {
    override fun onReceive(p0: Context?, p1: Intent?) {
        if (p1?.action == "android.intent.action.BOOT_COMPLETED") {
            p0?.startService(Intent(p0, ConnectService::class.java))
        }
    }
}