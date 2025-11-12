package miyucomics.efhexs.actions.particles

import at.petrak.hexcasting.api.casting.asActionResult
import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapBadCaster
import miyucomics.efhexs.misc.PlayerEntityMinterface
import miyucomics.hexpose.iotas.IdentifierIota
import net.minecraft.entity.player.PlayerEntity

object OpGetParticles : ConstMediaAction {
	override val argc = 0
	override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
		if (env.castingEntity !is PlayerEntity)
			throw MishapBadCaster()
		val caster = env.castingEntity as PlayerEntityMinterface
		return caster.getParticles().buffer().map(::IdentifierIota).asActionResult
	}
}