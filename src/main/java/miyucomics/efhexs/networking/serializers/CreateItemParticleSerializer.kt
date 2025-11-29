package miyucomics.efhexs.networking.serializers

import miyucomics.efhexs.networking.Serializer
import net.minecraft.client.MinecraftClient
import net.minecraft.client.particle.ItemPickupParticle
import net.minecraft.entity.ItemEntity
import net.minecraft.item.ItemStack
import net.minecraft.network.PacketByteBuf
import net.minecraft.util.math.Vec3d

object CreateItemParticleSerializer : Serializer<ItemParticleInfo> {
	override fun write(buf: PacketByteBuf, data: ItemParticleInfo) {
		buf.writeItemStack(data.stack)
		buf.writeVector3f(data.position.toVector3f())
		buf.writeInt(data.receiver)
	}

	override fun read(buf: PacketByteBuf) = ItemParticleInfo(buf.readItemStack(), Vec3d(buf.readVector3f()), buf.readInt())

	override fun apply(client: MinecraftClient, data: ItemParticleInfo) {
		client.particleManager.addParticle(ItemPickupParticle(
			client.entityRenderDispatcher, client.bufferBuilders, client.world,
			ItemEntity(client.world, data.position.x, data.position.y, data.position.z, data.stack),
			client.world!!.getEntityById(data.receiver)
		))
	}
}

data class ItemParticleInfo(val stack: ItemStack, val position: Vec3d, val receiver: Int)