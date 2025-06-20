package miyucomics.efhexs

import miyucomics.efhexs.inits.EfhexsPatterns
import miyucomics.efhexs.items.CameraItem
import miyucomics.efhexs.items.MicrophoneItem
import miyucomics.efhexs.misc.PlayerEntityMinterface
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.util.Identifier

class EfhexsMain : ModInitializer {
	override fun onInitialize() {
		EfhexsPatterns.init()
		Registry.register(Registries.ITEM, id("camera"), CameraItem())
		Registry.register(Registries.ITEM, id("microphone"), MicrophoneItem())

		ServerPlayNetworking.registerGlobalReceiver(PARTICLE_CHANNEL) { _, player, _, buf, _ ->
			val particleStorage = (player as PlayerEntityMinterface).getParticles()
			particleStorage.clear()
			val count = buf.readInt()
			for (i in 0 until count)
				particleStorage.add(buf.readIdentifier())
		}

		ServerPlayNetworking.registerGlobalReceiver(SOUND_CHANNEL) { _, player, _, buf, _ ->
			val soundStorage = (player as PlayerEntityMinterface).getSounds()
			soundStorage.clear()
			val count = buf.readInt()
			for (i in 0 until count)
				soundStorage.add(buf.readIdentifier())
		}
	}

	companion object {
		fun id(string: String) = Identifier("efhexs", string)
		val PARTICLE_CHANNEL = id("particles")
		val SOUND_CHANNEL = id("sounds")
	}
}