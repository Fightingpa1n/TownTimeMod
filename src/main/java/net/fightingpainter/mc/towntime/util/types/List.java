package net.fightingpainter.mc.towntime.util.types;

import java.util.function.Consumer;

public class List extends Listable {
    private Object[] content = new Object[0];

    //============================== Constructors ==============================\\
    /**
     * Creates an empty List 
    */
    public List() {} //constructor

    /**
     * Creates a List with the given elements
     * @param elements The elements to add to the list
    */
    public List(Object... elements) {
        for (Object element : elements) {
            checkType(element); //check if the element is a valid type
            add(element); //add the element to the list
        }
    }

    //============================== Operators ==============================\\
    //=============== Get ===============\\
    /**
     * Gets the element at the given index
     * @param index The index to get the element at
     * @return The element at the index
    */
    public Object get(int index) {
        return content[index]; //return the element at the index
    }


    //=============== Add ===============\\
    /**
     * Adds 1 or multiple elements to the list
     * @param elements The elements to add
    */
    public void add(Object... elements) {
        Object[] newContent = new Object[content.length+elements.length]; //create a new array with the elements
        for (int i=0; i<content.length; i++) {
            newContent[i] = content[i]; //copy the elements before the new elements
        }
        
        for (int i=0; i<elements.length; i++) {
            checkType(elements[i]); //check if the element is a valid type
            newContent[content.length+i] = elements[i]; //add the new elements
        }
        content = newContent; //set the content to the new array
    }

    /**
     * Adds a list to the list
     * @param list The list to add
    */
    public void addList(List list) {
        Object[] newContent = new Object[content.length+list.size()]; //create a new array with the elements
        for (int i=0; i<content.length; i++) {newContent[i] = content[i];} //copy the elements before the new elements

        list.forEach(element -> {
            checkType(element); //check if the element is a valid type
            newContent[content.length] = element; //add the new elements
        });
        content = newContent; //set the content to the new array
    }
    
    //=============== Set ===============\\
    /**
     * Sets the element at the given index
     * @param index The index to set the element at
     * @param element The element to set
    */
    public void set(int index, Object element) {
        checkType(element); //check if the element is a valid type
        content[index] = element; //set the element at the index
    }
    
    //=============== Remove ===============\\
    /**
     * Removes the element at the given index from the list
     * @param index The index to remove the element at
     * @return The removed element
    */
    public Object remove(int index) {
        Object element = content[index]; //get the element
        Object[] newContent = new Object[content.length-1]; //create a new array with one less element
        for (int i=0; i<index; i++) {
            newContent[i] = content[i]; //copy the elements before the index
        }
        for (int i=index+1; i<content.length; i++) {
            newContent[i-1] = content[i]; //copy the elements after the index
        }
        content = newContent; //set the content to the new array
        return element; //return the removed element
    }

    /**
     * Deletes the element at the given index from the list (like remove but without returning the element)
     * @param index The index to delete the element at
    */
    public void delete(int index) {
        remove(index); //remove the element
    }
    
    
    
    
    //============================== Checks/Info ==============================\\
    /**
     * Gets the size of the list
     * @return The size of the list
    */
    public int size() {
        return content.length; //return the size of the list
    }
    
    /**
     * Checks if the list is empty
     * @return true if the list is empty, false otherwise
    */
    public boolean isEmpty() {
        return content.length == 0; //return true if the list is empty
    }

    /**
     * Checks if the list contains the given element
     * @param element The element to check for
     * @return true if the list contains the element, false otherwise 
    */
    public boolean contains(Object element) {
        for (Object e : content) {
            if (e.equals(element)) {return true;} //return true if the element is found
        }
        return false; //return false if the element is not found
    }


    /**
     * Check if the Element at the given index is of the given type
     * @param index The index of the element
     * @param type The type to check
     * @return True if the element is of the given type, false otherwise
    */
    public boolean isType(int index, Class<?> type) {
        return type.isInstance(content[index]); //check if the value is of the given type
    }

    /**
     * Check if the Element at the given index is equal to the given value
     * @param index The index of the element
     * @param value The value to check
     * @return True if the element is equal to the given value, false otherwise
    */
    public boolean isEqual(int index, Object value) {
        return content[index].equals(value); //check if the value is equal to the given value
    }


    //============================== Methods ==============================\\
    /**
     * Clears the list
    */
    public void clear() {
        content = new Object[0]; //set the content to an empty array
    }
    
    /**
     * Copy the list
     * @return A copy of the list
    */
    public List copy() {
        List copy = new List();
        copy.content = content.clone(); //copy the content
        return copy;
    }
    
    /**
     * Iterates over each element in the list and applies the given consumer
     * @param consumer The consumer to apply to each element
    */
    public void forEach(Consumer<Object> consumer) {
        for (Object element : content) {
            try {
                consumer.accept(element);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
