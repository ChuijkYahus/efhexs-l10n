package miyucomics.efhexs.mixin;

import miyucomics.efhexs.misc.PlayerEntityMinterface;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;

import java.util.ArrayList;
import java.util.List;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin implements PlayerEntityMinterface {
	private List<Identifier> particles = new ArrayList<>();
	private List<Identifier> sounds = new ArrayList<>();

	@Override
	public List<Identifier> getParticles() {
		return particles;
	}

	@Override
	public List<Identifier> getSounds() {
		return sounds;
	}
}
