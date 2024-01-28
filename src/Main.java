import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws InvocationTargetException, IllegalAccessException {
        Scanner scnr = new Scanner(System.in);
        CommandLineInterpreter cmdObj = new CommandLineInterpreter();
        String input;


        while (true){
            System.out.print(">> ");
            input = scnr.nextLine();
            List<String> parsed = Parser.parseInput(input);

            cmdObj.runCommand(parsed);
        }
    }
}
