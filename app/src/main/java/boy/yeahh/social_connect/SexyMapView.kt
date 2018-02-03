package boy.yeahh.social_connect

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.support.constraint.ConstraintLayout
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import kotlin.math.roundToInt

/**
 * Created by Vova Lantsov on 03.02.2018.
 */
class SexyMapView(context: Context): ConstraintLayout(context) {
    private val imageView: ImageView = findViewById(R.id.sexy_image)
    val points = ArrayList<Point>()

    fun image(resourceId: Int) = imageView.setBackgroundResource(resourceId)

    fun add(pointId: Int, xInPercents: Int, yInPercents: Int) {
        if (points.find { it.pointId == pointId } == null) {
            val p = Point(context, pointId)
            p.setBackgroundResource(R.drawable.round_view_kisses_you)
            p.layoutParams = ViewGroup.LayoutParams(((x / 100) * xInPercents).roundToInt(), ((y / 100) * yInPercents).roundToInt())
            points.add(p)
            addView(p)
        }
    }

    fun removeAll() {
        points.forEach { removeView(it) }
        points.clear()
    }

    fun remove(pointId: Int): Boolean {
        val p = points.find { it.pointId == pointId }
        return if (p != null) {
            removeView(p)
            points.remove(p)
            true
        } else false
    }

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