package org.exception_handler;

import org.example.Continuation;
import org.example.ContinuationScope;

public class Effects {
    static final ContinuationScope RAISE_SCOPE = new ContinuationScope("raise");
    static final ContinuationScope ASK_SCOPE = new ContinuationScope("ask");

    // RAISE_SCOPE и ASK_SCOPE — области видимости для продолжений, связанных с эффектами raise и ask.
    public static <A> A raise(String message) {
        EffectContext context = EffectContext.getCurrent();
        context.setOperation("raise");
        context.setArgs(new Object[]{message});
        Continuation.yield(RAISE_SCOPE);
        throw new IllegalStateException("Unhandled raise");
    }

    public static <A> A ask() {
        EffectContext context = EffectContext.getCurrent();
        context.setOperation("ask");
        Continuation.yield(ASK_SCOPE);
        return (A) context.getResult();
    }
}