package miyucomics.efhexs.actions

import at.petrak.hexcasting.api.casting.asActionResult
import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapBadCaster
import miyucomics.efhexs.misc.PlayerEntityMinterface
import miyucomics.hexposition.iotas.IdentifierIota
import net.minecraft.entity.player.PlayerEntity

class OpGetSounds : ConstMediaAction {
	override val argc = 0
	override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
		if (env.castingEntity !is PlayerEntity)
			throw MishapBadCaster()
		val caster = env.castingEntity as PlayerEntityMinterface
		return caster.getSounds().map { IdentifierIota(it) }.asActionResult
	}
}