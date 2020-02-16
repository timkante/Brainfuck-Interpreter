package com.staffbase

import java.io.IOException

class Interpreter {

    companion object{
        const val MAX_MEMSIZE = 65536
    }

    data class State (
        val currentPointer: Int = 0,
        val memory: IntArray = IntArray(MAX_MEMSIZE),
        val output: String = ""
    ) {
        val currentValue: Int get() = memory[currentPointer]

        val incremented: State get() = copy(memory = memory.also {
            if (it[currentPointer] == Int.MAX_VALUE) throw ArithmeticException("Value overflow")
            else ++it[currentPointer]
        })

        val decremented: State get() = copy(memory = memory.also {
            if (it[currentPointer] == Int.MIN_VALUE) throw ArithmeticException("Value underflow")
            else --it[currentPointer]
        })

        val rightShifted: State get()= when(currentPointer) {
            MAX_MEMSIZE -> throw StackOverflowError("Exceeding memory upper bound")
            else -> copy(currentPointer = currentPointer + 1)
        }

        val leftShifted: State get() = when(currentPointer) {
            0 -> throw StackOverflowError("Exceeding memory lower bound")
            else -> copy(currentPointer = currentPointer - 1)
        }

        fun putChar(newChar: Char) = copy(memory = memory.also {
            it[currentPointer] = newChar.toInt()
        })

        val outputApplied: State get() = copy(output = output + (currentValue.takeIf { when(it){
            in 0..Char.MAX_VALUE.toInt() -> true
            else -> false
        }}?.toChar() ?: throw IOException()))
    }

    private val String.isValidProgram: Boolean
        get () = filter{ it in "[]"}
            .groupingBy { it }
            .eachCount()
            .let { it['['] == it[']'] }

    fun interpret(program: String): State {
        if (!program.isValidProgram) throw IllegalArgumentException("The program is not valid (brackets dont match)")
        var state = State()
        var balance: Int = 0
        var index: Int = 0
        while (index < program.length){
            when(program[index]){
                '+' -> state = state.incremented
                '-' -> state = state.decremented
                '>' -> state = state.rightShifted
                '<' -> state = state.leftShifted
                '.' -> state = state.outputApplied
                ',' -> state = state.putChar(readLine()?.get(0) ?: Char.MIN_VALUE)
                '[' -> if (state.currentValue == 0){
                    ++index
                    while (balance > 0 || program[index] != ']'){
                        if (program[index] == '[') ++balance
                        else if (program[index] == ']') --balance
                        ++index
                    }
                }
                ']' -> if (state.currentValue != 0){
                    --index
                    while (balance > 0 || program[index] != '['){
                        if (program[index] == ']') ++balance
                        else if (program[index] == '[') --balance
                        --index
                    }
                    --index
                }
                else -> {}
            }
            ++index
        }
        return state
    }
}