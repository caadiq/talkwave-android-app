package com.app.talkwave.model.service

import android.util.Log
import com.app.talkwave.BuildConfig
import com.google.gson.Gson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import ua.naiksoftware.stomp.Stomp
import ua.naiksoftware.stomp.dto.LifecycleEvent

class StompClient {
    private val baseUrl = BuildConfig.BASE_URL
    private val wsUrl = baseUrl.replace("http", "ws").run {
        if (this.endsWith("/"))
            "${this}ws"
        else
            "${this}/ws"
    }
    private val stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, wsUrl)

    private var disposable: Disposable? = null
    private var topicDisposable: Disposable? = null

    fun connect() {
        disposable = stompClient.lifecycle()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { lifecycleEvent ->
                when (lifecycleEvent.type) {
                    LifecycleEvent.Type.OPENED -> Log.d("STOMP", "Stomp connection opened")
                    LifecycleEvent.Type.ERROR -> Log.e("STOMP", "Error", lifecycleEvent.exception)
                    LifecycleEvent.Type.CLOSED -> Log.d("STOMP", "Stomp connection closed")
                    LifecycleEvent.Type.FAILED_SERVER_HEARTBEAT -> Log.e("STOMP", "Server heartbeat failed")
                    null -> {}
                }
            }

        stompClient.connect()
    }

    fun disconnect() {
        disposable?.dispose()
        topicDisposable?.dispose()
        stompClient.disconnect()
    }

//    fun sendMessage(roomId: Int, dto: ChatSendDto) {
//        val gson = Gson()
//        val chatRequestJson = gson.toJson(dto)
//
//        stompClient.send("/app/sendmessage/$roomId", chatRequestJson)
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe()
//    }
//
//    fun subscribe(roomId: Int, callback: (ChatReceiveDto) -> Unit): Disposable {
//        return stompClient.topic("/topic/public/$roomId")
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe { topicMessage ->
//                val gson = Gson()
//                val chatResponseDto = gson.fromJson(topicMessage.payload, ChatReceiveDto::class.java)
//                callback(chatResponseDto)
//            }
//    }
}