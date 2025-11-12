package miyucomics.efhexs.actions.particles

import at.petrak.hexcasting.api.casting.RenderedSpell
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.eval.vm.CastingImage
import at.petrak.hexcasting.api.casting.getEntity
import at.petrak.hexcasting.api.casting.getVec3
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.misc.MediaConstants
import miyucomics.efhexs.EfhexsMain
import miyucomics.efhexs.EfhexsMain.Companion.getTargetsFromImage
import miyucomics.hexpose.iotas.getItemStack
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.entity.Entity
import net.minecraft.item.ItemStack
import net.minecraft.util.math.Vec3d

object OpPlayItemPickup : SpellAction {
	override val argc = 3
	override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
		val stack = args.getItemStack(0, argc)
		val from = args.getVec3(1, argc)
		env.assertVecInRange(from)
		val receiver = args.getEntity(2, argc)
		env.assertEntityInRange(receiver)
		return SpellAction.Result(Spell(stack, from, receiver), MediaConstants.DUST_UNIT / 32, listOf())
	}

	private data class Spell(val stack: ItemStack, val from: Vec3d, val receiver: Entity) : RenderedSpell {
		override fun cast(env: CastingEnvironment) {}
		override fun cast(env: CastingEnvironment, image: CastingImage): CastingImage {
			getTargetsFromImage(env.world, image, from.x, from.y, from.z).forEach {
				ServerPlayNetworking.send(it, EfhexsMain.SPAWN_ITEM_PICKUP_CHANNEL, PacketByteBufs.create().apply {
					writeItemStack(stack)
					writeDouble(from.x)
					writeDouble(from.y)
					writeDouble(from.z)
					writeInt(receiver.id)
				})
			}
			return image
		}
	}
}