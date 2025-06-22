package miyucomics.efhexs.misc

import net.minecraft.network.PacketByteBuf
import net.minecraft.particle.ParticleEffect

interface ComplexParticleHandler {
	fun produceParticleEffect(buf: PacketByteBuf): ParticleEffect
}