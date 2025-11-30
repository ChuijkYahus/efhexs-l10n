package miyucomics.efhexs.networking.serializers

import miyucomics.efhexs.networking.Serializer
import net.minecraft.client.MinecraftClient
import net.minecraft.network.PacketByteBuf
import net.minecraft.particle.ParticleTypes
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.Vec3d

object CreatePotionParticleSerializer : Serializer<PotionParticleInfo> {
	override fun write(buf: PacketByteBuf, data: PotionParticleInfo) {
		buf.writeVector3f(data.position.toVector3f())
		buf.writeVector3f(data.velocity.toVector3f())
		buf.writeVector3f(data.color.toVector3f())
	}

	override fun read(buf: PacketByteBuf) = PotionParticleInfo(Vec3d(buf.readVector3f()), Vec3d(buf.readVector3f()), Vec3d(buf.readVector3f()))

	override fun apply(client: MinecraftClient, data: PotionParticleInfo) {
		val particle = client.particleManager.addParticle(ParticleTypes.ENTITY_EFFECT,
			data.position.x, data.position.y, data.position.z,
			MathHelper.clamp(data.color.x, 0.0, 1.0),
			MathHelper.clamp(data.color.y, 0.0, 1.0),
			MathHelper.clamp(data.color.z, 0.0, 1.0)
		)
		particle!!.setVelocity(data.velocity.x, data.velocity.y, data.velocity.z)
	}
}

data class PotionParticleInfo(val position: Vec3d, val velocity: Vec3d, val color: Vec3d)