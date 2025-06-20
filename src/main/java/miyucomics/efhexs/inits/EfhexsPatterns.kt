package miyucomics.efhexs.inits

import at.petrak.hexcasting.api.casting.ActionRegistryEntry
import at.petrak.hexcasting.api.casting.castables.Action
import at.petrak.hexcasting.api.casting.math.HexDir
import at.petrak.hexcasting.api.casting.math.HexPattern
import at.petrak.hexcasting.common.lib.hex.HexActions
import miyucomics.efhexs.EfhexsMain.Companion.id
import miyucomics.efhexs.actions.OpGetParticles
import miyucomics.efhexs.actions.OpGetSounds
import miyucomics.efhexs.actions.OpPlayParticle
import miyucomics.efhexs.actions.OpPlaySound
import net.minecraft.registry.Registry

object EfhexsPatterns {
	fun init() {
		register("get_sounds", "aawa", HexDir.WEST, OpGetSounds())
		register("play_sound", "dwdd", HexDir.WEST, OpPlaySound())
		register("get_particles", "qawawe", HexDir.WEST, OpGetParticles())
		register("play_particle", "qwdwde", HexDir.NORTH_EAST, OpPlayParticle())
	}

	private fun register(name: String, signature: String, startDir: HexDir, action: Action) =
		Registry.register(
			HexActions.REGISTRY, id(name),
			ActionRegistryEntry(HexPattern.Companion.fromAngles(signature, startDir), action)
		)
}