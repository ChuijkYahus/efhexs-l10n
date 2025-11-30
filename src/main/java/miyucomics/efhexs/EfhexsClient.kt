package miyucomics.efhexs

import miyucomics.efhexs.networking.Serializer
import miyucomics.efhexs.networking.Serializers
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking

class EfhexsClient : ClientModInitializer {
	override fun onInitializeClient() {
		ClientPlayNetworking.registerGlobalReceiver(EfhexsMain.EFFECTS_STREAM) { client, _, buf, _ ->
			val count = buf.readVarInt()
			val tasks = mutableListOf<Pair<Serializer<Any>, Any>>()

			for (i in 0 until count) {
				val serializer = Serializers.entries[buf.readVarInt()].serializer as? Serializer<Any> ?: continue
				tasks.add(serializer to serializer.read(buf))
			}

			client.execute {
				tasks.forEach { (serializer, data) -> serializer.apply(client, data) }
			}
		}
	}
}