package dev.timkante.interpreters.brainfuck

object Interpreter {

    private val String.asAST: Scope
        get() = asSequence()
            .mapNotNull { Token.fromChar(it) }
            .fold(ParserState()) { parser, token -> parser.parseNext(token) }
            .finish()

    fun interpret(program: String) = program.asAST
        .invoke(MemoryState())
        .output
}
