This KubeJS plugin allows modpack developers to easily create custom structure-locating eyes, similar to those seen in Cataclysm. (The eyes are not consumed on use, have a cooldown of 3 seconds, and cannot be burned in fire)

**How to use it:**
In a startup script, you can register custom eyes like any other item, using the custom "structure_eye" builder.
- The "structures" field (required) is a list of both structures and structure tags, entries being strings and tags beginning with # symbols. Thrown eyes lead toward whichever listed structure/tag is closest. Invalid entries leave a warning in the log.
- The "trailColor" field (defaults to white) is a hex value (0x000000 format) that determines the color of the particle trail left behind by thrown eyes.

Below is a minimal example, locating a single structure, with a green colored trail:
```
StartupEvents.registry('item', event => {
  event.create('pillager_eye', 'eyejs:structure_eye')
    .displayName('Eye of Pillager')
    .texture('kubejs:item/pillager_eye')
    .structures('minecraft:pillager_outpost')
    .trailColor(0x1FCC4D)
})
```

Below is another example, locating a mix of structures and tags, with a gray trail:
```
StartupEvents.registry('item', event => {
  event.create('ruins_eye', 'eyejs:structure_eye')
    .displayName('Eye of Ruins')
    .texture('kubejs:item/ruins_eye')
    .structures(
      'minecraft:ancient_city',
      'minecraft:trail_ruins',
      '#minecraft:ocean_ruin',
      '#minecraft:ruined_portal'
    )
    .trailColor(0x9C9C9C)
})
```
