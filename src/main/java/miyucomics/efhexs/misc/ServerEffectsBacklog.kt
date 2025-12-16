package miyucomics.efhexs.misc

import at.petrak.hexcasting.api.casting.eval.vm.CastingImage
import miyucomics.efhexs.EfhexsMain
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.command.argument.EntityArgumentType.player
import net.minecraft.nbt.NbtElement
import net.minecraft.network.PacketByteBuf
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.ChunkPos
import net.minecraft.util.math.Vec3d
import java.util.*

object ServerEffectsBacklog {
	private val chunkBuffers = mutableMapOf<ChunkPos, PacketByteBuf>()
	private val chunkCounts = mutableMapOf<ChunkPos, Int>()

	fun append(pos: Vec3d, encoder: (PacketByteBuf) -> Unit) {
		val chunk = ChunkPos(BlockPos(pos.x.toInt(), pos.y.toInt(), pos.z.toInt()))
		val buf = chunkBuffers.getOrPut(chunk) { PacketByteBufs.create() }
		encoder(buf)
		chunkCounts[chunk] = chunkCounts.getOrDefault(chunk, 0) + 1
	}

	fun flush(world: ServerWorld, image: CastingImage) {
		val targets = findTargets(world, image)

		chunkBuffers.forEach { (chunk, data) ->
			val packet = PacketByteBufs.create()
			packet.writeVarInt(chunkCounts[chunk]!!)
			packet.writeBytes(data)

			world.chunkManager.threadedAnvilChunkStorage.getPlayersWatchingChunk(chunk).filter { it in targets }.forEach {
				ServerPlayNetworking.send(it, EfhexsMain.EFFECTS_STREAM, packet)
			}
		}

		chunkBuffers.clear()
		chunkCounts.clear()
	}

	fun findTargets(world: ServerWorld, image: CastingImage): List<ServerPlayerEntity> {
		if (!image.userData.contains("efhexs_targets"))
			return world.getPlayers()
		val targets = image.userData.getList("efhexs_targets", NbtElement.STRING_TYPE.toInt()).map { UUID.fromString(it.asString()) }
		return world.getPlayers { targets.contains(it.uuid) }
	}
}