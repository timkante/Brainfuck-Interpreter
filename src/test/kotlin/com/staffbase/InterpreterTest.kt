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

        const val BOTTLES = "##########################\n" +
                "###\n" +
                "### Severely updated version!\n" +
                "### (now says \"1 bottle\" and\n" +
                "### contains no extra \"0\" verse)\n" +
                "###\n" +
                "##########################\n" +
                "### 99 Bottles of Beer ###\n" +
                "### coded in Brainfuck ###\n" +
                "### with explanations  ###\n" +
                "##########################\n" +
                "#\n" +
                "# This Bottles of Beer program\n" +
                "# was written by Andrew Paczkowski\n" +
                "# Coder Alias: thepacz\n" +
                "# three_halves_plus_one@yahoo.com\n" +
                "#####\n" +
                "\n" +
                ">                            0 in the zeroth cell\n" +
                "+++++++>++++++++++[<+++++>-] 57 in the first cell or \"9\"\n" +
                "+++++++>++++++++++[<+++++>-] 57 in second cell or \"9\"\n" +
                "++++++++++                   10 in third cell\n" +
                ">+++++++++                    9 in fourth cell\n" +
                "\n" +
                "##########################################\n" +
                "### create ASCII chars in higher cells ###\n" +
                "##########################################\n" +
                "\n" +
                ">>++++++++[<++++>-]               \" \"\n" +
                ">++++++++++++++[<+++++++>-]        b\n" +
                "+>+++++++++++[<++++++++++>-]       o\n" +
                "++>+++++++++++++++++++[<++++++>-]  t\n" +
                "++>+++++++++++++++++++[<++++++>-]  t\n" +
                ">++++++++++++[<+++++++++>-]        l\n" +
                "+>++++++++++[<++++++++++>-]        e\n" +
                "+>+++++++++++++++++++[<++++++>-]   s\n" +
                ">++++++++[<++++>-]                \" \"\n" +
                "+>+++++++++++[<++++++++++>-]       o\n" +
                "++>++++++++++[<++++++++++>-]       f\n" +
                ">++++++++[<++++>-]                \" \"\n" +
                ">++++++++++++++[<+++++++>-]        b\n" +
                "+>++++++++++[<++++++++++>-]        e\n" +
                "+>++++++++++[<++++++++++>-]        e\n" +
                ">+++++++++++++++++++[<++++++>-]    r\n" +
                ">++++++++[<++++>-]                \" \"\n" +
                "+>+++++++++++[<++++++++++>-]       o\n" +
                ">+++++++++++[<++++++++++>-]        n\n" +
                ">++++++++[<++++>-]                \" \"\n" +
                "++>+++++++++++++++++++[<++++++>-]  t\n" +
                "++++>++++++++++[<++++++++++>-]     h\n" +
                "+>++++++++++[<++++++++++>-]        e\n" +
                ">++++++++[<++++>-]                \" \"\n" +
                "++>+++++++++++++[<+++++++++>-]     w\n" +
                "+>++++++++++++[<++++++++>-]        a\n" +
                ">++++++++++++[<+++++++++>-]        l\n" +
                ">++++++++++++[<+++++++++>-]        l\n" +
                ">+++++[<++>-]                      LF\n" +
                "++>+++++++++++++++++++[<++++++>-]  t\n" +
                "+>++++++++++++[<++++++++>-]        a\n" +
                "+++>+++++++++++++[<++++++++>-]     k\n" +
                "+>++++++++++[<++++++++++>-]        e\n" +
                ">++++++++[<++++>-]                \" \"\n" +
                "+>+++++++++++[<++++++++++>-]       o\n" +
                ">+++++++++++[<++++++++++>-]        n\n" +
                "+>++++++++++[<++++++++++>-]        e\n" +
                ">++++++++[<++++>-]                \" \"\n" +
                ">++++++++++[<++++++++++>-]         d\n" +
                "+>+++++++++++[<++++++++++>-]       o\n" +
                "++>+++++++++++++[<+++++++++>-]     w\n" +
                ">+++++++++++[<++++++++++>-]        n\n" +
                ">++++++++[<++++>-]                \" \"\n" +
                "+>++++++++++++[<++++++++>-]        a\n" +
                ">+++++++++++[<++++++++++>-]        n\n" +
                ">++++++++++[<++++++++++>-]         d\n" +
                ">++++++++[<++++>-]                \" \"\n" +
                "++>+++++++++++[<++++++++++>-]      p\n" +
                "+>++++++++++++[<++++++++>-]        a\n" +
                "+>+++++++++++++++++++[<++++++>-]   s\n" +
                "+>+++++++++++++++++++[<++++++>-]   s\n" +
                ">++++++++[<++++>-]                \" \"\n" +
                "+>+++++++++++++[<++++++++>-]       i\n" +
                "++>+++++++++++++++++++[<++++++>-]  t\n" +
                ">++++++++[<++++>-]                \" \"\n" +
                "+>++++++++++++[<++++++++>-]        a\n" +
                ">+++++++++++++++++++[<++++++>-]    r\n" +
                "+>+++++++++++[<++++++++++>-]       o\n" +
                ">+++++++++++++[<+++++++++>-]       u\n" +
                ">+++++++++++[<++++++++++>-]        n\n" +
                ">++++++++++[<++++++++++>-]         d\n" +
                ">+++++[<++>-]                      LF\n" +
                "+++++++++++++                      CR\n" +
                "\n" +
                "[<]>>>>      go back to fourth cell\n" +
                "\n" +
                "#################################\n" +
                "### initiate the display loop ###\n" +
                "#################################\n" +
                "\n" +
                "[            loop\n" +
                " <           back to cell 3\n" +
                " [            loop\n" +
                "  [>]<<       go to last cell and back to LF\n" +
                "  ..          output 2 newlines\n" +
                "  [<]>        go to first cell\n" +
                "\n" +
                " ###################################\n" +
                " #### begin display of characters###\n" +
                " ###################################\n" +
                " #\n" +
                " #.>.>>>.>.>.>.>.>.>.>.>.>.>.>.>.>.>.>.>.>\n" +
                " #X X     b o t t l e s   o f   b e e r  \n" +
                " #.>.>.>.>.>.>.>.>.>.>.>.\n" +
                " #o n   t h e   w a l l N\n" +
                " #[<]>    go to first cell\n" +
                " #.>.>>>.>.>.>.>.>.>.>.>.>.>.>.>.>.>.>.>>>>>>>>>>>>>.>\n" +
                " #X X     b o t t l e s   o f   b e e r             N\n" +
                " #.>.>.>.>.>.>.>.>.>.>.>.>.>.>.>.>.>.>.>.>.>.>.>\n" +
                " #t a k e   o n e   d o w n   a n d   p a s s   \n" +
                " #.>.>.>.>.>.>.>.>.>.\n" +
                " #i t   a r o u n d N\n" +
                " #####\n" +
                "\n" +
                "  [<]>>      go to cell 2\n" +
                "  -          subtract 1 from cell 2\n" +
                "  <          go to cell 1\n" +
                "\n" +
                " ########################\n" +
                " ### display last line ##\n" +
                " ########################\n" +
                " #\n" +
                " #.>.>>>.>.>.>.>.>.>.>.>.>.>.>.>.>.>.>.>.>\n" +
                " #X X     b o t t l e s   o f   b e e r  \n" +
                " #.>.>.>.>.>.>.>.>.>.>.\n" +
                " #o n   t h e   w a l l\n" +
                " #####\n" +
                "\n" +
                "  [<]>>>-      go to cell 3/subtract 1\n" +
                " ]            end loop when cell 3 is 0\n" +
                " ++++++++++   add 10 to cell 3\n" +
                " <++++++++++  back to cell 2/add 10\n" +
                " <-           back to cell 1/subtract 1\n" +
                " [>]<.        go to last line/carriage return\n" +
                " [<]>         go to first line\n" +
                "\n" +
                "########################\n" +
                "### correct last line ##\n" +
                "########################\n" +
                "#\n" +
                "#.>.>>>.>.>.>.>.>.>.>.>.>.>.>.>.>.>.>.>.>\n" +
                "#X X     b o t t l e s   o f   b e e r  \n" +
                "#.>.>.>.>.>.>.>.>.>.>.\n" +
                "#o n   t h e   w a l l\n" +
                "#####\n" +
                "\n" +
                " [<]>>>>-    go to cell 4/subtract 1\n" +
                "]           end loop when cell 4 is 0\n" +
                "\n" +
                "##############################################################\n" +
                "### By this point verses 99\\10 are displayed but to work   ###\n" +
                "### with the lower numbered verses in a more readable way  ###\n" +
                "### we initiate a new loop for verses 9{CODE} that will not    ###\n" +
                "### use the fourth cell at all                             ###\n" +
                "##############################################################\n" +
                "\n" +
                "+           add 1 to cell four (to keep it non\\zero)\n" +
                "<--         back to cell 3/subtract 2\n" +
                "\n" +
                "[            loop\n" +
                " [>]<<       go to last cell and back to LF\n" +
                " ..          output 2 newlines\n" +
                " [<]>        go to first cell\n" +
                "\n" +
                " ###################################\n" +
                " #### begin display of characters###\n" +
                " ###################################\n" +
                " #\n" +
                " #>.>>>.>.>.>.>.>.>.>.>.>.>.>.>.>.>.>.>.>\n" +
                " # X     b o t t l e s   o f   b e e r  \n" +
                " #.>.>.>.>.>.>.>.>.>.>.>.\n" +
                " #o n   t h e   w a l l N\n" +
                " #[<]>    go to first cell\n" +
                " #>.>>>.>.>.>.>.>.>.>.>.>.>.>.>.>.>.>.>>>>>>>>>>>>>.>\n" +
                " # X     b o t t l e s   o f   b e e r             N\n" +
                " #.>.>.>.>.>.>.>.>.>.>.>.>.>.>.>.>.>.>.>.>.>.>.>\n" +
                " #t a k e   o n e   d o w n   a n d   p a s s   \n" +
                " #.>.>.>.>.>.>.>.>.>.\n" +
                " #i t   a r o u n d N\n" +
                " #####\n" +
                "\n" +
                " [<]>>       go to cell 2\n" +
                " -           subtract 1 from cell 2\n" +
                "\n" +
                " ########################\n" +
                " ### display last line ##\n" +
                " ########################\n" +
                " #\n" +
                " #.>>>.>.>.>.>.>.>.>.>.>.>.>.>.>.>.>.>.>\n" +
                " #X     b o t t l e s   o f   b e e r  \n" +
                " #.>.>.>.>.>.>.>.>.>.>.\n" +
                " #o n   t h e   w a l l\n" +
                " #####\n" +
                "\n" +
                " [<]>>>-     go to cell 3/subtract 1\n" +
                "]            end loop when cell 3 is 0\n" +
                "+            add 1 to cell 3 to keep it non\\zero\n" +
                "\n" +
                "[>]<.        go to last line/carriage return\n" +
                "[<]>         go to first line\n" +
                "\n" +
                "########################\n" +
                "### correct last line ##\n" +
                "########################\n" +
                "#\n" +
                "#>.>>>.>.>.>.>.>.>.>>.>.>.>.>.>.>.>.>.>\n" +
                "# X     b o t t l e    o f   b e e r  \n" +
                "#.>.>.>.>.>.>.>.>.>.>.<<<<.\n" +
                "#o n   t h e   w a l l\n" +
                "#####\n" +
                "\n" +
                "[>]<<       go to last cell and back to LF\n" +
                "..          output 2 newlines\n" +
                "[<]>        go to first line\n" +
                "\n" +
                "#########################\n" +
                "### the final verse    ##\n" +
                "#########################\n" +
                "#\n" +
                "#>.>>>.>.>.>.>.>.>.>>.>.>.>.>.>.>.>.>.>\n" +
                "# X     b o t t l e    o f   b e e r  \n" +
                "#.>.>.>.>.>.>.>.>.>.>.>.\n" +
                "#o n   t h e   w a l l N\n" +
                "#[<]>        go to first cell\n" +
                "#>.>>>.>.>.>.>.>.>.>>.>.>.>.>.>.>.>.>>>>>>>>>>>>>.>\n" +
                "# X     b o t t l e    o f   b e e r             N\n" +
                "#.>.>.>.>.>.>.>.>.>.>.>.>.>.>.>.>.>.>.>.>.>.>.>\n" +
                "#t a k e   o n e   d o w n   a n d   p a s s   \n" +
                "#.>.>.>.>.>.>.>.>.>.\n" +
                "#i t   a r o u n d N\n" +
                "#[>]<        go to last line\n" +
                "#<<<.<<.<<<.\n" +
                "#   n  o    \n" +
                "#[<]>>>>     go to fourth cell\n" +
                "#>.>.>.>.>.>.>.>.>.>.>.>.>.>.>.>.>.>\n" +
                "#   b o t t l e s   o f   b e e r  \n" +
                "#.>.>.>.>.>.>.>.>.>.>.>.\n" +
                "#o n   t h e   w a l l N\n" +
                "#####fin##"

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
        assertEquals(increments, finishingState.currentValue)
    }

    @Test
    fun `can do decrement`(){
        val finishingState = interpreterInstance.interpret("+++---")
        assertEquals(0, finishingState.currentValue)
    }

    @Test
    fun `can do increment and decrement`(){
        val finishingState = interpreterInstance.interpret("+++++++--")
        assertEquals(5, finishingState.currentValue)
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
        assertEquals(1, finishingState.currentValue)
    }

    @Test
    fun `can parse empty loop`(){
        val finishingState = interpreterInstance.interpret("[]")
        assertEquals(0, finishingState.currentValue)
    }

    @Test
    fun `can parse multiple empty loops`(){
        val finishingState = interpreterInstance.interpret("[][][][]")
        assertEquals(0, finishingState.currentValue)
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
        assertEquals(0, finishingState.currentValue)
    }

    @Test
    fun `can parse multiple nested empty loops`(){
        val finishingState = interpreterInstance.interpret("[[][[][]]]")
        assertEquals(0, finishingState.currentValue)
    }

   @Test
   fun `can parse hello world`(){
       val result = interpreterInstance.interpret(HELLO_WORLD)
       assertEquals("Hello World!\n\r", result.output)
   }

    @Test
    fun `can parse bottle program`(){
        val result = interpreterInstance.interpret(BOTTLES)
        assertNotEquals("", result.output)
    }
}
