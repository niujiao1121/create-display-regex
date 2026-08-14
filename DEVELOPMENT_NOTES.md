# Development notes — 26.2 prototype

## Upstream choices verified

The Create Fly `26.2` branch currently declares Minecraft `26.2-rc-2`, Fabric Loader `0.19.3`, Fabric API baseline
`0.152.0+26.2`, Create Fly `6.0.9`, and Java 25. Its wrapper uses Gradle 9.4.1.

The current Display Link implementation already gives us two useful stable seams:

1. `DisplayLinkBlockEntity` keeps source settings in a `CompoundTag` and writes that tag as the persisted `Source`
   data. The normal Display Link screen sends that same source configuration through `DisplayLinkConfigurationPacket`.
2. `DisplaySource.transferData` centrally calls both `provideFlapDisplayText(...)` and `provideText(...)` immediately
   before giving data to targets.

The prototype therefore avoids a custom block entity field and avoids a custom packet. It appends namespaced keys to
Create's source config and redirects those two calls inside `transferData`.

For the dev dependency, the workspace follows Create Fly's own published Modrinth Maven coordinate for 26.2 instead
of maintaining a custom downloaded jar.

## Test status in this workspace

Passed:

- Pure Java regex self-test (`scripts/test-regex-processor.sh`).
- JSON parse validation for Fabric metadata, mixin configs, and language files.
- Whole project Java syntax compile against minimal 26.2-signature stubs (JDK 21 used only as a syntax/linkage-stub
  check; project source itself uses no Java-25-only syntax).
- Manual comparison of Mixin targets against Create Fly 26.2 source signatures.

Not runnable in the current sandbox:

- Real Fabric Loom/Gradle build: sandbox contains Java 21 only and no Gradle installation, while Create Fly 26.2
  requires Java 25 / Gradle 9.4.1. Network access needed by Gradle is also unavailable here.
- Minecraft client smoke test for the same reason.

## Next test once opened in a Java 25 dev machine

1. `gradle build --stacktrace`
2. Fix any mapping/API mismatch that only the real Minecraft jar exposes.
3. `gradle runClient`
4. Place a Display Link reading Package Address into a Display Board.
5. Configure pattern `^[^/]+/`, replacement empty.
6. Send `AAA/BBB/CCC`; verify the board shows `BBB/CCC` while logistics still sees the original package address.
7. Test invalid pattern `[` and verify GUI blocks confirmation/no server crash.
8. Test a labeled SingleLine source and a flap display target.
