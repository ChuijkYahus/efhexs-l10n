package miyucomics.efhexs.networking.serializers

import miyucomics.efhexs.networking.Serializer
import net.minecraft.client.MinecraftClient
import net.minecraft.network.PacketByteBuf
import net.minecraft.particle.DefaultParticleType
import net.minecraft.registry.Registries
import net.minecraft.util.Identifier
import net.minecraft.util.math.Vec3d

object CreateSimpleParticleSerializer : Serializer<SimpleParticleInfo> {
	override fun write(buf: PacketByteBuf, data: SimpleParticleInfo) {
		buf.writeIdentifier(data.particle)
		buf.writeVector3f(data.position.toVector3f())
		buf.writeVector3f(data.velocity.toVector3f())
	}

	override fun read(buf: PacketByteBuf) = SimpleParticleInfo(buf.readIdentifier(), Vec3d(buf.readVector3f()), Vec3d(buf.readVector3f()))

	override fun apply(client: MinecraftClient, data: SimpleParticleInfo) {
		val particle = client.particleManager.addParticle(Registries.PARTICLE_TYPE.get(data.particle) as DefaultParticleType, data.position.x, data.position.y, data.position.z, 0.0, 0.0, 0.0)
		particle?.setVelocity(data.velocity.x, data.velocity.y, data.velocity.z)
	}
}

data class SimpleParticleInfo(val particle: Identifier, val position: Vec3d, val velocity: Vec3d)