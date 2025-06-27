package org.handlers;

import java.util.Map;

import static org.handlers.Handler.*;

public class Marks {

    public static int pure_compute() {
        int res = 0;
        for (int i = 0; i < 1_000; i++) {
            res += i;
        }
        return res;
    }

    public static int execute_one_effect() {
        return handle(
                Map.of("ask", (k) -> {
                            context.result = 2;
                            k.run();
                        }
                )
                , Handler::ask
        );
    }
}
