package org.handlers;

import org.handlers.Continuation.Continuation;

import java.util.Map;
import java.util.NoSuchElementException;

import static org.handlers.Handlers.Handle.*;

public class Main {

    public static void main(String[] args) {
        dependency_injection();
        iterator(2);
        nested_handling();
    }

    public static Object dependency_injection() {
        Object res = handle(
                Map.of("ASK", (k) -> {
                            context.setState(2);
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

    public static Object iterator(Integer n) {
        Object res = handle(
                Map.of("NEXT", (k) -> {
                            Integer i = 0;
                            while (i < n && !k.isDone()) {
                                context.setState(i);
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
                        perform("NEXT");
                        i += 1;
                    }
                }
        );
        return res;
    }

    public static Object nested_handling() {
        Object res = handle(
                Map.of("PUT", Continuation::run
                )
                , () -> {
                    Object res0 = handle(
                            Map.of("ASK", (k) -> {
                                        context.setState(2);
                                        k.run();
                                    }
                            )
                            , () -> {
                                Object res1 = handle(
                                        Map.of(
                                        )
                                        , () -> {
                                            Object x = perform("ASK");
                                            perform("PUT", x);
                                        }
                                );
                            }
                    );
                    perform("PUT", res0);
//                    ask(); will throw
                }
        );
        return res;
    }


}
