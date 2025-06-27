package org.handlers;


import java.util.Map;
import java.util.NoSuchElementException;

import static org.handlers.Handler.*;

public class Main {

    public static void main(String[] args) {
//        dependency_injection();
//        generators();
//        nested_handling();
        coroutines_scheduler();
    }

    public static void dependency_injection() {
        int res = handle(
                Map.of("ask", (k) -> {
                            context.result = 2;
                            k.run();
                        }
                        , "put", Continuation::run)
                , () -> {
                    put(ask() + ask());
                }
        );
        System.out.println(res);
    }

    public static void generator() {
        int res = handle(Map.of("next", (k) -> {
            int i = 0;
            while (i < 2 && !k.isDone()) {
                context.result = i;
                i += 1;
                k.run();
            }
            if (!k.isDone()) {
                throw new NoSuchElementException("ERROR");
            }
        }), () -> {
            System.out.println(1);
            next();
            next();
            System.out.println(2);
        });
        System.out.println(res);
    }

    public static void nested_handling() {
        int res = handle(Map.of("put", Continuation::run), () -> {
            int res0 = handle(Map.of("ask", (k) -> {
                context.result = 1;
                k.run();
            }), () -> {
                int res1 = handle(Map.of(),
                        () -> {
                            int x = ask();
                            put(x);
                        });
            });
            put(res0 + res0);
//          ask(); will throw
        });
        System.out.println(res);
    }

    public static void sleep() {
        try {
            Thread.sleep(3 * 1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static void coroutines_scheduler() {
        int res = handle(
                Map.of("spawn",
                        Handler::schedule)
                ,
                () -> {
                    // scheduled 1
                    spawn(() -> {
                        sleep(); // compute
                        suspend();
                        sleep(); // compute
                    });

                    // scheduled 2
                    spawn(() -> {
                        sleep();
                        Continuation.yield(scope);
                        sleep();
                    });

                }
        );
        System.out.println(res);
    }
}


/*
`spawn` -> создает поток с таской
`yield`(k) -> { // текущая операция приостанавливается и отдает управление планировщику
queue.push(k);
queue.pop().resume();
}
 */
