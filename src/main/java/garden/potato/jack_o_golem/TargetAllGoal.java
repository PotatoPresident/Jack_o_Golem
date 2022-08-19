package garden.potato.jack_o_golem;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.TargetGoal;
import net.minecraft.entity.passive.GolemEntity;

public class TargetAllGoal extends TargetGoal<LivingEntity> {
	public TargetAllGoal(GolemEntity golemEntity) {
		super(golemEntity, LivingEntity.class, 0, true, true, LivingEntity::isMobOrPlayer);
	}

	@Override
	public boolean canStart() {
		return ((PossesableGolem) this.mob).isPossessed() && super.canStart();
	}

	@Override
	public void start() {
		super.start();
		this.mob.setDespawnCounter(0);
	}
}

