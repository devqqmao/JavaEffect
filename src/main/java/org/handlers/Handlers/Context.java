package org.handlers.Handlers;

import org.handlers.Continuation.Continuation;

import java.util.ArrayList;
import java.util.Map;
import java.util.function.Consumer;

public class Context {

    private String effect;
    private Object state;
    public ArrayList<Map<String, Consumer<Continuation>>> handlersStack;

    public String getEffect() {
        return effect;
    }

    public void setEffect(String effect) {
        this.effect = effect;
    }

    public Object getState() {
        return state;
    }

    public void setState(Object state) {
        this.state = state;
    }

    public ArrayList<Map<String, Consumer<Continuation>>> getHandlersStack() {
        return handlersStack;
    }

    public void setHandlersStack(ArrayList<Map<String, Consumer<Continuation>>> handlersStack) {
        this.handlersStack = handlersStack;
    }

    Context() {
        effect = null;
        state = null;
        handlersStack = new ArrayList<>();
    }
}
