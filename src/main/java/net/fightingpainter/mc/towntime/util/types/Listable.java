package net.fightingpainter.mc.towntime.util.types;

abstract public class Listable {

    protected static final Class<?>[] validTypes = new Class<?>[] {
        String.class,
        Number.class,
        Boolean.class,
        Dict.class,
        List.class
    };

    protected static boolean isValidType(Object value) {
        if (value == null) return true; //null is valid
        for (Class<?>type:validTypes) {
            if (type.isInstance(value)) return true; //value is a valid type
        }
        return false; //value is not a valid type
    }


    protected static void checkType(Object value) {
        if (!isValidType(value)) { //check if the value is a valid type
            throw new IllegalArgumentException("Invalid type: "+value.getClass().getName());
        }
    }

    //============================== ToJson ==============================\\
    protected static String jsonify(Object data) {
        return jsonify(data, null, 0);
    }

    protected static String jsonify(Object data, Integer indent) {
        return jsonify(data, indent, 0);
    }

    protected static String jsonify(Object data, Integer indent, Integer _level) {
        if (data == null) return jsonifyNull(); //if the data is None
        if (data instanceof Boolean) return jsonifyBool((Boolean)data); //if the data is a boolean
        if (data instanceof Number) return jsonifyNumber((Number)data); //if the data is a number
        if (data instanceof String) return jsonifyString((String)data); //if the data is a string
        if (data instanceof List) return jsonifyList((List)data, indent, _level); //if the data is a list
        if (data instanceof Dict) return jsonifyDict((Dict)data, indent, _level); //if the data is a dict

        throw new IllegalArgumentException("Unsupported type: "+data.getClass().getName()); //if the data is not a supported type, raise an error
    }

    private static String jsonifyNull() {return "null";}
    
    private static String jsonifyBool(Boolean bool) {return bool.toString().toLowerCase();}

    private static String jsonifyNumber(Number number) {return number.toString();}

    private static String jsonifyString(String string) {
        string = string.replace("\\", "\\\\");
        string = string.replace("\"", "\\\"");
        string = string.replace("\n", "\\n");
        string = string.replace("\t", "\\t");
        string = string.replace("\r", "\\r");
        return "\""+string+"\"";
    }

    private static String jsonifyList(List list, Integer indent, Integer _level) {
        if (list.size() == 0) return "[]";

        if (indent == null) {
            StringBuilder result = new StringBuilder("[");
            list.forEach(item -> {
                result.append(jsonify(item, indent, _level)).append(",");
            });
            result.setLength(result.length() - 1); // remove the last comma
            result.append("]");
            return result.toString();
        } else {
            StringBuilder result = new StringBuilder("[\n");
            String space = " ".repeat(indent * (_level + 1));
            list.forEach(item -> {
                result.append(space).append(jsonify(item, indent, _level + 1)).append(",\n");
            });
            result.setLength(result.length() - 2);
            result.append("\n").append(" ".repeat(indent * _level)).append("]");
            return result.toString();
        }
    }

    private static String jsonifyDict(Dict dict, Integer indent, Integer _level) {
        if (dict.size() == 0) return "{}";

        if (indent == null) {
            StringBuilder result = new StringBuilder("{");
            dict.forEach((key, value) -> {
                result.append("\"").append(key).append("\": ").append(jsonify(value, indent, _level)).append(",");
            });
            result.setLength(result.length() - 1); // remove the last comma
            result.append("}");
            return result.toString();
        } else {
            StringBuilder result = new StringBuilder("{\n");
            String space = " ".repeat(indent * (_level + 1));
            dict.forEach((key, value) -> {
                result.append(space).append("\"").append(key).append("\": ").append(jsonify(value, indent, _level + 1)).append(",\n");
            });
            result.setLength(result.length() - 2);
            result.append("\n").append(" ".repeat(indent * _level)).append("}");
            return result.toString();
        }
    }



