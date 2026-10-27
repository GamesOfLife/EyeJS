package net.gamesoflife.eyejs.client;

import net.gamesoflife.eyejs.client.renderer.StructureEyeProjectileRenderer;
import net.gamesoflife.eyejs.entity.EyeJSEntities;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;


public class EyeJSClient {
    public static void init(IEventBus bus) {
        bus.addListener(EyeJSClient::registerEntityRenderers);
    }

    private static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(EyeJSEntities.STRUCTURE_EYE_PROJECTILE.get(), StructureEyeProjectileRenderer::new);
    }
}
