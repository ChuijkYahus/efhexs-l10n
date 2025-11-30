package miyucomics.efhexs

import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import miyucomics.efhexs.misc.EfhexsPusherComponent
import miyucomics.efhexs.misc.PlayerEntityMinterface
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.util.Identifier

class EfhexsMain : ModInitializer {
	override fun onInitialize() {
		EfhexsActions.init()
		CastingEnvironment.addCreateEventListener { env, _ -> env.addExtension(EfhexsPusherComponent(env)) }

		ServerPlayNetworking.registerGlobalReceiver(PARTICLE_CHANNEL) { _, player, _, buf, _ ->
			val new = buf.readIdentifier()
			val ring = (player as PlayerEntityMinterface).getParticles()
			if (!ring.buffer().contains(new))
				ring.add(new)
		}

		ServerPlayNetworking.registerGlobalReceiver(SOUND_CHANNEL) { _, player, _, buf, _ ->
			val new = buf.readIdentifier()
			val ring = (player as PlayerEntityMinterface).getSounds()
			if (!ring.buffer().contains(new))
				ring.add(new)
		}
	}

	companion object {
		fun id(string: String) = Identifier("efhexs", string)
		val EFFECTS_STREAM = id("effects")
		val PARTICLE_CHANNEL = id("particles")
		val SOUND_CHANNEL = id("sounds")
	}
}