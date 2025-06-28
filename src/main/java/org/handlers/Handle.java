package org.handlers;

import java.util.ListIterator;
import java.util.Map;
import java.util.function.Consumer;

import static org.handlers.Effect.*;

public class Handle {

    static Context context;
    static ContinuationScope scope;

    static {
        context = new Context();
        scope = new ContinuationScope("cont");
    }

    public static Integer ask() {
        context.effect = ASK;
        Continuation.yield(scope);
        return context.result;
    }

    public static void put(Integer p) {
        context.effect = PUT;
        context.result = p;
        Continuation.yield(scope);
    }

    public static Integer next() {
        context.effect = NEXT;
        Continuation.yield(scope);
        return context.result;
    }

    public static boolean find_handler(Continuation k) {
        Effect effect = context.effect;
        ListIterator<Map<Effect, Consumer<Continuation>>> it = context.handlersStack.listIterator(context.handlersStack.size());
        boolean is_handled = false;
        while (it.hasPrevious() && !is_handled) {
            Map<Effect, Consumer<Continuation>> element = it.previous();
            if (element.containsKey(effect)) {
                element.get(effect).accept(k);
                is_handled = true;
            }
        }
        return is_handled;
    }


    public static Integer handle(Map<Effect, Consumer<Continuation>> handlers, Runnable comp) {
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
