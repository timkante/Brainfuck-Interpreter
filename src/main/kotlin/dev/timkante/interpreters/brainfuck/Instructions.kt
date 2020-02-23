package dev.timkante.interpreters.brainfuck

sealed class Command : (MemoryState) -> MemoryState

data class ModifyPointer(val delta: Int) : Command() {
    override fun invoke(memoryState: MemoryState) = with(memoryState) {
        val limit = memory.size
        val newIndex = (index + delta) % limit
        copy(index = if (newIndex < 0) newIndex + limit else newIndex)
    }
}

data class ModifyValue(val delta: Int) : Command() {
    override fun invoke(memoryState: MemoryState) = with(memoryState) {
        copy(memory = memory.replace(index) { it + delta })
    }
}

object PrintValue : Command() {
    override fun invoke(memoryState: MemoryState) = with(memoryState) {
        copy(output = output + memory[index].toChar())
    }
}

data class Scope(val commands: List<Command>) : Command() {
    constructor(vararg commands: Command) : this(commands.asList())

    override fun invoke(oldMemoryState: MemoryState): MemoryState {
        return commands.fold(oldMemoryState) { state, command -> command(state) }
    }
}

data class Loop(val scope: Scope) : Command() {
    constructor(vararg commands: Command) : this(Scope(commands.asList()))

    override tailrec fun invoke(memoryState: MemoryState): MemoryState {
        if (memoryState.memory[memoryState.index] == 0) {
            return memoryState
        }

        return invoke(scope(memoryState))
    }
}