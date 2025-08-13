package com.autoconcrete.addon.modules;

import meteordevelopment.meteorclient.events.render.Render3DEvent;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.*;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.systems.friends.Friends;
import meteordevelopment.meteorclient.utils.render.color.SettingColor;
import meteordevelopment.meteorclient.utils.player.PlayerUtils;
import meteordevelopment.meteorclient.utils.player.InvUtils;
import meteordevelopment.meteorclient.utils.player.Rotations;
import meteordevelopment.meteorclient.utils.world.BlockUtils;
import meteordevelopment.meteorclient.renderer.ShapeMode;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import com.autoconcrete.addon.Xenon;

public class AutoCityPlus extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();
    private final SettingGroup sgRender = settings.createGroup("Render");

    private final Setting<Double> targetRange = sgGeneral.add(new DoubleSetting.Builder()
        .name("target-range")
        .description("Range to target players.")
        .defaultValue(5.5)
        .min(1)
        .sliderMax(6)
        .build()
    );

    private final Setting<Double> breakRange = sgGeneral.add(new DoubleSetting.Builder()
        .name("break-range")
        .description("Maximum block break range.")
        .defaultValue(4.5)
        .min(1)
        .sliderMax(6)
        .build()
    );

    private final Setting<Boolean> mineBedrock = sgGeneral.add(new BoolSetting.Builder()
        .name("mine-bedrock")
        .description("Allows mining bedrock blocks.")
        .defaultValue(false)
        .build()
    );

    // NEW: Prioritize bedrock the player is standing in (visible only when mining bedrock)
    private final Setting<Boolean> prioritizePlayerBedrock = sgGeneral.add(new BoolSetting.Builder()
        .name("prioritize-player-bedrock")
        .description("Prioritize mining the bedrock the target is standing in over surrounding blocks.")
        .defaultValue(true)
        .visible(mineBedrock::get)
        .build()
    );

    private final Setting<Boolean> ignoreFriends = sgGeneral.add(new BoolSetting.Builder()
        .name("ignore-friends")
        .description("Don't target players on your friends list.")
        .defaultValue(true)
        .build()
    );

    private final Setting<Boolean> ignoreNaked = sgGeneral.add(new BoolSetting.Builder()
        .name("ignore-naked")
        .description("Don't target players with no armor equipped.")
        .defaultValue(true)
        .build()
    );

    private final Setting<Boolean> support = sgGeneral.add(new BoolSetting.Builder()
        .name("support")
        .description("Places support block under break target if missing.")
        .defaultValue(true)
        .build()
    );

    private final Setting<Double> placeRange = sgGeneral.add(new DoubleSetting.Builder()
        .name("place-range")
        .description("How far to place support blocks.")
        .defaultValue(4.5)
        .min(1)
        .sliderMax(6)
        .visible(support::get)
        .build()
    );

    private final Setting<Boolean> rotate = sgGeneral.add(new BoolSetting.Builder()
        .name("rotate")
        .description("Rotate to block before mining.")
        .defaultValue(true)
        .build()
    );

    private final Setting<Boolean> chatInfo = sgGeneral.add(new BoolSetting.Builder()
        .name("chat-info")
        .description("Sends debug info in chat.")
        .defaultValue(true)
        .build()
    );

    private final Setting<Integer> chatDelay = sgGeneral.add(new IntSetting.Builder()
        .name("chat-delay")
        .description("Delay between chat messages in ticks.")
        .defaultValue(40)
        .min(0)
        .sliderMax(200)
        .build()
    );

    private final Setting<Boolean> swingHand = sgRender.add(new BoolSetting.Builder()
        .name("swing-hand")
        .description("Whether to swing your hand when mining.")
        .defaultValue(false)
        .build()
    );

    private final Setting<Boolean> renderBlock = sgRender.add(new BoolSetting.Builder()
        .name("render-block")
        .description("Renders the block being mined.")
        .defaultValue(true)
        .build()
    );

    private final Setting<ShapeMode> shapeMode = sgRender.add(new EnumSetting.Builder<ShapeMode>()
        .name("shape-mode")
        .description("How the render shape looks.")
        .defaultValue(ShapeMode.Both)
        .build()
    );

    private final Setting<SettingColor> sideColor = sgRender.add(new ColorSetting.Builder()
        .name("side-color")
        .description("Color of the sides of the block.")
        .defaultValue(new SettingColor(255, 0, 0, 75))
        .build()
    );

    private final Setting<SettingColor> lineColor = sgRender.add(new ColorSetting.Builder()
        .name("line-color")
        .description("Color of the lines of the block.")
        .defaultValue(new SettingColor(255, 0, 0, 255))
        .build()
    );

    private PlayerEntity target;
    private BlockPos targetPos;
    private int chatCooldown = 0;

    public AutoCityPlus() {
        super(Xenon.XENON_CATEGORY, "auto-city-plus", "Automatically mine blocks next to someone's feet.");
    }

    @Override
    public void onActivate() {
        target = null;
        targetPos = null;
        chatCooldown = 0;
    }

    // NEW: Safety check to avoid rotation/mining when you’re floating with a block above head
    private boolean shouldAllowRotation() {
        BlockPos playerPos = mc.player.getBlockPos();
        Block blockAtFeet = mc.world.getBlockState(playerPos).getBlock();
        Block blockAboveHead = mc.world.getBlockState(playerPos.up(1)).getBlock();

        // If standing in air AND there’s a block above head, skip rotate/mine (prevents scuffed rotation)
        if (blockAtFeet == Blocks.AIR && blockAboveHead != Blocks.AIR) return false;
        return true;
    }

    @EventHandler
    private void onTick(TickEvent.Post event) {
        if (chatCooldown > 0) chatCooldown--;

        target = null;
        double closestDistance = targetRange.get() * targetRange.get();

        for (PlayerEntity player : mc.world.getPlayers()) {
            if (player == mc.player || player.isCreative() || player.isSpectator()) continue;
            if (ignoreFriends.get() && Friends.get().isFriend(player)) continue;
            if (ignoreNaked.get() && isNaked(player)) continue;

            double distance = player.squaredDistanceTo(mc.player);
            if (distance <= closestDistance) {
                closestDistance = distance;
                target = player;
            }
        }

        if (target == null) return;

        // Prefer bedrock in the target's lower hitbox if enabled and in range
        boolean handledStandingBedrock = false;
        if (mineBedrock.get() && prioritizePlayerBedrock.get()) {
            BlockPos lowerHitboxPos = target.getBlockPos();
            Block lowerHitboxBlock = mc.world.getBlockState(lowerHitboxPos).getBlock();

            if (lowerHitboxBlock == Blocks.BEDROCK
                && PlayerUtils.squaredDistanceTo(lowerHitboxPos) <= breakRange.get() * breakRange.get()) {

                targetPos = lowerHitboxPos;
                handledStandingBedrock = true;

                if (chatInfo.get() && chatCooldown <= 0) {
                    info("Breaking bedrock in lower hitbox.");
                    chatCooldown = chatDelay.get();
                }
            }
        }

        // Otherwise pick a city block around the target
        if (!handledStandingBedrock) {
            targetPos = findCityBlock(target);
            if (targetPos == null) return;
        }

        if (PlayerUtils.squaredDistanceTo(targetPos) > breakRange.get() * breakRange.get()) return;

        if (support.get() && mc.world.getBlockState(targetPos.down()).isAir()) {
            BlockUtils.place(targetPos.down(), InvUtils.findInHotbar(Items.OBSIDIAN), rotate.get(), 0, true);
        }

        boolean allowActions = shouldAllowRotation();

        if (rotate.get() && allowActions) {
            Rotations.rotate(Rotations.getYaw(targetPos), Rotations.getPitch(targetPos));
        }

        if (allowActions) {
            mc.interactionManager.updateBlockBreakingProgress(targetPos, Direction.UP);
            if (swingHand.get()) mc.player.swingHand(Hand.MAIN_HAND);
        }
    }

    private BlockPos findCityBlock(PlayerEntity target) {
        BlockPos pos = target.getBlockPos();
        for (Direction dir : Direction.Type.HORIZONTAL) {
            BlockPos offset = pos.offset(dir);
            Block block = mc.world.getBlockState(offset).getBlock();

            if (PlayerUtils.squaredDistanceTo(offset) > breakRange.get() * breakRange.get()) continue;

            if (mineBedrock.get() && block == Blocks.BEDROCK) return offset;
            else if (!mineBedrock.get() && block != Blocks.AIR && block != Blocks.BEDROCK) return offset;
        }
        return null;
    }

    private boolean isNaked(PlayerEntity player) {
        return player.getEquippedStack(EquipmentSlot.HEAD).isEmpty()
            && player.getEquippedStack(EquipmentSlot.CHEST).isEmpty()
            && player.getEquippedStack(EquipmentSlot.LEGS).isEmpty()
            && player.getEquippedStack(EquipmentSlot.FEET).isEmpty();
    }

    @EventHandler
    private void onRender(Render3DEvent event) {
        if (targetPos != null && renderBlock.get()) {
            event.renderer.box(targetPos, sideColor.get(), lineColor.get(), shapeMode.get(), 0);
        }
    }
}
