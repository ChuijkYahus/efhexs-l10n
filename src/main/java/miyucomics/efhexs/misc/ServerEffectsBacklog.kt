package miyucomics.efhexs.misc

import at.petrak.hexcasting.api.casting.eval.vm.CastingImage
import miyucomics.efhexs.EfhexsMain
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.nbt.NbtElement
import net.minecraft.network.PacketByteBuf
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.world.ServerWorld
import java.util.*

object ServerEffectsBacklog {
	var count: Int = 0
	var buffer: PacketByteBuf = PacketByteBufs.create()

	// flushes at the end of a hex or when list of targets is changed
	// concerningly means that every single player everywhere has to see it
	// will fix in the future, possibly with multiple packet streams per chunk position??
	fun flush(world: ServerWorld, image: CastingImage) {
		if (count == 0)
			return

		findTargets(world, image).forEach {
			ServerPlayNetworking.send(it, EfhexsMain.EFFECTS_STREAM, PacketByteBufs.create().apply {
				writeVarInt(count)
				writeBytes(buffer)
			})
		}

		count = 0
		buffer = PacketByteBufs.create()
	}

	fun findTargets(world: ServerWorld, image: CastingImage): List<ServerPlayerEntity> {
		if (!image.userData.contains("efhexs_targets"))
			return world.getPlayers()
		val targets = image.userData.getList("efhexs_targets", NbtElement.STRING_TYPE.toInt()).map { UUID.fromString(it.asString()) }
		return world.getPlayers { targets.contains(it.uuid) }
	}
}