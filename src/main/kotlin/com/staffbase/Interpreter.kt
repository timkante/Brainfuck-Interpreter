package com.staffbase

class Interpreter {

    companion object{
        const val MAX_MEMSIZE = 1024
    }

    data class State (
        val currentPointer: Int = 0,
        val memory: ByteArray = ByteArray(MAX_MEMSIZE),
        val output: String = ""
    ) {
        val currentChar: Char
            get() = memory[currentPointer].toChar()

        val incremented: State
            get() = copy(memory = memory.also {
                if (it[currentPointer] == Byte.MAX_VALUE) throw ArithmeticException("Value overflow")
                else ++it[currentPointer]
            })

        val decremented: State
            get() = copy(memory = memory.also {
                if (it[currentPointer] == Byte.MIN_VALUE) throw ArithmeticException("Value underflow")
                else --it[currentPointer]
            })

        val rightShifted: State
            get()= when(currentPointer) {
                MAX_MEMSIZE -> throw StackOverflowError("Exceeding memory upper bound")
                else -> copy(currentPointer = currentPointer + 1)
            }

        val leftShifted: State
            get() = when(currentPointer) {
                0 -> throw StackOverflowError("Exceeding memory lower bound")
                else -> copy(currentPointer = currentPointer - 1)
            }

        fun putChar(newChar: Char) = copy(memory = memory.also {
            it[currentPointer] = newChar.toByte()
        })

        val outputApplied: State
            get() = copy(output = output + currentChar)
    }

    private val String.firstOpeningBraceIndex: Int
        get () = this.indexOfFirst { it == '[' }

    private val String.isValidProgram: Boolean
        get () = filter{ it in "[]"}
            .groupingBy { it }
            .eachCount()
            .let { it['['] == it[']'] }

    fun interpret(program: String) = when(program.isValidProgram){
        true -> interpret(program, State())
        else -> throw IllegalArgumentException("The program is not valid (brackets dont match)")
    }

    private fun interpret(program: String, interpretingState: State): State =
        program.fold(interpretingState){ prevState, instruction ->
            when(instruction){
                '+' -> prevState.incremented
                '-' -> prevState.decremented
                '>' -> prevState.rightShifted
                '<' -> prevState.leftShifted
                '.' -> prevState.outputApplied
                ',' -> prevState.putChar(readLine()?.get(0) ?: Char.MIN_VALUE)
                '[' -> return interpret(program.substring(program.firstOpeningBraceIndex + 1), prevState)
                ']' -> if (prevState.currentChar != 0.toChar()) return interpret(program, prevState) else prevState
                else -> prevState
            }
    }
}