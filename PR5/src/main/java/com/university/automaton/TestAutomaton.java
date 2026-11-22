package com.university.automaton;

public class TestAutomaton {

    public State process(String input) {
        if (input == null || input.isEmpty()) {
            return State.S;
        }

        State currentState = State.S;
        for (char ch : input.toCharArray()) {
            currentState = nextState(currentState, ch);
        }
        return currentState;
    }

    private State nextState(State state, char ch) {
        return switch (state) {
            case S -> (ch == 'T') ? State.ONE : State.S;
            case ONE -> {
                if (ch == 'E') yield State.TWO;
                if (ch == 'T') yield State.ONE;
                yield State.S;
            }
            case TWO -> {
                if (ch == 'S') yield State.THREE;
                if (ch == 'T') yield State.ONE;
                yield State.S;
            }
            case THREE -> (ch == 'T') ? State.F : State.S;
            case F -> State.F;
        };
    }
}