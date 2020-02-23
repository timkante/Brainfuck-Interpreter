package dev.timkante.interpreters.brainfuck

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.RepeatedTest
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.junit.jupiter.params.provider.ValueSource
import kotlin.random.Random
import kotlin.test.assertEquals

@DisplayName("Commands")
class CommandsTest {
    @DisplayName("modify pointer")
    @ParameterizedTest(name = "{index} ==> {1} + {2} should be {3} (memory size = {0}")
    @CsvSource(
        "10, 9, 1, 0",
        "10, 9, 2, 1",
        "10, 8, 3, 1",
        "10, 7, 10, 7",
        "10, 6, 23, 9",
        "5, 0, -1, 4",
        "5, 0, -2, 3",
        "5, 1, -3, 3",
        "5, 2, -5, 2",
        "5, 3, -10, 3",
        "5, 4, -21, 3"
    )
    fun modifyPointer(memorySize: Int, initialIndex: Int, delta: Int, expectedIndex: Int) {
        val oldState = MemoryState(index = initialIndex, memory = List(size = memorySize) { 0 })
        val command = ModifyPointer(delta)
        val newState = command(oldState)
        assertEquals(expected = expectedIndex, actual = newState.index)
    }

    @DisplayName("modify value at random location to random value")
    @RepeatedTest(50)
    fun modifyValue() {
        val memorySize = 100
        val value = Random.nextInt()
        val index = Random.nextInt(memorySize)
        val modPtr = ModifyPointer(delta = index)
        val modVal = ModifyValue(delta = value)
        val oldState = MemoryState(memorySize)
        val newState = modVal(modPtr(oldState))
        assertEquals(expected = index, actual = newState.index)
        assertEquals(expected = value, actual = newState.memory[newState.index])
    }

    @DisplayName("print value (single char)")
    @ParameterizedTest(name = "{index} ==> should print \"{0}\"")
    @ValueSource(chars = ['A', '1', 'a', '[', '\n'])
    fun printValueChar(c: Char) {
        val oldState = MemoryState()
        val modVal = ModifyValue(delta = c.toInt())
        val printVal = PrintValue
        val newState = printVal(modVal(oldState))
        assertEquals(expected = c.toString(), actual = newState.output)
    }

    @DisplayName("print value (string)")
    @ParameterizedTest(name = "{index} ==> should print \"{0}\"")
    @ValueSource(strings = ["hello", "42", "<b>this is fat</b>", """{"message": "enter JSON"}"""])
    fun printValueString(s: String) {
        val oldState = MemoryState()
        val commands = Scope(
            commands = s.asSequence()
                .flatMap { c ->
                    sequenceOf(ModifyValue(c.toInt()), PrintValue, ModifyPointer(1))
                }.toList()
        )
        val newState = commands(oldState)
        assertEquals(expected = s, actual = newState.output)
    }

    @ParameterizedTest(name = "{index} ==> looping {0} times")
    @ValueSource(ints = [0, 1, 10, 23])
    fun looping(count: Int) {
        val oldState = MemoryState()
        val program = Scope(
            ModifyValue(delta = count), // set field 0 to our count
            Loop(
                ModifyPointer(delta = 1), // switch to field 1
                ModifyValue(delta = 1), // increment by 1
                ModifyPointer(delta = -1), // switch back to field 0 (our counter)
                ModifyValue(delta = -1) // decrement by 1
            )
        )
        val newState = program(oldState)
        assertEquals(expected = count, actual = newState.memory[1])
    }

    @ParameterizedTest(name = "{index} ==> calculating sum of 0..{0}")
    @ValueSource(ints = [0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10])
    fun nestedLooping(count: Int) {
        val oldState = MemoryState()
        val program = Scope(
            ModifyValue(delta = count),
            Loop(
                Loop(
                    ModifyPointer(delta = 1),
                    ModifyValue(delta = 1),
                    ModifyPointer(delta = 1),
                    ModifyValue(delta = 1),
                    ModifyPointer(delta = -2),
                    ModifyValue(delta = -1)
                ),
                ModifyPointer(delta = 2),
                Loop(
                    ModifyPointer(delta = -2),
                    ModifyValue(delta = 1),
                    ModifyPointer(delta = 2),
                    ModifyValue(delta = -1)
                ),
                ModifyPointer(delta = -2),
                ModifyValue(delta = -1)
            )
        )
        val newState = program(oldState)
        assertEquals(expected = (0..count).sum(), actual = newState.memory[1])
    }
}
