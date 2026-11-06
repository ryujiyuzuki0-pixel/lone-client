package dev.pulse.client.module.impl;

import dev.pulse.client.module.Category;
import dev.pulse.client.module.Module;
import dev.pulse.client.module.setting.BoolSetting;
import dev.pulse.client.module.setting.ChoiceSetting;
import dev.pulse.client.module.setting.SliderSetting;
import dev.pulse.client.ui.HudRenderer;
import dev.pulse.client.ui.Theme;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.core.registries.BuiltInRegistries;
//? if >=1.21.11 {
import net.minecraft.resources.Identifier;
//?} else {
/*import net.minecraft.resources.ResourceLocation;
*///?}
import net.minecraft.world.effect.MobEffectInstance;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class PotionModule extends Module {
    private static final String[] ROMAN = {"", " II", " III", " IV", " V", " VI", " VII", " VIII", " IX", " X"};

    private static final int ICON = 18;
    private static final int ROW = 22;

    /** sprite = "namespace:path" of the vanilla mob_effect sprite (e.g. minecraft:speed). */
    private record Entry(String name, String time, String sprite, boolean low) {}

    //? if >=1.21.11 {
    private static final Map<String, Identifier> SPRITES = new HashMap<>();
    //?} else {
    /*private static final Map<String, ResourceLocation> SPRITES = new HashMap<>();
    *///?}

    private final BoolSetting showTime = add(new BoolSetting("Show duration", true));
    private final BoolSetting showLevel = add(new BoolSetting("Show level", true));
    private final BoolSetting icon = add(new BoolSetting("Show icon", true));
    private final ChoiceSetting sort = add(new ChoiceSetting("Sort", 0, "Longest first", "Shortest first", "Name"));
    private final SliderSetting lowAt = add(new SliderSetting("Low warning", 0, 30, 1, 10, "s").zero("Off"), "Layout");
    private final SliderSetting maxRows = add(new SliderSetting("Max rows", 1, 12, 1, 8, ""), "Layout");

    public PotionModule() {
        super("potions", "Potion Effects", "Active effects with a countdown.", Category.HUD,
                true, 0.992f, 0.02f, false);
    }

    private List<Entry> entries(Minecraft mc) {
        List<Entry> out = new ArrayList<>();
        if (HudRenderer.editing() && (mc.player == null || mc.player.getActiveEffects().isEmpty())) {
            out.add(new Entry("Speed" + (showLevel.value ? " II" : ""), "0:42", "minecraft:speed", false));
            out.add(new Entry("Strength", "0:08", "minecraft:strength", lowAt.value >= 8));
            return out;
        }
        if (mc.player == null) return out;
        List<MobEffectInstance> list = new ArrayList<>(mc.player.getActiveEffects());
        switch (sort.index) {
            case 1 -> list.sort(Comparator.comparingInt(MobEffectInstance::getDuration));
            case 2 -> list.sort(Comparator.comparing((MobEffectInstance e) -> e.getEffect().value().getDisplayName().getString()));
            default -> list.sort(Comparator.comparingInt(MobEffectInstance::getDuration).reversed());
        }
        int cap = maxRows.intValue();
        for (MobEffectInstance e : list) {
            if (out.size() >= cap) break;
            int amp = Math.max(0, Math.min(ROMAN.length - 1, e.getAmplifier()));
            String name = e.getEffect().value().getDisplayName().getString() + (showLevel.value ? ROMAN[amp] : "");
            String time;
            boolean low = false;
            if (e.isInfiniteDuration()) {
                time = "**";
            } else {
                int s = e.getDuration() / 20;
                time = (s / 60) + ":" + String.format("%02d", s % 60);
                low = lowAt.value > 0 && s < lowAt.value;
            }
            var key = BuiltInRegistries.MOB_EFFECT.getKey(e.getEffect().value());
            out.add(new Entry(name, time, key == null ? "minecraft:speed" : key.getNamespace() + ":" + key.getPath(), low));
        }
        return out;
    }

    @Override
    public int width(Minecraft mc) {
        List<Entry> list = entries(mc);
        if (list.isEmpty()) return 0;
        int w = 0;
        for (Entry e : list) {
            int tw = Math.max(Theme.width(e.name()), showTime.value ? Theme.width(e.time()) : 0);
            w = Math.max(w, tw);
        }
        return (icon.value ? ICON + 4 : 0) + w + 2;
    }

    @Override
    public int height(Minecraft mc) {
        int n = entries(mc).size();
        return n == 0 ? 0 : n * ROW - (ROW - ICON);
    }

    //? if >=1.21.11 {
    private static Identifier sprite(String s) {
        return SPRITES.computeIfAbsent(s, k -> {
            int c = k.indexOf(':');
            return Identifier.fromNamespaceAndPath(k.substring(0, c), "mob_effect/" + k.substring(c + 1));
        });
    }
    //?} else {
    /*private static ResourceLocation sprite(String s) {
        return SPRITES.computeIfAbsent(s, k -> {
            int c = k.indexOf(':');
            return ResourceLocation.fromNamespaceAndPath(k.substring(0, c), "mob_effect/" + k.substring(c + 1));
        });
    }
    *///?}

    @Override
    public void draw(GuiGraphics g, Minecraft mc, int w, int h) {
        int y = 0;
        int textX = icon.value ? ICON + 4 : 0;
        for (Entry e : entries(mc)) {
            // Low-duration effects blink like they do in vanilla / Lunar.
            float a = 1f;
            if (e.low()) {
                a = 0.5f + 0.5f * (float) Math.cos(System.currentTimeMillis() / 1000.0 * Math.PI * 2.5);
                a = 0.35f + 0.65f * Math.max(0f, Math.min(1f, a));
            }
            if (icon.value) {
                g.blitSprite(RenderPipelines.GUI_TEXTURED, sprite(e.sprite()), 0, y, ICON, ICON,
                        ((int) (a * 255) << 24) | 0xFFFFFF);
            }
            int nameY = showTime.value ? y + 1 : y + (ICON - 8) / 2;
            text(g, e.name(), textX, nameY, textColor());
            if (showTime.value) text(g, e.time(), textX, y + 10, e.low() ? Theme.BAD : dimColor());
            y += ROW;
        }
    }
}
