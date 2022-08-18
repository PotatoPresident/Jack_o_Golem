package garden.potato.possessed_golems.client;

import net.fabricmc.fabric.api.client.rendering.v1.LivingEntityFeatureRendererRegistrationCallback;
import net.minecraft.client.render.entity.IronGolemEntityRenderer;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.client.ClientModInitializer;

public class PossessedGolemsClient implements ClientModInitializer {
	@Override
	public void onInitializeClient(ModContainer mod) {
		LivingEntityFeatureRendererRegistrationCallback.EVENT.register((entityType, entityRenderer, registrationHelper, context) -> {
			if (entityRenderer instanceof IronGolemEntityRenderer ironGolemEntityRenderer) {
				registrationHelper.register(new IronGolemPumpkinFeatureRenderer(ironGolemEntityRenderer, context.getBlockRenderManager(), context.getItemRenderer()));
			}
		});
	}
}
