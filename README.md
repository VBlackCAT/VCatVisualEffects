# V猫的视觉特效 / VCat Visual Effects

给**任意实体**挂载、同步并渲染视觉特效的轻量框架。

核心模型很简单：每个 `Entity` 身上挂一个特效容器（Forge capability），往容器里塞任意多个「特效实例」，
框架负责把状态同步给追踪该实体的客户端、到期自动清理、并在客户端把它们画出来。渲染分两条路：

- **世界空间**：注册一个渲染器，在 `RenderLevelStageEvent` 里用自定义核心着色器画几何体；
- **屏幕空间**：领域展开类效果走一次全屏深度重建后处理（`entity_depth_reconstruct`）。

> 使用本模组的代码或着色器即表示你接受 [LICENSE.md](LICENSE.md)。**该协议要求：任何使用本模组代码的
> 第三方模组必须以依赖形式引用本模组，不得把代码复制进自己的 jar。**

---

## 环境

| 项目 | 值 |
|---|---|
| Minecraft | 1.20.1 |
| Forge | 47.3.22 及以上 |
| mod id | `vcat_visual_effects` |
| 额外依赖 | 无（除 Forge 外零依赖） |
| 安装侧 | **双端都需要**（特效类型注册表不参与网络同步，见第二节注意事项） |
| 命令 | `/vcatvisual` |

---

## 一、把它加进你的开发环境

本模组暂未发布到公共 Maven，推荐直接把 release jar 放进 `libs/`：

```groovy
// build.gradle
repositories {
    flatDir { dir 'libs' }
}

dependencies {
    // libs/vcat_visual_effects-1.0.0.jar
    modImplementation "blank:vcat_visual_effects-1.0.0"
}
```

如果之后发布到 CurseForge，改成：

```groovy
dependencies {
    modImplementation "curse.maven:vcat-visual-effects-<projectId>:<fileId>"
}
```

然后在你自己的 `mods.toml` 里声明必需的依赖 —— **这是许可证的硬性要求**：

```toml
# src/main/templates/META-INF/mods.toml
[[dependencies.你的modid]]
    modId = "vcat_visual_effects"
    mandatory = true
    versionRange = "[1.0.0,)"
    ordering = "AFTER"
    side = "BOTH"
```

> 只有在你**没有**复制任何代码、只是调用 API 时，才可以考虑 `mandatory = false`；
> 只要用到了本模组的类或着色器，就必须是 `mandatory = true`。

---

## 二、注册你自己的特效类型

特效类型是一个 Forge 自定义注册表（`vcat_visual_effects:entity_visual_effect_types`），
你的模组可以往同一个注册表里注册自己的条目：

```java
public final class MyVisualEffects {

    // 用自己模组的 id 建一个 DeferredRegister，注册进本模组的注册表
    private static final DeferredRegister<EntityVisualEffectType> TYPES =
            DeferredRegister.create(
                    VCatRegistries.ENTITY_VISUAL_EFFECT_TYPE_KEY,
                    MyMod.MODID);

    public static final RegistryObject<EntityVisualEffectType> MY_GLOW =
            TYPES.register("my_glow", () -> new EntityVisualEffectType(
                    EntityVisualEffectType.properties()
                            .defaultDuration(40)     // duration 传 0/省略时用的默认时长
                            .renderDistance(64.0D)   // 超出这个距离的实体不渲染
                            .persistent()            // 随存档保存
                            .renderInFirstPerson()   // 持有者自己第一人称时也渲染
                            .renderWhenInvisible()   // 实体隐身时照样渲染
            ));

    public static void init(IEventBus modEventBus) {
        TYPES.register(modEventBus);   // 在你的 @Mod 构造里调用
    }
}
```

特效的完整 id 就是 `你的modid:my_glow`。

### `EntityVisualEffectType.Properties`

| 方法 | 默认值 | 说明 |
|---|---|---|
| `defaultDuration(int)` | `EntityVisualEffects.INFINITE` | `addEffect` 传 0 时使用的时长（tick） |
| `infiniteDuration()` | — | 等价于 `defaultDuration(INFINITE)`，即 `-1` |
| `renderDistance(double)` | `64.0` | 渲染距离上限；`<= 0` 表示不限 |
| `persistent()` | `false` | 为 `true` 时随实体存档保存并跨重登保留 |
| `renderInFirstPerson()` | `false` | 允许渲染第一人称下的玩家自己 |
| `renderWhenInvisible()` | `false` | 实体 `isInvisible()` 时仍然渲染 |

### 注意事项

- 注册表用 `disableSync()` 建，**类型不会通过网络同步**。所以你的模组必须**服务端和客户端都安装**，
  否则客户端拿到 `get(id)` 会是 `null`，特效直接不渲染。
