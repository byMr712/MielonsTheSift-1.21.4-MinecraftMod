# Mielon's The Sift (Minecraft 1.21.4)
> **Language:** Русский · [English](README.en.md)

Порт и адаптация модификации **Mielon's The Sift** для **Minecraft 1.21.4 (Fabric)** на платформе **Java 21**.

Оригинальный автор модификации: **[Mielon](https://modrinth.com/mod/mielons-the-sift)**.

## Описание порта
Модификация добавляет новое измерение — The Sift: потусторонний мир из сифтслейта (siftslate), ихора (Ichor), резонирующих душ и древних существ.

### Изменения и адаптация под 1.21.4
- Проект переведён на toolchain Fabric Loom 1.10.1 под Minecraft 1.21.4 и Java 21 LTS.
- Датапак измерения и биомов обновлён до стандарта 1.21.4 (`pack_format 61`, схема `dimension_type`, стандартные `worldgen/biome` со структурой `effects`, `spawners`, `carvers`).
- Модели сущностей и анимации обновлены под GeckoLib 4.8.5.
- Все определения предметов (`assets/the_sift/items/*.json`) приведены к стандарту Client Item Definitions 1.21.4.
- Рендеринг атмосферы, неба и облаков интегрирован через Fabric API `DimensionRenderingRegistry`.
- Добавлена полная русская локализация (`ru_ru.json`) и сохранена оригинальная английская (`en_us.json`).

## Контент мода
- **Измерение The Sift**: Уникальные биомы (`sift_wastes`, `overgrown_clearing`, `overgrown_forest`, `siftslate_slopes`, `overgrown_slopes`, `ichor_snowy_peaks`, `sift_deep_dark` и др.).
- **Существа**:
  - **Blub** — дружелюбное приручаемое существо глубин.
  - **Echo Golem** — голем из эхо-материалов, резонирующий с душами.
  - **Sifter** — загадочный обитатель сифтслейта.
  - **Singer** — древнее поющее существо с уникальными фазами поведения.
  - **Dark Sniffer** — заражённый тёмный нюхач.
- **Блоки и материалы**: Разновидности сифтслейта (кирпичи, плиты, ступени, полированные варианты), жидкость Ихор, растения (Overgrown Stalks, Overgrown Fronds, Overgrown Lotus, Whisperbloom), блоки Sonorous Deepslate, портальные структуры.
- **Предметы и снаряжение**: Снаряжение и инструменты из чароита (Charoite) и сифтита (Siftite), копья, снежки ихора, музыкальный диск Rift.

## Установка и требования
- **Minecraft:** 1.21.4
- **Fabric Loader:** >=0.16.10
- **Fabric API:** >=0.115.0
- **GeckoLib:** >=4.8.5
- **Java:** 21 LTS

## Сборка из исходников
```bash
./gradlew build
```
Готовый jar-файл будет находиться в каталоге `build/libs/`.

## Лицензия
- Исходный мод и ассеты: **Mielon**, лицензия [MIT](LICENSE).
- Текстура Dark Sniffer: **ImpSteve**.
- Музыкальный диск Rift: **Fuzja Jądrowa**.
- Порт и адаптация под 1.21.4: **byMr712**, лицензия [MIT](LICENSE).
