# Command-line Interpreter 


**Comp 377 Operating Systems**    
**Programming Assignment 1**  
**10 points**  
**Due Wed Jan 26, 2024**  


This assignment is an extension/alternation to your work in Comp 376.  

In this assignment you will write a command interpreter in Python or Java.
The user types a command in response to a prompt that appears on the screen.
When the command is entered, the command interpreter determines what the command means and executes the command.



Your command interpreter should do the following:
- Display a prompt for the user.  
- Read the user's input. 
- Determine if the entered command is valid  
- If it is valid, print a message that says the command is being executed
and then re-display the prompt.  
- If the command is not valid, display an appropriate error message and
re-display the prompt.  
- Continue until the user enters the command “QUIT”.  




The valid commands will be:  
- LIST (lists contents of current directory)  
- LIST path (lists contents of the directory identified by path)  
- CHDIR path (changes current directory to the directory identified by
path)  
- RUN path/filename (executes an executable file in path)
- REMOVE path/filename  
- RENAME old new (renames the file or directory name old to new)  
- HELP (displays all valid commands and what they do)  
- QUIT (terminates the program)  
