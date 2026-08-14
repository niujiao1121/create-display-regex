# Pinned upstream baseline

Inspected on 2026-08-14.

- Repository: `ZurrTum/Create-Fly`
- Branch: `26.2`
- Branch head inspected: `fc1b30b1c96b400ec3833f9c5e3ce57bccaf13b9`
- Create Fly: `6.0.9`
- Minecraft: `26.2-rc-2` (published as compatible with Minecraft 26.2)
- Fabric Loader: `0.19.3`
- Fabric API baseline in Create Fly: `0.152.0+26.2`
- Loom: `1.16-SNAPSHOT`
- Java: `25`
- Gradle wrapper upstream: `9.4.1`
- Modrinth Maven version: `26.2-rc-2-6.0.9-1`

Critical source paths:

```text
src/main/java/com/zurrtum/create/api/behaviour/display/DisplaySource.java
src/main/java/com/zurrtum/create/content/redstone/displayLink/DisplayLinkBlockEntity.java
src/main/java/com/zurrtum/create/content/redstone/displayLink/DisplayLinkContext.java
src/main/java/com/zurrtum/create/infrastructure/packet/c2s/DisplayLinkConfigurationPacket.java
src/client/java/com/zurrtum/create/client/content/redstone/displayLink/DisplayLinkScreen.java
src/client/java/com/zurrtum/create/client/foundation/gui/AllIcons.java
src/client/java/com/zurrtum/create/client/foundation/gui/widget/IconButton.java
```
