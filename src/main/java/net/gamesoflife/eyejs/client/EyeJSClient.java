package net.gamesoflife.eyejs.client;

import net.gamesoflife.eyejs.client.renderer.StructureEyeProjectileRenderer;
import net.gamesoflife.eyejs.entity.EyeJSEntities;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class EyeJSClient {
    public static void init() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(EyeJSClient::registerRenderers);
    }

    private static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(EyeJSEntities.STRUCTURE_EYE_PROJECTILE.get(), StructureEyeProjectileRenderer::new);
    }
}