    /* 
        *Python Prototype code: (I made this in python so I know how the logic should be)
        
        def jsonify(data, indent=None, _level=0) -> str: #the main jsonify function
            if data is None: #if the data is None
                return _none() #return the None function
            
            if isinstance(data, bool): #if the data is a boolean
                return _bool(data) #return the boolean function
            
            if isinstance(data, (int, float)): #if the data is a number
                return _number(data) #return the number function
            
            if isinstance(data, str): #if the data is a string
                return _str(data) #return the string function
            
            if isinstance(data, list): #if the data is a list
                return _list(data, indent, _level) #return the list function
            
            if isinstance(data, dict): #if the data is a dict
                return _dict(data, indent, _level) #return the dict function
            
            raise TypeError(f"Unsupported type: {type(data)}") #if the data is not a supported type, raise an error


        def _none() -> str: #the function that will jsonify None (null)
            return "null" #return null

        def _bool(boolean:bool) -> str: #the function that will jsonify a boolean
            return str(boolean).lower() #return the boolean as a string

        def _number(number) -> str: #the function that will jsonify a number (int or float)
            return str(number) #return the number as a string

        def _str(string:str) -> str: #the function that will jsonify a string
            string = string.replace("\\", "\\\\") #replace all \ with \\
            string = string.replace('"', '\\"') #replace all " with \"
            string = string.replace("\n", "\\n") #replace all newlines with \n
            string = string.replace("\t", "\\t") #replace all tabs with \t
            string = string.replace("\r", "\\r") #replace all carriage returns with \r
            return f'"{string}"' #return the string with "" around it

        def _list(lst: list, indent, _level) -> str: #the function that will jsonify a list
            if len(lst) == 0: return "[]" #if the list is empty return []

            if indent is None: #if no indent is given
                return "[" + ", ".join(jsonify(item, indent, _level) for item in lst) + "]" #return the list with the items jsonifyed
            
            else: #if indent is given
                result = "[\n" #init result string
                space = " " * indent * (_level + 1)  #calculate the space of the indent
                for value in lst: #loop through the list
                    result += space + jsonify(value, indent, _level + 1) + ",\n" #add the value to the result string
                result = result.rstrip(",\n") + "\n"  #remove the last ",\n" and add a newline
                result += " " * indent * _level + "]" #add the closing bracket with the correct indent
                return result

        def _dict(dictionary: dict, indent, _level) -> str: #the function that will jsonify a dict
            if len(dictionary) == 0: return "{}" #if the dict is empty return {}

            if indent is None: #if no indent is given
                return "{" + ", ".join(f'"{key}": {jsonify(value, indent, _level)}' for key, value in dictionary.items()) + "}" #return the dict with the keys and values jsonifyed
            
            else: #if indent is given
                result = "{\n" #init result string
                space = " " * indent * (_level + 1)  #calculate the space of the indent
                for key, value in dictionary.items(): #loop through the dictionary
                    result += space + f'"{key}": {jsonify(value, indent, _level + 1)},\n' #add the key and value to the result string
                result = result.rstrip(",\n") + "\n"
                result += " " * indent * _level + "}"
                return result
    */

    //============================== FromJson ==============================\\
    protected static Object unjsonify(String jsonStr) {
        if (jsonStr.equals("null")) return unjsonifyNull(); //if json string is null it is None
        if (jsonStr.equalsIgnoreCase("true") || jsonStr.equalsIgnoreCase("false")) return unjsonifyBool(jsonStr); //if json string is true or false it is a boolean
        try {return unjsonifyNumber(jsonStr);} //try to convert to number (this has to be try instead of a simple if since, numbers can be a bit more complex)
        catch (NumberFormatException e) {} //if it fails, it's not a number
        if (jsonStr.startsWith("\"") && jsonStr.endsWith("\"")) return unjsonifyString(jsonStr); //if json string starts and ends with "" it is a string
        if (jsonStr.startsWith("[") && jsonStr.endsWith("]")) return unjsonifyList(jsonStr); //if json string starts and ends with [] it is a list
        if (jsonStr.startsWith("{") && jsonStr.endsWith("}")) return unjsonifyDict(jsonStr); //if json string starts and ends with {} it is a dict
        throw new IllegalArgumentException("Unsupported type: "+jsonStr); //if the data is not a supported type, raise an error
    }

