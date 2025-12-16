package miyucomics.efhexs.actions

import at.petrak.hexcasting.api.casting.RenderedSpell
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.eval.vm.CastingImage
import at.petrak.hexcasting.api.casting.getList
import at.petrak.hexcasting.api.casting.iota.EntityIota
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.NullIota
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota
import at.petrak.hexcasting.api.utils.putList
import miyucomics.efhexs.misc.ServerEffectsBacklog
import net.minecraft.nbt.NbtList
import net.minecraft.nbt.NbtString
import java.util.*

object OpSetTargets : SpellAction {
	override val argc = 1
	override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
		if (args[0] is NullIota)
			return SpellAction.Result(Clear(0), 0, listOf())

		val list = args.getList(0, argc)
		val uuids = mutableListOf<UUID>()
		list.forEach {
			if (it !is EntityIota)
				throw MishapInvalidIota.of(args[0], 0, "entity_list")
			uuids.add(it.entity.uuid)
		}
		return SpellAction.Result(Set(uuids), 0, listOf())
	}

	private data class Clear(val random: Int) : RenderedSpell {
		override fun cast(env: CastingEnvironment) {}
		override fun cast(env: CastingEnvironment, image: CastingImage): CastingImage {
			ServerEffectsBacklog.flush(env.world, image)
			val newData = image.userData.copy()
			newData.remove("efhexs_targets")
			return image.copy(userData = newData)
		}
	}

	private data class Set(val targets: List<UUID>) : RenderedSpell {
		override fun cast(env: CastingEnvironment) {}
		override fun cast(env: CastingEnvironment, image: CastingImage): CastingImage {
			ServerEffectsBacklog.flush(env.world, image)
			val newData = image.userData.copy()
			newData.putList("efhexs_targets", NbtList().apply { targets.forEach { add(NbtString.of(it.toString())) } })
			return image.copy(userData = newData)
		}
	}
}