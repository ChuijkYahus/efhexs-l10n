package miyucomics.efhexs.actions.particles

import at.petrak.hexcasting.api.casting.RenderedSpell
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getVec3
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.misc.MediaConstants
import miyucomics.efhexs.misc.ServerEffectsBacklog
import miyucomics.efhexs.networking.Serializer
import miyucomics.efhexs.networking.Serializers
import miyucomics.efhexs.networking.serializers.PotionParticleInfo
import net.minecraft.util.math.Vec3d

object OpPlayPotionEffect : SpellAction {
	override val argc = 3
	override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
		val position = args.getVec3(0, argc)
		env.assertVecInRange(position)
		val velocity = args.getVec3(1, argc)
		val color = args.getVec3(2, argc)
		return SpellAction.Result(Spell(position, velocity, color), MediaConstants.DUST_UNIT / 32, listOf())
	}

	private data class Spell(val position: Vec3d, val velocity: Vec3d, val color: Vec3d) : RenderedSpell {
		override fun cast(env: CastingEnvironment) {
			ServerEffectsBacklog.count += 1
			ServerEffectsBacklog.buffer.writeVarInt(Serializers.CREATE_POTION_PARTICLE.ordinal)
			(Serializers.CREATE_POTION_PARTICLE.serializer as Serializer<PotionParticleInfo>).write(ServerEffectsBacklog.buffer, PotionParticleInfo(position, velocity, color))
		}
	}
}