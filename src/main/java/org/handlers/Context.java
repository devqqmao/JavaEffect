package org.handlers;

import java.util.ArrayList;
import java.util.Map;
import java.util.function.Consumer;

import static org.handlers.Effect.*;

public class Context {

    public Effect effect;
    public Integer param;
    public Integer result;
    public ArrayList<Map<Effect, Consumer<Continuation>>> handlersStack;

    Context() {
        effect = Effect.NONE;
        param = 0;
        result = 0;
        handlersStack = new ArrayList<>();
    }
}