    private static Object unjsonifyNull() {return null;}

    private static Boolean unjsonifyBool(String jsonStr) {return jsonStr.equals("true");}

    private static Number unjsonifyNumber(String jsonStr) {
        if (jsonStr.contains(".") || jsonStr.toLowerCase().contains("e")) {
            // Decimal number or scientific notation
            double doubleValue = Double.parseDouble(jsonStr); // Start with double
            if (Math.abs(doubleValue) <= Float.MAX_VALUE) {
                return (float) doubleValue; // Convert to float if within range
            } else {
                return doubleValue; // Use double if too large for float
            }
        } else {
            // Whole number
            long longValue = Long.parseLong(jsonStr); // Start with long
            if (longValue <= Integer.MAX_VALUE && longValue >= Integer.MIN_VALUE) {
                return (int) longValue; // Convert to int if within range
            } else {
                return longValue; // Use long if too large for int
            }
        }
    }    

    private static String unjsonifyString(String jsonStr) {
        jsonStr = jsonStr.substring(1, jsonStr.length()-1); //remove the first and last char since they are ""
        StringBuilder result = new StringBuilder();
        int i = 0;
        int length = jsonStr.length();
        while (i < length) {
            char c = jsonStr.charAt(i);
            if (c == '\\' && i + 1 < length) {
                char nextChar = jsonStr.charAt(i + 1);
                if (nextChar == '"') {
                    result.append('"');
                    i += 2;
                    continue;
                } else if (nextChar == 'n') {
                    result.append('\n');
                    i += 2;
                    continue;
                } else if (nextChar == 't') {
                    result.append('\t');
                    i += 2;
                    continue;
                } else if (nextChar == 'r') {
                    result.append('\r');
                    i += 2;
                    continue;
                } else if (nextChar == '\\') {
                    result.append('\\');
                    i += 2;
                    continue;
                } else {
                    result.append('\\');
                    i += 1;
                    continue;
                }
            } else {
                result.append(c);
                i += 1;
            }
        }
        return result.toString();
    }

    private static List unjsonifyList(String jsonStr) {
        List elements = topLevelParser(jsonStr);
        if (elements.size() == 0) return new List();

        List result = new List();
        elements.forEach(element -> {
            result.add(unjsonify((String)element));
        });

        return result;
    }

    private static Dict unjsonifyDict(String jsonStr) {
        List elements = topLevelParser(jsonStr);
        if (elements.size() == 0) return new Dict();

        Dict result = new Dict();
        elements.forEach(element -> {
            String key = "";
            String value = "";
            int splitIndex = 0;
            boolean isString = false;

            for (int j = 0; j < ((String)element).length(); j++) {
                char c = ((String)element).charAt(j);
                if (c == '"') {
                    if (!isString) {
                        isString = true;
                    } else {
                        if (((String)element).charAt(j-1) != '\\') {
                            isString = false;
                        }
                    }
                }
                if (!isString) {
                    if (c == ':') {
                        splitIndex = j;
                        break;
                    }
                }
            }
            key = ((String)element).substring(0, splitIndex).strip();
            value = ((String)element).substring(splitIndex + 1).strip();
            result.set((String)unjsonifyString(key), unjsonify(value));
        });
        return result;
    }

    private static List topLevelParser(String jsonStr) {
        jsonStr = jsonStr.substring(1, jsonStr.length()-1); //remove the first and last char since they are {} or []
        if (jsonStr.strip().length() == 0) return new List(); //if the json string is empty return []

        List elements = new List();
        int level = 0;
        boolean isString = false;
        StringBuilder currentElement = new StringBuilder();
        for (char c:jsonStr.toCharArray()) {
            if (c == '"') {
                if (!isString) {
                    isString = true;
                } else {
                    if (currentElement.charAt(currentElement.length()-1) != '\\') {
                        isString = false;
                    }
                }
            }
            if (!isString) {
                if (c == '{' || c == '[') level += 1;
                else if (c == '}' || c == ']') level -= 1;
                if (c == ',' && level == 0) {
                    elements.add(currentElement.toString().strip());
                    currentElement = new StringBuilder();
                    continue;
                }
                if (Character.isWhitespace(c)) {
                    if (currentElement.length() == 0 && level == 0) continue;
                }
            }
            currentElement.append(c);
        }
        elements.add(currentElement.toString().strip());
        return elements;
    }



