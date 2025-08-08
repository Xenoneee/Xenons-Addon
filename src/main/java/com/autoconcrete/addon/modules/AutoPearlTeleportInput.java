package com.autoconcrete.addon.modules;

import com.autoconcrete.addon.Xenon;
import meteordevelopment.meteorclient.events.game.GameJoinedEvent;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.*;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.player.ChatUtils;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public class AutoPearlTeleportInput extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<String> altName = sgGeneral.add(new StringSetting.Builder()
        .name("alt-account-name")
        .description("Enter Alt Account Username Here -->")
        .defaultValue("")
        .build()
    );

    private final Setting<Integer> totemThreshold = sgGeneral.add(new IntSetting.Builder()
        .name("totem-threshold")
        .description("Only sends a /msg to the alt when reaching this totem count. Sync with AutoPearlTeleport (Output).")
        .defaultValue(1)
        .sliderMax(10)
        .build()
    );

    private final Setting<Boolean> forceTeleport = sgGeneral.add(new BoolSetting.Builder()
        .name("force-teleport")
        .description("Forces teleportation to the alt-account.")
        .defaultValue(false)
        .build()
    );

    private int lastCount = -1;
    private int joinCooldown = 60;
    private boolean shouldResetForceTeleport = false;

    public AutoPearlTeleportInput() {
        super(Xenon.XENON_CATEGORY, "AutoPearlTeleport (Input)", "Teleports you to your enderpearl when totem threshold was reached. (!!!use this module on main-account!!!)");
    }

    @Override
    public void onActivate() {
        lastCount = countTotems();
        joinCooldown = 60;
    }

    @EventHandler
    private void onJoin(GameJoinedEvent event) {
        joinCooldown = 60;
    }

    @EventHandler
    private void onTick(TickEvent.Post event) {
        if (joinCooldown > 0) {
            joinCooldown--;
            return;
        }

        int current = countTotems();

        if (forceTeleport.get()) {
            ChatUtils.sendPlayerMsg("/msg " + altName.get() + " " + current + " totem remaining");
            shouldResetForceTeleport = true;
        }

        if (shouldResetForceTeleport) {
            forceTeleport.set(false);
            shouldResetForceTeleport = false;
        }

        if (current != lastCount && current <= totemThreshold.get()) {
            ChatUtils.sendPlayerMsg("/msg " + altName.get() + " " + current + " totem remaining");
        }

        lastCount = current;
    }

    private int countTotems() {
        int count = 0;

        for (int i = 0; i < mc.player.getInventory().size(); i++) {
            ItemStack stack = mc.player.getInventory().getStack(i);
            if (stack.getItem() == Items.TOTEM_OF_UNDYING) {
                count += stack.getCount();
            }
        }

        ItemStack offhand = mc.player.getOffHandStack();
        if (offhand.getItem() == Items.TOTEM_OF_UNDYING) {
            count += offhand.getCount();
        }

        return count;
    }
}
