package miyucomics.efhexs.misc

import java.util.List
import net.minecraft.util.Identifier

interface PlayerEntityMinterface {
	fun getSounds(): List<Identifier>
	fun setSounds(sounds: List<Identifier>)
}