package org.handlers;

import org.handlers.Continuation.Continuation;

import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

import static org.handlers.Handlers.Handle.*;

public class Main {

    public static void main(String[] args) {
//        dependency_injection();
//        nested_handling();
        generator(3);
    }

    public static Object pure_effect() {
        return handle(
                Map.of("ASK", Continuation::run
                )
                , () -> {
                    perform("ASK");
                }
        );
    }

    public static Object pure_compute() {
        int res = 0;
        for (int i = 0; i < 1000; i++) {
            res += i;
        }
        return res;
    }

    public static Object perform_effect() {
        return handle(
                Map.of("ASK", (k) -> {
                            setState(0);
                            k.run();
                        }
                )
                , () -> {
                    perform("ASK");
                }
        );
    }

    public static Object dependency_injection() {
        Object res = handle(
                Map.of("ASK", (k) -> {
                            setState(2);
                            k.run();
                        }
                        , "PUT", (k) -> {
                            k.run();
                        }
                )
                , () -> {
                    Object x = perform("ASK");
                    perform("PUT", x);
                }
        );

        return res;
    }

    public static Object generator(Integer n) {
        return handle(
                Map.of("NEXT", (k) -> {
                            Integer i = 0;
                            while (i < n && !k.isDone()) {
                                setState(i);
                                i += 1;
                                k.run();
                            }
                            if (!k.isDone()) {
                                throw new NoSuchElementException();
                            }
                        }
                )
                , () -> {
                    Integer i = 0;
                    while (i < n) {
                        perform("NEXT");
                        i++;
                    }
                }
        );
    }

    public static Object nested_handling() {
        Object res0 = handle(
                Map.of("PUT", Continuation::run
                )
                , () -> {
                    Object res1 = handle(
                            Map.of("ASK", (k) -> {
                                        context.setState(2);
                                        k.run();
                                    }
                            )
                            , () -> {
                                Object res2 = handle(
                                        Map.of()
                                        , () -> {
                                            Object x = perform("ASK");
                                            perform("PUT", x);
                                        }
                                );
                            }
                    );
                    perform("PUT", res1);
//                    perform("ASK"); // ASK will throw
                }
        );
        return res0;
    }

}
