package miyucomics.efhexs.misc

import net.minecraft.client.MinecraftClient
import net.minecraft.network.PacketByteBuf

interface ComplexParticleHandler {
	fun produceParticleEffect(client: MinecraftClient, buf: PacketByteBuf, x: Double, y: Double, z: Double)
}