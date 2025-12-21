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
# 构建 Debug APK
./gradlew :composeApp:assembleDebug

# 构建 Release APK
./gradlew :composeApp:assembleRelease

# 安装到已连接的设备
./gradlew :composeApp:installDebug
```

### Desktop (JVM)

```shell
# 运行桌面应用
./gradlew :composeApp:run

# 热重载运行（开发推荐，代码修改后自动重载）
./gradlew :composeApp:hotRunJvm

# 仅编译 JVM 代码
./gradlew :composeApp:compileKotlinJvm

# 构建可分发包 (MSI/DMG/DEB)
./gradlew :composeApp:packageDistributionForCurrentOS

# 创建可运行的分发版本
./gradlew :composeApp:createDistributable
```

### Web (Wasm)

```shell
# 开发模式运行（带热重载）
./gradlew :composeApp:wasmJsBrowserDevelopmentRun

# 生产模式构建
./gradlew :composeApp:wasmJsBrowserProductionWebpack
```

### Web (JS)

```shell
# 开发模式运行（带热重载）
./gradlew :composeApp:jsBrowserDevelopmentRun

# 生产模式构建
./gradlew :composeApp:jsBrowserProductionWebpack
```

### iOS

在 Xcode 中打开 `iosApp` 目录运行。

```shell
# 编译 iOS Framework
./gradlew :composeApp:compileKotlinIosArm64
./gradlew :composeApp:compileKotlinIosSimulatorArm64
```

## 常用开发命令

```shell
# 清理构建缓存
./gradlew clean

# 查看所有可用任务
./gradlew :composeApp:tasks

# 运行测试
./gradlew :composeApp:allTests

# 格式化检查
./gradlew :composeApp:check
```
