package net.gamesoflife.eyejs;

import com.mojang.logging.LogUtils;
import net.gamesoflife.eyejs.client.EyeJSClient;
import net.gamesoflife.eyejs.entity.EyeJSEntities;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(EyeJS.MODID)
public class EyeJS {

    public static final String MODID = "eyejs";
    public static final Logger LOGGER = LogUtils.getLogger();

    public EyeJS() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        EyeJSEntities.ENTITY_TYPE.register(bus);
        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> EyeJSClient::init);
    }
}
