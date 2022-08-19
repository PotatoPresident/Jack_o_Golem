package garden.potato.jack_o_golem.client;

import net.fabricmc.fabric.api.client.rendering.v1.LivingEntityFeatureRendererRegistrationCallback;
import net.minecraft.client.render.entity.IronGolemEntityRenderer;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.client.ClientModInitializer;

public class JackOGolemClient implements ClientModInitializer {
	@Override
	public void onInitializeClient(ModContainer mod) {
		LivingEntityFeatureRendererRegistrationCallback.EVENT.register((entityType, entityRenderer, registrationHelper, context) -> {
			if (entityRenderer instanceof IronGolemEntityRenderer ironGolemEntityRenderer) {
				registrationHelper.register(new IronGolemPumpkinFeatureRenderer(ironGolemEntityRenderer, context.getBlockRenderManager(), context.getItemRenderer()));
			}
		});
	}
}
