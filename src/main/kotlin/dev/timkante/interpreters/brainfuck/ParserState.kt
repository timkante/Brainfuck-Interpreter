package dev.timkante.interpreters.brainfuck

data class ParserState(val scope: Scope = Scope(), val stack: List<Scope> = listOf()) {
    fun parseNext(token: Token): ParserState =
        when (token) {
            Token.INCREMENT -> copy(scope = Scope(commands = scope.commands + ModifyValue(delta = 1)))
            Token.DECREMENT -> copy(scope = Scope(commands = scope.commands + ModifyValue(delta = -1)))
            Token.SHIFT_RIGHT -> copy(scope = Scope(commands = scope.commands + ModifyPointer(delta = 1)))
            Token.SHIFT_LEFT -> copy(scope = Scope(commands = scope.commands + ModifyPointer(delta = -1)))
            Token.PRINT -> copy(scope = Scope(commands = scope.commands + PrintValue))
            Token.LOOP_BEGIN -> ParserState(scope = Scope(), stack = stack + scope)
            Token.LOOP_END -> ParserState(
                scope = (stack.lastOrNull() ?: throw IllegalStateException("unexpected closing bracket")).run {
                    copy(commands = commands + Loop(scope))
                },
                stack = stack.dropLast(1)
            )
        }

    fun finish(): Scope {
        check(stack.isEmpty()) { "missing closing bracket" }
        return scope
    }
}