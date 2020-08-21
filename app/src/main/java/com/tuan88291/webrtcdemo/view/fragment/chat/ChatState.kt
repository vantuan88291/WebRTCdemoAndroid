package com.tuan88291.webrtcdemo.view.fragment.chat

sealed class ChatState {
    data class Success<T>(val data: T): ChatState()
    data class AllData<T>(val data: MutableList<T>): ChatState()
    data class Failure(val message:String): ChatState()
    data class Loading(val loading: Boolean): ChatState()
    data class Typing(val listTyping: String): ChatState()
}