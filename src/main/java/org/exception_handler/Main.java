package org.exception_handler;

public class Main {

    public static void main(String[] args) {
        // === handleRaise demo ===
        int raiseResult = EffectHandlers.handleRaise(() -> {
            int x = Effects.raise("error");
            System.out.println("This won't execute");
        });
        System.out.println("handleRaise result: " + raiseResult); // 42

        // === handleAsk demo ===
        int askResult = EffectHandlers.handleAsk(() -> {
            int x = Effects.ask(); // Приостанавливается, обработчик устанавливает результат = 21
            System.out.println("After ask: x = " + x); // Выведет: "After ask: x = 21"
        });
        System.out.println("handleAsk result: " + askResult); // 21
    }
}