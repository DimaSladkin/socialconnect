package boy.yeahh.social_connect

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.support.constraint.ConstraintLayout
import android.view.View
import android.widget.ImageView

/**
 * Created by Vova Lantsov on 03.02.2018.
 */
class CustomMapView(context: Context): ConstraintLayout(context) {
    private val imageView: ImageView = findViewById(R.id.map_image)
    private val points = ArrayList<Point>()

    fun color(pointId: Int, color: Int): Boolean {
        val p = points.find { it.pointId == pointId }
        return if (p != null) {
            p.background.colorFilter = PorterDuffColorFilter(color, PorterDuff.Mode.CLEAR)
            true
        } else false
    }

    @SuppressLint("ViewConstructor")
    class Point(context: Context, val pointId: Int) : View(context)
}