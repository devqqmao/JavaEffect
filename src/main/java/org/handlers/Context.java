package org.handlers;

import java.util.Map;
import java.util.Vector;
import java.util.function.Consumer;

public class Context {

    public String effect;
    public Integer param;
    public Integer result;

    Context() {
        effect = "";
        param = 0;
        result = 0;
    }
}
