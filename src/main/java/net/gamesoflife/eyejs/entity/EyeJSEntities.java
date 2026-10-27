package net.gamesoflife.eyejs.entity;

import net.gamesoflife.eyejs.EyeJS;
import net.gamesoflife.eyejs.entity.projectile.StructureEyeProjectile;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class EyeJSEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPE = DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, EyeJS.MODID);

    public static final DeferredHolder<EntityType<?>,EntityType<StructureEyeProjectile>> STRUCTURE_EYE_PROJECTILE = ENTITY_TYPE.register("structure_eye_projectile", () -> EntityType.Builder.<StructureEyeProjectile>of(StructureEyeProjectile::new, MobCategory.MISC)
            .sized(0.25F, 0.25F)
            .clientTrackingRange(4)
            .updateInterval(4)
            .build(EyeJS.MODID + ":structure_eye_projectile"));
}
