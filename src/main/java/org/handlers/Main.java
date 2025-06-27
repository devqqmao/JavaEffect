package org.handlers;


import java.util.Map;
import java.util.NoSuchElementException;

import static org.handlers.Handler.*;

public class Main {

    public static void main(String[] args) {
        dependency_injection();
    }

    public static Integer dependency_injection() {
        int res = handle(
                Map.of("ask", (k) -> {
                            context.result = 2;
                            k.run();
                        }
                        , "put", Continuation::run
                )
                , () -> {
                    put(ask() + ask());
                }
        );
        return res;
    }

    public static Integer generator(Integer n) {
        int res = handle(
                Map.of("next", (k) -> {
                            int i = 0;
                            while (i < n && !k.isDone()) {
                                context.result = i;
                                i += 1;
                                k.run();
                            }
                            if (!k.isDone()) {
                                throw new NoSuchElementException("ERROR");
                            }
                        }
                )
                , () -> {
                    int i = 0;
                    while (i < n) {
                        next();
                        i += 1;
                    }
                }
        );
        return 0;
    }

    public static Integer nested_handling() {
        int res = handle(
                Map.of("put", Continuation::run
                )
                , () -> {
                    int res0 = handle(
                            Map.of("ask", (k) -> {
                                        context.result = 1;
                                        k.run();
                                    }
                            )
                            , () -> {
                                int res1 = handle(
                                        Map.of(
                                        )
                                        , () -> {
                                            int x = ask();
                                            put(x);
                                        }
                                );
                            }
                    );
                    put(res0 + res0);
//                    ask(); will throw
                }
        );
        return 0;
    }


}
