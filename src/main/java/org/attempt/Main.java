package org.attempt;


import static org.attempt.StateHandler.handleState;

public class Main {


    public static void main(String[] args) {
        handleState(() -> {
            StateHandler.put(41);
            System.out.println(StateHandler.get());
        });
    }

}
