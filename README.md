# 数学派

面向数学试卷拍照识题的 Android 原生工程。

## 当前已搭建

- Kotlin + Jetpack Compose UI
- CameraX 拍照入口
- ML Kit 本地中文 OCR
- 数学公式 ONNX OCR 接口与 ONNX Runtime 依赖
- DeepSeek OpenAI 兼容接口数据结构
- 独立 `deepseek.properties` 配置入口
- 拍照后本地识别、结果人工校正、DeepSeek 解题页面
- 后续可接入 Room 历史记录、数学公式 LaTeX 模型和 SymPy 校验服务

## 打开方式

使用 Android Studio 打开此目录，等待 Gradle Sync。

工程使用 Gradle Wrapper 8.9 和 JDK 17，并以 API 35 编译。

## DeepSeek 本地配置

复制 `deepseek.properties.example` 为项目根目录的 `deepseek.properties`：

```properties
baseUrl=https://api.deepseek.com/
model=deepseek-chat
apiKey=你的密钥
```

该文件已被 `.gitignore` 排除。正式发布时不要把真实 API Key 打包到 APK，应该改成自己的后端代理。

## 当前测试范围

已经可以测试：

1. 首页进入相机并拍照。
2. 照片预览。
3. 手机上的中文与数字 OCR。
4. 手动修改识别文本。
5. 使用 DeepSeek 生成解题结果。

当前没有连接的 Android 设备或模拟器，因此本轮只完成了 APK 编译验证。

## 下一步

1. 加入数学公式 ONNX 模型和图像预处理。
2. 改进试卷裁剪、透视校正和多题分割。
3. 将 DeepSeek 返回的 JSON 解析成多解法卡片。
4. 增加 SymPy 校验服务与 Room 历史记录。
