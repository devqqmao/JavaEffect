package org.handlers;

import java.util.ArrayList;
import java.util.Map;
import java.util.function.Consumer;

import static org.handlers.Effect.*;

public class Context {

    public Effect effect;
    public Object param;
    public Object result;
    public ArrayList<Map<Effect, Consumer<Continuation>>> handlersStack;

    public Effect getEffect() {
        return effect;
    }

    public void setEffect(Effect effect) {
        this.effect = effect;
    }

    public Object getParam() {
        return param;
    }

    public void setParam(Object param) {
        this.param = param;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public ArrayList<Map<Effect, Consumer<Continuation>>> getHandlersStack() {
        return handlersStack;
    }

    public void setHandlersStack(ArrayList<Map<Effect, Consumer<Continuation>>> handlersStack) {
        this.handlersStack = handlersStack;
    }

    Context() {
        effect = NONE;
        param = null;
        result = null;
        handlersStack = new ArrayList<>();
    }
}
