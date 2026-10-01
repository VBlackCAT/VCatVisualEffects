# V猫的视觉特效 非商业许可协议 1.0

**VCat Visual Effects Non-Commercial License 1.0（简称 "VCatNC-1.0"）**

版权所有 (C) 2025 V_BlackCAT。保留所有权利。

> 本协议以中文版本为准。文末附英文译本，仅供参考；中文与英文表述不一致时，以中文为准。
> *The Chinese text of this license is authoritative. An English translation is provided at the
> end for convenience only; where the two differ, the Chinese text prevails.*

---

## 第 0 条　本协议的来源

本协议是**独立的自定义协议**，不是下列任何协议的官方版本，也不由它们背书。本协议在条款结构与措辞上
参考并沿用了以下既有协议的部分内容：

| 参考协议 | 沿用的部分 |
|---|---|
| **PolyForm Noncommercial License 1.0.0** | 「非商业目的」的定义方式与许可限制的整体结构 |
| **GNU Lesser General Public License v2.1** | 「以依赖方式使用」与「不得整体并入」的区分思路 |
| **Creative Commons BY-NC-SA 4.0** | 署名（BY）＋非商业（NC）＋相同方式共享（SA）的组合方式 |

本协议与上述协议不兼容，也不得互换使用。

---

## 第 1 条　定义

1. **「本模组」**指 V猫的视觉特效（VCat Visual Effects，mod id `vcat_visual_effects`），
   包括其 Java 源代码、GLSL 着色器、配置文件及其附带的全部文件。
2. **「软件部分」**指本模组中的 Java 源代码与 GLSL 着色器（`.java` / `.vsh` / `.fsh`）。
3. **「资源部分」**指本模组中的贴图、模型、音效、Logo、语言文件及其他美术与音频素材。
4. **「你」**指行使本协议所授予权利的个体或组织。
5. **「衍生作品」**指基于软件部分全部或部分内容创作的、包含其代码或着色器实质性部分的作品，
   包括但不限于第三方 Minecraft 模组、插件、整合包补丁与重新打包的版本。
6. **「依赖形式引用」**指你的作品在运行时不包含本模组的代码副本，而是通过载入器的模组依赖机制
   在运行时要求本模组同时存在，并直接调用本模组对外公开的类与方法。
7. **「商业目的」**见第 3.1 条。

---

## 第 2 条　授予的权利

在**完整遵守本协议全部条款**的前提下，版权人授予你一项全球范围、免费、非独占、不可再许可的许可，
允许你：

1. 复制、使用、修改软件部分；
2. 将软件部分与你的作品结合，并以依赖形式引用本模组；
3. 以源代码或编译产物的形式分发软件部分及其衍生作品。

本协议**不授予**任何商标权、专利权，也不授予对资源部分的任何权利。

---

## 第 3 条　条件

你行使上述权利，必须同时满足以下全部条件。

### 3.1　非商业性

你只能将软件部分用于**非商业目的**。

**「商业目的」**包括但不限于：

- 将本模组或其衍生作品作为商品出售，或要求付费后才能获取、下载、使用；
- 将本模组或其衍生作品置于付费墙、付费会员专属内容、订阅制服务之后；
- 通过广告、赞助或分成，从包含本模组或其衍生作品的发行物中直接获取收入；
- 将本模组或其衍生作品整体或部分地编入以营利为目的出售的整合包、服务器或服务中；
- 其他以获取金钱或其他商业利益为主要目的的使用方式。

**以下情形不构成商业目的**（属于允许的非商业使用）：

- 免费发布、免费分发的整合包、模组包与数据包；
- 接受玩家**自愿**捐赠的服务器（前提是捐赠不解锁本模组或其衍生作品的内容）；
- 使用本模组制作并发布的免费视频、直播、图文等内容；
- 个人学习、研究、测试与非公开的使用。

### 3.2　依赖义务（核心条款）

任何使用了软件部分全部或任何实质性部分的第三方作品，**必须**：

1. 以**依赖形式引用**本模组，即在你作品的 `META-INF/mods.toml`（Forge）或
   `META-INF/neoforge.mods.toml`（NeoForge）中声明对本模组的必需依赖，例如：

   ```toml
   [[dependencies.你的modid]]
       modId = "vcat_visual_effects"
       mandatory = true
       versionRange = "[1.0.0,)"
       ordering = "AFTER"
       side = "BOTH"
   ```

2. **不得**将本模组的类文件、GLSL 着色器或资源复制、内嵌、重新打包（vendoring）进你自己的 jar
   或其他发行物中；
3. 在你的作品说明页面上标明它依赖本模组，并给出本模组的获取地址。

只有在你**完全没有**使用软件部分的任何内容、仅通过公开接口与本模组进行可选交互时，
才可以使用 `mandatory = false`。

### 3.3　署名与声明保留

你必须在你的作品或其附带文档中，保留本协议全文或指向本协议的链接，以及原始的版权声明。
你不得删除、隐藏或修改软件部分中已有的版权声明、许可声明与作者署名。

### 3.4　相同方式共享

你对软件部分的修改、以及基于软件部分的衍生作品，必须以**本协议（VCatNC-1.0）或经版权人书面认可的
兼容协议**发布，不得改用限制更宽松的协议（例如 MIT、Apache-2.0、GPL、LGPL 或任何允许商业使用的协议）。
你不得对衍生作品的接收者施加任何超出本协议范围的额外限制。

### 3.5　资源部分

本协议**不授予**对资源部分的任何权利。除适用法律明确允许或事先取得版权人书面许可外，
你不得复制、修改或再分发本模组的贴图、模型、音效、Logo 与语言文件。
若你只是以依赖形式引用本模组，则不构成对资源部分的再分发。

---

## 第 4 条　商业授权

