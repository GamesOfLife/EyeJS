package net.gamesoflife.eyejs.item;

import dev.latvian.mods.kubejs.item.ItemBuilder;
import net.gamesoflife.eyejs.item.custom.StructureEyeItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StructureEyeItemBuilder extends ItemBuilder {
    public StructureEyeItemBuilder(ResourceLocation i) {
        super(i);
        fireResistant = true;
        trailR = 255;
        trailG = 255;
        trailB = 255;
    }

    public transient List<String> structures = new ArrayList<>();
    public transient int trailR;
    public transient int trailG;
    public transient int trailB;

    @Override
    public Item createObject() {
        return new StructureEyeItem(createItemProperties(), structures, trailR, trailG, trailB);
    }

    public StructureEyeItemBuilder structures(String... ids) {
        Collections.addAll(this.structures, ids);
        return this;
    }

    public StructureEyeItemBuilder trailColor(int color) {
        trailR = (color >> 16) & 255;
        trailG = (color >> 8) & 255;
        trailB = color & 255;
        return this;
    }

}
