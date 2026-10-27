package net.gamesoflife.eyejs.entity;

import net.gamesoflife.eyejs.EyeJS;
import net.gamesoflife.eyejs.entity.projectile.StructureEyeProjectile;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(modid = EyeJS.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class EyeJSEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPE = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, EyeJS.MODID);

    public static final RegistryObject<EntityType<StructureEyeProjectile>> STRUCTURE_EYE_PROJECTILE = ENTITY_TYPE.register("structure_eye_projectile", () -> EntityType.Builder.<StructureEyeProjectile>of(StructureEyeProjectile::new, MobCategory.MISC)
            .sized(0.25F, 0.25F)
            .clientTrackingRange(4)
            .updateInterval(4)
            .build(EyeJS.MODID + ":structure_eye_projectile"));
}
