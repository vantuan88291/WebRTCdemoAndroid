package com.tuan88291.webrtcdemo.view.activity.videocall

import org.webrtc.IceCandidate
import org.webrtc.SessionDescription

interface SignallingClientListener {
    fun onAnswerAccept()
    fun onEndCall()
    fun onOfferReceived(description: SessionDescription)
    fun onAnswerReceived(description: SessionDescription)
    fun onIceCandidateReceived(iceCandidate: IceCandidate)
}