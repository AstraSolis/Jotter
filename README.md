# Jotter

一个使用 **Compose Multiplatform** 构建的跨平台笔记应用，支持日记、待办事项和笔记管理。

## 项目结构

```
Jotter/
├── composeApp/          # 跨平台共享代码
│   └── src/
│       ├── commonMain/  # 通用代码 (UI, 业务逻辑)
│       ├── androidMain/ # Android 平台特定代码
│       ├── iosMain/     # iOS 平台特定代码
│       ├── jvmMain/     # Desktop (JVM) 平台代码
│       ├── jsMain/      # Web JS 目标代码
│       └── wasmJsMain/  # Web Wasm 目标代码
└── iosApp/              # iOS 应用入口
```

## 开发环境

- Kotlin 2.1.0
- Compose Multiplatform
- Java 11+

## 构建与运行

### Android
```shell
.\gradlew.bat :composeApp:assembleDebug
```

### Desktop
```shell
.\gradlew.bat :composeApp:run
```

### Web (Wasm)
```shell
.\gradlew.bat :composeApp:wasmJsBrowserDevelopmentRun
```

### Web (JS)
```shell
.\gradlew.bat :composeApp:jsBrowserDevelopmentRun
```

### iOS
在 Xcode 中打开 `iosApp` 目录运行。
