# VCat Visual Effects

**A visual-effects framework that gives any entity glowing auras, halos, volumetric flames and screen-space domains, synced to every client automatically.**

VCat Visual Effects attaches a visual-effect container to **every** entity in the world. Drop any number of effects into it, and the framework handles the rest — syncing state to tracking clients, expiring effects on time, and rendering them either as world-space geometry or as a full-screen screen-space pass.

It currently ships with **41 built-in effects** and a public API, so other mods can register their own effect types and renderers and use it as a dependency.

---

## Features

* **Works on any entity** — players, mobs, projectiles, even invisible markers. No per-entity setup required.
* **Automatic synchronisation** — effects are server-authoritative and pushed to every tracking client, including players who join later, change dimension, or die and respawn.
* **Two rendering paths** — world-space geometry with custom core shaders, or a depth-reconstructed full-screen post-processing pass.
* **Animation built in** — grow-in, fade-in and fade-out, and life progress are computed on both sides from a single start timestamp, with no per-tick packets.
* **No dependencies** — nothing beyond Minecraft Forge.
* **Shader-pack aware** — the screen-space pass detects Oculus / Iris through a reflective bridge and defers to the pack's own composite step so it can still read the depth buffer.

---

## Built-in effects

### World-space effects (16)

Rendered as geometry in the world, each with its own core shader.

`orbit_sphere` · `helix_trail` · `soft_trail` · `block_crack_light` · `red_eye_flash` · `tilted_halo` · `doom_corona` · `abyssal_rift_eye` · `holy_judgement_halo` · `astral_crown` · `blood_moon_backwheel` · `causal_chains` · `inverted_cross_mark` · `volumetric_flame` · `phantom_rift_shards` · `supreme_chaos_cosmos`

### Screen-space effects (25)

Rendered by a single depth-reconstruction post-processing chain, so they can occlude against and refract the real scene.

**Utility effects (8)**

`screen_space_shockwave` · `depth_occluded_halo` · `contact_edge_glow` · `outline_scan` · `volumetric_light_column` · `depth_refraction_heatwave` · `depth_refraction_pressure` · `black_cat_head_fog_field`

**Domain expansions (8)** — the showcase effects:

| Effect | What it looks like |
|---|---|
| `cosmic_domain` | A miniature galaxy inside the field sphere — tilted spiral arms, nebula, dense star field, polar jets, and a gravitational-lensing black hole at the centre |
| `lunar_domain` | A solid, cratered moon carved by SDF raymarching, with a hard terminator line, anti-gravity regolith and an eclipse-grade backlit halo |
| `blueprint_domain` | The whole scene collapses into a cyan surveyor's blueprint — contour lines by world height, mapping rings and spokes around the field axis, depth-discontinuity outlines |
| `thunder_domain` | World-anchored lightning bolts striking from the upper shell to the ground, leaving scorch marks, with a cold iron-grey cast and ground-hugging ionised mist |
| `mirror_domain` | Upward-facing surfaces become a mercury mirror via depth-reconstructed normals and screen-space reflection — flat ground, slopes and pits each mirror independently |
| `clockwork_domain` | Three analytic brass clockwork rings at different tilts and speeds, plus a clock face that travels up and down with height |
| `sand_domain` | A height-field dune basin whose sand level is driven by world-anchored noise and rises as the domain expands |
| `flora_domain` | Surface-growth projection — vines and flower clusters grow only on upward-facing ground, with world-anchored drifting petals |

**Domain components (9)**

`malevolent_shrine_domain` · `malevolent_shrine_slash` · `malevolent_shrine_target_glow` · `malevolent_shrine_fire` · `malevolent_shrine_fire_legacy` · `malevolent_shrine_black_domain` · `malevolent_shrine_black_mist` · `malevolent_shrine_void` · `malevolent_shrine_starfield`

> ### This catalogue keeps growing
>
> Everything listed above reflects the **current release**. Development is ongoing, so:
>
> * **More effects are on the way** — the total count will grow in future updates.
> * **Existing effects are still being refined** — shading, colour, timing and motion are
>   re-tuned as they are polished, and the full-screen effects continue to receive
>   performance optimisation.
>
> That means the exact number of effects, and the precise look of any single one, may
> change between versions. If you are describing this mod somewhere else, prefer
> "40+ effects" over a fixed number so your text does not go stale.

