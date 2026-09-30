> **Language:** Русский · [English](README.en.md)

# Mielon's The Sift (Minecraft 1.21.4 Fabric Port)

![Java 21](https://img.shields.io/badge/Java-21-blue.svg)
![Minecraft](https://img.shields.io/badge/Minecraft-1.21.4-blue.svg)
![Fabric](https://img.shields.io/badge/Loader-Fabric-blue.svg)
![ModMenu](https://img.shields.io/badge/ModMenu-Supported-blue.svg)
![License](https://img.shields.io/badge/License-MIT-blue.svg)

Порт и адаптация модификации **Mielon's The Sift** для **Minecraft 1.21.4 (Fabric)**.

Оригинальный разработчик: [Mielon/mielons-the-sift](https://modrinth.com/mod/mielons-the-sift).

---

## О моде

**Mielon's The Sift** добавляет в игру новое таинственное измерение — The Sift: потусторонний мир из сифтслейта (siftslate), ихора (Ichor), резонирующих душ и древних существ.

---

## Возможности

- **Измерение The Sift**: уникальные биомы (`sift_wastes`, `overgrown_clearing`, `overgrown_forest`, `siftslate_slopes`, `overgrown_slopes`, `ichor_snowy_peaks`, `sift_deep_dark` и др.).
- **Уникальные существа**:
  - **Blub** — дружелюбное приручаемое существо глубин.
  - **Echo Golem** — голем из эхо-материалов, резонирующий с душами.
  - **Sifter** — загадочный обитатель сифтслейта.
  - **Singer** — древнее поющее существо с уникальными фазами поведения.
  - **Dark Sniffer** — заражённый тёмный нюхач.
- **Блоки и материалы**: разновидности сифтслейта (кирпичи, плиты, ступени, полированные варианты), жидкость Ихор, растения (Overgrown Stalks, Overgrown Fronds, Overgrown Lotus, Whisperbloom), блоки Sonorous Deepslate, портальные структуры.
- **Предметы и снаряжение**: экипировка и инструменты из чароита (Charoite) и сифтита (Siftite), копья, снежки ихора, музыкальный диск Rift.

---

## Что изменено в порте для 1.21.4 (byMr712)

- Проект переведён на toolchain Fabric Loom 1.10.1 под **Minecraft 1.21.4** и Java 21 LTS.
- Датапак измерения и биомов обновлён до стандарта 1.21.4 (`pack_format 61`, схема `dimension_type`, стандартные `worldgen/biome` со структурой `effects`, `spawners`, `carvers`).
- Модели сущностей и анимации обновлены под GeckoLib 4.8.5.
- Все определения предметов приведены к стандарту Client Item Definitions 1.21.4 (`assets/the_sift/items/*.json`).
- Рендеринг атмосферы, неба и облаков интегрирован через Fabric API `DimensionRenderingRegistry`.
- Добавлена полная русская локализация (`ru_ru.json`) и сохранена оригинальная английская (`en_us.json`).
- Настроена оптимизированная конфигурация сборки мода.

---

## Установка

1. Скачайте последнюю версию со страницы [GitHub Releases](https://github.com/byMr712/Mielon's-The-Sift-1.21.4-MinecraftMod/releases).
2. Требуются:
   - [Fabric API](https://modrinth.com/mod/fabric-api)
   - [GeckoLib](https://modrinth.com/mod/geckolib) (версии >=4.8.5)
3. Поместите `.jar` файл в папку `mods`.
4. Запустите игру.

---

## Сборка

1. Требуется Java 21 и Fabric Loader для Minecraft 1.21.4.
2. Для сборки выполните:
   ```bash
   ./gradlew build
   ```
3. Собранный файл находится в `build/libs/MielonsTheSift-1.21.4-byMr712.jar`.

---

## Авторы и лицензия

- Исходный мод и ассеты: **Mielon**, лицензия [MIT](LICENSE).
- Текстура Dark Sniffer: **ImpSteve**.
- Музыкальный диск Rift: **Fuzja Jądrowa**.
- Порт и адаптация под 1.21.4: **byMr712**, лицензия [MIT](LICENSE).
