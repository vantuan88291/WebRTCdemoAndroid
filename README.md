# Video Call App using MVVM with databinding, webrtc, socket IO

Library: DI with koin, RXjava, mvvm, webrtc, Socket IO

## Overview
<div>
<img src='https://github.com/vantuan88291/WebRTCdemoAndroid/raw/master/img2.jpeg' width='250"'>
<img src='https://github.com/vantuan88291/WebRTCdemoAndroid/raw/master/img1.jpeg' width='250"'>
</div>

Play Store: https://play.google.com/store/apps/details?id=com.tuan88291.webrtcdemo


## Explanation of folder structure

```
packagename
├── data
│   ├── local
│   │     ├──entity
│   │     ├──model
│   │     └──room
│   │
│   ├── remote
├── di
│   ├── AppModule.kt
├── utils
├── view
│   ├──activity
│   ├──adapter
│   ├──components
│   └──fragment
│
├── App.kt
├── BaseActivity.kt
├── BaseContract.kt
├── BaseFragment.kt
└── BaseView.kt
```


All lib in ```dependencies.gradle```

Change your wifi IP address at Common.kt:

```DOMAIN = "http://192.168.0.162:3000"```

To use socket for signal Server, clone project and setup node server: https://github.com/vantuan88291/WebRTCsignalServer

## React Native version
https://github.com/vantuan88291/WebRTC_React_Native
## Flutter version
https://github.com/vantuan88291/webrtc_flutter_demo_bloc