    /*
        *Python Prototype code: (I made this in python so I know how the logic should be)

        def unjsonify(json_str:str): #the main unjsonify function
            if json_str == "null": #if json string is null it is None
                return _none() #return the None function
            
            if json_str.lower() in ["true", "false"]: #if json string is true or false it is a boolean
                return _bool(json_str) #return the boolean function
            
            try: return _number(json_str) #try to convert to number (this has to be try instead of a simple if since, numbers can be a bit more complex)
            except ValueError: pass #if it fails, it's not a number
            
            if (json_str[0] == '"') and (json_str[-1] == '"'): #if json string starts and ends with "" it is a string
                return _str(json_str) #return the string function
            
            if (json_str[0] == "[") and (json_str[-1] == "]"): #if json string starts and ends with [] it is a list
                return _list(json_str) #return the list function
            
            if (json_str[0] == "{") and (json_str[-1] == "}"): #if json string starts and ends with {} it is a dict
                return _dict(json_str) #return the dict function
            
            raise TypeError(f"Unsupported type: {json_str}") #if the data is not a supported type, raise an error


        def _none(): #the function that will unjsonify None (null)
            return None #return Non

        def _bool(json_str:str): #the function that will unjsonify a boolean
            return json_str == "true" #return True if the json string is true, else False

        def _number(json_str:str): #the function that will unjsonify a number (int or float)
            if "." in json_str or "e" in json_str or "E" in json_str: #if json is decimal or scientific notation
                try:
                    return float(json_str) #try to return a float
                except ValueError:
                    raise ValueError(f"Invalid number format: {json_str}")
            else: #else if json is whole number
                try:
                    return int(json_str) #try to return an int
                except ValueError:
                    raise ValueError(f"Invalid number format: {json_str}")
            """ Java pseudo code for doubles and longs
            try {
                return Double.parseDouble(json_str);
            } catch (NumberFormatException e) {
                // not a double
            }
            try {
                return Long.parseLong(json_str);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid number format: " + json_str);
            } """


        def _str(json_str: str): #the function that will unjsonify a string
            json_str = json_str[1:-1] #remove the first and last char since they are ""

            result = [] #init result list
            i = 0 #init index
            length = len(json_str) #get the length of the json string

            while i < length: #loop through the json string char by char
                char = json_str[i] #get the current char

                if char == "\\" and i + 1 < length: #if we see a \ and there is a next char
                    next_char = json_str[i + 1] #get the next char

                    #handle valid escape sequences
                    if next_char == '"': #if the next char is a "
                        result.append('"') #add " to the result list
                        i += 2 #skip the next char
                        continue
                    elif next_char == "n": #if the next char is n
                        result.append("\n") #add newline to the result list
                        i += 2 #skip the next char
                        continue
                    elif next_char == "t": #if the next char is t
                        result.append("\t") #add tab to the result list
                        i += 2 #skip the next char
                        continue
                    elif next_char == "r": #if the next char is r
                        result.append("\r") #add carriage return to the result list
                        i += 2 #skip the next char
                        continue
                    elif next_char == "\\": #if the next char is \
                        result.append("\\") #add \ to the result list
                        i += 2 #skip the next char
                        continue
                    else: #if it's an unknown escape sequence
                        result.append("\\") #add \ to the result list
                        i += 1 #move to the next char
                        continue
                else: #if the char is not a \
                    result.append(char) #add the char to the result list
                    i += 1 #move to the next char

            return "".join(result) #join the result list to a string and return it

        def _list(json_str:str): #the function that will unjsonify a list
            elements = _top_level_parser(json_str) #parse the top level of the list (since lists can have nested dicts and lists)
            if len(elements) == 0: return [] #if the list is empty return []

            result = [] #init result list
            for i in range(len(elements)): #loop through the elements
                element = elements[i] #get the element
                result.append(unjsonify(element)) #add the unjsonified element to the result list
            
            return result #return the result list

        def _dict(json_str:str): #the function that will unjsonify a dict
            elements = _top_level_parser(json_str) #parse the top level of the dict (since dicts can have nested dicts and lists)
            if len(elements) == 0: return {} #if the dict is empty return {}
            
            result = {} #init result dict
            for i in range(len(elements)): #loop through the elements
                element = elements[i] #get the element
                split_index = 0 #init split index (the index where the key ends and the value starts)
                is_string = False #init is_string (the var that keeps track if we are currently in a string since strings can have : in them)

                for j in range(len(element)): #loop through the element char by char
                    if element[j] == '"': #if we see a " we check if we are entering or exiting a string
                        if not is_string: #if we are currently not in a string we are always about to enter a string
                            is_string = True #enter the string
                        
                        else: #if we are currently in a string and we see a " that doesn't nessesarily mean we are exiting the string, we could be escaping the " with a \, so we check if the last char was a \
                            if element[j-1] != "\\": #if the last char was not a \ we are exiting the string
                                is_string = False #exit the string

                    if not is_string: #we only care about the checks if we are not in a string
                        if element[j] == ":": #if we see a : and we are not in a string we have reached the end of the key
                            split_index = j #set the split index to the current index
                            break #break the loop since we found the split index

                key, value = element[:split_index], element[split_index+1:] #split the element at the split index
                key = _str(key.strip()) #get the key and remove leading and trailing spaces and unjsonify it
                value = unjsonify(value.strip()) #get the value and remove leading and trailing spaces and unjsonify it

                result[key] = value #add the key and value to the result dict

            return result #return the result dict


        def _top_level_parser(json_str:str): #the function that will parse the top level of a dict or list (as both can have nested dicts and lists which means we can't just split by ", ")
            json_str = json_str[1:-1] #remove the first and last char since they are {} or []
            if len(json_str.strip()) == 0: return [] #if the json string is empty return []

            elements = [] #init elements list
            level = 0 #init level (the var that keeps track of how deep we are in the nested dicts and lists (0 is top level))
            is_string = False #init is_string (the var that keeps track if we are currently in a string since strings can have , and " in them)
            
            current_element = ""
            for char in json_str: #loop through the json string char by char

                if char == '"': #if we see a " we check if we are entering or exiting a string
                    if not is_string: #if we are currently not in a string we are always about to enter a string
                        is_string = True #enter the string

                    else: #if we are currently in a string and we see a " that doesn't nessesarily mean we are exiting the string, we could be escaping the " with a \, so we check if the last char was a \
                        if current_element[-1] != "\\": #if the last char was not a \ we are exiting the string
                            is_string = False #exit the string


                if not is_string: #we only care about the checks if we are not in a string
                    if char in ["{", "["]: level += 1 #if we see a { or [ we are entering a nested dict or list so we increase the level
                    elif char in ["}", "]"]: level -= 1 #if we see a } or ] we are exiting a nested dict or list so we decrease the level

                    if (char == ",") and (level == 0): #if we see a , and we are at the top level we have reached the end of an element
                        elements.append(current_element.strip()) #add the current element to the elements list (make sure to strip it to remove leading and trailing spaces)
                        current_element = "" #reset the current element
                        continue
                    
                    if (char.isspace()): #if we see a whitespace char we want to check if we can skip it
                        if (current_element == "") and (level == 0): continue #if we are at the start of a new element and at the top level we can skip the whitespace
                
                current_element += char #add the char to the current element

            elements.append(current_element.strip()) #add the last element to the elements list (make sure to strip it to remove leading and trailing spaces)
            return elements #return the elements lis
    */


}
