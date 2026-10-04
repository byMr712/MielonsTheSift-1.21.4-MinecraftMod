# AGENTS.md — Mielon's The Sift (Fabric 1.21.4)

Инструкция и руководство по проекту **Mielon's The Sift** для агентов и разработчиков.

## 1. Стек и окружение сборки
- **Minecraft:** `1.21.4`
- **Java:** `21` (LTS)
- **Fabric Loader:** `0.16.14`
- **Fabric API:** `0.119.4+1.21.4`
- **GeckoLib:** `4.8.5` (`software.bernie.geckolib.*`)
- **Mappings:** Mojang Official Mappings (`loom.officialMojangMappings()`)
- **Loom:** `1.10.1`

## 2. Команды сборки и тестирования
- Сборка: `./gradlew build` (или запуск `build.bat`)
- Компиляция: `./gradlew compileJava`
- Тестовый запуск сервера: `./gradlew runServer --args="nogui"`
- Целевой артефакт: `build/libs/MielonsTheSift-1.21.4-byMr712.jar`

## 3. Архитектура и структура кода
- `mielon.thesift.TheSift` — главный инициализатор мода.
- `mielon.thesift.TheSiftClient` — клиентский инициализатор (GeckoLib рендереры, слои брони, частицы, регистрация `BlockRenderLayerMap`).
- `mielon.thesift.block.ModBlocks` — реестр блоков (с обязательным `.setId(ResourceKey)`).
- `mielon.thesift.item.ModItems` — реестр предметов (с обязательным `Properties.setId(ResourceKey)`).
- `mielon.thesift.entity.ModEntities` — реестр сущностей и их атрибутов.
- `mielon.thesift.client.entity.*` — GeckoLib модели и рендереры (`SifterModel`, `BlubModel`, `EchoGolemModel`, `SingerModel`, `RiftRenderer`, `RiftMesh`).
- `mielon.thesift.client.render.*` — кастомные рендереры:
  - `SiftPortalRenderer` — рендерер портала, использующий трехслойный полупрозрачный параллакс-рендеринг в мировых координатах (`world-space UV mapping`) по аналогии с порталом в Нижний мир.
  - `SiftPortalRenderTypes` — кастомные `RenderType` (`RENDERTYPE_BEACON_BEAM_SHADER` с текстурами тумана и шейдерпака, `TRANSLUCENT_TRANSPARENCY`, `COLOR_DEPTH_WRITE` и `TriState.FALSE` в `TextureStateShard`).
- `mielon.thesift.worldgen.*` — генерация мира и регистрация измерений.
- `mielon.thesift.mixin.*` — миксины для физики стен (`WallBlockMixin`), портала и серверного тика.

## 4. Датапак генерации мира и структуры данных (1.21.4)
- `data/the_sift/worldgen/dimension/the_sift.json` — конфигурация измерения The Sift.
- `data/the_sift/worldgen/dimension_type/the_sift.json` — параметры типа измерения.
- `data/the_sift/worldgen/noise_settings/the_sift.json` — 1.21.4 настройки шума и surface rules.
- `data/the_sift/worldgen/configured_feature/` — конфигурации фич (используют `"type": "minecraft:simple_state_provider"`).
- `data/the_sift/worldgen/configured_carver/` — карверы пещер и каньонов (`yScale`, прямой массив блоков в `replaceable`).
- `data/the_sift/worldgen/placed_feature/` — размещение фич (используют `"type": "minecraft:random_offset"`).
- `data/the_sift/worldgen/density_function/` — функции плотности ландшафта (`argument1`/`argument2`, `y_clamped_gradient`).

## 5. Строгие правила схем лут-таблиц, рецептов и достижений (1.21.4)
- **Loot Tables (`data/<namespace>/loot_table/`):**
  - Одиночные `"condition": { ... }` и `"modifier": [ ... ]` категорически **запрещены**. Кодеки 1.21.4 требуют массивы `"conditions": [ ... ]` и `"functions": [ ... ]`.
  - Внутри составных условий (`any_of`, `all_of`, `inverted`) каждый элемент `terms` обязан иметь ключ `"condition": "minecraft:..."` (а НЕ `"type"`).
  - Условие `"minecraft:match_block"` заменено на `"condition": "minecraft:block_state_property", "block": "...", "properties": { ... }`.
- **Recipes (`data/<namespace>/recipe/`):**
  - Ванильная цепь имеет идентификатор строго `"minecraft:chain"` (а НЕ `minecraft:iron_chain`).
- **Advancements (`data/<namespace>/advancement/`):**
  - Предикаты сущностей оформляются списком условий: `"entity": [ { "condition": "minecraft:entity_properties", "entity": "this", "predicate": { "type": "minecraft:..." } } ]`.
  - Поле `type` строго валидируется по `BuiltInRegistries.ENTITY_TYPE`. Несуществующие ID сущностей вызывают отказ загрузки всего датапака.

