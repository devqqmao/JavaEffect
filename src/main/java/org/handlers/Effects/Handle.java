package org.handlers.Effects;

import org.handlers.Continuation.Continuation;
import org.handlers.Continuation.ContinuationScope;

import java.util.ListIterator;
import java.util.Map;
import java.util.function.Consumer;

import static org.handlers.Effects.Effect.*;

public class Handle {

    public static Context context;
    public static ContinuationScope scope;

    static {
        context = new Context();
        scope = new ContinuationScope("Handle");
    }

    public static Object ask() {
        context.effect = ASK;
        Continuation.yield(scope);
        return context.getResult();
    }

    public static void put(Object p) {
        context.effect = PUT;
        context.result = p;
        Continuation.yield(scope);
    }

    public static Object next() {
        context.effect = NEXT;
        Continuation.yield(scope);
        return context.getResult();
    }

    public static boolean find_handler(Continuation k) {
        Effect effect = context.getEffect();
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


    public static Object handle(Map<Effect, Consumer<Continuation>> handlers, Runnable comp) {
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

        return context.getResult();
    }
}
