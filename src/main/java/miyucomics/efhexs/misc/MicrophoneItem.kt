package miyucomics.efhexs.misc

import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.util.UseAction
import net.minecraft.world.World

class MicrophoneItem : Item(Settings().maxCount(1)) {
	override fun use(world: World, user: PlayerEntity, hand: Hand): TypedActionResult<ItemStack> {
		user.setCurrentHand(hand)
		if (world.isClient)
			ClientStorage.recording = true
		return TypedActionResult.success(user.getStackInHand(hand))
	}

	override fun onStoppedUsing(stack: ItemStack, world: World, livingEntity: LivingEntity?, i: Int) {
		if (!world.isClient)
			return
		ClientStorage.recording = false
		ClientStorage.pushParticlesToServer()
		ClientStorage.pushSoundsToServer()
	}

	override fun getMaxUseTime(stack: ItemStack) = 72000
	override fun getUseAction(itemStack: ItemStack) = UseAction.BOW
}