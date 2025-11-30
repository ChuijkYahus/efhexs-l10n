package miyucomics.efhexs.actions.particles

import at.petrak.hexcasting.api.casting.RenderedSpell
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getVec3
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota
import at.petrak.hexcasting.api.misc.MediaConstants
import miyucomics.efhexs.misc.ServerEffectsBacklog
import miyucomics.efhexs.networking.Serializer
import miyucomics.efhexs.networking.Serializers
import miyucomics.efhexs.networking.serializers.SimpleParticleInfo
import miyucomics.hexpose.iotas.getIdentifier
import net.minecraft.particle.DefaultParticleType
import net.minecraft.registry.Registries
import net.minecraft.util.Identifier
import net.minecraft.util.math.Vec3d

object OpPlaySimpleParticle : SpellAction {
	override val argc = 3
	override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
		val id = args.getIdentifier(0, argc)
		if (!Registries.PARTICLE_TYPE.containsId(id))
			throw MishapInvalidIota.of(args[0], 2, "particle_id")
		if (Registries.PARTICLE_TYPE.get(id) !is DefaultParticleType)
			throw MishapInvalidIota.of(args[0], 2, "simple_particle_id")
		val pos = args.getVec3(1, argc)
		env.assertVecInRange(pos)
		val velocity = args.getVec3(2, argc)
		return SpellAction.Result(Spell(id, pos, velocity), MediaConstants.DUST_UNIT / 32, listOf())
	}

	private data class Spell(val particle: Identifier, val pos: Vec3d, val velocity: Vec3d) : RenderedSpell {
		override fun cast(env: CastingEnvironment) {
			ServerEffectsBacklog.count += 1
			ServerEffectsBacklog.buffer.writeVarInt(Serializers.CREATE_SIMPLE_PARTICLE.ordinal)
			(Serializers.CREATE_SIMPLE_PARTICLE.serializer as Serializer<SimpleParticleInfo>).write(ServerEffectsBacklog.buffer, SimpleParticleInfo(particle, pos, velocity))
		}
	}
}