package org.handlers.Handlers;

import org.handlers.Continuation.Continuation;

import java.util.ArrayList;
import java.util.Map;
import java.util.function.Consumer;

public class Context {

    public String effect;
    public Object param;
    public Object result;
    public ArrayList<Map<String, Consumer<Continuation>>> handlersStack;

    public String getEffect() {
        return effect;
    }

    public void setEffect(String effect) {
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

    public ArrayList<Map<String, Consumer<Continuation>>> getHandlersStack() {
        return handlersStack;
    }

    public void setHandlersStack(ArrayList<Map<String, Consumer<Continuation>>> handlersStack) {
        this.handlersStack = handlersStack;
    }

    Context() {
        effect = null;
        param = null;
        result = null;
        handlersStack = new ArrayList<>();
    }
}
