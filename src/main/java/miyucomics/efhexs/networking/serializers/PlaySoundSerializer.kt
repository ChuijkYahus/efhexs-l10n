package miyucomics.efhexs.networking.serializers

import miyucomics.efhexs.networking.Serializer
import net.minecraft.client.MinecraftClient
import net.minecraft.network.PacketByteBuf
import net.minecraft.registry.Registries
import net.minecraft.sound.SoundCategory
import net.minecraft.util.Identifier
import net.minecraft.util.math.Vec3d

object PlaySoundSerializer : Serializer<SoundInfo> {
	override fun write(buf: PacketByteBuf, data: SoundInfo) {
		buf.writeIdentifier(data.sound)
		buf.writeVector3f(data.position.toVector3f())
		buf.writeFloat(data.volume)
		buf.writeFloat(data.pitch)
	}

	override fun read(buf: PacketByteBuf) = SoundInfo(buf.readIdentifier(), Vec3d(buf.readVector3f()), buf.readFloat(), buf.readFloat())

	override fun apply(client: MinecraftClient, data: SoundInfo) {
		client.world!!.playSound(client.player, data.position.x, data.position.y, data.position.z, Registries.SOUND_EVENT.get(data.sound)!!, SoundCategory.MASTER, data.volume, data.pitch, 0)
	}
}

data class SoundInfo(val sound: Identifier, val position: Vec3d, val volume: Float, val pitch: Float)