如你希望将软件部分用于商业目的，或者希望豁免第 3.2 条的依赖义务，
**必须事先取得版权人的书面授权**。取得授权后，你的使用范围以该授权文件为准，
本协议与之冲突的部分以授权文件为准。

---

## 第 5 条　协议终止

1. 你一旦违反本协议任何条款，本协议授予你的权利**自动终止**，无需版权人另行通知。
2. 若你在发现或被告知违约后的 **30 日**内完全纠正违约行为，并以书面形式通知版权人，
   则本协议授予的权利自纠正之日起恢复。
3. 本协议终止后，第 3.3 条、第 6 条与第 7 条继续有效。
4. 已经从你处合法获得本模组副本的第三方，只要其自身遵守本协议，其许可不因你的违约而终止。

---

## 第 6 条　免责声明

本模组按**「现状」**提供，不附带任何形式的明示或默示担保，包括但不限于对适销性、
特定用途适用性及不侵权的担保。使用本模组所造成的任何风险由你自行承担，
包括但不限于存档损坏、游戏崩溃、数据丢失与设备损害。

---

## 第 7 条　责任限制

在适用法律允许的最大范围内，版权人在任何情况下均不对因本协议或本模组产生的、
或与之相关的任何间接、附带、特殊、惩罚性或后果性损害承担责任，
亦不对任何利润损失、数据丢失或业务中断承担责任，
无论其基于合同、侵权或其他理论，即使版权人已被告知此类损害的可能性。

---

## 第 8 条　其他

1. 若本协议任何条款被认定为无效或不可执行，其余条款仍然完全有效。
2. 本协议构成你与版权人之间关于本模组的完整协议，取代此前的一切口头或书面沟通。
3. 版权人保留随时发布本协议新版本的权利。新版本不具有溯及力，
   你已按旧版本取得的许可在旧版本范围内继续有效。

---

---

# VCat Visual Effects Non-Commercial License 1.0 ("VCatNC-1.0")

*English translation — for convenience only. The Chinese text above is authoritative.*

Copyright (C) 2025 V_BlackCAT. All rights reserved.

**Section 0 — Origin.** This is an independent custom license, not an official version of, nor
endorsed by, any other license. Its structure and wording draw on the PolyForm Noncommercial
License 1.0.0 (the definition of non-commercial purpose and the overall shape of the
restrictions), the GNU LGPL v2.1 (the distinction between use-by-dependency and wholesale
incorporation), and Creative Commons BY-NC-SA 4.0 (attribution + non-commercial + share-alike).

**Section 1 — Definitions.** "The Mod" means VCat Visual Effects (mod id `vcat_visual_effects`).
"The Software" means its Java source and GLSL shaders. "The Assets" means its textures, models,
sounds, logo and language files. "Derivative Work" means anything incorporating a substantial part
of the Software. "Reference by dependency" means your work contains no copy of the Software and
instead declares a runtime mod dependency on it and calls its public API. "Commercial purpose" is
defined in Section 3.1.

**Section 2 — Grant.** Subject to full compliance with this license, you are granted a worldwide,
royalty-free, non-exclusive, non-sublicensable license to copy, use and modify the Software, to
combine it with your own work by reference by dependency, and to distribute the Software and
Derivative Works. No trademark or patent rights are granted, and no rights in the Assets are
granted.

**Section 3 — Conditions.**

*3.1 Non-commercial only.* You may use the Software for non-commercial purposes only. Commercial
purposes include selling it, paywalling it, monetising it through ads or revenue share, or
including it in a for-profit bundle, server or service. Free modpacks, donation-supported servers
where donations do not unlock content, free videos and streams, and private or academic use are
not commercial purposes.

*3.2 Dependency obligation (core clause).* Any third-party work using all or any substantial part
of the Software must (a) declare a required dependency on `vcat_visual_effects` in its
`META-INF/mods.toml` (Forge) or `META-INF/neoforge.mods.toml` (NeoForge) with `mandatory = true`;
(b) not copy, inline or repackage the Mod's classes, shaders or assets into its own jar; and
(c) state on its project page that it depends on the Mod and where to obtain it. `mandatory =
false` is permitted only if you use none of the Software and merely interact with the Mod
optionally.

*3.3 Attribution.* Keep this license, or a link to it, together with the original copyright
notice. Do not remove or alter existing notices.

*3.4 Share-alike.* Modifications and Derivative Works must be released under this license or a
compatible license approved in writing by the copyright holder. You may not relicense them under
a more permissive license, and you may not impose further restrictions on recipients.

*3.5 Assets.* No rights in the Assets are granted. You may not copy, modify or redistribute them
without prior written permission, except as expressly allowed by applicable law. Referencing the
Mod by dependency is not redistribution of the Assets.

**Section 4 — Commercial licensing.** Commercial use, or an exemption from Section 3.2, requires
prior written authorisation from the copyright holder.

**Section 5 — Termination.** Your rights terminate automatically upon any breach. They are
reinstated if you fully cure the breach within 30 days and notify the copyright holder in writing.
Sections 3.3, 6 and 7 survive termination. Third parties who lawfully obtained a copy from you
keep their own license while they comply.

**Section 6 — Disclaimer.** The Mod is provided "as is", without warranty of any kind, express or
implied. You bear all risk of use, including corrupted worlds, crashes, data loss and hardware
damage.

**Section 7 — Limitation of liability.** To the maximum extent permitted by law, the copyright
holder is not liable for any indirect, incidental, special, punitive or consequential damages, or
for lost profits, data or business, arising out of this license or the Mod.

**Section 8 — Miscellaneous.** If any provision is held invalid, the rest remains in force. This
license is the entire agreement regarding the Mod. The copyright holder may publish new versions;
they are not retroactive.
