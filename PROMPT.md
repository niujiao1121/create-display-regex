# Development prompt

Develop a lightweight **Minecraft 26.2 + Fabric + Create Fly 26.2** addon that adds text post-processing to Create's
**Display Link**.

## Goal

Any selected Create Display Source should be able to pass its produced text through a configurable Java regular
expression before the text is sent to the Display Target. This must affect only the displayed text; source data such as
package addresses must remain unchanged.

## MVP behavior

Add a control to the existing Display Link GUI with:

- enable/disable regex processing
- Regex Pattern
- Replacement
- `Matcher.replaceAll` semantics
- validation that prevents an invalid pattern/replacement from crashing the game

Canonical use case:

```text
input:       AAA/BBB/CCC
pattern:     ^[^/]+/
replacement: <empty>
output:      BBB/CCC
```

## Integration constraints

- Target Create Fly's `26.2` branch first.
- Follow Create Fly's existing Fabric/Mixin architecture and GUI conventions.
- Do not add a new block/item/model/texture unless necessary.
- Avoid modifying or forking Create Fly source directly.
- Store settings with the corresponding Display Link using Create's existing persisted source configuration when
  practical.
- Reuse Create's existing configuration packet if possible instead of adding a new network protocol.
- Keep regex processing server-authoritative where Create transfers display data.
- Cover both normal text targets and Create's flap/display-board path.
- Invalid user regex must fail safely and leave the original text unchanged.
- Keep the implementation isolated so future Create Fly 26.2 updates require minimal patching.

## UX direction

Reference Create Fly's own `DisplayLinkScreen`, `IconButton`, `AllIcons`, and other Create-style screens. A small
secondary screen is acceptable for the first version. The primary Display Link screen should stay uncluttered.

## Engineering / test requirements

- Independent Gradle/Fabric Loom workspace.
- Java 25 / Gradle versions aligned to Create Fly 26.2.
- Pure-Java tests for regex behavior.
- Static verification of Mixin target method names/signatures.
- Full Gradle compile and Minecraft client smoke test when Java 25 and dependency network access are available.
- Deliver source workspace, build notes, test status, known limitations, and follow-up tasks.
