package miyucomics.efhexs.misc

import net.minecraft.util.Identifier
import java.util.List

interface PlayerEntityMinterface {
	fun getParticles(): List<Identifier>
	fun getSounds(): List<Identifier>
}