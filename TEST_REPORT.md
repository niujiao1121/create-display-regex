# Test report — 2026-08-14

## Passed in this workspace

- Pure Java regex behavior tests.
  - delete first slash-delimited segment (`AAA/BBB/CCC` -> `BBB/CCC`)
  - keep final segment (`AAA/BBB/CCC` -> `CCC`)
  - ordinary replacement
  - numbered replacement-group validation
  - named replacement-group validation
  - malformed pattern/replacement validation
  - disabled-mode pass-through
  - invalid runtime rule fail-open
- JSON parsing for Fabric metadata, mixin configs, and all language files.
- Source invariants for the two Mixin hooks used by the addon.
- Full addon Java source compiled against minimal stubs matching the inspected Create Fly 26.2 method signatures.
  The only javac warning comes from the test stub's generic `IconButton.withCallback` implementation, not addon source.

## Upstream structures inspected

- `DisplaySource.transferData(...)` centrally invokes both `provideText(...)` and `provideFlapDisplayText(...)`.
- `DisplayLinkScreen.onClose()` sends a `CompoundTag` through `DisplayLinkConfigurationPacket`.
- `DisplayLinkBlockEntity` persists the source configuration CompoundTag.
- `DisplayLinkScreen` uses Create `IconButton`; `AllIcons` provides `I_CONFIG_OPEN`, `I_CONFIG_BACK`, `I_CONFIRM`, `I_ACTIVE`, and `I_PASSIVE`.
- Create Fly 26.2 itself uses Mixin compatibility level `JAVA_25`.

## Not executed here

A real Fabric Loom build and Minecraft client launch were not executable in the sandbox because it only has Java 21,
no Gradle installation, and binary/dependency downloads are blocked. The target Create Fly 26.2 workspace requires
Java 25 and uses Gradle 9.4.1.

Run on a normal Java 25 development machine:

```bash
gradle build --stacktrace
gradle runClient
```

The first in-game smoke test should read a package address with a Display Link and apply:

```text
Pattern:     ^[^/]+/
Replacement: <empty>
```

For `AAA/BBB/CCC`, the display should show `BBB/CCC`; the actual package address should remain unchanged.
