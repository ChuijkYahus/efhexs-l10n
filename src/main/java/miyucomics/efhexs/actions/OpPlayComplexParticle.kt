package miyucomics.efhexs.actions

import at.petrak.hexcasting.api.casting.RenderedSpell
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getVec3
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.misc.MediaConstants
import miyucomics.efhexs.EfhexsMain
import miyucomics.efhexs.misc.ComplexParticleHandler
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.network.PacketByteBuf
import net.minecraft.registry.Registry
import net.minecraft.util.Identifier

class OpPlayComplexParticle(val particleType: Identifier, argCount: Int, val populateBuffer: (PacketByteBuf, List<Iota>) -> Unit, receiver: ComplexParticleHandler) : SpellAction {
	init {
		Registry.register(EfhexsMain.COMPLEX_PARTICLE_REGISTRY, particleType, receiver)
	}

	override val argc = argCount
	override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
		val pos = args.getVec3(0, argc)
		env.assertVecInRange(pos)
		val velocity = args.getVec3(1, argc)
		val buf = PacketByteBufs.create()
		buf.writeIdentifier(particleType)
		buf.writeDouble(pos.x)
		buf.writeDouble(pos.y)
		buf.writeDouble(pos.z)
		buf.writeDouble(velocity.x)
		buf.writeDouble(velocity.y)
		buf.writeDouble(velocity.z)
		populateBuffer(buf, args)
		return SpellAction.Result(Spell(buf), MediaConstants.DUST_UNIT / 32, listOf())
	}

	private data class Spell(val buf: PacketByteBuf) : RenderedSpell {
		override fun cast(env: CastingEnvironment) {
			for (player in env.world.getPlayers()) {
				val playerBuf = PacketByteBufs.create()
				playerBuf.writeBytes(buf.copy())
				ServerPlayNetworking.send(player, EfhexsMain.SPAWN_COMPLEX_PARTICLE_CHANNEL, buf)
			}
		}
	}
}