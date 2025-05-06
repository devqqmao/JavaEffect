package org.example;

// dynamic method invocation

// Represents a direct reference to a method, constructor, or field, enabling invocation without traditional reflection.

import java.lang.invoke.MethodHandle;

// Utility class for creating and manipulating MethodHandle instances.
import java.lang.invoke.MethodHandles;

// Describes a method's signature (return type and parameter types).
// MethodType.methodType(int.class, String.class, int.class) defines a method returning int and taking String and int parameters.
import java.lang.invoke.MethodType;


/*
Dynamic Method Invocation

Dynamic method invocation refers to calling a method at runtime without knowing
its exact signature or target class at compile time. Instead of hardcoding method calls
(e.g., obj.myMethod()), you resolve and invoke the method dynamically based on runtime conditions.

Reflection is Java’s built-in mechanism for introspecting and modifying classes, methods, fields, and constructors at runtime. It allows you to:

Inspect class metadata (e.g., list all methods in a class).
Create objects of unknown types.
Invoke methods by name (even private ones).
Access or modify fields dynamically.

Example

Method method = String.class.getMethod("length");
int result = (int) method.invoke("Hello"); // result = 5

MethodHandles.Lookup lookup = MethodHandles.lookup();
MethodType type = MethodType.methodType(int.class); // Signature: () -> int
MethodHandle handle = lookup.findVirtual(String.class, "length", type);
int result = (int) handle.invokeExact("Hello"); // result = 5

The java.lang.Class class represents metadata about a Java class (e.g., its name, methods, fields, etc.)

 */


public final class Continuation {

    // The key differences are the scope (instance vs. class level) and the type of data they hold (an Object instance vs. a Class metadata object)
    final Object impl;

    //  the java.lang.Class class represents metadata about a Java class

    static final Class<?> IMPL_CLASS;

    /*
    static : belongs to the class, not to the instance
    final : mutability
     */

    static final MethodHandle NEW;
    static final MethodHandle YIELD;
    static final MethodHandle RUN;
    static final MethodHandle IS_DONE;

    // First, the `static` block is a static initializer. It runs when the class is loaded. So it's used to initialize static variables or perform one-time setup for the class.
    static {
        try {

            // Class.forName("..."): Dynamically loads the class with the given name (jdk.internal.vm.Continuation) and returns its Class object.
            IMPL_CLASS = Class.forName("jdk.internal.vm.Continuation");

            // Кладем lookup для класса Cont, перед этим расширяя privateLookupIn
            var lookup = MethodHandles.privateLookupIn(
                    IMPL_CLASS,
                    MethodHandles.lookup()
            );

            // Ищем конструктор
            NEW = lookup.findConstructor(
                    IMPL_CLASS,
                    // Runnable.class : что будет исполняться ContinuationScope.IMPL_CLASS
                    // return type, (class params : Cont, Runnable)
                    // используется для группировки Continuation и управления их жизненным циклом
                    // Здесь передается класс ContinuationScope
                    MethodType.methodType(void.class, ContinuationScope.IMPL_CLASS, Runnable.class)
            );
            // Этот метод приостанавливает выполнение текущего Continuation и возвращает управление вызывающему коду.
            // YIELD связан с классом, т.к. связан с классом, то требует явной передачи контекста
            /*
            Это Static, потому что он управляет текущим выполняющимся Continuation в заданном ContinuationScope.
            Ему не нужен экземпляр Continuation — он работает с тем, который активен в текущем потоке.
             */
            YIELD = lookup.findStatic(
                    IMPL_CLASS,
                    "yield",
                    // объект, задающий контекст после остановки
                    // 2-й параметр – передача контекста
                    MethodType.methodType(boolean.class, ContinuationScope.IMPL_CLASS)
            );
            // для диспатча виртуальных методов нужен инстанс
            // Связан с экземпляром

            /*
            Эти методы работают с конкретным экземпляром Continuation.
            Например, run(), is_done запускает выполнение именно того Continuation, у которого вызван метод.

            ContinuationScope — это контекст, который группирует Continuation и управляет их изоляцией.
            Статические методы (как yield) работают на уровне класса и требуют явного указания контекста.

            Виртуальные методы (как run, isDone) работают с конкретным экземпляром Continuation.
            ContinuationScope.IMPL_CLASS — техническая деталь для доступа к внутреннему API JDK.
            */

            RUN = lookup.findVirtual(
                    IMPL_CLASS,
                    "run",
                    MethodType.methodType(void.class)
            );
            // Связан с экземпляром
            IS_DONE = lookup.findVirtual(
                    IMPL_CLASS,
                    "isDone",
                    MethodType.methodType(boolean.class)
            );
        } catch (Throwable t) {
            throw new ExceptionInInitializerError(t);
        }
    }

    /*
    ContinuationScope как контекст
    ContinuationScope — это логическая группа для Continuation. Его можно считать "областью видимости" или "контекстом выполнения", в котором работают Continuation.
    Пример: Если у вас есть два разных ContinuationScope, Continuation из разных областей не будут мешать друг другу при вызовах yield.


    При конструировании принимает scope и comp.

    scope.impl — внутренний объект ContinuationScope (из jdk.internal.vm), связанный с переданным scope.
    runnable — задача, которая будет выполняться внутри Continuation.
     */

    public Continuation(ContinuationScope scope, Runnable runnable) {
        try {
            // когда вызываем конструктор, вызывается конструктор Cont
            this.impl = NEW.invoke(scope.impl, runnable);
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }

    public static void yield(ContinuationScope scope) {
        try {
            /*
            ContinuationScope определяет область (контекст) выполнения, в котором работают Continuation.
            Не closure, а явная передача:
            В Java лямбды захватывают переменные из внешней области видимости, только если они final или effectively final. Однако ContinuationScope здесь явно передается в методы (countUp, yield), а не захватывается через замыкание.
             */
            YIELD.invoke(scope.impl);
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }

    public void run() {
        try {
            // impl.run(); эквивалентно
            RUN.invoke(impl);
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }

    public boolean isDone() {
        try {
            return (boolean) IS_DONE.invoke(impl);
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }
}