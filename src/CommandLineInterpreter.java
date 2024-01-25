import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;


@Retention(RetentionPolicy.RUNTIME)
@interface commandInfo {
    public String description();
    public String parameters();

}


public class CommandLineInterpreter {
    private final Map<String, List<Method>> commandsMap = new HashMap<>();

    public CommandLineInterpreter(){
        List<Method> methods = List.of((CommandLineInterpreter.class.getDeclaredMethods()));

        for(var m : methods){
            var methodName = m.getName();
            List<Method> validArgumentsQuant = commandsMap.get(methodName);

            if (methodName.equals("runCommand"))continue;

            if (validArgumentsQuant != null){
                validArgumentsQuant.add(m);
            } else {
                commandsMap.put(methodName, new ArrayList<>(Collections.singleton(m)));
            }
        }


    }


    //REMEMBER to handle exceptions
     public void runCommand(List<String> parsedEntry) throws InvocationTargetException, IllegalAccessException {
        String command = parsedEntry.getFirst();
        var args = parsedEntry.subList(1, parsedEntry.size()).toArray();

        if(!commandsMap.containsKey(command)){
            System.out.printf("%s: command not found \n\n", command);

            return;
        }

        for( Method x : commandsMap.get(command)){
            if( x.getParameterCount() == args.length){
                  x.invoke(this, args);

                  return;
            }
        }

        System.out.println("Incorrect amount amount of arguments: type help for more info\n");
    }


    @commandInfo(
            description = "displays all valid commands and what they do",
            parameters = ""
    )
    private void help(){

        for(var x : commandsMap.values()){
            for(var y : x){

                var a = y.getAnnotation(commandInfo.class);


                System.out.printf("%s%s: %s\n", y.getName(), a.parameters(), a.description());
            }
        }


    }

    @commandInfo(
            description = "terminates the program",
            parameters = ""
    )
    private void quit(){
        System.out.println("quit");
        System.exit(0);

    }

    @commandInfo(

            description = "lists contents of current directory",
            parameters = ""

    )
    private void list(){

    }

    @commandInfo(
            description = "lists contents of the directory identified by path",
            parameters = " path"

    )
    private void list(String path){

    }

    @commandInfo(

            description = "changes current directory to the directory identified by path",
            parameters = " path"

    )
    private void chdir(String path){

    }

    @commandInfo(

            description = "executes an executable file in path",
            parameters = " path/file"

    )
    private void run(String pathFile){

    }

    @commandInfo(

            description = "removes a file in path",
            parameters = " path/file"

    )
    private void remove(String pathFile){

    }

    @commandInfo(

            description = "renames the file or directory name old to new",
            parameters = " old new"

    )
    private void rename(String old, String now){

    }








}
