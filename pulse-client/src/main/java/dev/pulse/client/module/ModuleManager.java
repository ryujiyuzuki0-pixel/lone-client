package dev.pulse.client.module;

import dev.pulse.client.module.impl.*;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class ModuleManager {
    private static final List<Module> MODULES = new ArrayList<>();
    private static ComboModule combo;
    private static ReachModule reach;
    private static SpotifyModule spotify;
    private static CrosshairModule crosshair;
    private static WaypointsModule waypoints;
    private static CapeModule capeModule;
    private static ThemeModule themeModule;
    private static ClientModule client;

    private ModuleManager() {}

    public static void init() {
        MODULES.clear();
        FpsModule fps = new FpsModule();
        CpsModule cps = new CpsModule();
        KeystrokesModule keys = new KeystrokesModule();
        ArmorModule armor = new ArmorModule();
        PotionModule potions = new PotionModule();
        CoordsModule coords = new CoordsModule();
        ZoomModule zoom = new ZoomModule();
        for (Module m : new Module[]{fps, cps, keys, armor, potions, coords, zoom}) m.enabled = true;

        combo = new ComboModule();
        reach = new ReachModule();
        spotify = new SpotifyModule();

        crosshair = new CrosshairModule();
        waypoints = new WaypointsModule();
        capeModule = new CapeModule();
        capeModule.enabled = true;
        ThemeModule theme = new ThemeModule();
        theme.enabled = true;
        themeModule = theme;
        client = new ClientModule();
        LowHealthModule lowHealth = new LowHealthModule();
        lowHealth.enabled = true;
        FpsBoostModule boost = new FpsBoostModule();
        boost.enabled = true;

        Collections.addAll(MODULES, fps, new PingModule(), coords, new ClockModule(), keys, potions,
                cps, combo, reach, armor, new TotemModule(),
                zoom, new ToggleSprintModule(), boost,
                theme, capeModule, crosshair, lowHealth,
                spotify,
                new WatermarkModule(), new InventoryHudModule(), new CooldownModule(),
                new ItemCountersModule(), waypoints, client);
        for (Module m : MODULES) {
            m.defaultEnabled = m.enabled;
            m.syncPosition();
        }
    }

    public static List<Module> all() {
        return MODULES;
    }

    /** Every module shown in the menu (hides the client-settings holder). */
    public static List<Module> listed() {
        List<Module> out = new ArrayList<>();
        for (Module m : MODULES) if (!m.hidden) out.add(m);
        return out;
    }

    public static List<Module> byCategory(Category c) {
        List<Module> out = new ArrayList<>();
        for (Module m : MODULES) if (!m.hidden && m.category == c) out.add(m);
        return out;
    }

    public static ClientModule client() {
        return client;
    }

    public static ThemeModule theme() {
        return themeModule;
    }

    public static Module byId(String id) {
        for (Module m : MODULES) if (m.id.equals(id)) return m;
        return null;
    }

    public static CrosshairModule crosshair() {
        return crosshair;
    }

    public static CapeModule cape() {
        return capeModule;
    }

    public static WaypointsModule waypoints() {
        return waypoints;
    }

    public static SpotifyModule spotify() {
        return spotify;
    }

    public static void tick(Minecraft mc) {
        for (Module m : MODULES) if (m.enabled) m.tick(mc);
    }

    public static void onAttack(Player p, Entity target) {
        if (combo != null && combo.enabled) combo.hit();
        if (reach != null && reach.enabled) reach.record(p, target);
    }

    public static void shutdown() {
        for (Module m : MODULES) if (m.enabled) m.onDisable();
    }

    public static void resetLayout() {
        for (Module m : MODULES) m.resetPosition();
    }
}
