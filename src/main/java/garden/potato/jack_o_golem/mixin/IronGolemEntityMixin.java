package garden.potato.jack_o_golem.mixin;

import garden.potato.jack_o_golem.PossesableGolem;
import garden.potato.jack_o_golem.TargetAllGoal;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.Shearable;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(IronGolemEntity.class)
public abstract class IronGolemEntityMixin extends PathAwareEntity implements PossesableGolem, Shearable {
	@Unique
	private static final TrackedData<Boolean> POSSESSED = DataTracker.registerData(IronGolemEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

	protected IronGolemEntityMixin(EntityType<? extends PathAwareEntity> entityType, World world) {
		super(entityType, world);
	}

	@Inject(method = "initGoals", at = @At("HEAD"))
	private void initGoals(CallbackInfo ci) {
		var golem = (IronGolemEntity) (Object) this;
		this.goalSelector.add(2, new TargetAllGoal(golem));
	}

	@Inject(method = "initDataTracker", at = @At("RETURN"))
	private void jackOGolem$addDataTracker(CallbackInfo ci) {
		this.dataTracker.startTracking(POSSESSED, false);
	}

	@Inject(method = "canTarget", at = @At("HEAD"), cancellable = true)
	private void jackOGolem$targetPlayer(EntityType<?> type, CallbackInfoReturnable<Boolean> cir) {
		if (isPossessed()) {
			cir.setReturnValue(super.canTarget(type));
		}
	}

	@Inject(method = "interactMob", at = @At("HEAD"), cancellable = true)
	private void jackOGolem$interactMob(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
		ItemStack itemStack = player.getStackInHand(hand);
		if (itemStack.isOf(Items.SHEARS) && this.isShearable()) {
			this.sheared(SoundCategory.PLAYERS);
			this.emitGameEvent(GameEvent.SHEAR, player);
			if (!this.world.isClient) {
				itemStack.damage(1, player, playerx -> playerx.sendToolBreakStatus(hand));
			}

			cir.setReturnValue(ActionResult.success(this.world.isClient));
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

	@Override
	public void sheared(SoundCategory shearedSoundCategory) {
		this.world.playSoundFromEntity(null, this, SoundEvents.ENTITY_SNOW_GOLEM_SHEAR, shearedSoundCategory, 1.0F, 1.0F);
		if (!this.world.isClient()) {
			this.setPossessed(false);
			this.dropStack(new ItemStack(Items.CARVED_PUMPKIN), 1.7F);
		}

	}

	@Override
	public boolean isShearable() {
		return this.isAlive() && this.isPossessed();
	}
}
