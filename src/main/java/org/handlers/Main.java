package org.handlers;


import java.util.Map;

import static org.handlers.Handler.*;

public class Main {

    public static void main(String[] args) {
        execute();
    }

    public static void execute() {
        int res = handle(
                Map.of("ask", (k) -> {
                            context.result = 2;
                            k.run();
                        }
                        , "put", Continuation::run
                )
                , () -> {
                    put(ask() + ask());
                }
        );
        System.out.println(res);
    }
}
