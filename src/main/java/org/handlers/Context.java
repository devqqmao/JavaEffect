package org.handlers;

import java.util.*;
import java.util.function.Consumer;

public class Context {

    public String effect;
    public Integer param;
    public Integer result;
    public ArrayList<Map<String, Consumer<Continuation>>> handlersStack;
    public Queue<Continuation> coroutinesQueue;


    Context() {
        effect = "";
        param = 0;
        result = 0;
        handlersStack = new ArrayList<>();
        coroutinesQueue = new LinkedList<>();
    }
}
