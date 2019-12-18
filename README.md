# Video Call App using MVVM with databinding, webrtc, socket IO

Library: DI with koin, RXjava, mvvm, webrtc, Socket IO

## Overview
![WebRTCdemoAndroid](https://github.com/vantuan88291/WebRTCdemoAndroid/raw/master/img2.jpeg)![WebRTCdemoAndroid](https://github.com/vantuan88291/WebRTCdemoAndroid/raw/master/img1.jpeg)


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

Change your wifi IP address at AppModule.kt:

```IO.socket("http://192.168.0.162:3000", opts)```

To use socket for signal Server, clone project and setup node server: https://github.com/vantuan88291/WebRTCsignalServer