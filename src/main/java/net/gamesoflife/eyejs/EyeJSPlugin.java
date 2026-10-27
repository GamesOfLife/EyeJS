package net.gamesoflife.eyejs;

import dev.latvian.mods.kubejs.KubeJSPlugin;
import dev.latvian.mods.kubejs.registry.RegistryInfo;
import net.gamesoflife.eyejs.item.StructureEyeItemBuilder;

public class EyeJSPlugin extends KubeJSPlugin {
    @Override
    public void init() {
        RegistryInfo.ITEM.addType("eyejs:structure_eye", StructureEyeItemBuilder.class, StructureEyeItemBuilder::new);
    }
}
