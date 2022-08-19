package garden.potato.jack_o_golem.mixin;

import garden.potato.jack_o_golem.PossesableGolem;
import garden.potato.jack_o_golem.TargetAllGoal;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.passive.SnowGolemEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SnowGolemEntity.class)
public abstract class SnowGolemEntityMixin extends PathAwareEntity implements PossesableGolem {
	@Unique
	private static final TrackedData<Boolean> POSSESSED = DataTracker.registerData(SnowGolemEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

	protected SnowGolemEntityMixin(EntityType<? extends PathAwareEntity> entityType, World world) {
		super(entityType, world);
	}

	@Inject(method = "initGoals", at = @At("HEAD"))
	private void initGoals(CallbackInfo ci) {
		var golem = (SnowGolemEntity) (Object) this;
		this.goalSelector.add(2, new TargetAllGoal(golem));
	}

	@Inject(method = "initDataTracker", at = @At("RETURN"))
	private void jackOGolem$addDataTracker(CallbackInfo ci) {
		this.dataTracker.startTracking(POSSESSED, false);
	}

	@Inject(method = "sheared", at = @At("HEAD"))
	private void jackOGolem$sheared(CallbackInfo ci) {
		if (isPossessed()) {
			this.setPossessed(false);
		}
	}

	@Inject(method = "writeCustomDataToNbt", at = @At("RETURN"))
	private void jackOGolem$writeCustomDataToNbt(NbtCompound nbt, CallbackInfo ci) {
		nbt.putBoolean("Possessed", this.isPossessed());
	}

	@Inject(method = "readCustomDataFromNbt", at = @At("RETURN"))
	private void jackOGolem$readCustomDataFromNbt(NbtCompound nbt, CallbackInfo ci) {
		this.setPossessed(nbt.getBoolean("Possessed"));
	}

	@Override
	public void setPossessed(boolean possessed) {
		this.dataTracker.set(POSSESSED, possessed);
		if (!possessed) setTarget(null);
	}

	@Override
	public boolean isPossessed() {
		return this.dataTracker.get(POSSESSED);
	}
}
