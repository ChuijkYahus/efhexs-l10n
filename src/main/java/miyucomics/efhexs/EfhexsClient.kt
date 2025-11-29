package miyucomics.efhexs

import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.minecraft.client.particle.ItemPickupParticle
import net.minecraft.entity.ItemEntity
import net.minecraft.particle.DefaultParticleType
import net.minecraft.particle.ParticleTypes
import net.minecraft.registry.Registries
import net.minecraft.util.math.Vec3d

class EfhexsClient : ClientModInitializer {
	override fun onInitializeClient() {
		ClientPlayNetworking.registerGlobalReceiver(EfhexsMain.SPAWN_ITEM_PICKUP_CHANNEL) { client, _, buf, _ ->
			val stack = buf.readItemStack()
			val position = Vec3d(buf.readDouble(), buf.readDouble(), buf.readDouble())
			val receiver = client.world!!.getEntityById(buf.readInt())
			client.execute {
				val createdItemEntity = ItemEntity(client.world, position.x, position.y, position.z, stack)
				client.particleManager.addParticle(ItemPickupParticle(client.entityRenderDispatcher, client.bufferBuilders, client.world, createdItemEntity, receiver))
			}
		}

		ClientPlayNetworking.registerGlobalReceiver(EfhexsMain.SPAWN_POTION_EFFECT_CHANNEL) { client, _, buf, _ ->
			val position = Vec3d(buf.readDouble(), buf.readDouble(), buf.readDouble())
			val velocity = Vec3d(buf.readDouble(), buf.readDouble(), buf.readDouble())
			val raw = buf.readVector3f()
			client.execute {
				val particle = client.particleManager.addParticle(ParticleTypes.ENTITY_EFFECT, position.x, position.y, position.z, raw.x.toDouble(), raw.y.toDouble(), raw.z.toDouble())
				particle!!.setVelocity(velocity.x, velocity.y, velocity.z)
			}
		}

		ClientPlayNetworking.registerGlobalReceiver(EfhexsMain.SPAWN_SIMPLE_PARTICLE_CHANNEL) { client, _, buf, _ ->
			val particleId = buf.readIdentifier()
			val x = buf.readDouble()
			val y = buf.readDouble()
			val z = buf.readDouble()
			val vx = buf.readDouble()
			val vy = buf.readDouble()
			val vz = buf.readDouble()
			val particleType = Registries.PARTICLE_TYPE.get(particleId)
			client.execute {
				if (client.world != null) {
					val particle = client.particleManager.addParticle(particleType as DefaultParticleType, x, y, z, 0.0, 0.0, 0.0)
					particle!!.setVelocity(vx, vy, vz)
				}
			}
		}

		ClientPlayNetworking.registerGlobalReceiver(EfhexsMain.SPAWN_COMPLEX_PARTICLE_CHANNEL) { client, _, buf, _ ->
			val particleId = buf.readIdentifier()
			val x = buf.readDouble()
			val y = buf.readDouble()
			val z = buf.readDouble()
			val dx = buf.readDouble()
			val dy = buf.readDouble()
			val dz = buf.readDouble()
			if (!EfhexsMain.PARTICLE_HANDLER_REGISTRY.containsId(particleId))
				return@registerGlobalReceiver
			val effect = EfhexsMain.PARTICLE_HANDLER_REGISTRY.get(particleId)!!.produceParticleEffect(buf)
			client.execute {
				if (client.world != null) {
					val particle = client.particleManager.addParticle(effect, x, y, z, 0.0, 0.0, 0.0)
					particle?.setVelocity(dx, dy, dz)
				}
			}
		}
	}
}