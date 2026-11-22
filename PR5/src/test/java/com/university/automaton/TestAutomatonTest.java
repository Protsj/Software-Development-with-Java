package com.university.automaton;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TestAutomatonTest {

    private final TestAutomaton automaton = new TestAutomaton();

    @ParameterizedTest(name = "Рядок \"{0}\" -> стан {1}")
    @CsvSource(nullValues = "NULL", value = {
            "NULL, S",            // null -> S
            "'', S",              // Порожній рядок -> S
            "abcdef, S",          // Немає символу 'T' -> S
            "abcT, ONE",          // Зупинка на 'T' -> 1
            "abcTabc, S",         // Після 'T' не 'E' -> скидання в S
            "abcTE, TWO",         // Зупинка на "TE" -> 2
            "abcTEabc, S",        // Після "TE" не 'S' -> скидання в S
            "abcTES, THREE",      // Приклад з умови -> 3
            "abcTESabc, S",       // Після "TES" не 'T' -> скидання в S
            "TEST, F",            // Точний збіг -> F
            "abcTESTabc, F",      // Приклад з умови -> F
            "TEST12345, F",       // Залишається у F після знаходження слова
            "test, S",            // Чутливість до регістру
            "TTEST, F",           // Перехід 1 --'T'--> 1 замість скидання в S
            "TTTEST, F",          // Кілька 'T' поспіль перед "EST"
            "abcTTESTxyz, F",
            "TETEST, F",          // Перехід 2 --'T'--> 1 замість скидання в S
            "TETETEST, F",        // Повторення "TE" перед "TEST"
            "abcTETESTxyz, F",
            "TTETEST, F",         // Комбінований випадок
            "TESTEST, F",         // Накладання слів
            "abcTT, ONE",         // Незавершене перекриття в стані 1
            "abcTET, ONE"         // Незавершене перекриття в стані 2
    })
    void testFiniteAutomaton(String input, State expectedState) {
        assertEquals(expectedState, automaton.process(input));
    }
}