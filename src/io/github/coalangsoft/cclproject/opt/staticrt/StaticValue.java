package io.github.coalangsoft.cclproject.opt.staticrt;

import ccl.rt.Value;
import io.github.coalangsoft.cclproject.opt.utils.ArrayUtil;
import io.github.coalangsoft.lib.data.ImmutablePair;

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
            default: throw new RuntimeException("Unhandled value type: " + type);
        }
    }

    private StaticValue property(StaticValue other) {
        if(other.getType() == StaticType.STRING){
            if("bind".equals(other.getCurrentValue())){
                return new StaticValue(StaticType.PROPERTY_BIND, new StaticBindFunc(this));
            }else{
                return unknown();
            }
        }if(other.getType() == StaticType.VARIABLE){
            return property(((StaticVariable) other.getCurrentValue()).getValue());
        }
        return unknown();
    }

}
