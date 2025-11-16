package miyucomics.efhexs.actions.particles

import at.petrak.hexcasting.api.casting.RenderedSpell
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.eval.vm.CastingImage
import at.petrak.hexcasting.api.casting.getVec3
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.misc.MediaConstants
import miyucomics.efhexs.EfhexsMain
import miyucomics.efhexs.EfhexsMain.Companion.getTargetsFromImage
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.util.math.Vec3d

object OpPlayPotionEffect : SpellAction {
	override val argc = 3
	override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
		val position = args.getVec3(0, argc)
		env.assertVecInRange(position)
		val velocity = args.getVec3(1, argc)
		val color = args.getVec3(2, argc)
		return SpellAction.Result(Spell(position, velocity, color), MediaConstants.DUST_UNIT / 32, listOf())
	}

	private data class Spell(val position: Vec3d, val velocity: Vec3d, val color: Vec3d) : RenderedSpell {
		override fun cast(env: CastingEnvironment) {}
		override fun cast(env: CastingEnvironment, image: CastingImage): CastingImage {
			getTargetsFromImage(env.world, image, position.x, position.y, position.z).forEach {
				ServerPlayNetworking.send(it, EfhexsMain.SPAWN_POTION_EFFECT_CHANNEL, PacketByteBufs.create().apply {
					writeDouble(position.x)
					writeDouble(position.y)
					writeDouble(position.z)
					writeDouble(velocity.x)
					writeDouble(velocity.y)
					writeDouble(velocity.z)
					writeVector3f(color.toVector3f())
				})
			}
			return image
		}
	}
}