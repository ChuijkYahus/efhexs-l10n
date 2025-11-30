package miyucomics.efhexs.misc

import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.eval.CastingEnvironmentComponent
import at.petrak.hexcasting.api.casting.eval.vm.CastingImage

class EfhexsPusherComponent(val env: CastingEnvironment) : CastingEnvironmentComponent.PostCast {
	override fun getKey() = EfhexsPusherComponent()
	class EfhexsPusherComponent : CastingEnvironmentComponent.Key<CastingEnvironmentComponent.PostCast>

	override fun onPostCast(image: CastingImage) {
		ServerEffectsBacklog.flush(env.world, image)
	}
}