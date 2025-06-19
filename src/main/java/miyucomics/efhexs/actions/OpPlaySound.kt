package miyucomics.efhexs.actions

import at.petrak.hexcasting.api.casting.RenderedSpell
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getDouble
import at.petrak.hexcasting.api.casting.getPositiveDoubleUnderInclusive
import at.petrak.hexcasting.api.casting.getVec3
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota
import miyucomics.hexposition.iotas.getIdentifier
import net.minecraft.registry.Registries
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvent
import net.minecraft.util.math.Vec3d

class OpPlaySound : SpellAction {
	override val argc = 4
	override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
		val pos = args.getVec3(0, argc)
		env.assertVecInRange(pos)
		val id = args.getIdentifier(1, argc)
		if (!Registries.SOUND_EVENT.containsId(id))
			throw MishapInvalidIota.of(args[1], 0, "sound_id")
		val volume = args.getPositiveDoubleUnderInclusive(2, 2.0, argc)
		val pitch = args.getPositiveDoubleUnderInclusive(3, 2.0, argc)
		return SpellAction.Result(Spell(Registries.SOUND_EVENT.get(id)!!, pos, volume.toFloat(), pitch.toFloat()), 0, listOf())
	}

	private data class Spell(val sound: SoundEvent, val pos: Vec3d, val volume: Float, val pitch: Float) : RenderedSpell {
		override fun cast(env: CastingEnvironment) {
			env.world.playSound(null, pos.x, pos.y, pos.z, sound, SoundCategory.NEUTRAL, volume, pitch)
		}
	}
}