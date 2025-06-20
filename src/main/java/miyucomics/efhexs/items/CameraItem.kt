package miyucomics.efhexs.items

import miyucomics.efhexs.misc.ClientStorage
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.world.World

class CameraItem : Item(Settings().maxCount(1)) {
	override fun use(world: World, user: PlayerEntity, hand: Hand): TypedActionResult<ItemStack> {
		user.setCurrentHand(hand)
		if (world.isClient)
			ClientStorage.recordingParticle = true
		return TypedActionResult.success(user.getStackInHand(hand))
	}

	override fun onStoppedUsing(stack: ItemStack, world: World, livingEntity: LivingEntity?, i: Int) {
		if (!world.isClient)
			return
		ClientStorage.recordingParticle = false
		ClientStorage.pushParticlesToServer()
	}

	override fun getMaxUseTime(stack: ItemStack) = 72000
}