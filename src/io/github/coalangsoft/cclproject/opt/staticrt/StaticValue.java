package io.github.coalangsoft.cclproject.opt.staticrt;

import ccl.rt.Value;
import io.github.coalangsoft.cclproject.opt.utils.ArrayUtil;
import io.github.coalangsoft.lib.data.ImmutablePair;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class StaticValue {

    private StaticType type = StaticType.UNKNOWN;
    private Object currentValue;

    public StaticValue(StaticType type, Object o){
        this.type = type;
        this.currentValue = o;
    }

    public StaticType getType() {
        return type;
    }

    public Object getCurrentValue() {
        return currentValue;
    }

    public static StaticValue dynamicType(Object stuff){
        if(stuff instanceof String){
            return string((String) stuff);
        }if(stuff instanceof Number){
            return number(((Number) stuff).doubleValue());
        }
        throw new RuntimeException("Unexpected value: " + stuff);
    }

    public static StaticValue number(double num){
        return new StaticValue(StaticType.NUMBER, num);
    }
    public static StaticValue string(String str){
        return new StaticValue(StaticType.STRING, str);
    }
    public static StaticValue undefined() {
        return new StaticValue(StaticType.UNDEFINED, null);
    }
    public static StaticValue unknown() {
        return new StaticValue(StaticType.UNKNOWN, null);
    }
    public static StaticValue variable(StaticVariable var) {
        return new StaticValue(StaticType.VARIABLE, var);
    }
    public static StaticValue bound(StaticBindFunc base, StaticValue... parameters){
        return new StaticValue(StaticType.BOUND, new ImmutablePair<StaticBindFunc, StaticValue[]>(base, parameters));
    }
    public static StaticValue error(){
        return new StaticValue(StaticType.ERROR, null);
    }

    @Override
    public String toString() {
        return "StaticValue{" +
                "type=" + type +
                ", currentValue=" + currentValue +
                '}';
    }

    public StaticValue invoke(StaticValue... others){
        switch (type){
            case NUMBER: {
                if(others.length == 0){
                    return this;
                }else if(others.length == 1){
                    return property(others[0]);
                }else{
                    return error();
                }
            }
            case UNKNOWN: return this;
            case VARIABLE: return ((StaticVariable) currentValue).getValue().invoke(others);
            case PROPERTY_BIND: return bound((StaticBindFunc) currentValue);
            case BOUND: {
                ImmutablePair<StaticBindFunc,StaticValue[]> p = (ImmutablePair<StaticBindFunc, StaticValue[]>) currentValue;
                return p.getA().origin.invoke(ArrayUtil.concatenate(p.getB(),others));
            }
            case PRINTLN_FUNCTION: {
                if(others.length >= 1){
                    return StaticValue.undefined();
                }if(others.length == 0){
                    throw new RuntimeException("The default println function without parameter does not work!");
                }
            }
            case JAVA_CLASS: {
                return new StaticValue(StaticType.JAVA_OBJECT, currentValue);
            }
            case STRING: {
                if(others.length == 0){
                    return this;
                }else if(others.length == 1){
                    return property(others[0]);
                }else{
                    return unknown();
                }
            }
            default: throw new RuntimeException("Unhandled value type: " + type);
        }
    }

    public StaticValue property(StaticValue other) {
        if(this.getType() == StaticType.VARIABLE){
            return ((StaticVariable) getCurrentValue()).getValue().property(other);
        }
        if(other.getType() == StaticType.STRING){
            if("bind".equals(other.getCurrentValue())){
                return new StaticValue(StaticType.PROPERTY_BIND, new StaticBindFunc(this));
            }if("type".equals(other.getCurrentValue())){
                switch(getType()){
                    case STRING: return string("string");
                    case NUMBER: return string("number");
                }
            }if(getType() == StaticType.JAVA_CLASS || getType() == StaticType.JAVA_OBJECT){
                try {
                    Field f = ((Class<?>) getCurrentValue()).getField(other.getCurrentValue().toString());
                    if(Modifier.isFinal(f.getModifiers()) && Modifier.isStatic(f.getModifiers())){
                        f.setAccessible(true);
                        return dynamicType(f.get(null));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }if(other.getType() == StaticType.VARIABLE){
            return property(((StaticVariable) other.getCurrentValue()).getValue());
        }
        return unknown();
    }

    public static StaticValue java(String clss) {
        try {
            Class<?> c = Class.forName(clss);
            return new StaticValue(StaticType.JAVA_CLASS, c);
        } catch (ClassNotFoundException e) {
            return unknown();
        }
    }

    public static StaticValue array() {
        return new StaticValue(StaticType.ARRAY, null);
    }
}
