package miyucomics.efhexs.actions

import at.petrak.hexcasting.api.casting.RenderedSpell
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getVec3
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota
import miyucomics.hexposition.iotas.getIdentifier
import net.minecraft.particle.DefaultParticleType
import net.minecraft.particle.ParticleType
import net.minecraft.registry.Registries
import net.minecraft.util.math.Vec3d

class OpPlayParticle : SpellAction {
	override val argc = 3
	override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
		val pos = args.getVec3(0, argc)
		env.assertVecInRange(pos)
		val id = args.getIdentifier(1, argc)
		if (!Registries.PARTICLE_TYPE.containsId(id))
			throw MishapInvalidIota.of(args[1], 0, "particle_id")
		val velocity = args.getVec3(2, argc)
		return SpellAction.Result(Spell(Registries.PARTICLE_TYPE.get(id)!!, pos, velocity), 0, listOf())
	}

	private data class Spell(val particle: ParticleType<*>, val pos: Vec3d, val velocity: Vec3d) : RenderedSpell {
		override fun cast(env: CastingEnvironment) {
			if (particle !is DefaultParticleType)
				return
			for (player in env.world.getPlayers())
				env.world.spawnParticles(player, particle, true, pos.x, pos.y, pos.z, 1, velocity.x, velocity.y, velocity.z, 1.0)
		}
	}
}