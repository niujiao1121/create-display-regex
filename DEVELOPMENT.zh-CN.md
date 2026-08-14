# 开发与发布指南

## 环境要求

- Linux、Windows 或 macOS
- JDK 25（`java -version` 与 `javac -version` 都应显示 25）
- 网络可访问 Gradle、Fabric Maven 与 Modrinth Maven
- 不需要单独安装 Gradle：仓库已包含 Gradle Wrapper

## 首次运行

```bash
git clone https://github.com/<你的用户名>/create-display-regex.git
cd create-display-regex
./gradlew build
```

若 Linux 上脚本没有执行权限，执行一次：

```bash
chmod +x gradlew
```

## 日常开发

修改 Java 或资源文件后，使用开发客户端验证：

```bash
./gradlew runClient
```

开发客户端的数据目录是仓库内的 `run/`。Loom 会自动把当前项目加载为模组，因此不要把
`build/libs` 中的 JAR 再放进 `run/mods`，否则会造成重复模组 ID。

界面文字位于：

```text
src/client/resources/assets/create_display_regex/lang/
```

目前提供 `en_us.json`、`zh_cn.json` 与 `zh_tw.json`。新增 UI 文本时，三个文件必须使用完全相同的键。

## 验证清单

快速验证纯 Java 的正则逻辑：

```bash
./scripts/test-regex-processor.sh
```

完整编译与打包：

```bash
./gradlew build --stacktrace
```

在开发客户端中至少测试：

1. Display Link 读取 Package Address 并输出到 Display Board。
2. 启用正则，填写 `^[^/]+/`，替换内容留空。
3. 输入 `AAA/BBB/CCC`，确认显示为 `BBB/CCC`，而包裹地址保持不变。
4. 输入非法 Pattern（例如 `[`），确认界面禁止应用，游戏不会崩溃。
5. 测试普通文字目标与 flap/display-board 目标。

## 安装与发布

发布前运行：

```bash
./gradlew clean build
```

可分发文件是：

```text
build/libs/create-display-regex-0.1.0.jar
```

将它复制到目标 Fabric 实例的 `mods/` 目录；不要复制 `-sources.jar`。目标实例需要与本项目基线兼容的
Minecraft、Fabric Loader、Create Fly 和 Fabric API 版本。

版本号在 `gradle.properties` 的 `mod_version` 中维护。GitHub 已配置自动 Release：推送以 `v` 开头的 Git
标签时，会自动构建、创建 GitHub Release，并仅上传正式 JAR（不上传 `-sources.jar`）。带连字符的标签会自动
标记为预发布版本。

发版顺序：

```bash
./gradlew clean build
git add gradle.properties
git commit -m "Release v0.1.0"
git push
git tag v0.1.0
git push origin v0.1.0
```

推送标签后，在 GitHub 仓库的 Actions 页面查看发布流程；完成后在 Releases 页面下载 JAR。
