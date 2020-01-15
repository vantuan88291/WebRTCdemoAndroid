# Video Call App using MVVM with databinding, webrtc, socket IO

Library: DI with koin, RXjava, mvvm, webrtc, Socket IO

## Overview
![WebRTCdemoAndroid](https://github.com/vantuan88291/WebRTCdemoAndroid/raw/master/img2.jpeg)![WebRTCdemoAndroid](https://github.com/vantuan88291/WebRTCdemoAndroid/raw/master/img1.jpeg)


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