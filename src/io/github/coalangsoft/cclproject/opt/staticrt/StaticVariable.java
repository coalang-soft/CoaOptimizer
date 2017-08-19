package io.github.coalangsoft.cclproject.opt.staticrt;

public class StaticVariable {

    private String name;
    private StaticValue value;

    public StaticVariable(String name, StaticValue value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public StaticValue getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "StaticVariable{" +
                "name='" + name + '\'' +
                ", value=" + value +
                '}';
    }
}
