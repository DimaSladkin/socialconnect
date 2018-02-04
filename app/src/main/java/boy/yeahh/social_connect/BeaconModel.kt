package boy.yeahh.social_connect

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by dima on 03.02.2018.
 */
class BeaconModel(var name: String, var description: String, var category: String) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(description)
        parcel.writeString(category)
    }

    override fun describeContents() = 0

    companion object CREATOR : Parcelable.Creator<BeaconModel> {
        override fun createFromParcel(parcel: Parcel) = BeaconModel(parcel)
        override fun newArray(size: Int) = arrayOfNulls<BeaconModel?>(size)
    }
}