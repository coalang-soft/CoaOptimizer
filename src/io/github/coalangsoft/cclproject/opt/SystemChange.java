package io.github.coalangsoft.cclproject.opt;

/**
 * Created by Matthias on 06.07.2017.
 */
public class SystemChange {

    private boolean forChanged, whileChanged, ifChanged, javaChanged, printlnChanged;

    public boolean isForChanged() {
        return forChanged;
    }

    public void setForChanged(boolean forChanged) {
        this.forChanged = forChanged;
    }

    public boolean isWhileChanged() {
        return whileChanged;
    }

    public void setWhileChanged(boolean whileChanged) {
        this.whileChanged = whileChanged;
    }

    public boolean isIfChanged() {
        return ifChanged;
    }

    public void setIfChanged(boolean ifChanged) {
        this.ifChanged = ifChanged;
    }

    public boolean isJavaChanged() {
        return javaChanged;
    }

    public void setJavaChanged(boolean javaChanged) {
        this.javaChanged = javaChanged;
    }

    public boolean isPrintlnChanged() {
        return printlnChanged;
    }

    public void setPrintlnChanged(boolean printlnChanged) {
        this.printlnChanged = printlnChanged;
    }
}
