package miyucomics.efhexs.inits

import at.petrak.hexcasting.api.casting.ActionRegistryEntry
import at.petrak.hexcasting.api.casting.castables.Action
import at.petrak.hexcasting.api.casting.getPositiveDoubleUnderInclusive
import at.petrak.hexcasting.api.casting.getVec3
import at.petrak.hexcasting.api.casting.math.HexDir
import at.petrak.hexcasting.api.casting.math.HexPattern
import at.petrak.hexcasting.common.lib.hex.HexActions
import at.petrak.hexcasting.common.particles.ConjureParticleOptions
import miyucomics.efhexs.EfhexsMain.Companion.id
import miyucomics.efhexs.actions.OpGetParticles
import miyucomics.efhexs.actions.OpGetSounds
import miyucomics.efhexs.actions.OpPlaySimpleParticle
import miyucomics.efhexs.actions.OpPlaySound
import miyucomics.efhexs.actions.OpPlayComplexParticle
import miyucomics.efhexs.misc.ComplexParticleHandler
import net.minecraft.client.MinecraftClient
import net.minecraft.network.PacketByteBuf
import net.minecraft.registry.Registry
import net.minecraft.util.Identifier
import net.minecraft.util.math.ColorHelper

object EfhexsPatterns {
	fun init() {
		register("get_sounds", "aawa", HexDir.WEST, OpGetSounds())
		register("play_sound", "dwdd", HexDir.WEST, OpPlaySound())
		register("get_particles", "qawawe", HexDir.WEST, OpGetParticles())
		register("play_particle", "qwdwde", HexDir.NORTH_EAST, OpPlaySimpleParticle())

//		register("play_dust_particle", "qwdwdeweew", HexDir.NORTH_EAST, OpPlayComplexParticle(Identifier("dust"), 3,
//			{ buf, args ->
//				val velocity = args.getVec3(1, 3)
//				val color = args.getVec3(2, 3)
//				buf.writeVector3f(color.toVector3f())
//				buf.writeDouble(velocity.x)
//				buf.writeDouble(velocity.y)
//				buf.writeDouble(velocity.z)
//			},
//			object : ComplexParticleHandler {
//				override fun produceParticleEffect(client: MinecraftClient, buf: PacketByteBuf, x: Double, y: Double, z: Double) {
//					val particle = client.particleManager.addParticle(DustParticleEffect(buf.readVector3f(), buf.readFloat()), x, y, z, 0.0, 0.0, 0.0)
//					particle!!.setVelocity(buf.readDouble(), buf.readDouble(), buf.readDouble())
//				}
//			}
//		))
//
//		register("play_dust_transition_particle", "qwdwdeweew", HexDir.NORTH_EAST, OpPlayComplexParticle(Identifier("dust_color_transition"), 5,
//			{ buf, args ->
//				val velocity = args.getVec3(1, 5)
//				val colorA = args.getVec3(2, 5)
//				val colorB = args.getVec3(3, 5)
//				val duration = args.getDouble(4, 5).toFloat()
//
//				buf.writeVector3f(colorA.toVector3f())
//				buf.writeVector3f(colorB.toVector3f())
//				buf.writeFloat(duration)
//
//				buf.writeDouble(velocity.x)
//				buf.writeDouble(velocity.y)
//				buf.writeDouble(velocity.z)
//			},
//			object : ComplexParticleHandler {
//				override fun produceParticleEffect(client: MinecraftClient, buf: PacketByteBuf, x: Double, y: Double, z: Double) {
//					val particle = client.particleManager.addParticle(DustColorTransitionParticleEffect(buf.readVector3f(), buf.readVector3f(), buf.readFloat()), x, y, z, 0.0, 0.0, 0.0)
//					particle!!.setVelocity(buf.readDouble(), buf.readDouble(), buf.readDouble())
//				}
//			}
//		))
//
//		register("play_block_particle", "qwdwdeweew", HexDir.NORTH_EAST, OpPlayComplexParticle(Identifier("block"), 3,
//			{ buf, args ->
//				val velocity = args.getVec3(1, 3)
//				val id = args.getIdentifier(2, 3)
//				if (!Registries.BLOCK.containsId(id))
//					throw MishapInvalidIota.of(args[2], 0, "block_id")
//				buf.writeIdentifier(id)
//				buf.writeDouble(velocity.x)
//				buf.writeDouble(velocity.y)
//				buf.writeDouble(velocity.z)
//			},
//			object : ComplexParticleHandler {
//				override fun produceParticleEffect(client: MinecraftClient, buf: PacketByteBuf, x: Double, y: Double, z: Double) {
//					val particle = client.particleManager.addParticle(BlockStateParticleEffect(ParticleTypes.BLOCK, Registries.BLOCK.get(buf.readIdentifier()).defaultState), x, y, z, 0.0, 0.0, 0.0)
//					particle!!.setVelocity(buf.readDouble(), buf.readDouble(), buf.readDouble())
//				}
//			}
//		))
//
//		register("play_falling_dust_particle", "qwdwdeweew", HexDir.NORTH_EAST, OpPlayComplexParticle(Identifier("falling_dust"), 3,
//			{ buf, args ->
//				val velocity = args.getVec3(1, 3)
//				val id = args.getIdentifier(2, 3)
//				if (!Registries.BLOCK.containsId(id))
//					throw MishapInvalidIota.of(args[2], 0, "block_id")
//				buf.writeIdentifier(id)
//				buf.writeDouble(velocity.x)
//				buf.writeDouble(velocity.y)
//				buf.writeDouble(velocity.z)
//			},
//			object : ComplexParticleHandler {
//				override fun produceParticleEffect(client: MinecraftClient, buf: PacketByteBuf, x: Double, y: Double, z: Double) {
//					val particle = client.particleManager.addParticle(BlockStateParticleEffect(ParticleTypes.FALLING_DUST, Registries.BLOCK.get(buf.readIdentifier()).defaultState), x, y, z, 0.0, 0.0, 0.0)
//					particle!!.setVelocity(buf.readDouble(), buf.readDouble(), buf.readDouble())
//				}
//			}
//		))
//
//		register("play_item_particle", "qwdwdeweew", HexDir.NORTH_EAST, OpPlayComplexParticle(Identifier("item"), 3,
//			{ buf, args ->
//				val velocity = args.getVec3(1, 3)
//				val item = args.getItemStack(2, 3)
//				buf.writeItemStack(item)
//				buf.writeDouble(velocity.x)
//				buf.writeDouble(velocity.y)
//				buf.writeDouble(velocity.z)
//			},
//			object : ComplexParticleHandler {
//				override fun produceParticleEffect(client: MinecraftClient, buf: PacketByteBuf, x: Double, y: Double, z: Double) {
//					val particle = client.particleManager.addParticle(ItemStackParticleEffect(ParticleTypes.ITEM, buf.readItemStack()), x, y, z, 0.0, 0.0, 0.0)
//					particle!!.setVelocity(buf.readDouble(), buf.readDouble(), buf.readDouble())
//				}
//			}
//		))

		register("play_hex_particle", "qwdwdeweew", HexDir.NORTH_EAST, OpPlayComplexParticle(Identifier("hex"), 4,
			{ buf, args ->
				val velocity = args.getVec3(1, 4)
				val color = args.getVec3(2, 4)
				val alpha = (args.getPositiveDoubleUnderInclusive(3, 1.0, 4) * 255).toInt()
				buf.writeVector3f(color.toVector3f())
				buf.writeInt(alpha)
				buf.writeDouble(velocity.x)
				buf.writeDouble(velocity.y)
				buf.writeDouble(velocity.z)
			},
			object : ComplexParticleHandler {
				override fun produceParticleEffect(client: MinecraftClient, buf: PacketByteBuf, x: Double, y: Double, z: Double) {
					val raw = buf.readVector3f()
					val color = ColorHelper.Argb.getArgb(buf.readInt(), (raw.x * 255).toInt(), (raw.y * 255).toInt(), (raw.z * 255).toInt())
					val particle = client.particleManager.addParticle(ConjureParticleOptions(color), x, y, z, 0.0, 0.0, 0.0)
					particle!!.setVelocity(buf.readDouble(), buf.readDouble(), buf.readDouble())
				}
			}
		))
	}

	private fun register(name: String, signature: String, startDir: HexDir, action: Action) =
		Registry.register(
			HexActions.REGISTRY, id(name),
			ActionRegistryEntry(HexPattern.Companion.fromAngles(signature, startDir), action)
		)
}