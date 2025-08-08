package com.autoconcrete.addon.modules;

import com.autoconcrete.addon.Xenon;
import meteordevelopment.meteorclient.events.game.ReceiveMessageEvent;
import meteordevelopment.meteorclient.settings.*;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.block.Block;
import net.minecraft.block.TrapdoorBlock;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.*;

public class AutoPearlTeleportOutput extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<String> mainName = sgGeneral.add(new StringSetting.Builder()
        .name("main-account-name")
        .description("Enter Main Account Username Here -->")
        .defaultValue("")
        .build()
    );

    private final Setting<Integer> threshold = sgGeneral.add(new IntSetting.Builder()
        .name("totem-threshold")
        .description("Totem threshold to teleport at. Sync with AutoPearlTeleport (Input).")
        .defaultValue(1)
        .min(0)
        .max(10)
        .sliderMax(10)
        .build()
    );

    public AutoPearlTeleportOutput() {
        super(Xenon.XENON_CATEGORY, "AutoPearlTeleport (Output)", "Teleports player to their enderpearl when totem threshold was reached. (!!!use this module on alt-account!!!)");
    }

    @EventHandler
    private void onMessageReceived(ReceiveMessageEvent event) {
        Text text = event.getMessage();
        String msg = text.getString().toLowerCase();

        if (mainName.get().isEmpty()) return;
        if (!msg.contains(mainName.get().toLowerCase() + " whispers")) return;
        if (!msg.contains("totem remaining")) return;

        int count = -1;
        for (String word : msg.split(" ")) {
            try {
                count = Integer.parseInt(word);
                break;
            } catch (NumberFormatException ignored) {}
        }

        if (count == -1 || count > 64) return;

        if (count <= threshold.get()) {
            useNearestTrapdoor();
        }
    }

    private void useNearestTrapdoor() {
        BlockPos closest = null;
        double closestDistance = Double.MAX_VALUE;

        BlockPos playerPos = mc.player.getBlockPos();
        BlockPos.Mutable pos = new BlockPos.Mutable();

        for (int x = -5; x <= 5; x++) {
            for (int y = -2; y <= 2; y++) {
                for (int z = -5; z <= 5; z++) {
                    pos.set(playerPos.getX() + x, playerPos.getY() + y, playerPos.getZ() + z);
                    Block block = mc.world.getBlockState(pos).getBlock();

                    if (block instanceof TrapdoorBlock) {
                        double dist = mc.player.getPos().squaredDistanceTo(Vec3d.ofCenter(pos));
                        if (dist < closestDistance) {
                            closest = pos.toImmutable();
                            closestDistance = dist;
                        }
                    }
                }
            }
        }

        if (closest != null) {
            Vec3d hitVec = Vec3d.ofCenter(closest);
            BlockHitResult hitResult = new BlockHitResult(hitVec, Direction.UP, closest, false);
            mc.getNetworkHandler().sendPacket(new PlayerInteractBlockC2SPacket(Hand.MAIN_HAND, hitResult, 0));
        }
    }
}
