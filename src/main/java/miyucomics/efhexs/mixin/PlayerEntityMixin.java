package miyucomics.efhexs.mixin;

import miyucomics.efhexs.misc.PlayerEntityMinterface;
import miyucomics.hexpose.utils.RingBuffer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin implements PlayerEntityMinterface {
	@Unique private final RingBuffer<Identifier> particles = new RingBuffer<>(32);
	@Unique private final RingBuffer<Identifier> sounds = new RingBuffer<>(32);

	@Override
	public @NotNull RingBuffer<Identifier> getParticles() {
		return particles;
	}

	@Override
	public @NotNull RingBuffer<Identifier> getSounds() {
		return sounds;
	}
}