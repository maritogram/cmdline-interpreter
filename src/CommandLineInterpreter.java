import java.io.IOException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.*;
import java.util.*;


@Retention(RetentionPolicy.RUNTIME)
@interface commandInfo {
    public String description();
    public String parameters();

}


public class CommandLineInterpreter {
    private final Map<String, List<Method>> commandsMap = new HashMap<>();
    private String curDir = Paths.get("").toAbsolutePath().toString();
    private final String  separator = FileSystems.getDefault().getSeparator();

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

        System.out.println("Incorrect amount of arguments: for more info type help\n");
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

            System.out.println();

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
            Path dir = Paths.get(curDir);

            try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir)) {
                for (Path file: stream) {
                    System.out.println(file.getFileName());
                }
            } catch (IOException | DirectoryIteratorException e) {
                System.out.println(e);
            }


        }

        @commandInfo(
                description = "lists contents of the directory identified by path",
                parameters = " path"
        )
        private void list(String path){

            String pathBuffer;

            if(path.startsWith(separator)){
                if (!Files.exists(Path.of(path))){
                    System.out.println("Invalid path.");
                    return;
                }
                else
                    pathBuffer = path;
            } else {
                if (!Files.exists(Path.of(curDir.concat(separator).concat(path)))){
                    System.out.println("Invalid path.");
                    return;
                }
                else
                    pathBuffer = curDir.concat(separator).concat(path);
            }


            try (DirectoryStream<Path> stream = Files.newDirectoryStream(Path.of(pathBuffer))) {
                for (Path file: stream) {
                    System.out.println(file.getFileName());
                }
            } catch (IOException | DirectoryIteratorException e) {

                System.out.println(e);
            }

        }

        @commandInfo(
                description = "changes current directory to the directory identified by path",
                parameters = " path"
        )
        private void chdir(String path){

            StringBuffer curpath = new StringBuffer(200);

            // Steps 1 and 2, from man pages of cd (simplified)
            if(path.isEmpty())
                return;

            // Step 3
            if(path.startsWith(separator))
                curpath.append(path);
            else{

                //Step 4
                if(!(path.startsWith(".") || path.startsWith(".."))){
                    //Step 5
                    if(Files.exists(Path.of(curDir.concat(separator).concat(path))))
                        curpath.append(path);
                    else {
                        System.out.println("Directory does not exist.");
                        return;
                    }

                } else {
                    //Step 6
                    curpath.append(path);
                }
            }

            // Step 7
            if(!curpath.toString().startsWith(separator)){
                String buf = curpath.toString();
                curpath.delete(0, curpath.length());
                if(!curDir.endsWith(separator))
                    curpath.append(curDir.concat(separator).concat(buf));
                else
                    curpath.append(curDir.concat(buf));
            }

            String[] dirs = curpath.toString().split(separator);
            ArrayList<String> ok = new ArrayList<>();

            for(int i = 0; i < dirs.length; i++){
                if(Objects.equals(dirs[i], "."))
                    continue;
                else if(Objects.equals(dirs[i], ".."))
                    ok.remove(i - 1);
                else
                    ok.add(dirs[i]);
            }

            curDir = String.join(separator,ok);
            System.out.printf("Current directory is now: %s\n", curDir);

        }

        @commandInfo(
                description = "executes an executable file in path",
                parameters = " path/file"
        )
        private void run(String pathFile){
            try{
                ProcessBuilder processBuilder = new ProcessBuilder(pathFile);

                processBuilder.redirectOutput(ProcessBuilder.Redirect.INHERIT);
                processBuilder.redirectError(ProcessBuilder.Redirect.INHERIT);
                processBuilder.redirectInput(ProcessBuilder.Redirect.INHERIT);

                Process process = processBuilder.start();

                process.waitFor();


            } catch (Exception e){
                System.out.println(e.getMessage());

            }

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
