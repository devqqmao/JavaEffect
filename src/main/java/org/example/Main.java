package org.example;

import java.util.Scanner;

public class Main {

    static void countUp(ContinuationScope scope) {
        for (int i = 0; i < 10; i++) {
            System.out.println(i);
            /*
            yield — это статический метод, который работает с активным Continuation в текущем потоке и указанном scope.
            Активным может быть только один метод.
             */
            Continuation.yield(scope);
        }
    }

    public static void main(String[] args) {
        var scope = new ContinuationScope("scope");
        var continuation = new Continuation(scope, () -> countUp(scope));

        var scanner = new Scanner(System.in);

        while (!continuation.isDone()) {
            System.out.print("Press enter to run one more step");
            scanner.nextLine();
            continuation.run();
        }
    }

}

/*
Один поток может управлять несколькими Continuation, они выполняются последовательно, а не параллельно.

Аналог
Thread virtualThread = Thread.ofVirtual().start(() -> {
    for (int i = 0; i < 10; i++) {
        System.out.println(i);
        Thread.yield(); // Аналог Continuation.yield
    }
});
virtualThread.join(); // Ждём завершения
 */