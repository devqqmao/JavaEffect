package org.exception_handler;

import org.example.Continuation;

import static org.exception_handler.Effects.ASK_SCOPE;
import static org.exception_handler.Effects.RAISE_SCOPE;

public class EffectHandlers {
    public static int handleRaise(Runnable code) {

        EffectContext context = new EffectContext();

        Continuation cont = new Continuation(RAISE_SCOPE, () -> {
            EffectContext.setCurrent(context);
            code.run();
        });

        cont.run();
        if ("raise".equals(context.getOperation())) {
            return 42;
        }

        return (int) context.getResult();
    }

    public static int handleAsk(Runnable code) {
        EffectContext context = new EffectContext();
        Continuation cont = new Continuation(ASK_SCOPE, () -> {
            EffectContext.setCurrent(context);
            code.run();
        });

        while (!cont.isDone()) {
            cont.run();
            if ("ask".equals(context.getOperation())) {
                context.setResult(21);
//                cont.run();
            }
        }
        return (int) context.getResult();
    }
}