---

## Commands

Requires permission level 2 (command blocks work).

```
/vcatvisual add <targets> <effect> [duration] [data]
/vcatvisual remove <targets> <effect>
/vcatvisual clear <targets>
/vcatvisual random <targets> [count] [duration]
```

`duration` — `-1` for permanent, `0` or omitted to use the effect's default, or a tick count.

`data` is an optional NBT compound that tunes the effect. Common keys:

| Key | Meaning |
|---|---|
| `Radius` | Effect radius |
| `Intensity` | Brightness multiplier |
| `Tint` / `FogColor` | Colour, as `[r,g,b]` or `0xRRGGBB` |
| `GrowTicks` / `GrowFrom` | Grow-in duration (default 12 ticks) and start ratio (default 0.15) |
| `FadeIn` / `FadeOut` | Brightness ramp, in ticks |
| `KeepStart` | Keep the original start time on re-add, so the grow/fade animation does not replay |
| `AnchorX` / `AnchorY` / `AnchorZ` | Lock the effect to a world position instead of following the entity |

### Examples

Permanent 25-block cosmic domain centred on yourself, without replaying the grow-in animation every tick:

```
/vcatvisual add @s vcat_visual_effects:cosmic_domain -1 {Radius:25f,KeepStart:1}
```

Give every mob within 8 blocks three random effects lasting 200 ticks:

```
/vcatvisual random @e[type=!minecraft:player,distance=..8] 3 200
```

Clear everything nearby:

```
/vcatvisual clear @e[type=!minecraft:player,distance=..8]
```

---

## Requirements

| | |
|---|---|
| Minecraft | 1.20.1 |
| Forge | 47.3.22 or newer |
| Side | **Client and server both required** |
| Dependencies | None |

Both sides are required because the effect-type registry is not synced over the network. Install it on your server and on every connecting client, exactly like any other content mod.

---

## For developers

VCat Visual Effects is designed to be depended on. Register your own effect types into the shared `vcat_visual_effects:entity_visual_effect_types` registry, then attach them to any entity from the server side.

```java
private static final DeferredRegister<EntityVisualEffectType> TYPES =
        DeferredRegister.create(VCatRegistries.ENTITY_VISUAL_EFFECT_TYPE_KEY, MyMod.MODID);

public static final RegistryObject<EntityVisualEffectType> MY_GLOW =
        TYPES.register("my_glow", () -> new EntityVisualEffectType(
                EntityVisualEffectType.properties()
                        .defaultDuration(40)
                        .renderDistance(64.0D)
                        .persistent()
        ));
```

```java
CompoundTag data = new CompoundTag();
data.putFloat("Radius", 6.0F);
EntityVisualEffectSystem.addEffect(entity, new ResourceLocation(MyMod.MODID, "my_glow"), 100, data);
```

Client-side, bind a renderer to draw it:

```java
EntityVisualEffectRenderers.register(id, (event, entity, effect) -> {
    // draw your geometry during RenderLevelStageEvent
});
```

The framework already filters by render distance, invisibility and first-person camera before your renderer is called.

Full API documentation — effect type properties, the lifetime and synchronisation model, the `data` key reference, and how to write a world-space renderer — is in the [README on GitHub](https://github.com/VBlackCAT/VCatVisualEffects).

---

## Configuration

`config/vcat_visual_effects-common.toml`

| Key | Default | Description |
|---|---|---|
| `skeletonEye.enabled` | `false` | Play a red-eye flash when a skeleton targets a player below 6 health |

---

## License

VCat Visual Effects is released under a custom non-commercial license, **VCatNC-1.0**. In short:

* You **may** use, modify and redistribute the code and shaders for **non-commercial** purposes.
* You **must** reference this mod as a declared dependency — you may **not** copy its code, shaders or assets into your own jar.
* Derivative works must keep the same license.
* Textures, models, sounds and the logo remain all rights reserved.
* Commercial use requires separate written permission.

The full terms are in [LICENSE.md](https://github.com/VBlackCAT/VCatVisualEffects/blob/master/LICENSE.md). Using the code means you accept them.
