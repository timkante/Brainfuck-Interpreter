package com.staffbase

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import kotlin.ArithmeticException

class InterpreterTest{

    companion object{
        val interpreterInstance = Interpreter()

        const val HELLO_WORLD = "++++++++++[>+++++++>++++++++++>+++>+<<<<-]>++.>+." +
                "+++++++..+++.>++.<<+++++++++++++++.>.+++.------.--------.>+.>.+++."

        const val CELL_WIDTH = "Calculate the value 256 and test if it's zero\n" +
                "If the interpreter errors on overflow this is where it'll happen\n" +
                "++++++++[>++++++++<-]>[<++++>-]\n" +
                "+<[>-<\n" +
                "    Not zero so multiply by 256 again to get 65536\n" +
                "    [>++++<-]>[<++++++++>-]<[>++++++++<-]\n" +
                "    +>[>\n" +
                "        # Print \"32\"\n" +
                "        ++++++++++[>+++++<-]>+.-.[-]<\n" +
                "    <[-]<->] <[>>\n" +
                "        # Print \"16\"\n" +
                "        +++++++[>+++++++<-]>.+++++.[-]<\n" +
                "<<-]] >[>\n" +
                "    # Print \"8\"\n" +
                "    ++++++++[>+++++++<-]>.[-]<\n" +
                "<-]<\n" +
                "# Print \" bit cells\\n\"\n" +
                "+++++++++++[>+++>+++++++++>+++++++++>+<<<<-]>-.>-.+++++++.+++++++++++.<.\n" +
                ">>.++.+++++++..<-.>>-\n" +
                "Clean up used cells.\n" +
                "[[-]<]"

        @JvmStatic
        fun getIncrements() = (0 until 100).map {index -> Arguments.of("+".repeat(index), index)}.stream()

        @JvmStatic
        fun getRightShifts() = (0 until 100).map {index -> Arguments.of(">".repeat(index), index)}.stream()
    }

    @Test
    fun `can interpret empty string`(){
        assertEquals("", interpreterInstance.interpret("").output)
    }

    @ParameterizedTest
    @MethodSource("getIncrements")
    fun `can do increment`(program: String, increments: Int){
        val finishingState = interpreterInstance.interpret(program)
        assertEquals(increments, finishingState.currentChar.toInt())
    }

    @Test
    fun `can do decrement`(){
        val finishingState = interpreterInstance.interpret("+++---")
        assertEquals(0, finishingState.currentChar.toInt())
    }

    @Test
    fun `can do increment and decrement`(){
        val finishingState = interpreterInstance.interpret("+++++++--")
        assertEquals(5, finishingState.currentChar.toInt())
    }

    @ParameterizedTest
    @MethodSource("getRightShifts")
    fun `can do right shift`(program: String, shifts: Int){
        val finishingState = interpreterInstance.interpret(program)
        assertEquals(shifts, finishingState.currentPointer)
    }

    @Test
    fun `can do left shift`(){
        val finishingState = interpreterInstance.interpret(">>><<")
        assertEquals(1, finishingState.currentPointer)
    }

    @Test
    fun `can do shifts, increment and decrement`(){
        val finishingState = interpreterInstance.interpret(">++-+->><<")
        assertEquals(1, finishingState.currentPointer)
        assertEquals(1, finishingState.currentChar.toInt())
    }

    @Test
    fun `can parse empty loop`(){
        val finishingState = interpreterInstance.interpret("[]")
        assertEquals(0, finishingState.currentChar.toInt())
    }

    @Test
    fun `can parse multiple empty loops`(){
        val finishingState = interpreterInstance.interpret("[][][][]")
        assertEquals(0, finishingState.currentChar.toInt())
    }

    @Test
    fun `can detect not matching brackets`(){
        assertThrows(IllegalArgumentException::class.java) {
            interpreterInstance.interpret("[[[][]]")
        }
        assertThrows(IllegalArgumentException::class.java) {
            interpreterInstance.interpret("[[[]]")
        }
    }

    @Test
    fun `can parse nested empty loops`(){
        val finishingState = interpreterInstance.interpret("[[[]]]")
        assertEquals(0, finishingState.currentChar.toInt())
    }

    @Test
    fun `can parse multiple nested empty loops`(){
        val finishingState = interpreterInstance.interpret("[[][[][]]]")
        assertEquals(0, finishingState.currentChar.toInt())
    }

   @Test
   fun `can parse hello world`(){
       val result = interpreterInstance.interpret(HELLO_WORLD)
       assertEquals("Hello World!\n\r", result.output)
   }

    @Test
    fun `can test cell width`(){
        assertThrows(ArithmeticException::class.java) {
            interpreterInstance.interpret(CELL_WIDTH)
        }
    }
}
