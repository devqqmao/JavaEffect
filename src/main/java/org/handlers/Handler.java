package org.handlers;

import java.util.Map;
import java.util.function.Consumer;

public class Handler {

    static Context context;
    static ContinuationScope scope;

    static {
        context = new Context();
        scope = new ContinuationScope("cont");
    }

    public static Integer ask() {
        context.effect = "ask";
        Continuation.yield(scope);
        return context.result;
    }

    public static void put(Integer p) {
        context.effect = "put";
        context.result = p;
        Continuation.yield(scope);
    }

    public static Integer handle(Map<String, Consumer<Continuation>> handlers, Runnable comp) {
        Continuation k = new Continuation(scope, comp);
        k.run();

        while (!k.isDone()) {
            handlers.get(context.effect).accept(k);
        }
        return context.result;
    }
}
