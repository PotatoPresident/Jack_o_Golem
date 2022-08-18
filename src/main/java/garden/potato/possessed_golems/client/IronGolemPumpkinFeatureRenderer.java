package garden.potato.possessed_golems.client;

import garden.potato.possessed_golems.PossesableGolem;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.IronGolemEntityModel;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.item.Items;
import net.minecraft.util.math.Vec3f;

public class IronGolemPumpkinFeatureRenderer extends FeatureRenderer<IronGolemEntity, IronGolemEntityModel<IronGolemEntity>> {
	private final BlockRenderManager blockRenderManager;
	private final ItemRenderer itemRenderer;

	public IronGolemPumpkinFeatureRenderer(
			FeatureRendererContext<IronGolemEntity, IronGolemEntityModel<IronGolemEntity>> featureRendererContext,
			BlockRenderManager blockRenderManager,
			ItemRenderer itemRenderer
	) {
		super(featureRendererContext);
		this.blockRenderManager = blockRenderManager;
		this.itemRenderer = itemRenderer;
	}

	public void render(
			MatrixStack matrixStack,
			VertexConsumerProvider vertexConsumerProvider,
			int i,
			IronGolemEntity entity,
			float f,
			float g,
			float h,
			float j,
			float k,
			float l
	) {
		if (((PossesableGolem) entity).isPossessed()) {
			boolean bl = MinecraftClient.getInstance().hasOutline(entity) && entity.isInvisible();
			if (!entity.isInvisible() || bl) {
				matrixStack.push();
				this.getContextModel().getPart().getChild("head").rotate(matrixStack);
				matrixStack.translate(0.0, -0.44375, 0.0);
				matrixStack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(180.0F));
				matrixStack.scale(0.7F, -0.7F, -0.7F);
				if (bl) {
					BlockState blockState = Blocks.JACK_O_LANTERN.getDefaultState();
					BakedModel bakedModel = this.blockRenderManager.getModel(blockState);
					int n = LivingEntityRenderer.getOverlay(entity, 0.0F);
					matrixStack.translate(-0.5, -0.5, -0.5);
					this.blockRenderManager
							.getModelRenderer()
							.render(
									matrixStack.peek(),
									vertexConsumerProvider.getBuffer(RenderLayer.getOutline(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE)),
									blockState,
									bakedModel,
									0.0F,
									0.0F,
									0.0F,
									i,
									n
							);
				} else {
					this.itemRenderer
							.renderItem(
									entity,
									Items.JACK_O_LANTERN.getDefaultStack(),
									ModelTransformation.Mode.HEAD,
									false,
									matrixStack,
									vertexConsumerProvider,
									entity.world,
									i,
									LivingEntityRenderer.getOverlay(entity, 0.0F),
									entity.getId()
							);
				}

				matrixStack.pop();
			}
		}
	}
}
