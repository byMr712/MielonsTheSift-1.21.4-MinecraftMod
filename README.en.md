> **Language:** [Русский](README.md) · English

# Mielon's The Sift (Minecraft 1.21.4 Fabric Port)

![Java 21](https://img.shields.io/badge/Java-21-blue.svg)
![Minecraft](https://img.shields.io/badge/Minecraft-1.21.4-blue.svg)
![Fabric](https://img.shields.io/badge/Loader-Fabric-blue.svg)
![ModMenu](https://img.shields.io/badge/ModMenu-Supported-blue.svg)
![License](https://img.shields.io/badge/License-MIT-blue.svg)

Port and adaptation of **Mielon's The Sift** mod for **Minecraft 1.21.4 (Fabric)**.

Original Developer: [Mielon/mielons-the-sift](https://modrinth.com/mod/mielons-the-sift).

---

## About

**Mielon's The Sift** introduces a new mysterious dimension — The Sift: an eerie world of siftslate, Ichor, resonant souls, and ancient creatures.

---

## Features

- **The Sift Dimension**: Unique biomes (`sift_wastes`, `overgrown_clearing`, `overgrown_forest`, `siftslate_slopes`, `overgrown_slopes`, `ichor_snowy_peaks`, `sift_deep_dark`, etc.).
- **Unique Entities**:
  - **Blub** — Friendly tameable creature of the deep.
  - **Echo Golem** — Soul-resonant golem composed of echo materials.
  - **Sifter** — Mysterious denizen of the siftslate depths.
  - **Singer** — Ancient entity with custom behavioral phases.
  - **Dark Sniffer** — Infected variant of the sniffer.
- **Blocks & Materials**: Siftslate variants (bricks, slabs, stairs, polished), Ichor fluid, foliage (Overgrown Stalks, Overgrown Fronds, Overgrown Lotus, Whisperbloom), Sonorous Deepslate, portal structures.
- **Items & Equipment**: Charoite and Siftite gear, spears, Ichor snowballs, Rift music disc.

---

## Changes in 1.21.4 Port (byMr712)

- Migrated build toolchain to Fabric Loom 1.10.1 for **Minecraft 1.21.4** and Java 21 LTS.
- Updated dimension and biome data schema to 1.21.4 standards (`pack_format 61`, `dimension_type`, standard `worldgen/biome` format with `effects`, `spawners`, `carvers`).
- Entity models and animations updated for GeckoLib 4.8.5.
- Client item definitions (`assets/the_sift/items/*.json`) configured according to Minecraft 1.21.4 standards.
- Dimension sky, cloud, and weather rendering ported via Fabric API `DimensionRenderingRegistry`.
- Added complete Russian localization (`ru_ru.json`) alongside English (`en_us.json`).
- Configured optimized build scripts and toolchain.

---

## Installation

1. Download the latest release from [GitHub Releases](https://github.com/byMr712/Mielon's-The-Sift-1.21.4-MinecraftMod/releases).
2. Requires:
   - [Fabric API](https://modrinth.com/mod/fabric-api)
   - [GeckoLib](https://modrinth.com/mod/geckolib) (>=4.8.5)
3. Place the `.jar` file into your `mods` folder.
4. Launch the game.

---

## Building from Source

1. Requires Java 21 and Fabric Loader for Minecraft 1.21.4.
2. To build the project, run:
   ```bash
   ./gradlew build
   ```
3. The compiled jar file will be located at `build/libs/MielonsTheSift-1.21.4-byMr712.jar`.

---

## Credits & License

- Original mod and assets: **Mielon**, licensed under [MIT](LICENSE).
- Dark Sniffer texture: **ImpSteve**.
- Rift music disc: **Fuzja Jądrowa**.
- Port and adaptation to 1.21.4: **byMr712**, licensed under [MIT](LICENSE).
