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
        k.run();

    }

    public static Integer get() {
        Continuation.yield(scope);
        return context.getValue();
    }


    public static void put(Integer value) {
        context.setValue(value);
        Continuation.yield(scope);
    }

    public static void modify(Function<Integer, Integer> f) {
        context.setValue(f.apply(context.getValue()));
        Continuation.yield(scope);
    }

    public static Integer gets(Function<Integer, Integer> f) {
        Continuation.yield(scope);
        return f.apply(context.getValue());
    }

}
