package dev.timkante.interpreters.brainfuck

data class MemoryState(
    val index: Int,
    val memory: List<Int>,
    val output: String = ""
) {
    companion object {
        const val DEFAULT_MEMORY_SIZE = 65536
    }

    constructor(memorySize: Int = DEFAULT_MEMORY_SIZE) : this(
        index = 0,
        memory = List(size = memorySize) { 0 }
    )
}
