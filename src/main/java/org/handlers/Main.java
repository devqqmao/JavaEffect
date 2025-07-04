package org.handlers;

import org.handlers.Continuation.Continuation;

import java.util.Map;
import java.util.NoSuchElementException;

import static org.handlers.Handlers.Handle.*;

public class Main {

    public static void main(String[] args) {
        System.out.println(dependency_injection());
        System.out.println(nested_handling());
    }

    public static Object pure_effect() {
        return handle(
                Map.of("ASK", (k) -> {
                        }
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
                        }
                        , "PUT", (k) -> {
                        }
                )
                , () -> {
                    Object x = perform("ASK");
                    perform("PUT", x);
                }
        );

        return res;
    }


    public static Object nested_handling() {
        Object res0 = handle(
                Map.of("PUT", (k) -> {
                        }
                )
                , () -> {
                    Object res1 = handle(
                            Map.of("ASK", (k) -> {
                                        context.setState(2);
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
