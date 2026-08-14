# Create: Display Regex (26.2-first prototype)

A tiny Fabric addon for **Create Fly** that post-processes Display Link output with a Java regular expression.
It changes only what is sent to the display target; it does **not** alter package addresses, inventories, or the
underlying source block.

## Baseline

This workspace follows Create Fly's `26.2` branch baseline used during development:

- Minecraft `26.2-rc-2`
- Fabric Loader `0.19.3`
- Fabric API `0.152.0+26.2` (used to register the addon's client language resources)
- Create Fly `6.0.9`, build `1`
- Java `25`
- Gradle `9.4.1`

## Intended in-game use

Open a Display Link. A new Create-style configuration icon appears immediately left of the normal confirm button.
It opens a small screen with:

- enable/disable toggle
- **Pattern**
- **Replacement**
- live validation

Example:

```text
input       AAA/BBB/CCC
pattern     ^[^/]+/
replacement <empty>
output      BBB/CCC
```

Because this runs after the selected Display Source has produced its text, the same mechanism can work with Package
Address and other Display Sources.

## Architecture

1. Regex settings are appended to Create's existing Display Link `Source` CompoundTag. This reuses Create's existing
   `DisplayLinkConfigurationPacket`; no custom packet or extra block entity state is introduced.
2. `DisplaySourceMixin` redirects the two central calls inside `DisplaySource.transferData`:
   - `provideText(...)`
   - `provideFlapDisplayText(...)`
3. Text is processed with `Matcher.replaceAll`. Invalid rules fail open and return the original text.
4. Flap-display sections are processed individually so their component count remains stable for Create's existing
   flap layout.

## Build

A Java 25 JDK is required.

```bash
./gradlew build
```

The installable mod is `build/libs/create-display-regex-0.1.0-alpha.1.jar`; do not install the accompanying
`-sources.jar`. Create Fly is resolved from its published Modrinth Maven coordinate
(`26.2-rc-2-6.0.9-1`).

## Release

Pushing a version tag beginning with `v` runs the release workflow. It builds the project and creates a GitHub
Release containing only the installable JAR. Tags containing a hyphen, such as `v0.1.0-alpha.1`, are marked as
pre-releases automatically.

## Development

For a Chinese development and release guide, see [DEVELOPMENT.zh-CN.md](DEVELOPMENT.zh-CN.md).

Start a development client with:

```bash
./gradlew runClient
```

Do not manually copy this project's JAR into `run/mods`: Loom loads the project automatically in development.

## Tests

The regex engine is pure Java and can be tested even without Minecraft dependencies:

```bash
./scripts/test-regex-processor.sh
```

The workspace also includes `scripts/verify-upstream-hooks.py` for checking that the two upstream methods targeted by
Mixins still have the expected structure when rebasing onto a newer Create Fly 26.2 build.

## Current prototype limitations

- Full Minecraft/Gradle compilation requires Java 25 plus dependency/network access.
- Text components are converted to literals after processing; top-level style is kept, but complex nested component
  styling may be flattened.
- Flap-display regex is applied per section, not across multiple flap sections. This keeps Create's layout stable.
- The UI currently uses Create's icon buttons plus a lightweight custom panel; it can be reskinned with a dedicated
  Create-style texture later without changing persistence or processing code.
