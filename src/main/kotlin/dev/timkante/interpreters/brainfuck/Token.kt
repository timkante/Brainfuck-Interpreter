package dev.timkante.interpreters.brainfuck

enum class Token(val character: Char) {
    INCREMENT('+'),
    DECREMENT('-'),
    SHIFT_RIGHT('>'),
    SHIFT_LEFT('<'),
    LOOP_END(']'),
    LOOP_BEGIN('['),
    PRINT('.');

    companion object {
        fun fromChar(c: Char): Token? =
            values().firstOrNull { it.character == c }
    }
}