- **不要**调用 `VCatVisualEffects.register(eventBus)` —— 那是本模组自己注册内置类型用的，
  它的 `DeferredRegister` 绑定在 `vcat_visual_effects` 命名空间上，你调用会把条目注册到错误的 modid 下。
  请像上面那样建自己的 `DeferredRegister`。
- 运行时查询：`VCatVisualEffects.get(ResourceLocation)`、`VCatVisualEffects.isRegistered(id)`、
  `VCatVisualEffects.registeredIds()`。

---

## 三、加 / 删 / 查特效

**全部必须在服务端调用**（`entity.level().isClientSide()` 为 true 时 `addEffect` 会直接返回 `false`）。
同步、到期清理、新玩家进服补发、跨维度保留，框架都会自动处理。

```java
ResourceLocation id = new ResourceLocation(MyMod.MODID, "my_glow");

CompoundTag data = new CompoundTag();
data.putFloat("Radius", 6.0F);
data.putFloat("Intensity", 0.8F);

// 100 tick
EntityVisualEffectSystem.addEffect(entity, id, 100, data);

// 永久（不会到期）
EntityVisualEffectSystem.addEffect(entity, id, EntityVisualEffects.INFINITE, data);

// 用类型自带的 defaultDuration
EntityVisualEffectSystem.addEffect(entity, id, 0, data);

// 也可以用 ResourceKey
EntityVisualEffectSystem.addEffect(entity, MY_GLOW.getKey(), 100, data);

// 查询 / 移除
boolean has = EntityVisualEffectSystem.hasEffect(entity, id);
EntityVisualEffectSystem.removeEffect(entity, id);
```

| 方法 | 说明 |
|---|---|
| `addEffect(Entity, ResourceLocation, int)` | 无 data，`durationTicks` 见下表 |
| `addEffect(Entity, ResourceLocation, int, CompoundTag)` | 同上，带 data |
| `addEffect(Entity, ResourceKey<EntityVisualEffectType>, int[, CompoundTag])` | `ResourceKey` 重载 |
| `removeEffect(Entity, ResourceLocation)` / `(Entity, ResourceKey<...>)` | 移除单个特效 |
| `hasEffect(Entity, ResourceLocation)` | 是否已挂该特效 |
| `getEffects(Entity)` | 拿到容器本体（见第四节），可能为 `null` |
| `sync(Entity)` | 手动重发同步包（见第四节） |

`durationTicks` 取值：

| 值 | 含义 |
|---|---|
| `-1` / `EntityVisualEffects.INFINITE` | 永久 |
| `0` | 用类型的 `defaultDuration` |
| `> 0` | 持续多少 tick |

`addEffect` 会自动往你的 data 里补两个东西：

- `StartGameTime`：特效开始时的游戏时间（**已有则不会覆盖**），
  `progress()` / `growthScale()` 和所有淡入展开动画都靠它；
- `AnchorX` / `AnchorY` / `AnchorZ`：仅对内置的「地狱火焰」类特效自动写入，把你的实体位置锁成世界锚点。

---

## 四、读取与更新已经挂上的特效

```java
EntityVisualEffects effects = EntityVisualEffectSystem.getEffects(entity);
if (effects == null) {
    return;
}

ActiveEntityVisualEffect active = effects.get(id);
if (active != null) {
    CompoundTag updated = active.data().copy();
    updated.putFloat("Intensity", 1.0F);

    // 关键：想把「展开 + 淡入」保留在原来的时间轴上，就必须把旧的 StartGameTime 写回去，
    // 否则每一次刷新都会从 0 重新播一遍。
    if (active.data().contains(ActiveEntityVisualEffect.START_GAME_TIME)) {
        updated.putLong(ActiveEntityVisualEffect.START_GAME_TIME,
                active.data().getLong(ActiveEntityVisualEffect.START_GAME_TIME));
    }

    active.setData(updated);               // setData 内部会 copy
    EntityVisualEffectSystem.sync(entity); // 改完必须同步，否则客户端看不到
}
```

### `EntityVisualEffects`（容器）

| 方法 | 说明 |
|---|---|
| `get(ResourceLocation)` | 取单个特效，没有则 `null` |
| `has(ResourceLocation)` / `isEmpty()` | 查询 |
| `effects()` | 只读的 `Collection<ActiveEntityVisualEffect>` |
| `add(...)` / `remove(...)` / `clear()` | 低层增删；除非你知道自己在做什么，否则用 `EntityVisualEffectSystem` |
| `serializeNBT(...)` / `deserializeNBT(...)` | 序列化 |

### `ActiveEntityVisualEffect`（实例）

