package net.fightingpainter.mc.towntime.util.types;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * A Simple Key Value Pair Dictionary Class
 * (This is a Substitution for Dictionarys since Java doesn't have them)
*/
public class Dict extends Listable {
    private Map<String, Object> data = new HashMap<>(); //Dictionary data

    //============================== Constructors ==============================\\
    /**
     * Creates an empty dictionary
    */
    public Dict() {}

    /**
     * Creates a dictionary with the given key-value pairs
     * @param pairs The key-value pairs to add to the dictionary
    */
    public Dict(Pair... pairs) {add(pairs);}


    //============================== Operators ==============================\\
    //=============== Get ===============\\
    /**
     * Get a value from the dictionary
     * @param key The key of the value
     * @return The value of the key
    */
    public Object get(String key) {
        return this.data.get(key);
    }

    /**
     *  Get a value from the dictionary and return a default value if the key doesn't exist
     * @param key
     * @param defaultValue
     * @return 
    */
    public Object get(String key, Object defaultValue) {
        Object value = this.data.get(key);
        return value != null ? value : defaultValue;
    }
    
    /**
     * Get a value from the dictionary and return a default value if the key doesn't exist (Generic version)
     * @param key The key of the value
     * @param defaultValue The default value to return if the key doesn't exist
     * @param <T> The type of the default value
     * @return The value of the key or the default value
    */
    @SuppressWarnings("unchecked")
    public <T> T getOrDefault(String key, T defaultValue) {
        Object value = this.data.get(key);
        return value != null ? (T) value : defaultValue;
    }

    /**
     * Get a key-value pair from the dictionary
     * @param key The key of the pair
     * @return The key-value pair
    */
    public Pair getPair(String key) {
        return new Pair(key, this.data.get(key));
    }

    //=============== Add ===============\\
    /**
     * Add a key-value pair to the dictionary
     * @param pair The key-value pair to add
    */
    public void add(Pair pair) {
        if (pair == null) {throw new IllegalArgumentException("Pair cannot be null");} //check if the pair is null
        if (this.data.containsKey(pair.getKey())) {throw new IllegalArgumentException("Key already exists in dictionary: " + pair.getKey());} //check if the key already exists
        this.data.put(pair.getKey(), pair.getValue());
    }

    /**
     * Add multiple key-value pairs to the dictionary
     * @param pairs The key-value pairs to add
    */
    public void add(Pair... pairs) {
        for (Pair pair : pairs) {add(pair);} //add each pair
    }

    /**
     * Add a key-value pair to the dictionary
     * @param key The key of the pair
     * @param value The value of the pair
    */
    public void add(String key, Object value) {
        add(new Pair(key, value)); //add the pair
    }
    
    //=============== Set ===============\\
    /**
     * Set a key-value pair in the dictionary (like add but overwrites the value if the key already exists instead of throwing an error)
     * @param pair The key-value pair to set
    */
    public void set(Pair pair) {
        if (pair == null) {throw new IllegalArgumentException("Pair cannot be null");} //check if the pair is null
        this.data.put(pair.getKey(), pair.getValue());
    }

    /**
     * Set multiple key-value pairs in the dictionary
     * @param pairs The key-value pairs to set
    */
    public void set(Pair... pairs) {
        for (Pair pair : pairs) {set(pair);} //set each pair
    }

    /**
     * Set a key-value pair in the dictionary
     * @param key The key of the pair
     * @param value The value of the pair
    */
    public void set(String key, Object value) {
        set(new Pair(key, value)); //set the pair
    }

    //=============== Remove ===============\\
    /**
     * Remove a value from the dictionary
     * @param key The key of the value
     * @return The value of the key
    */
    public Object remove(String key) {
        return this.data.remove(key); //remove
    }

    /**
     * Deletes a value from the dictionary
     * @param key The key of the value 
    */
    public void delete(String key) {
        this.data.remove(key); //remove without returning
    }
    
    //============================== Checks/Info ==============================\\
    /**
     * Get the size of the dictionary
     * @return The size of the dictionary
    */
    public int size() {
        return this.data.size();
    }

    /**
     * Check if the dictionary is empty
     * @return True if the dictionary is empty, false otherwise
    */
    public boolean isEmpty() {
        return this.data.isEmpty();
    }

    /**
     * Get all keys in the dictionary
     * @return A list of all keys
    */
    public List keys() { //NOTE: I know I could do something where I directly convert an array to a list but I'm lazy
        List keys = new List();
        this.data.forEach((key, value) -> keys.add(key));
        return keys;
    }

    /**
     * Get all values in the dictionary
     * @return A list of all values
    */
    public List values() { //NOTE: I know I could do something where I directly convert an array to a list but I'm lazy
        List values = new List();
        this.data.forEach((key, value) -> values.add(value));
        return values;
    }

    /**
     * Get all key-value pairs in the dictionary
     * @return A list of all key-value pairs
    */
    public List pairs() {
        List pairs = new List();
        this.data.forEach((key, value) -> pairs.add(new Pair(key, value)));
        return pairs;
    }

    /**
     * Check if the dictionary contains a key
     * @param key The key to check
     * @return True if the key exists, false otherwise
    */
    public boolean contains(String key) {
        return this.data.containsKey(key); //check if key exists
    }

    /**
     * Check if the Value at the given key is of the given type
     * @param key The key of the value
     * @param type The type to check
     * @return True if the value is of the given type, false otherwise
    */
    public boolean isType(String key, Class<?> type) {
        return type.isInstance(this.data.get(key)); //check if the value is of the given type
    }

    /**
     * Check if the Value at the given key is equal to the given value
     * @param key The key of the value
     * @param value The value to check
     * @return True if the value is equal to the given value, false otherwise
    */
    public boolean isEqual(String key, Object value) {
        return this.data.get(key).equals(value); //check if the value is equal to the given value
    }


    //============================== Methods ==============================\\
    /**
     * clears the dictionary
    */
    public void clear() {
        this.data.clear(); //clear
    }

    /**
     * Copy the dictionary
     * @return A copy of the dictionary
    */
    public Dict copy() {
        Dict copy = new Dict();
        copy.data = new HashMap<>(this.data);
        return copy;
    }

    /**
     * Iterate over the dictionary's key-value pairs
     * @param action The action to perform on each pair
     * @see BiConsumer
    */
    public void forEach(BiConsumer<String, Object> action) {
        this.data.forEach(action);
    }

    /**
     * Iterate over the dictionary's key-value pairs
     * @param action The action to perform on each pair
     * @see Consumer
     * @see Pair
    */
    public void forEach(Consumer<Pair> action) {
        this.data.forEach((key, value) -> action.accept(new Pair(key, value)));
    }
    

    //============================== Json ==============================\\
    /**
     * Convert the dictionary to a JSON string
     * @return The JSON string
    */
    public String toJson() {
        return jsonify(copy(), null, null);
    }

    /**
     * Convert the dictionary to a JSON string with the given indentation
     * @param indent The indentation to use
     * @return The JSON string
    */
    public String toJson(int indent) {
        return jsonify(copy(), indent);
    }

    /**
     * Convert a Json string to a dictionary
     * @param json The JSON string
     * @return The dictionary
    */
    public static Dict fromJson(String json) {
        return (Dict) unjsonify(json);
    }

    //============================== String ==============================\\

    /**
     * Convert the dictionary to a string
     * @param formatted Whether the string should be formatted
     * @return The string
    */
    public String toString(boolean formatted) {
        return jsonify(copy(), formatted ? 4 : null);
    }

    /**
     * Convert the dictionary to a string
     * @return The string
    */
    public String toString() {
        return jsonify(copy());
    }
}