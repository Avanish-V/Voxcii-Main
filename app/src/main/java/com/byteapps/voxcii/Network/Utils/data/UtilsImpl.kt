package com.byteapps.voxcii.Network.Utils.data

import com.byteapps.voxcii.Network.Utils.domain.UtilsRepository
import com.byteapps.voxcii.Utils.ResultState
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class UtilsImpl(private val database: FirebaseDatabase):UtilsRepository {

    override fun getOnlineUsers(): Flow<ResultState<Int>> {
        return callbackFlow {
            trySend(ResultState.Loading)

            try {

                database.reference.child("ConnectionRoom")
                    .addValueEventListener(object :ValueEventListener{
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if (snapshot.exists()){
                                val onlineUsers = snapshot.childrenCount.toInt()
                                trySend(ResultState.Success(onlineUsers))
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {

                        }

                    })


            }catch (e:Exception){
                trySend(ResultState.Error(e.message.toString()))
            }

            awaitClose { close() }

        }
    }
}