package org.handlers;

import java.util.ArrayList;
import java.util.ListIterator;
import java.util.Map;
import java.util.NoSuchElementException;
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

    public static Integer next() {
        context.effect = "next";
        Continuation.yield(scope);
        return context.result;
    }

    public static boolean find_handler(Continuation k) {
        String effect = context.effect;
        ListIterator<Map<String, Consumer<Continuation>>> it = context.handlersStack.listIterator(context.handlersStack.size());
        boolean is_handled = false;
        while (it.hasPrevious() && !is_handled) {
            Map<String, Consumer<Continuation>> element = it.previous();
            if (element.containsKey(effect)) {
                element.get(effect).accept(k);
                is_handled = true;
            }
        }
        return is_handled;
    }


    public static Integer handle(Map<String, Consumer<Continuation>> handlers, Runnable comp) {
        Continuation k = new Continuation(scope, comp);

        context.handlersStack.add(handlers);

        k.run();

        while (!k.isDone()) {
            boolean is_handled = find_handler(k);
            if (!is_handled) {
                throw new RuntimeException("No handler");
            }
        }

        context.handlersStack.removeLast();

        return context.result;
    }
}
