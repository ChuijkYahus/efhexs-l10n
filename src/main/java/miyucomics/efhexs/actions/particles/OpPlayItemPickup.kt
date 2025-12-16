package miyucomics.efhexs.actions.particles

import at.petrak.hexcasting.api.casting.RenderedSpell
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getEntity
import at.petrak.hexcasting.api.casting.getVec3
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.misc.MediaConstants
import miyucomics.efhexs.misc.ServerEffectsBacklog
import miyucomics.efhexs.networking.Serializer
import miyucomics.efhexs.networking.Serializers
import miyucomics.efhexs.networking.serializers.ItemParticleInfo
import miyucomics.hexpose.iotas.getItemStack
import net.minecraft.entity.Entity
import net.minecraft.item.ItemStack
import net.minecraft.util.math.Vec3d

object OpPlayItemPickup : SpellAction {
	override val argc = 3
	override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
		val stack = args.getItemStack(0, argc)
		val position = args.getVec3(1, argc)
		env.assertVecInRange(position)
		val receiver = args.getEntity(2, argc)
		env.assertEntityInRange(receiver)
		return SpellAction.Result(Spell(stack, position, receiver), MediaConstants.DUST_UNIT / 32, listOf())
	}

	private data class Spell(val stack: ItemStack, val position: Vec3d, val receiver: Entity) : RenderedSpell {
		override fun cast(env: CastingEnvironment) {
			ServerEffectsBacklog.append(position) {
				it.writeVarInt(Serializers.CREATE_ITEM_PARTICLE.ordinal)
				(Serializers.CREATE_ITEM_PARTICLE.serializer as Serializer<ItemParticleInfo>).write(it, ItemParticleInfo(stack, position, receiver.id))
			}
		}
	}
}