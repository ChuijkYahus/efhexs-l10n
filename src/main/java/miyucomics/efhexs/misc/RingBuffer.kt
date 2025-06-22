package miyucomics.efhexs.misc

@Suppress("UNCHECKED_CAST")
class RingBuffer<T>(private val capacity: Int)  : Iterable<T> {
	private val buffer = Array<Any?>(capacity) { null }
	private var head = 0
	private var tail = 0
	private var full = false
	private var size = 0

	fun push(item: T) {
		if (any { it === item || it == item })
			return

		buffer[tail] = item
		tail = (tail + 1) % capacity

		if (full) {
			head = (head + 1) % capacity
		} else {
			size++
			if (tail == head) full = true
		}
	}

	fun getSize(): Int = size

	override fun iterator(): Iterator<T> = object : Iterator<T> {
		private var count = 0
		private var index = head

		override fun hasNext(): Boolean = count < size

		override fun next(): T {
			if (!hasNext()) throw NoSuchElementException()
			val item = buffer[index] as T
			index = (index + 1) % capacity
			count++
			return item
		}
	}
}