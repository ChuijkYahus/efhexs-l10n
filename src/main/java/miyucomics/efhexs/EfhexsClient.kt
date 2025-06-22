package miyucomics.efhexs

import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.minecraft.particle.DefaultParticleType
import net.minecraft.registry.Registries
import net.minecraft.util.math.Vec3d
import org.joml.Vector3f

class EfhexsClient : ClientModInitializer {
	override fun onInitializeClient() {
		ClientPlayNetworking.registerGlobalReceiver(EfhexsMain.SPAWN_SIMPLE_PARTICLE_CHANNEL) { client, handler, buf, responseSender ->
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

		ClientPlayNetworking.registerGlobalReceiver(EfhexsMain.SPAWN_COMPLEX_PARTICLE_CHANNEL) { client, handler, buf, responseSender ->
			val particleId = buf.readIdentifier()
			val x = buf.readDouble()
			val y = buf.readDouble()
			val z = buf.readDouble()
			if (!EfhexsMain.COMPLEX_PARTICLE_REGISTRY.containsId(particleId))
				return@registerGlobalReceiver
			client.execute {
				if (client.world != null)
					EfhexsMain.COMPLEX_PARTICLE_REGISTRY.get(particleId)!!.produceParticleEffect(client, buf, x, y, z)
			}
		}
	}
}