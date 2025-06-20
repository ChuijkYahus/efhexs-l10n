package miyucomics.efhexs.misc

import java.util.List
import net.minecraft.util.Identifier

interface PlayerEntityMinterface {
	fun getParticles(): List<Identifier>
	fun getSounds(): List<Identifier>
}