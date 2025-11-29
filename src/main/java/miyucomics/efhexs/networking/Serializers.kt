package miyucomics.efhexs.networking

import miyucomics.efhexs.networking.serializers.CreateItemParticleSerializer
import miyucomics.efhexs.networking.serializers.CreatePotionParticleSerializer
import miyucomics.efhexs.networking.serializers.CreateSimpleParticleSerializer
import miyucomics.efhexs.networking.serializers.PlaySoundSerializer

enum class Serializers(val serializer: Serializer<*>) {
	CREATE_SIMPLE_PARTICLE(CreateSimpleParticleSerializer),
	CREATE_ITEM_PARTICLE(CreateItemParticleSerializer),
	CREATE_POTION_PARTICLE(CreatePotionParticleSerializer),
	PLAY_SOUND(PlaySoundSerializer),
}