package com.byteapps.voxcii.Agora


import android.content.Context
import android.media.AudioManager
import android.util.Log
import com.byteapps.voxcii.Agora.media.ConnectionStatus
import com.byteapps.voxcii.Agora.media.RtcConnectionStatus
import com.byteapps.voxcii.Agora.media.RtcTokenBuilder2
import com.byteapps.voxcii.Agora.media.joiningParameters
import com.byteapps.voxcii.Utils.ResultState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.snapshot.StringNode
import io.agora.rtc2.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class AgoraRepoImpl(private val context: Context) : AgoraRepository {

    private var mRtcEngine: RtcEngine? = null
    var channelId:String = ""
    private val auth = FirebaseAuth.getInstance()
    private val database: FirebaseDatabase by lazy { FirebaseDatabase.getInstance() }

    override fun rtcEngineInit(): Flow<ResultState<RtcConnectionStatus>> = callbackFlow {
        trySend(ResultState.Loading) // Send loading state at the beginning

        if (mRtcEngine != null) {
            Log.d("AgoraRepoImpl", "RtcEngine already initialized")
            // Do NOT return early! Just send the current state

        }

        val iRtcEngineEventHandler = object : IRtcEngineEventHandler() {
            override fun onJoinChannelSuccess(channel: String, uid: Int, elapsed: Int) {
                Log.d("AgoraRepoImpl", "Joined Channel: $channel, UID: $uid")
                channelId = channel
                mRtcEngine?.muteRemoteAudioStream(uid, false)
            }

            override fun onUserJoined(uid: Int, elapsed: Int) {

                Log.d("AgoraRepoImpl", "User Joined: UID - $uid")
                mRtcEngine?.muteRemoteAudioStream(uid, false)
                mRtcEngine?.setEnableSpeakerphone(true)

                trySend(ResultState.Success(
                    RtcConnectionStatus(
                        status = ConnectionStatus.CHANNEL_JOINED,
                        channelId = channelId
                    )
                ))
            }

            override fun onUserOffline(uid: Int, reason: Int) {
                Log.d("AgoraRepoImpl", "User Offline: UID - $uid")
                trySend(ResultState.Success(
                    RtcConnectionStatus(
                        status = ConnectionStatus.IS_USER_OFFLINE,
                        channelId = channelId
                    )
                ))
            }

            override fun onLeaveChannel(stats: RtcStats?) {
                Log.d("AgoraRepoImpl", "Leaved Channel ${stats?.users}")
                //trySend(ResultState.Success(RtcConnectionStatus.CHANNEL_LEAVED)) // 🔥 Keeps sending updates



                //database.getReference("ConnectionRoom").child(auth.uid!!).removeValue()
            }

            override fun onError(err: Int) {
                Log.e("AgoraRepoImpl", "Agora Error: $err - ${getAgoraErrorDescription(err)}")
                trySend(ResultState.Error("Agora Error: $err"))
            }


        }

        // ✅ Ensures the SDK is set up properly
        setUpSDKEngine(iRtcEngineEventHandler)

        if (mRtcEngine == null) {
            Log.e("AgoraRepoImpl", "RtcEngine is not initialized!")
            trySend(ResultState.Error("RtcEngine Initialization Failed"))
        } else {
            Log.d("AgoraRepoImpl", "RtcEngine created successfully")
        }

        // ✅ Keep the Flow alive so it continues emitting updates
        awaitClose {
            Log.d("AgoraRepoImpl", "Closing RTC Flow")
        }
    }

    private fun setUpSDKEngine(iRtcEngineEventHandler: IRtcEngineEventHandler) {

        val config = RtcEngineConfig()
        config.mContext = context
        config.mAppId = "8be7292d7bb44b46b541bc72316ebc5a"
        config.mEventHandler = iRtcEngineEventHandler
        mRtcEngine = RtcEngine.create(config)

        mRtcEngine?.enableAudio()
        mRtcEngine?.enableLocalAudio(true)
        mRtcEngine?.muteLocalAudioStream(false)
        mRtcEngine?.muteAllRemoteAudioStreams(false)
        mRtcEngine?.setDefaultAudioRoutetoSpeakerphone(true)
        mRtcEngine?.setEnableSpeakerphone(true)
    }

    override  fun joinChannel(channelId: String, token: String, uid: Int) {

        val options = ChannelMediaOptions().apply {
            clientRoleType = Constants.CLIENT_ROLE_BROADCASTER
            channelProfile = Constants.CHANNEL_PROFILE_LIVE_BROADCASTING
            publishMicrophoneTrack = true
            autoSubscribeAudio = true
        }

        mRtcEngine?.joinChannel(token, channelId, uid, options)
    }

    override fun startSignaling(onRoomFound: (joiningParameters) -> Unit,filterList: List<String>) {


        database.reference.child("ConnectionRoom")
            .orderByChild("isAvailable").equalTo(true)
            .limitToFirst(10)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {

                        val roomKey = snapshot.children.first().key ?: return
                        val matchedUserRef = database.reference.child("ConnectionRoom").child(roomKey)

                        matchedUserRef.child("joinedUser").setValue(auth.uid)
                        matchedUserRef.child("isAvailable").setValue(false)

                        val uid = generateDigitRandom()
                        val token : String = generateDynamicToken(roomKey,uid)

                       if (!snapshot.hasChild("filters")){
                           onRoomFound(
                               joiningParameters(
                                   channelName = roomKey,
                                   joinedUser = auth.uid,
                                   uid = uid,
                                   token = token
                               )
                           )
                       }else if(snapshot.hasChild("filters")){

                           for (room in snapshot.children) {
                               val roomKey = room.key ?: continue
                               val roomFilters = room.child("filters").getValue(object : GenericTypeIndicator<List<String>>() {}) ?: emptyList()

                               // Check if at least one item in filterList matches
                               if (roomFilters.any { it in filterList }) {
                                   val uid = generateDigitRandom()
                                   val token: String = generateDynamicToken(roomKey, uid)

                                   onRoomFound(
                                       joiningParameters(
                                           channelName = roomKey,
                                           joinedUser = auth.uid,
                                           uid = uid,
                                           token = token
                                       )
                                   )
                                   return // Stop after finding the first match
                               }
                           }

                       }else{

                           onRoomFound(
                               joiningParameters(
                                   channelName = roomKey,
                                   joinedUser = auth.uid,
                                   uid = uid,
                                   token = token
                               )
                           )

                       }

                    } else {
                        createNewRoom(onRoomFound = {onRoomFound(it)},filterList = filterList)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("AgoraRepoImpl", "Database error: ${error.message}")
                }
            })
    }

    override fun deleteChannelRoom(userId: String) {
        database.reference.child("ConnectionRoom").child(auth.uid!!)
            .removeValue()
            .addOnSuccessListener {
                Log.d("AgoraRepoImpl","Deleted")
            }.addOnFailureListener{
                Log.d("AgoraRepoImpl",it.message.toString())
            }
    }

    override fun onRightSwipe(userId: String) {

        database.reference.child("ConnectionRoom").child(auth.uid!!)
            .updateChildren(
                mapOf(
                    "isAvailable" to true,
                    "joinedUser" to ""
                )
            )
            .addOnSuccessListener {

            }.addOnFailureListener {

            }

    }

    private fun createNewRoom(onRoomFound: (joiningParameters) -> Unit,filterList:List<String>) {

        val uid = generateDigitRandom()
        val token : String = generateDynamicToken(auth.uid!!,uid)

        val roomData = mapOf(
            "joinedUser" to "",
            "createdBy" to auth.uid,
            "isAvailable" to true,
            "channelID" to auth.uid,
            "filters" to filterList
        )

        database.getReference().child("ConnectionRoom").child(auth.uid!!).setValue(roomData).addOnSuccessListener {
            onRoomFound(
                joiningParameters(
                    channelName = auth.uid!!,
                    uid = uid,
                    token = token
                )
            )

        }

        Log.d("AgoraRepoImpl", uid.toString())

    }

    override fun leaveChannel() {
        mRtcEngine?.leaveChannel()
        //RtcEngine.destroy()
        mRtcEngine = null

       // deleteChannelRoom(auth.uid!!)
    }

    override fun leaveChannelWhenOffline() {
        mRtcEngine?.leaveChannel()
    }

    override fun MicMute() {
        mRtcEngine?.muteLocalAudioStream(true)
    }

    override fun MicUnMute() {
        mRtcEngine?.muteLocalAudioStream(false)
    }

    override fun SpeakerMute() {
        mRtcEngine?.muteAllRemoteAudioStreams(true)
    }

    override fun SpeakerUnMute() {
        mRtcEngine?.muteAllRemoteAudioStreams(false)
    }

    private fun getAgoraErrorDescription(errorCode: Int): String {
        return when (errorCode) {
            101 -> "Invalid App ID"
            109 -> "Invalid Token"
            110 -> "Token Expired"
            112 -> "Channel Key Expired"
            113 -> "Invalid Channel Name"
            17 -> "Cannot join the channel due to network issues"
            18 -> "Audio device failure"
            else -> "Unknown error: $errorCode"
        }
    }

    private fun generateDynamicToken(channelId: String, uid: Int):String{

        val tokenBuilder = RtcTokenBuilder2()
        val timestamp = (System.currentTimeMillis() / 1000 + 3600).toInt()
        val token = tokenBuilder.buildTokenWithUid(
            "8be7292d7bb44b46b541bc72316ebc5a","80e43674c9ce4c20bca7db785983fdd7",
            channelId,
            uid,RtcTokenBuilder2.Role.ROLE_PUBLISHER,
            timestamp,timestamp
        )
        return token
    }

    fun generateDigitRandom(): Int {
        return (1000..9999).random()
    }

}
