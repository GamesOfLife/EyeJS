package net.gamesoflife.eyejs.entity.projectile;

import net.gamesoflife.eyejs.entity.EyeJSEntities;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class StructureEyeProjectile extends Entity implements ItemSupplier {
    private static final EntityDataAccessor<ItemStack> DATA_ITEM_STACK = SynchedEntityData.defineId(StructureEyeProjectile.class, EntityDataSerializers.ITEM_STACK);
    private static final EntityDataAccessor<Integer> R = SynchedEntityData.defineId(StructureEyeProjectile.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> G = SynchedEntityData.defineId(StructureEyeProjectile.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> B = SynchedEntityData.defineId(StructureEyeProjectile.class, EntityDataSerializers.INT);


    private double tx;
    private double ty;
    private double tz;
    private int life;
    private Vec3[] trailPositions = new Vec3[64];
    private int trailPointer = -1;


    public StructureEyeProjectile(EntityType<? extends StructureEyeProjectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public StructureEyeProjectile(Level pLevel, double pX, double pY, double pZ) {
        this(EyeJSEntities.STRUCTURE_EYE_PROJECTILE.get(), pLevel);
        this.setPos(pX, pY, pZ);
    }



    public ItemStack getItem() {
        return this.getEntityData().get(DATA_ITEM_STACK);
    }

    public void setItem(ItemStack pStack) {
        this.getEntityData().set(DATA_ITEM_STACK, Util.make(pStack.copy(), (p_36978_) -> {
            p_36978_.setCount(1);
        }));
    }

    private ItemStack getItemRaw() {
        return this.getEntityData().get(DATA_ITEM_STACK);
    }


    public int getR()
    {
        return this.entityData.get(R);
    }

    public void setR(int r)
    {
        this.entityData.set(R, r);
    }

    public int getG()
    {
        return this.entityData.get(G);
    }

    public void setG(int g)
    {
        this.entityData.set(G, g);
    }


    public int getB()
    {
        return this.entityData.get(B);
    }

    public void setB(int b)
    {
        this.entityData.set(B, b);
    }


    protected void defineSynchedData(SynchedEntityData.Builder p_326229_) {
        p_326229_.define(DATA_ITEM_STACK, ItemStack.EMPTY);
        p_326229_.define(R, 0);
        p_326229_.define(G, 0);
        p_326229_.define(B, 0);
    }


    public void lerpMotion(double pX, double pY, double pZ) {
        this.setDeltaMovement(pX, pY, pZ);
        if (this.xRotO == 0.0F && this.yRotO == 0.0F) {
            double d0 = Math.sqrt(pX * pX + pZ * pZ);
            this.setYRot((float)(Mth.atan2(pX, pZ) * (double)(180F / (float)Math.PI)));
            this.setXRot((float)(Mth.atan2(pY, d0) * (double)(180F / (float)Math.PI)));
            this.yRotO = this.getYRot();
            this.xRotO = this.getXRot();
        }

    }

    public void signalTo(BlockPos pPos) {
        double d0 = (double)pPos.getX();
        int i = pPos.getY();
        double d1 = (double)pPos.getZ();
        double d2 = d0 - this.getX();
        double d3 = d1 - this.getZ();
        double d4 = Math.sqrt(d2 * d2 + d3 * d3);
        if (d4 > 12.0D) {
            this.tx = this.getX() + d2 / d4 * 12.0D;
            this.tz = this.getZ() + d3 / d4 * 12.0D;
            this.ty = this.getY() + 8.0D;
        } else {
            this.tx = d0;
            this.ty = (double)i;
            this.tz = d1;
        }

        this.life = 0;
    }

    public void tick() {
        if (this.getItem().isEmpty()) {
            this.discard();
        } else {
            super.tick();
            Vec3 vec3 = this.getDeltaMovement();
            double d0 = this.getX() + vec3.x;
            double d1 = this.getY() + vec3.y;
            double d2 = this.getZ() + vec3.z;
            double d3 = vec3.horizontalDistance();
            this.setXRot(lerpRotation(this.xRotO, (float) (Mth.atan2(vec3.y, d3) * (double) (180F / (float) Math.PI))));
            this.setYRot(lerpRotation(this.yRotO, (float) (Mth.atan2(vec3.x, vec3.z) * (double) (180F / (float) Math.PI))));
            if (!this.level().isClientSide) {
                double d4 = this.tx - d0;
                double d5 = this.tz - d2;
                float f = (float) Math.sqrt(d4 * d4 + d5 * d5);
                float f1 = (float) Mth.atan2(d5, d4);
                double d6 = Mth.lerp(0.0025D, d3, (double) f);
                double d7 = vec3.y;
                if (f < 1.0F) {
                    d6 *= 0.8D;
                    d7 *= 0.8D;
                }

                int j = this.getY() < this.ty ? 1 : -1;
                vec3 = new Vec3(Math.cos((double) f1) * d6, d7 + ((double) j - d7) * (double) 0.015F, Math.sin((double) f1) * d6);
                this.setDeltaMovement(vec3);
            }

            float f2 = 0.25F;
            if (this.isInWater()) {
                for (int i = 0; i < 4; ++i) {
                    this.level().addParticle(ParticleTypes.BUBBLE, d0 - vec3.x * 0.25D, d1 - vec3.y * 0.25D, d2 - vec3.z * 0.25D, vec3.x, vec3.y, vec3.z);
                }
            }

            Vec3 trailAt = this.position().add(0, this.getBbHeight() / 2F, 0);
            if (trailPointer == -1) {
                Vec3 backAt = trailAt;
                for (int i = 0; i < trailPositions.length; i++) {
                    trailPositions[i] = backAt;
                }
            }
            if (++this.trailPointer == this.trailPositions.length) {
                this.trailPointer = 0;
            }
            this.trailPositions[this.trailPointer] = trailAt;


            if (!this.level().isClientSide) {
                this.setPos(d0, d1, d2);
                ++this.life;
                if (this.life > 80 && !this.level().isClientSide) {
                    this.playSound(SoundEvents.ENDER_EYE_DEATH, 1.0F, 1.0F);
                    this.discard();
                }
            } else {
                this.setPosRaw(d0, d1, d2);
            }

        }
    }

    public static float lerpRotation(float pCurrentRotation, float pTargetRotation) {
        while(pTargetRotation - pCurrentRotation < -180.0F) {
            pCurrentRotation -= 360.0F;
        }

        while(pTargetRotation - pCurrentRotation >= 180.0F) {
            pCurrentRotation += 360.0F;
        }

        return Mth.lerp(0.2F, pCurrentRotation, pTargetRotation);
    }

    public void addAdditionalSaveData(CompoundTag pCompound) {
        ItemStack itemstack = this.getItemRaw();
        if (!itemstack.isEmpty()) {
            pCompound.put("Item", this.getItem().save(this.registryAccess()));
        }
        pCompound.putInt("R", this.getR());
        pCompound.putInt("G", this.getG());
        pCompound.putInt("B", this.getB());
    }

    public void readAdditionalSaveData(CompoundTag pCompound) {
        if (pCompound.contains("Item", 10)) {
            this.setItem(ItemStack.parse(this.registryAccess(), pCompound.getCompound("Item")).orElse(this.getDefaultItem()));
        } else {
            this.setItem(this.getDefaultItem());
        }
        this.setR(pCompound.getInt("R"));
        this.setG(pCompound.getInt("G"));
        this.setB(pCompound.getInt("B"));
    }

    private ItemStack getDefaultItem() {
        return new ItemStack(Items.ENDER_EYE);
    }

    public float getLightLevelDependentMagicValue() {
        return 1.0F;
    }

    public boolean isAttackable() {
        return false;
    }

    public Vec3 getTrailPosition(int pointer, float partialTick) {
        if (this.isRemoved()) {
            partialTick = 1.0F;
        }
        int i = this.trailPointer - pointer & 63;
        int j = this.trailPointer - pointer - 1 & 63;
        Vec3 d0 = this.trailPositions[j];
        Vec3 d1 = this.trailPositions[i].subtract(d0);
        return d0.add(d1.scale(partialTick));
    }

    public boolean hasTrail() {
        return trailPointer != -1;
    }

}
