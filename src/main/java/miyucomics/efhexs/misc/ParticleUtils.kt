package miyucomics.efhexs.misc

import net.minecraft.block.BlockState
import net.minecraft.item.ItemStack
import net.minecraft.particle.*
import net.minecraft.registry.Registries
import net.minecraft.util.Identifier
import org.joml.Vector3f

object ParticleUtils {
	fun createParticleEffect(particleIdentifier: String, color: Vector3f, scale: Float, blockState: BlockState, itemStack: ItemStack): ParticleEffect? {
		val id = Identifier(particleIdentifier)
		val particleType = Registries.PARTICLE_TYPE.get(id)!!
		return when (particleType) {
			ParticleTypes.DUST -> DustParticleEffect(color, scale)
			ParticleTypes.BLOCK -> BlockStateParticleEffect(particleType as ParticleType<BlockStateParticleEffect>, blockState)
			ParticleTypes.FALLING_DUST -> BlockStateParticleEffect(particleType as ParticleType<BlockStateParticleEffect>, blockState)
			ParticleTypes.ITEM -> ItemStackParticleEffect(particleType as ParticleType<ItemStackParticleEffect>, itemStack)
			is DefaultParticleType -> particleType
			else -> null
		}
	}
}
