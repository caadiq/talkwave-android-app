package com.app.talkwave.model.service

import android.util.Log
import com.app.talkwave.BuildConfig
import com.app.talkwave.model.dto.ChatMessageReceiveDto
import com.app.talkwave.model.dto.ChatMessageSendDto
import com.google.gson.Gson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import ua.naiksoftware.stomp.Stomp
import ua.naiksoftware.stomp.dto.LifecycleEvent

class StompClient {
    private val baseUrl = BuildConfig.BASE_URL
    private val wsUrl = "${baseUrl.replace("http", "ws")}/api/talkwave/ws-stomp"

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

    fun sendMessage(dto: ChatMessageSendDto) {
        val gson = Gson()
        val requestBody = gson.toJson(dto)

        stompClient.send("/send/message", requestBody)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()
    }

    fun subscribe(roomId: Int, callback: (ChatMessageReceiveDto) -> Unit): Disposable {
        return stompClient.topic("/room/$roomId")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ message ->
                val gson = Gson()
                val dto = gson.fromJson(message.payload, ChatMessageReceiveDto::class.java)
                callback(dto)
            }, { error ->
                Log.e("STOMP", "ERROR", error)
            })
    }
}