package miyucomics.efhexs

import miyucomics.efhexs.inits.EfhexsPatterns
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
		Registry.register(Registries.ITEM, id("microphone"), MicrophoneItem())

		ServerPlayNetworking.registerGlobalReceiver(SOUND_CHANNEL) { _, player, _, buf, _ ->
			val soundStorage = (player as PlayerEntityMinterface).getSounds()
			val count = buf.readInt()
			for (i in 0 until count)
				soundStorage.add(buf.readIdentifier())
		}
	}

	companion object {
		fun id(string: String) = Identifier("efhexs", string)
		val SOUND_CHANNEL = id("sounds")
	}
}