| 方法 | 说明 |
|---|---|
| `id()` | 特效 id |
| `data()` | 你的 data（**返回的是内部引用**，改之前先 `copy()`） |
| `setData(CompoundTag)` | 替换 data（内部 copy） |
| `initialDuration()` | 初始时长；`-1` 表示永久 |
| `remainingTicks()` / `expiresAtGameTime()` / `isExpired(long)` | 剩余时间 |
| `startGameTime()` | 开始时间；未记录时返回 `-1` |
| `progress(gameTime, partialTick)` | 生命进度 `0 → 1`（用开始时间推算，双端算法一致） |
| `growthScale(gameTime, partialTick)` | 入场展开系数，受 `GrowTicks` / `GrowFrom` 控制 |

`gameTime` 用 `entity.level().getGameTime()`，`partialTick` 用 `event.getPartialTick()`。

---

## 五、写你自己的渲染器（世界空间）

渲染器是客户端的概念，注册要在 `Dist.CLIENT` 阶段做：

```java
@Mod.EventBusSubscriber(modid = MyMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD,
                        value = Dist.CLIENT)
public final class MyClientSetup {

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        EntityVisualEffectRenderers.register(
                new ResourceLocation(MyMod.MODID, "my_glow"),
                (renderEvent, entity, effect) -> {
                    // renderEvent: RenderLevelStageEvent（此时 stage 是 AFTER_PARTICLES）
                    // entity:      挂着这个特效的实体
                    // effect:      实例，effect.id() / effect.data() / effect.progress(...) / growthScale(...)
                    float intensity = effect.data().contains("Intensity")
                            ? effect.data().getFloat("Intensity")
                            : 1.0F;
                    // ... 用 PoseStack / BufferBuilder 画你的几何体
                });
    }
}
```

```java
@FunctionalInterface
public interface EntityVisualEffectRenderer {
    void render(RenderLevelStageEvent event, Entity entity, ActiveEntityVisualEffect effect);
}
```

分发器已经帮你做过这些过滤，你在渲染器里**不用重复判断**：

- 实体距离超过 `renderDistance` → 跳过；
- 实体隐身且类型没开 `renderWhenInvisible()` → 跳过；
- 玩家自己且 `renderInFirstPerson()` 没开、当前又是第一人称 → 跳过；
- 类型或渲染器没注册 → 跳过。

想画世界空间几何体，可以照抄本模组内置渲染器的套路：用 `RegisterShadersEvent` 注册一个 core shader，
在渲染器里 `RenderSystem.setShader` 后按 `POSITION_COLOR_TEX` 之类的格式提交顶点。
着色器资源放在 `assets/<你的modid>/shaders/core/<名字>.{json,vsh,fsh}`。

> 记住：`gradlew build` **不会**编译 GLSL，着色器错误只会在游戏运行时暴露，而且一旦有一个加载失败，
> 整条屏幕空间特效链都可能一起静默失效。改完着色器请务必进游戏验证。

---

## 六、屏幕空间（领域类）特效

内置的领域效果（`cosmic_domain`、`lunar_domain`、`blueprint_domain`、`thunder_domain`、
`mirror_domain`、`clockwork_domain`、`sand_domain`、`flora_domain`）不是世界空间几何体，
而是走一次全屏的深度重建后处理：`assets/vcat_visual_effects/shaders/program/entity_depth_reconstruct.*`。

这条链的调度点是写死的（效果 id → 内部 mode 编号的映射在
`visual/client/ScreenSpaceDepthEffectPostProcessor` 里），**目前没有对外的注册入口**，
所以第三方模组不能直接新增自己的屏幕空间模式。要做需要自己实现一套 `PostChain`，
本模组的 `EntityVisualEffectSystem` 仍然可以负责挂载与同步。

另外，使用光影（Oculus / Iris）时，屏幕空间链需要等光影自己的合成 pass 结束才能拿到深度缓冲，
这点由内置的 `compat/ShaderModCompat` 反射桥处理，接入方不用管。

---

## 七、data 键参考

### 框架级（由 `EntityVisualEffectSystem` / `ActiveEntityVisualEffect` 处理，全部特效通用）

| 键 | 类型 | 说明 |
|---|---|---|
| `StartGameTime` | `long` | 自动写入，已有则不覆盖。动画时间轴的基准 |
| `GrowTicks` | `int` | 入场展开时长（默认 `12`）；`<= 0` 关闭，瞬间成型。有限时长的特效最多用生命的前 1/3 做展开 |
| `GrowFrom` | `float` | 展开起始比例（默认 `0.15`，`0~1`） |
| `KeepStart` | `boolean` | 重新 `addEffect` 时保留旧的 `StartGameTime`（循环命令方块专用，避免每 tick 重播展开/淡入） |
| `AnchorX` / `AnchorY` / `AnchorZ` | `double` | 世界锚点。对内置地狱火焰类特效由系统自动写入 |

