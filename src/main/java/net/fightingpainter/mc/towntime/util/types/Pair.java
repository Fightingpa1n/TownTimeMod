package net.fightingpainter.mc.towntime.util.types;

public class Pair {
    public final String key;
    public final Object value;

    /**
     * Creates a New Key-Value Pair
     * @param key the Key
     * @param value the Value
    */
    public Pair(String key, Object value) {
        this.key = key;
        this.value = value;
    }

    /**
     * Gets the Key of the Pair 
     * @return the Key
    */
    public String getKey() {return key;}

    /**
     * Gets the Value of the Pair
     * @return the Value
    */
    public Object getValue() {return value;}

    /**
     * Gets the Value of the Pair as a String
     * @return the Value as a String
    */
    public String toString() {return key + ": " + value;}
}
