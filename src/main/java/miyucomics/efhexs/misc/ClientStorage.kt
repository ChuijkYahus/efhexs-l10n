package miyucomics.efhexs.misc

import miyucomics.efhexs.EfhexsMain
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs
import net.minecraft.client.sound.SoundInstance
import net.minecraft.util.Identifier

object ClientStorage {
	@JvmField
	var recording = false
	private var sounds: RingBuffer<Identifier> = RingBuffer(32)

	@JvmStatic
	fun pushSoundInstance(sound: SoundInstance) {
		sounds.push(sound.id)
	}

	fun pushToServer() {
		val buf = PacketByteBufs.create()
		buf.writeInt(sounds.getSize())
		sounds.forEach { buf.writeIdentifier(it) }
		ClientPlayNetworking.send(EfhexsMain.SOUND_CHANNEL, buf)
	}
}