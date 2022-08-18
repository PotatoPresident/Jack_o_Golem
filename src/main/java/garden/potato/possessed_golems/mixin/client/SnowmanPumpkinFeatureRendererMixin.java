package garden.potato.possessed_golems.mixin.client;

import garden.potato.possessed_golems.PossesableGolem;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.SnowmanPumpkinFeatureRenderer;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.SnowGolemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(SnowmanPumpkinFeatureRenderer.class)
public abstract class SnowmanPumpkinFeatureRendererMixin {
	@Redirect(
			method = "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/entity/passive/SnowGolemEntity;FFFFFF)V",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/block/Block;getDefaultState()Lnet/minecraft/block/BlockState;")
	)
	private BlockState getDefaultState(Block instance,
									   MatrixStack matrixStack,
									   VertexConsumerProvider vertexConsumerProvider,
									   int i,
									   SnowGolemEntity snowGolemEntity,
									   float f, float g, float h, float j, float k, float l
	) {
		if (snowGolemEntity instanceof PossesableGolem possessable && possessable.isPossessed()) {
			return Blocks.JACK_O_LANTERN.getDefaultState();
		} else {
			return instance.getDefaultState();
		}
	}

	@ModifyArg(
			method = "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/entity/passive/SnowGolemEntity;FFFFFF)V",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/item/ItemRenderer;renderItem(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelTransformation$Mode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;Lnet/minecraft/world/World;III)V"),
			index = 1
	)
	private ItemStack getDefaultState(@Nullable LivingEntity entity,
									  ItemStack itemStack,
									  ModelTransformation.Mode renderMode,
									  boolean leftHanded,
									  MatrixStack matrices,
									  VertexConsumerProvider vertexConsumers,
									  @Nullable World world,
									  int light,
									  int overlay,
									  int seed
	) {
		if (entity instanceof PossesableGolem possessable && possessable.isPossessed()) {
			return Items.JACK_O_LANTERN.getDefaultStack();
		} else {
			return itemStack;
		}
	}
}
