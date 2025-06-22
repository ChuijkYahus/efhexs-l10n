package miyucomics.efhexs

import miyucomics.efhexs.inits.EfhexsPatterns
import miyucomics.efhexs.misc.MicrophoneItem
import miyucomics.efhexs.misc.ComplexParticleHandler
import miyucomics.efhexs.misc.PlayerEntityMinterface
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder
import net.fabricmc.fabric.api.event.registry.RegistryAttribute
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.SimpleRegistry
import net.minecraft.util.Identifier

class EfhexsMain : ModInitializer {
	override fun onInitialize() {
		EfhexsPatterns.init()
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
		val SPAWN_SIMPLE_PARTICLE_CHANNEL = id("spawn_simple_particle")
		val SPAWN_COMPLEX_PARTICLE_CHANNEL = id("spawn_complex_particle")

		private val COMPLEX_PARTICLE_REGISTRY_KEY: RegistryKey<Registry<ComplexParticleHandler>> = RegistryKey.ofRegistry(id("complex_particle_registry"))
		val COMPLEX_PARTICLE_REGISTRY: SimpleRegistry<ComplexParticleHandler> = FabricRegistryBuilder.createSimple(COMPLEX_PARTICLE_REGISTRY_KEY).attribute(RegistryAttribute.MODDED).buildAndRegister()
	}
}