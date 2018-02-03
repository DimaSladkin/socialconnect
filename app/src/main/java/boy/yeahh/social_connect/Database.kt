package boy.yeahh.social_connect

import android.content.Context
import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import rx.Single
import rx.subjects.PublishSubject

/**
 * Created by dima on 03.02.2018.
 */

data class BeaconModel(var name: String, var description: String, var category: String)

class Database {


    val beaconSubject: PublishSubject<BeaconModel> = PublishSubject.create()

    val dbReference = FirebaseDatabase.getInstance().reference

    fun parseSnapshotToBeaconModel(dataSnapshot: DataSnapshot) =
            BeaconModel(
                    dataSnapshot.child("name").value.toString(),
                    dataSnapshot.child("description").value.toString(),
                    dataSnapshot.child("category").value.toString()
            )

    fun getBeaconInfo(id: String): Single<BeaconModel> {
        return beaconSubject.doOnSubscribe {
            Log.i("onxCheck", "subscribe")
            getBeaconListener(id)
        }.take(1).toSingle()
    }

    fun getBeaconListener(id: String) {
        dbReference.child("beacons").child(id).addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError?) {
                Log.i("onxCheck", p0.toString())
            }

            override fun onDataChange(p0: DataSnapshot?) {
                Log.i("onxCheck", "found db")
                beaconSubject.onNext(parseSnapshotToBeaconModel(p0!!))
            }
        })
    }

}