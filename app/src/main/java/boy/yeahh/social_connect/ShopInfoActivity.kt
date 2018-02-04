package boy.yeahh.social_connect

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.AppCompatTextView
import butterknife.BindView
import butterknife.ButterKnife

/**
 * Created by dima on 04.02.2018.
 */
class ShopInfoActivity: AppCompatActivity() {

    @BindView(R.id.shop_name_tv)
    private lateinit var shopNameTv: AppCompatTextView

    @BindView(R.id.shop_cat_tv)
    private lateinit var shopCatTv: AppCompatTextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.shop_info_layout)
        ButterKnife.bind(this)

        if (intent.hasExtra(MainActivity.BEACON_ACTION)) {
            val beaconModel = intent.getParcelableArrayListExtra<BeaconModel>(MainActivity.BEACON_EXTRA)[0]
            setInfo(beaconModel)
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        if (intent!!.hasExtra(MainActivity.BEACON_ACTION)) {
            val beaconModel = intent.getParcelableArrayListExtra<BeaconModel>(MainActivity.BEACON_EXTRA)[0]
            setInfo(beaconModel)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(applicationContext, MainActivity::class.java))
        finish()
    }

    private fun setInfo(beaconModel: BeaconModel) {
        shopNameTv.text = beaconModel.name
        shopCatTv.text = beaconModel.category
    }
}