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

// Food detection (1.21.x)
import net.minecraft.component.DataComponentTypes;

import com.autoconcrete.addon.Xenon;

public class AutoMinePlus extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();
    private final SettingGroup sgRender = settings.createGroup("Render");

    // Targeting & ranges
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

    // Bedrock options
    private final Setting<Boolean> mineBedrock = sgGeneral.add(new BoolSetting.Builder()
        .name("mine-bedrock")
        .description("Allows mining bedrock blocks around the target.")
        .defaultValue(false)
        .build()
    );

    private final Setting<Boolean> prioritizePlayerBedrock = sgGeneral.add(new BoolSetting.Builder()
        .name("prioritize-target-standing-bedrock")
        .description("Prioritize mining the bedrock the target is standing in over surrounding blocks.")
        .defaultValue(true)
        .visible(mineBedrock::get)
        .build()
    );

    // NEW: Clear your own upper hitbox bedrock (independent of mineBedrock)
    private final Setting<Boolean> clearUpperBedrock = sgGeneral.add(new BoolSetting.Builder()
        .name("clear-upper-bedrock")
        .description("If phased, automatically mine the bedrock at your upper hitbox to free AutoMine/AutoCrystal.")
        .defaultValue(true)
        .build()
    );

    // Filters
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

    // Placement/support
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

    // QoL
    private final Setting<Boolean> rotate = sgGeneral.add(new BoolSetting.Builder()
        .name("rotate")
        .description("Rotate to block before mining.")
        .defaultValue(true)
        .build()
    );

    // NEW: Pause while eating
    private final Setting<Boolean> pauseWhileEating = sgGeneral.add(new BoolSetting.Builder()
        .name("pause-while-eating")
        .description("Temporarily pauses AutoMinePlus while you're eating food.")
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

    // Render
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

    // Track eating state for clean one-time messages
    private boolean wasEating = false;

    public AutoMinePlus() {
        super(Xenon.XENON_CATEGORY, "AutoMinePlus", "Expanded Automine with bedrock utilities.");
    }

    @Override
    public void onActivate() {
        target = null;
        targetPos = null;
        chatCooldown = 0;
        wasEating = false;
    }

    // Safety: avoid weird rotates when floating with a block above head.
    // We will override this guard if we are explicitly clearing our own upper-bedrock.
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

        // --- Pause-while-eating early-out ---
        if (pauseWhileEating.get() && mc.player != null) {
            boolean eatingNow = mc.player.isUsingItem() && isFood(mc.player.getActiveItem());
            if (eatingNow) {
                if (!wasEating) {
                    if (chatInfo.get() && chatCooldown <= 0) {
                        info("Paused: eating.");
                        chatCooldown = chatDelay.get();
                    }
                    wasEating = true;
                }
                return; // do nothing while eating
            } else if (wasEating) {
                if (chatInfo.get() && chatCooldown <= 0) {
                    info("Resuming after eating.");
                    chatCooldown = chatDelay.get();
                }
                wasEating = false;
            }
        }
        // ------------------------------------

        // 1) NEW: Clear your own upper hitbox bedrock first (independent of targeting others)
        if (clearUpperBedrock.get()) {
            BlockPos headPos = mc.player.getBlockPos().up(1);
            Block headBlock = mc.world.getBlockState(headPos).getBlock();

            if (headBlock == Blocks.BEDROCK) {
                targetPos = headPos;

                // Optional support (rarely needed for your head block, but harmless)
                if (support.get() && mc.world.getBlockState(targetPos.down()).isAir()) {
                    BlockUtils.place(targetPos.down(), InvUtils.findInHotbar(Items.OBSIDIAN), rotate.get(), 0, true);
                }

                // Override anti-scuff: we DO want to rotate/mine this
                if (rotate.get()) {
                    Rotations.rotate(Rotations.getYaw(targetPos), Rotations.getPitch(targetPos));
                }

                mc.interactionManager.updateBlockBreakingProgress(targetPos, Direction.UP);
                if (swingHand.get()) mc.player.swingHand(Hand.MAIN_HAND);

                if (chatInfo.get() && chatCooldown <= 0) {
                    info("Clearing upper-hitbox bedrock.");
                    chatCooldown = chatDelay.get();
                }

                // Render handled below; exit early because this has priority.
                return;
            }
        }

        // 2) Acquire/refresh target
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

        if (target == null) {
            targetPos = null;
            return;
        }

        // 3) Prefer bedrock in the target's lower hitbox if enabled and in range
        boolean handledStandingBedrock = false;
        if (mineBedrock.get() && prioritizePlayerBedrock.get()) {
            BlockPos lowerHitboxPos = target.getBlockPos();
            Block lowerHitboxBlock = mc.world.getBlockState(lowerHitboxPos).getBlock();

            if (lowerHitboxBlock == Blocks.BEDROCK
                && PlayerUtils.squaredDistanceTo(lowerHitboxPos) <= breakRange.get() * breakRange.get()) {

                targetPos = lowerHitboxPos;
                handledStandingBedrock = true;

                if (chatInfo.get() && chatCooldown <= 0) {
                    info("Breaking bedrock in target's lower hitbox.");
                    chatCooldown = chatDelay.get();
                }
            }
        }

        // 4) Otherwise pick a city block around the target
        if (!handledStandingBedrock) {
            targetPos = findCityBlock(target);
            if (targetPos == null) return;
        }

        if (PlayerUtils.squaredDistanceTo(targetPos) > breakRange.get() * breakRange.get()) return;

        // Support placement if needed
        if (support.get() && mc.world.getBlockState(targetPos.down()).isAir()
            && PlayerUtils.squaredDistanceTo(targetPos.down()) <= placeRange.get() * placeRange.get()) {
            BlockUtils.place(targetPos.down(), InvUtils.findInHotbar(Items.OBSIDIAN), rotate.get(), 0, true);
        }

        boolean allowActions = shouldAllowRotation();
        // If our chosen targetPos happens to be our own head (unlikely here), allow anyway:
        if (targetPos.equals(mc.player.getBlockPos().up(1))) allowActions = true;

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

            if (mineBedrock.get() && block == Blocks.BEDROCK) {
                return offset;
            } else if (!mineBedrock.get() && block != Blocks.AIR && block != Blocks.BEDROCK) {
                return offset;
            }
        }
        return null;
    }

    private boolean isNaked(PlayerEntity player) {
        return player.getEquippedStack(EquipmentSlot.HEAD).isEmpty()
            && player.getEquippedStack(EquipmentSlot.CHEST).isEmpty()
            && player.getEquippedStack(EquipmentSlot.LEGS).isEmpty()
            && player.getEquippedStack(EquipmentSlot.FEET).isEmpty();
    }

    // Detect if an ItemStack is food (1.21.x data components)
    private boolean isFood(ItemStack stack) {
        return stack != null && !stack.isEmpty() && stack.get(DataComponentTypes.FOOD) != null;
    }

    @EventHandler
    private void onRender(Render3DEvent event) {
        if (targetPos != null && renderBlock.get()) {
            event.renderer.box(targetPos, sideColor.get(), lineColor.get(), shapeMode.get(), 0);
        }
    }
}
