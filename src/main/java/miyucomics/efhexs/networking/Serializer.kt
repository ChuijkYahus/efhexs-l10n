package miyucomics.efhexs.networking

import net.minecraft.client.MinecraftClient
import net.minecraft.network.PacketByteBuf

interface Serializer<T> {
	fun write(buf: PacketByteBuf, data: T)
	fun read(buf: PacketByteBuf): T
	fun apply(client: MinecraftClient, data: T)
}