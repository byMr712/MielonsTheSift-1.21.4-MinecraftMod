# Mielon's The Sift (Minecraft 1.21.4)
> **Language:** [Русский](README.md) · English

Port and adaptation of **Mielon's The Sift** mod for **Minecraft 1.21.4 (Fabric)** on **Java 21**.

Original mod author: **[Mielon](https://modrinth.com/mod/mielons-the-sift)**.

## Fork Overview
The mod introduces a new dimension — The Sift: an eerie world of siftslate, Ichor, resonant souls, and ancient creatures.

### 1.21.4 Port & Changes
- Migrated build toolchain to Fabric Loom 1.10.1 for Minecraft 1.21.4 and Java 21 LTS.
- Updated dimension and biome data schema to 1.21.4 standards (`pack_format 61`, `dimension_type`, standard `worldgen/biome` format with `effects`, `spawners`, `carvers`).
- Entity models and animations updated for GeckoLib 4.8.5.
- Client item definitions (`assets/the_sift/items/*.json`) configured according to Minecraft 1.21.4 standards.
- Dimension sky, cloud, and weather rendering ported via Fabric API `DimensionRenderingRegistry`.
- Added complete Russian localization (`ru_ru.json`) alongside English (`en_us.json`).

## Mod Features
- **The Sift Dimension**: Unique biomes (`sift_wastes`, `overgrown_clearing`, `overgrown_forest`, `siftslate_slopes`, `overgrown_slopes`, `ichor_snowy_peaks`, `sift_deep_dark`, etc.).
- **Entities**:
  - **Blub** — Friendly tameable creature of the deep.
  - **Echo Golem** — Soul-resonant golem composed of echo materials.
  - **Sifter** — Mysterious denizen of the siftslate depths.
  - **Singer** — Ancient entity with custom behavioral phases.
  - **Dark Sniffer** — Infected variant of the sniffer.
- **Blocks & Materials**: Siftslate variants (bricks, slabs, stairs, polished), Ichor fluid, foliage (Overgrown Stalks, Overgrown Fronds, Overgrown Lotus, Whisperbloom), Sonorous Deepslate, portal structures.
- **Items & Equipment**: Charoite and Siftite gear, spears, Ichor snowballs, Rift music disc.

## Requirements
- **Minecraft:** 1.21.4
- **Fabric Loader:** >=0.16.10
- **Fabric API:** >=0.115.0
- **GeckoLib:** >=4.8.5
- **Java:** 21 LTS

## Building from Source
```bash
./gradlew build
```
The compiled jar file will be placed in `build/libs/`.

## License & Credits
- Original mod and assets: **Mielon**, licensed under [MIT](LICENSE).
- Dark Sniffer texture: **ImpSteve**.
- Rift music disc: **Fuzja Jądrowa**.
- Port and adaptation to 1.21.4: **byMr712**, licensed under [MIT](LICENSE).
