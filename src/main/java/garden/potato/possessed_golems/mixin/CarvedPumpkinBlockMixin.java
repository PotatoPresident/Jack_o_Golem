package garden.potato.possessed_golems.mixin;

import garden.potato.possessed_golems.PossesableGolem;
import net.minecraft.block.Blocks;
import net.minecraft.block.CarvedPumpkinBlock;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CarvedPumpkinBlock.class)
public abstract class CarvedPumpkinBlockMixin {
	@Unique
	private boolean spawnedJackOLantern = false;

	@Inject(method = "trySpawnEntity", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z"))
	private void possessedGolems$checkPossessed(World world, BlockPos pos, CallbackInfo ci) {
		if (world.getBlockState(pos).isOf(Blocks.JACK_O_LANTERN)) {
			spawnedJackOLantern = true;
		}
	}

	@ModifyArg(method = "trySpawnEntity", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;spawnEntity(Lnet/minecraft/entity/Entity;)Z"))
	private Entity possessedGolems$spawnPossessed(Entity entity) {
		if (spawnedJackOLantern && entity instanceof PossesableGolem golem) {
			golem.setPossessed(true);
			spawnedJackOLantern = false;
		}

		return entity;
	}
}
