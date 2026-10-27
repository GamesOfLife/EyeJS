package net.gamesoflife.eyejs;

import com.mojang.logging.LogUtils;
import net.gamesoflife.eyejs.client.EyeJSClient;
import net.gamesoflife.eyejs.entity.EyeJSEntities;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import org.slf4j.Logger;

@Mod(EyeJS.MODID)
public class EyeJS {
    public static final String MODID = "eyejs";
    public static final Logger LOGGER = LogUtils.getLogger();
    public EyeJS(IEventBus bus, Dist dist, ModContainer modContainer) {
        EyeJSEntities.ENTITY_TYPE.register(bus);
        if (dist.isClient()) {
            EyeJSClient.init(bus);
        }
    }
}
