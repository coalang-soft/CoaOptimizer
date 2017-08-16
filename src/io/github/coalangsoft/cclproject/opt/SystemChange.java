package io.github.coalangsoft.cclproject.opt;

import java.util.HashMap;
import java.util.function.Consumer;

/**
 * Created by Matthias on 06.07.2017.
 *
 */
public class SystemChange extends HashMap<String,Boolean>{

    public boolean isForChanged() {
        return getOrDefault("for", false);
    }

    public void setForChanged(boolean forChanged) {
        put("for", forChanged);
    }

    public boolean isWhileChanged() {
        return getOrDefault("while", false);
    }

    public void setWhileChanged(boolean whileChanged) {
        put("while", whileChanged);
    }

    public boolean isIfChanged() {
        return getOrDefault("if", false);
    }

    public void setIfChanged(boolean ifChanged) {
        put("if", ifChanged);
    }

    public boolean isJavaChanged() {
        return getOrDefault("java", false);
    }

    public void setJavaChanged(boolean javaChanged) {
        put("java", javaChanged);
    }

    public boolean isPrintlnChanged() {
        return getOrDefault("println", false);
    }

    public void setPrintlnChanged(boolean printlnChanged) {
        put("println", printlnChanged);
    }

    public void modifyAll(Iterable<String> changed){
        changed.forEach(s -> put(s,true));
    }

}
