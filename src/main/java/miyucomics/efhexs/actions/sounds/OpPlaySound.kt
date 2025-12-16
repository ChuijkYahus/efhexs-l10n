package miyucomics.efhexs.actions.sounds

import at.petrak.hexcasting.api.casting.RenderedSpell
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getPositiveDoubleUnderInclusive
import at.petrak.hexcasting.api.casting.getVec3
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota
import at.petrak.hexcasting.api.misc.MediaConstants
import miyucomics.efhexs.misc.ServerEffectsBacklog
import miyucomics.efhexs.networking.Serializer
import miyucomics.efhexs.networking.Serializers
import miyucomics.efhexs.networking.serializers.SoundInfo
import miyucomics.hexpose.iotas.getIdentifier
import net.minecraft.registry.Registries
import net.minecraft.util.Identifier
import net.minecraft.util.math.Vec3d

object OpPlaySound : SpellAction {
	override val argc = 4
	override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
		val id = args.getIdentifier(0, argc)
		if (!Registries.SOUND_EVENT.containsId(id))
			throw MishapInvalidIota.of(args[0], 3, "sound_id")
		val pos = args.getVec3(1, argc)
		env.assertVecInRange(pos)
		val volume = args.getPositiveDoubleUnderInclusive(2, 2.0, argc)
		val pitch = args.getPositiveDoubleUnderInclusive(3, 2.0, argc)
		return SpellAction.Result(Spell(id, pos, volume.toFloat(), pitch.toFloat()), MediaConstants.DUST_UNIT / 16, listOf())
	}

	private data class Spell(val sound: Identifier, val pos: Vec3d, val volume: Float, val pitch: Float) : RenderedSpell {
		override fun cast(env: CastingEnvironment) {
			ServerEffectsBacklog.append(pos) {
				it.writeVarInt(Serializers.PLAY_SOUND.ordinal)
				(Serializers.PLAY_SOUND.serializer as Serializer<SoundInfo>).write(it, SoundInfo(sound, pos, volume, pitch))
			}
		}
	}
}