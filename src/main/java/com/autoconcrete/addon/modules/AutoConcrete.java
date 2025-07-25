package com.autoconcrete.addon.modules;

import com.autoconcrete.addon.Xenon;
import meteordevelopment.meteorclient.events.game.OpenScreenEvent;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.*;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.entity.EntityUtils;
import meteordevelopment.meteorclient.utils.entity.SortPriority;
import meteordevelopment.meteorclient.utils.entity.TargetUtils;
import meteordevelopment.meteorclient.utils.player.FindItemResult;
import meteordevelopment.meteorclient.utils.player.InvUtils;
import meteordevelopment.meteorclient.utils.world.BlockUtils;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.gui.screen.ingame.AnvilScreen;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class AutoConcrete extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<Double> range = sgGeneral.add(new DoubleSetting.Builder()
        .name("range").defaultValue(4).min(0).sliderMax(6).build());

    private final Setting<Integer> concreteCount = sgGeneral.add(new IntSetting.Builder()
        .name("concrete-count").description("How many concrete blocks to drop at once.")
        .defaultValue(1).min(1).max(3).sliderMax(3).build());

    private final Setting<Integer> pillarDelay = sgGeneral.add(new IntSetting.Builder()
        .name("pillar-delay").defaultValue(30).min(0).sliderMax(100).build());

    private final Setting<Integer> concreteDelay = sgGeneral.add(new IntSetting.Builder()
        .name("concrete-delay").defaultValue(50).min(0).sliderMax(100).build());

    private final Setting<Boolean> rotate = sgGeneral.add(new BoolSetting.Builder()
        .name("rotate").defaultValue(true).build());

    private final Setting<Boolean> detectCrystals = sgGeneral.add(new BoolSetting.Builder()
        .name("detect-crystals").defaultValue(true).build());

    private final Setting<Boolean> airPlace = sgGeneral.add(new BoolSetting.Builder()
        .name("air-place").defaultValue(false).build());

    private final Setting<Boolean> placeSupport = sgGeneral.add(new BoolSetting.Builder()
        .name("place-support").defaultValue(true).build());

    private final Setting<Boolean> disableOnUse = sgGeneral.add(new BoolSetting.Builder()
        .name("disable-on-use").description("Turns off the module after placing concrete.")
        .defaultValue(false).build());

    private PlayerEntity target;
    private BlockPos basePos;
    private BlockPos[] concretePositions;
    private BlockPos lastTargetPos;
    private Direction placedDirection;
    private int currentPillarHeight;
    private int cooldown = 0;

    public AutoConcrete() {
        super(Xenon.XENON_CATEGORY, "auto-concrete", "Drops concrete above enemy's head with optional obsidian pillar.");
    }

    @Override
    public void onActivate() {
        reset();
    }

    private void reset() {
        basePos = null;
        placedDirection = null;
        lastTargetPos = null;
        currentPillarHeight = 1 + concreteCount.get();
        concretePositions = new BlockPos[concreteCount.get()];
        cooldown = 0;
    }

    @EventHandler
    private void onOpenScreen(OpenScreenEvent event) {
        if (event.screen instanceof AnvilScreen) event.cancel();
    }

    @EventHandler
    private void onTick(TickEvent.Pre event) {
        if (cooldown > 0) {
            cooldown--;
            return;
        }

        if (TargetUtils.isBadTarget(target, range.get())) {
            target = TargetUtils.getPlayerTarget(range.get(), SortPriority.LowestHealth);
            if (TargetUtils.isBadTarget(target, range.get())) return;
            reset();
        }

        BlockPos targetPos = target.getBlockPos();

        if (lastTargetPos != null && !lastTargetPos.equals(targetPos)) {
            info("Target moved. Resetting.");
            reset();
        }
        lastTargetPos = targetPos;

        FindItemResult obsidian = InvUtils.findInHotbar(stack -> Block.getBlockFromItem(stack.getItem()) == Blocks.OBSIDIAN);
        FindItemResult concrete = InvUtils.findInHotbar(stack -> Block.getBlockFromItem(stack.getItem()).getTranslationKey().contains("concrete_powder"));

        if (!concrete.found() || (!obsidian.found() && !airPlace.get())) return;

        boolean crystalPresent = detectCrystals.get() && isCrystalOnSurround(target);
        currentPillarHeight = 1 + concreteCount.get();
        if (crystalPresent) currentPillarHeight++;

        if (airPlace.get()) {
            for (int i = 0; i < concreteCount.get(); i++) {
                concretePositions[i] = targetPos.up(2 + i);
            }
        } else if (placeSupport.get()) {
            if (placedDirection == null || basePos == null) {
                for (Direction dir : Direction.Type.HORIZONTAL) {
                    BlockPos side = targetPos.offset(dir);
                    if (!mc.world.getBlockState(side).isAir()) {
                        boolean clear = true;
                        for (int i = 0; i < currentPillarHeight; i++) {
                            if (!mc.world.getBlockState(side.up(i + 1)).isReplaceable()) {
                                clear = false;
                                break;
                            }
                        }
                        if (clear) {
                            placedDirection = dir;
                            basePos = side.up(); // Start pillar above surround
                            break;
                        }
                    }
                }
            }

            if (basePos == null || placedDirection == null) return;

            // Place obsidian pillar
            boolean allPlaced = true;
            for (int i = 0; i < currentPillarHeight; i++) {
                BlockPos pos = basePos.up(i);
                if (!mc.world.getBlockState(pos).isOf(Blocks.OBSIDIAN)) {
                    BlockUtils.place(pos, obsidian, rotate.get(), 0);
                    cooldown = pillarDelay.get();
                    allPlaced = false;
                    break;
                }
            }

            if (!allPlaced) return;

            // FIXED: Place concrete beside pillar above target
            for (int i = 0; i < concreteCount.get(); i++) {
                concretePositions[i] = targetPos.up(2 + i);
            }
        }

        for (BlockPos pos : concretePositions) {
            if (mc.world.getBlockState(pos).isReplaceable()) {
                BlockUtils.place(pos, concrete, rotate.get(), 0);
            }
        }

        cooldown = concreteDelay.get();

        if (disableOnUse.get()) toggle();
    }

    private boolean isCrystalOnSurround(PlayerEntity target) {
        BlockPos pos = target.getBlockPos();
        for (Direction dir : Direction.Type.HORIZONTAL) {
            BlockPos surround = pos.offset(dir);
            for (net.minecraft.entity.Entity entity : mc.world.getEntities()) {
                if (entity instanceof EndCrystalEntity) {
                    if (entity.getBoundingBox().intersects(
                        surround.toCenterPos().add(-0.5, 0, -0.5),
                        surround.toCenterPos().add(0.5, 2.5, 0.5))) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public String getInfoString() {
        return target != null ? (airPlace.get() ? "AirPlace - " : "Pillar - ") + EntityUtils.getName(target) : null;
    }
}
