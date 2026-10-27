package net.gamesoflife.eyejs.item.custom;

import com.mojang.datafixers.util.Pair;
import net.gamesoflife.eyejs.EyeJS;
import net.gamesoflife.eyejs.entity.projectile.StructureEyeProjectile;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.levelgen.structure.Structure;

import java.util.*;

public class StructureEyeItem extends Item {
    private final List<String> targets;
    private final int Red;
    private final int Green;
    private final int Blue;

    public StructureEyeItem(Properties group,List<String> structures,int r,int g,int b) {
        super(group);
        this.targets = structures;
        this.Red = r;
        this.Green = g;
        this.Blue = b;
    }

    private HolderSet<Structure> resolveTargets(ServerLevel level) {
        Registry<Structure> registry = level.registryAccess().registryOrThrow(Registries.STRUCTURE);
        Set<Holder<Structure>> holders = new LinkedHashSet<>();

        for (String target : targets) {
            if (target.startsWith("#")) {
                ResourceLocation id = ResourceLocation.tryParse(target.substring(1));
                if (id == null) {
                    EyeJS.LOGGER.warn("Invalid structure tag '{}'", target);
                    continue;
                }
                TagKey<Structure> tag = TagKey.create(Registries.STRUCTURE, id);
                Optional<HolderSet.Named<Structure>> holderSet = registry.getTag(tag);
                holderSet.ifPresent(set -> {
                    for (Holder<Structure> holder : set) {
                        holders.add(holder);
                    }
                });
            } else {
                ResourceLocation id = ResourceLocation.tryParse(target);
                if (id == null) {
                    EyeJS.LOGGER.warn("Invalid structure '{}'", target);
                    continue;
                }
                ResourceKey<Structure> key = ResourceKey.create(Registries.STRUCTURE, id);
                Optional<Holder.Reference<Structure>> holder = registry.getHolder(key);
                holder.ifPresent(holders::add);
            }
        }
        return HolderSet.direct(holders.stream().toList());
    }

    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pHand) {
        ItemStack itemstack = pPlayer.getItemInHand(pHand);
        pPlayer.startUsingItem(pHand);
        if (pLevel instanceof ServerLevel) {
            ServerLevel serverlevel = (ServerLevel) pLevel;
            HolderSet<Structure> targets = resolveTargets(serverlevel);
            Pair<BlockPos, Holder<Structure>> result =
                    serverlevel.getChunkSource()
                            .getGenerator()
                            .findNearestMapStructure(
                                    serverlevel,
                                    targets,
                                    pPlayer.blockPosition(),
                                    100,
                                    false
                            );

            BlockPos pos = result != null ? result.getFirst(): null;
            if (pos != null) {
                StructureEyeProjectile eyeofender = new StructureEyeProjectile(pLevel, pPlayer.getX(), pPlayer.getY(0.5D), pPlayer.getZ());
                eyeofender.setItem(itemstack);
                eyeofender.setR(Red);
                eyeofender.setG(Green);
                eyeofender.setB(Blue);
                eyeofender.signalTo(pos);
                pLevel.gameEvent(GameEvent.PROJECTILE_SHOOT, eyeofender.position(), GameEvent.Context.of(pPlayer));
                pLevel.addFreshEntity(eyeofender);
                if (pPlayer instanceof ServerPlayer) {
                    CriteriaTriggers.USED_ENDER_EYE.trigger((ServerPlayer) pPlayer, pos);
                }

                pLevel.playSound((Player) null, pPlayer.getX(), pPlayer.getY(), pPlayer.getZ(), SoundEvents.ENDER_EYE_LAUNCH, SoundSource.NEUTRAL, 0.5F, 0.4F / (pLevel.getRandom().nextFloat() * 0.4F + 0.8F));
                pLevel.levelEvent((Player) null, 1003, pPlayer.blockPosition(), 0);
                pPlayer.getCooldowns().addCooldown(this, 60);
                pPlayer.awardStat(Stats.ITEM_USED.get(this));
                pPlayer.swing(pHand, true);
                return InteractionResultHolder.success(itemstack);
            }
        }

        return InteractionResultHolder.consume(itemstack);
    }
}