### 内置渲染器读取的键（供参考）

你自己的渲染器可以自由定义键名；用同样的名字只是方便和内置特效保持一致。

| 键 | 读取方 | 说明 |
|---|---|---|
| `Radius` | 多数特效 | 半径 |
| `Intensity` | 多数特效 | 亮度倍率 |
| `Height` / `Width` / `Depth` | 火焰、光柱类 | 体积尺寸 |
| `Scale` | 光环、王冠、星盘类 | 整体缩放 |
| `Yaw` | 猫头雾场 | 朝向 |
| `YOffset` / `BehindOffset` / `SideOffset` / `ForwardOffset` | 光环、王冠类 | 相对实体的位移 |
| `TiltDegrees` / `Tilt1Degrees` / `Tilt2Degrees` | 光环、星盘类 | 倾角 |
| `OrbitRadius` / `ShardCount` | 幻影裂片 | 轨道半径、碎片数 |
| `FadeIn` / `FadeOut` | 领域、雾场类 | 亮度淡入/淡出时长（tick） |
| `Tint` / `FogColor` | 领域类后处理 | 颜色，`[r,g,b]` 列表或 `0xRRGGBB` 整数 |
| `Color` / `CoreColor` / `TipColor` / `SmokeColor` | 体积火焰 | 火焰/焰心/焰尖/烟的颜色 |

> 小提示：`OrbitingSphereRenderer`、`PlayerHelixTrailRenderer`、`DepthVisualEffectRenderer`
> 这三个内置渲染器完全不吃 data，观感是写死的。

---

## 八、指令

需要权限等级 2（命令方块可用）。

```mcfunction
/vcatvisual add <targets> <effect> [duration] [data]
/vcatvisual remove <targets> <effect>
/vcatvisual clear <targets>
/vcatvisual random <targets> [count] [duration]
```

`data` 是 CompoundTag，可以省略 `duration` 直接写：

```mcfunction
# 给自己挂一个 25 格半径的宇宙领域，永久，且不重播展开
/vcatvisual add @s vcat_visual_effects:cosmic_domain -1 {Radius:25f,KeepStart:1}

# 给周围 8 格内的生物各随机挂 3 个特效，持续 200 tick
/vcatvisual random @e[type=!minecraft:player,distance=..8] 3 200

# 清空
/vcatvisual clear @e[type=!minecraft:player,distance=..8]
```

`random` 的 `count` 上限是 64，省略时默认为 1；省略 `duration` 时默认 200 tick。

---

## 九、内置特效清单（41 个）

**世界空间（24）**
`orbit_sphere`、`helix_trail`、`depth_occluded_halo`、`contact_edge_glow`、`soft_trail`、
`screen_space_shockwave`、`depth_refraction_heatwave`、`volumetric_light_column`、`outline_scan`、
`block_crack_light`、`red_eye_flash`、`tilted_halo`、`doom_corona`、`abyssal_rift_eye`、
`holy_judgement_halo`、`astral_crown`、`blood_moon_backwheel`、`causal_chains`、
`inverted_cross_mark`、`depth_refraction_pressure`、`volumetric_flame`、`phantom_rift_shards`、
`supreme_chaos_cosmos`、`black_cat_head_fog_field`

**屏幕空间领域（17）**
`malevolent_shrine_domain`、`malevolent_shrine_target_glow`、`malevolent_shrine_slash`、
`malevolent_shrine_fire`、`malevolent_shrine_fire_legacy`、`malevolent_shrine_black_domain`、
`malevolent_shrine_black_mist`、`malevolent_shrine_void`、`malevolent_shrine_starfield`、
`cosmic_domain`、`lunar_domain`、`blueprint_domain`、`thunder_domain`、`mirror_domain`、
`clockwork_domain`、`sand_domain`、`flora_domain`

---

## 十、配置

`config/vcat_visual_effects-common.toml`

| 键 | 默认 | 说明 |
|---|---|---|
| `skeletonEye.enabled` | `false` | 骷髅把目标锁定到生命值低于 6 的玩家时，播放一次红眼闪光 |

---

## 十一、许可

本项目采用自定义协议 **VCatNC-1.0**，要点：

- **允许**非商业地使用、修改、分发代码与着色器；
- **必须**以声明式依赖引用本模组，不得把代码复制/内嵌进自己的 jar；
- 衍生作品必须沿用同一协议；
- 贴图、模型、音效、Logo 等美术资源保留所有权利；
- 商业用途需另行取得授权。

完整条款见 [LICENSE.md](LICENSE.md)。
