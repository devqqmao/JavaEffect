package org.exception_handler;

/*
Обеспечивает передачу данных между точками возникновения эффектов и их обработчиками, сохраняя состояние выполнения
*/

public class EffectContext {
    // Каждый поток должен иметь свой собственный контекст, чтобы избежать конфликтов при параллельном выполнении Continuation
    private static final ThreadLocal<EffectContext> current = new ThreadLocal<>(); // чтобы каждый поток имел собственный контекст
    private String operation; // "ask42" or "raise"
    private Object[] args; // arguments passed to an operation
    private Object result; // result, возвращенный после обработки эффекта

    /*
    EffectContext.getCurrent() возвращает контекст, привязанный к текущему потоку.
    EffectContext.setCurrent(context) устанавливает контекст для текущего потока.
     */

    public static EffectContext getCurrent() {
        return current.get();
    }

    public static void setCurrent(EffectContext context) {
        current.set(context);
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getOperation() {
        return operation;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public Object getResult() {
        return result;
    }

}