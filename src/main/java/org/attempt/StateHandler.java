package org.attempt;


/*
Реализовать интерфейс монады State

1. get
2. put
3. modify
4. gets
------------------------------------
Сделать return block

------------------------------------

`handle hReader (handle hException comp)`

 */

import java.util.function.Function;

public class StateHandler {
    static StateContext context;
    static ContinuationScope scope;

    static {
        context = new StateContext();
        scope = new ContinuationScope("state");
    }

    public static void handleState(Runnable comp) {
        Continuation k = new Continuation(scope, comp);
//        while (!k.isDone()) {
//        }
//        k.run();
        while(!k.isDone()) {
            k.run();
        }

    }

    public static Integer get() {
        Continuation.yield(scope);
        throw new IllegalStateException("Illegal control flow passing");
    }


    public static void put(Integer value) {
        context.setValue(value);
        Continuation.yield(scope);
        throw new IllegalStateException("Illegal control flow passing");
    }

    public static void modify(Function<Integer, Integer> f) {
        context.setValue(f.apply(context.getValue()));
        Continuation.yield(scope);
        throw new IllegalStateException("Illegal control flow passing");
    }

    public static Integer gets(Function<Integer, Integer> f) {
        Continuation.yield(scope);
        throw new IllegalStateException("Illegal control flow passing");
    }

}
