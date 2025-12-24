# 💥 MaceShock

**Make every smash count!**
MaceShock is a lightweight plugin designed for Minecraft 1.21 that enhances the new **Mace** weapon mechanics. It turns a simple falling attack into an epic, ground-shaking event.

When you hit an enemy with a Mace while falling (Smash Attack), the plugin creates a shockwave that affects all nearby mobs.

## ✨ Features

*   **🌊 Physical Shockwave:** Enemies around your target are knocked back violently away from the impact point.
*   **⚔️ AOE Damage:** Deal splash damage to groups of mobs (perfect for Trial Chambers!).
*   **😵 Stun Effect:** Concussed enemies get a temporary Slowness effect.
*   **🎨 Immersive Visuals:** beautiful particle explosions and heavy impact sounds (using new 1.21 sound effects).
*   **⚙️ Fully Configurable:** Turn off features you don't need or tweak the power in `config.yml`.

## 🎮 How it works

1.  Equip a **Mace**.
2.  Jump or fall from a height (default: > 1.5 blocks).
3.  Hit an enemy.
4.  **BOOM!** Watch the sparks fly and mobs scatter.

## 📄 Configuration

Everything is adjustable in the `config.yml`:

```yaml
# Minimum fall distance to trigger the shockwave
min-fall-distance: 1.5

# Radius of the shockwave
radius: 4.0

# Strength of the push-back
knockback-strength: 1.2

# Visual and Sound effects
enable-particles: true
enable-sound: true

# Area of Effect Damage
aoe-damage:
  enabled: true
  amount: 5.0 # Damage in hearts/health points

# Stun Effect (Slowness)
stun-effect:
  enabled: true
  duration-ticks: 60 # 3 seconds
