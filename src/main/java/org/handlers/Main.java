package org.handlers;

import java.util.Map;
import java.util.NoSuchElementException;

import static org.handlers.Effect.*;
import static org.handlers.Handle.*;

public class Main {

    public static void main(String[] args) {
        dependency_injection();
        generator(2);
        nested_handling();
    }

    public static Object dependency_injection() {
        Object res = handle(
                Map.of(ASK, (k) -> {
                            context.setResult(2);
                            k.run();
                        }
                        , PUT, Continuation::run
                )
                , () -> {
                    put(ask());
                }
        );

        return res;
    }

    public static Object generator(Integer n) {
        Object res = handle(
                Map.of(NEXT, (k) -> {
                            Integer i = 0;
                            while (i < n && !k.isDone()) {
                                context.setResult(i);
                                i += 1;
                                k.run();
                            }
                            if (!k.isDone()) {
                                throw new NoSuchElementException("ERROR");
                            }
                        }
                )
                , () -> {
                    Integer i = 0;
                    while (i < n) {
                        next();
                        i += 1;
                    }
                }
        );
        return res;
    }

    public static Object nested_handling() {
        Object res = handle(
                Map.of(PUT, Continuation::run
                )
                , () -> {
                    Object res0 = handle(
                            Map.of(ASK, (k) -> {
                                        context.setResult(2);
                                        k.run();
                                    }
                            )
                            , () -> {
                                Object res1 = handle(
                                        Map.of(
                                        )
                                        , () -> {
                                            Object x = ask();
                                            put(x);
                                        }
                                );
                            }
                    );
                    put(res0);
//                    ask(); will throw
                }
        );
        return res;
    }


}