## 6. Важные правила ассетов и рендеринга (1.21.4)
- **GeckoLib 4.8.5**: модели и анимации сущностей располагаются в `assets/the_sift/geo/entity/` и `assets/the_sift/animations/entity/`.
- **Equipment JSON (`equipment/siftite.json`)**: в 1.21.4 допустимы только слои `humanoid`, `humanoid_leggings`, `wings`, `wolf_body`, `horse_body`, `llama_body`. Слой `humanoid_baby` запрещен.
- **Display Contexts**: контекст `on_shelf` отсутствует в 1.21.4 и должен быть удален из моделей и селекторов предметов.
- **Block Models Табличек**: при наследовании от `template_wall_sign` и `template_sign_rot_*` в `textures` обязательно указывать переменную `"sign"` (а не только `"all"`), иначе игра выдает предупреждение о потерянной текстуре `#sign`. Для висячих табличек указываются `"board"` и `"planks"`.
- **BlockRenderLayerMap**: все растения, ростки, саженцы, лозы, двери, люки и листва обязаны регистрироваться в `BlockRenderLayerMap.INSTANCE.putBlocks(RenderType.cutout(), ...)` (или `cutoutMipped()`) на клиенте.
- **VertexConsumer в 1.21.4**: для формата `DefaultVertexFormat.NEW_ENTITY` (`POSITION_COLOR_TEX_OVERLAY_LIGHT_NORMAL`) цепочка вершин обязана передавать все элементы: `.addVertex(...)`, `.setColor(...)`, `.setUv(...)`, `.setOverlay(OverlayTexture.NO_OVERLAY)`, `.setLight(...)`, `.setNormal(...)`.
- **Полупрозрачные порталы (Nether style) и буфер глубины**: Для создания полупрозрачного портала с динамическим параллаксом создается кастомный `RenderType` на базе `RENDERTYPE_BEACON_BEAM_SHADER` с `TRANSLUCENT_TRANSPARENCY` и **`COLOR_DEPTH_WRITE`**. Запись в буфер глубины (`COLOR_DEPTH_WRITE`) критически важна: она сохраняет полупрозрачность для уже отрисованного фона (ландшафта), но предотвращает просвечивание и наложение облаков и фоновых сущностей, рисуемых игрой на более поздних этапах кадра. В 1.21.4 `TextureStateShard` принимает `TriState` (например `TriState.FALSE`) вместо примитивного `boolean`. UV-координаты вычисляются в абсолютных мировых координатах `(pos.getX() + localX) * scale + offset` для 100% бесшовности между смежными блоками.

## 7. Оптимизация производительности (Performance Guidelines)
- **Исключение Stream API в горячих методах**: Никогда не использовать `Collection.stream()` / `filter` / `map` внутри часто вызываемых методов проверки блоков (`isWaterlogged`, `createTick`, `getFluidState`), заменяя их на простые циклы `for-each` для предотвращения сборки мусора (GC pressure).
- **Оптимизация BlockEntityTicker**: Пассивные BlockEntity обязаны проверять активное состояние (например `if (blockEntity.animationMode != 0)`) в самом начале статического метода `tick()` до вызова проверок мира.
- **Переиспользование MutableBlockPos**: В циклах проверки вертикальных столбов или пространственного сканирования обязательно использовать один экземпляр `MutableBlockPos` вместо создания новых `BlockPos` в цикле.

## 8. Локализация
- Полная английская (`assets/the_sift/lang/en_us.json`) и русская (`assets/the_sift/lang/ru_ru.json`) локализация для всех элементов.

## 9. Стандарты оформления коммитов (Commit Message Guidelines)

1. **Заголовок (Title):**
   * Краткое и емкое описание изменений на английском языке в повелительном наклонении (`Fix ...`, `Add ...`, `Refactor ...`, `Update ...`).
   * Без точки на конце строки.
   * Длина строки заголовка предпочтительно до 72 символов.

2. **Пустая строка** между заголовком и подробным описанием.

3. **Тело коммита (Body):**
   * Маркированный список конкретных изменений (`- <Компонент/Класс/Файл>: <описание сути изменения>`).
   * Четкое техническое объяснение: что именно исправлено, оптимизировано или добавлено.

**Пример структуры:**
```
Fix shaped recipe sliding matching, LAN permissions, and GUI freeze

- CustomDynamicCraftingRecipe: implement sliding window (dx, dy) matching for recipes smaller than 3x3
- RecipeEditorMod: verify integrated server host to prevent unauthorized LAN changes
- RecipeInspector: scan mod JARs asynchronously on startup to eliminate screen freeze
```

## 10. Стандарты составления README (Player-Friendly Documentation)

Файлы `README.md` и `readme.en.md` (при наличии) предназначены для **обычных игроков**, а не разработчиков.
* **Никакого кода и внутреннего сленга:** избегать упоминания имён Java-классов, низкоуровневых методов, сетевых пакетов и чисто технических терминов.
* **Фокус на игровом опыте:** описывать, что игрок видит, нажимает и получает в игре.
* **Понятные описания:** объяснять механики языком игрового процесса и атмосферы мода.

