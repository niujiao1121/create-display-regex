# Create: Display Regex

A small Fabric addon for Create Fly that applies a Java regular-expression replacement to Display Link text.
Only the text sent to the display changes; the source block, packages, and inventories are untouched.

[中文玩家指南](README.zh-CN.md)

## Install

Install the release JAR in the `mods` folder of a compatible Fabric instance, alongside Fabric API and Create Fly.
Install it on the server. Clients that need to edit regex rules should install it too.

Supported baseline: Minecraft `26.2`, Create Fly `6.0.x`.

## Use

Open a Display Link and select the new configuration button next to Confirm. Enable regex replacement, then enter a
Pattern and Replacement. Invalid patterns show an error instead of crashing the game.

For examples and a beginner-friendly introduction to regular expressions, see the [Chinese player guide](README.zh-CN.md).

## Screenshots

![Post-processed text on a display board](docs/images/display-link-result.png)

![Regex configuration screen](docs/images/regex-configuration-zh-cn.png)
