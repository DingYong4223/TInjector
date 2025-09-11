package com.tenpay.injector.plugin;

/**
 * Injector plugin entry.
 * this class should not written by kotlin, or the config in the project will throw exception.
 * Created by delanding on 15/11/2020.
 */
public class LoggerExtension {

    public RunVariant variant = RunVariant.ALWAYS;
    public boolean safeMode = false;

    @Override
    public String toString() {
        return "LoggerExtension{" + "variant=" + variant + ", safeMode=" + safeMode + '}';
    }
}
