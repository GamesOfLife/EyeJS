package net.gamesoflife.eyejs;

import dev.latvian.mods.kubejs.plugin.KubeJSPlugin;
import dev.latvian.mods.kubejs.registry.BuilderTypeRegistry;
import net.gamesoflife.eyejs.item.StructureEyeItemBuilder;
import net.minecraft.core.registries.Registries;

public class EyeJSPlugin implements KubeJSPlugin {
    @Override
    public void registerBuilderTypes(BuilderTypeRegistry registry) {
        registry.of(Registries.ITEM, reg -> {
            reg.add("eyejs:structure_eye", StructureEyeItemBuilder.class, StructureEyeItemBuilder::new);
        });
    }
}
