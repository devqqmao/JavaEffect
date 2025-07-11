package org.handlers.Handlers;

import org.handlers.Continuation.Continuation;
import org.handlers.Continuation.ContinuationScope;

import java.util.Map;
import java.util.function.Consumer;

public class Handle {

    public static Context context;
    public static ContinuationScope scope;

    static {
        context = new Context();
        scope = new ContinuationScope("Handle");
    }

    public static Object getState() {
        return context.getState();
    }

    public static void setState(Object state) {
        context.setState(state);
    }

    public static Object perform(String effect, Object state) {
        context.setEffect(effect);
        context.setState(state);
        Continuation.yield(scope);
        return context.getState();
    }

    public static Object perform(String effect) {
        context.setEffect(effect);
        Continuation.yield(scope);
        return context.getState();
    }

    public static void handleEffect(Continuation k) {
        String effect = context.getEffect();
        var it = context.getHandlersStack().listIterator(context.getHandlersStack().size());

        while (it.hasPrevious()) {
            var element = it.previous();
            if (element.containsKey(effect)) {
                element.get(effect).accept(k);
                return;
            }
        }
        throw new RuntimeException("No handler found");
    }


    public static Object handle(Map<String, Consumer<Continuation>> handlers, Runnable comp) {
        Continuation k = new Continuation(scope, comp);

        context.getHandlersStack().add(handlers);


        while (!k.isDone()) {
            k.run();
            handleEffect(k);
        }

        context.getHandlersStack().removeLast();

        return context.getState();
    }
}
