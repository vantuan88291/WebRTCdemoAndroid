# Video Call App using MVVM with databinding, webrtc, socket IO

Library: DI with koin, RXjava, room, mvvm, webrtc

This app has a small example about chat with socket io using MVVM

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


To use socket for signal Server, clone project and setup node server: https://github.com/vantuan88291/WebRTCsignalServer