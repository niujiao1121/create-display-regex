#!/usr/bin/env python3
"""Static verifier for the exact Create Fly 26.2 source snippets this prototype targets.

Paste/fetching is intentionally not done here; CI can call GitHub separately. This checks source files supplied
as positional arguments, making breakage obvious when updating Create Fly.
"""
import pathlib
import sys

checks = {
    "DisplaySource.java": [
        "public void transferData(DisplayLinkContext context, DisplayTarget activeTarget, int line)",
        "provideFlapDisplayText(context, stats)",
        "provideText(context, stats)",
    ],
    "DisplayLinkScreen.java": [
        "protected void init()",
        "new DisplayLinkConfigurationPacket(",
        "blockEntity.getSourceConfig()",
    ],
}

if len(sys.argv) != 3:
    print("usage: verify-upstream-hooks.py DisplaySource.java DisplayLinkScreen.java", file=sys.stderr)
    sys.exit(2)

for path_str in sys.argv[1:]:
    path = pathlib.Path(path_str)
    text = path.read_text(encoding="utf-8")
    expected = checks.get(path.name)
    if expected is None:
        raise SystemExit(f"Unexpected file: {path.name}")
    missing = [needle for needle in expected if needle not in text]
    if missing:
        raise SystemExit(f"{path.name}: missing expected hooks: {missing}")
    print(f"{path.name}: hook structure OK")
