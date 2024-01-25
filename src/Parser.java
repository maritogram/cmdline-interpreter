import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;

public class Parser{

    static List<String> parseInput(String str){
        // Trim white space before and after input, then split the string ignoring any quantity of white space in between words;
        return List.of(str.trim().split("\\s+"));

    }

}