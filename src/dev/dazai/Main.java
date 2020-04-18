package dev.dazai;

import org.fusesource.jansi.AnsiConsole;
import java.util.List;
import java.util.Scanner;
import static org.fusesource.jansi.Ansi.*;

public class Main{
    static DatabaseJDBC databaseJDBC;

    public static void cls(){
        //ASCI CODE cls in cmd
        System.out.print("\033[H\033[2J");
        System.out.flush();

    }

    public static void exit(){
        System.out.println(ansi().bgRed().fgBlack().a("Exit").reset());
        System.exit(1);

    }

    public static void printLogo(){
        cls();
        System.out.println(ansi().fgRed().a("     _                _      _            "));
        System.out.println(ansi().fgRed().a("  __| | __ _ ______ _(_)  __| | _____   __"));
        System.out.println(ansi().fgRed().a(" / _` |/ _` |_  / _` | | / _` |/ _ \\ \\ / /"));
        System.out.println(ansi().fgRed().a("| (_| | (_| |/ / (_| | || (_| |  __/\\ V / "));
        System.out.println(ansi().fgRed().a(" \\__,_|\\__,_/___\\__,_|_(_)__,_|\\___| \\_/  @2020 TASK LIST"));
        System.out.println(ansi().reset());

    }

    private static void printTaskListHeader(){
        System.out.println("-------------------------------------------------------------------------");
        System.out.println("                                " + ansi().bgRed().fgBlack().a("TASK LIST").reset() + "                                ");
        System.out.println("-------------------------------------------------------------------------");

    }

    private static void printSelectedTaskHeader(){
        System.out.println("-------------------------------------------------------------------------");
        System.out.println("                              " + ansi().bgRed().fgBlack().a("SELECTED TASK").reset() + "                              ");
        System.out.println("-------------------------------------------------------------------------");

    }

    private static void printTastListMenu(){
        System.out.println("-------------------------------------------------------------------------");
        System.out.print(ansi().bgRed().fgBlack().a(" a ").reset() + " " + ansi().fgRed().a("add task").reset() + "  ");
        System.out.print(ansi().bgRed().fgBlack().a(" s ").reset() + " " + ansi().fgRed().a("show task").reset() + "  ");
        System.out.print(ansi().bgRed().fgBlack().a(" d ").reset() + " " + ansi().fgRed().a("delete task").reset() + "  ");
        System.out.print(ansi().bgRed().fgBlack().a(" r ").reset() + " " + ansi().fgRed().a("reload list").reset() + "\n");
        System.out.println("-------------------------------------------------------------------------");

    }

    private static void printSelectedTaskMenu(){
        System.out.println("-------------------------------------------------------------------------");
        System.out.print(ansi().bgRed().fgBlack().a(" e ").reset() + " " + ansi().fgRed().a("edit task").reset() + "  ");
        System.out.print(ansi().bgRed().fgBlack().a(" d ").reset() + " " + ansi().fgRed().a("delete task").reset() + "  ");
        System.out.print(ansi().bgRed().fgBlack().a(" b ").reset() + " " + ansi().fgRed().a("go back").reset() + "\n");
        System.out.println("-------------------------------------------------------------------------");
    }

    private static void showBase(){
        DatabaseJDBC databaseJDBC = new DatabaseJDBC();
        List<UserList> tasklist = databaseJDBC.showTask();
        //if there is no tasks print msg about status
        if(tasklist.size()==0){
            System.out.println();
            System.out.println("                              "+ansi().bg(Color.WHITE).fgBlack().a("Nothing here.")+ansi().reset()+"                              ");
            System.out.println();

        }else{
            for (UserList task: tasklist) {
                System.out.println(task);

            }

        }
        databaseJDBC.closeConnection();

    }

    private static void addTask(String taskName, String description){
        DatabaseJDBC databaseJDBC = new DatabaseJDBC();
        databaseJDBC.insertTask(taskName, description);

    }

    private static String getInput(){
        Scanner userInput = new Scanner(System.in);
        String value = userInput.nextLine();

        return value;

    }

    private static void printTaskList(){
        printTaskListHeader();
        showBase();
        printTastListMenu();

    }

    private static void printSelectedTask(String Id){
        cls();
        printSelectedTaskHeader();
        databaseJDBC.moreTask(Id);
        printSelectedTaskMenu();

    }

    public static void main(String[] args){
        AnsiConsole.systemInstall();
        databaseJDBC = new DatabaseJDBC();
        Boolean commandsLoop = true;

        printLogo();

        while(true){
            //this if is for printing task list but no when user enter a wrong command
            if(commandsLoop==true){
                printTaskList();

            }
            commandsLoop=true;

            System.out.print("\n" + ansi().bgRed().fgBlack().a("@taskList#:").reset()+" ");
            String mainCommand = getInput();

            if(mainCommand.equals("add") || mainCommand.equals("a")){
                System.out.print(ansi().bgRed().fgBlack().a("[ENTER TASK NAME]#:").reset()+" ");
                String setTaskName = getInput();
                System.out.print(ansi().bgRed().fgBlack().a("[ENTER TASK DESCRIPTION]#:").reset()+" ");
                String setDescription = getInput();
                addTask(setTaskName, setDescription);
                cls();

            }else if(mainCommand.equals("show") || mainCommand.equals("s")){
                System.out.print(ansi().bgRed().fgBlack().a("[ENTER TASK ID]#:").reset()+" ");
                String getId = getInput();


                while(true){
                    if(commandsLoop==true){
                        cls();

                    }
                    commandsLoop=true;
                    printSelectedTask(getId);

                    System.out.print("\n" + ansi().bgRed().fgBlack().a("@taskList->[").reset() + ansi().bg(Color.WHITE).fgBlack().a("@SHOW_TASK")+ansi().bgRed().fgBlack().a("]#:").reset()+" ");
                    String mainCommandOnTask = getInput();

                    if(mainCommandOnTask.equals("edit") || mainCommandOnTask.equals("e")){
                        System.out.print(ansi().bgRed().fgBlack().a("[ENTER NEW TASK NAME]#:").reset()+" ");
                        String updatedTaskName = getInput();
                        System.out.print(ansi().bgRed().fgBlack().a("[ENTER NEW DESCRIPTION]#:").reset()+" ");
                        String updatedDescription = getInput();
                        databaseJDBC.editTask(getId, updatedTaskName, updatedDescription);
                        printSelectedTask(getId);

                    }else if(mainCommandOnTask.equals("delete") || mainCommandOnTask.equals("d")){
                        databaseJDBC.deleteTask(getId);
                        //about this i wrote comment in DatabaseJDBC class
                        databaseJDBC.autoincrementReset();

                        //just for little sleep
                        try {
                            Thread.sleep(2000);
                            cls();
                            break;

                        }
                        catch(InterruptedException ex){
                            Thread.currentThread().interrupt();

                        }

                    }else if(mainCommandOnTask.equals("back") || mainCommandOnTask.equals("b")){
                        break;

                    }else {
                        System.out.println(ansi().fgRed().a("no such a command in @taskList is available.").reset());
                        commandsLoop=false;

                    }

                }

            }else if(mainCommand.equals("delete") || mainCommand.equals("d")){
                System.out.print(ansi().bgRed().fgBlack().a("[ENTER TASK ID]#:").reset()+" ");
                String getId = getInput();
                databaseJDBC.deleteTask(getId);
                databaseJDBC.autoincrementReset();
                cls();

            }else if(mainCommand.equals("reload") || mainCommand.equals("r")){
                cls();

            }else if(mainCommand.equals("exit") || mainCommand.equals("e")){
                exit();

            }else{
                System.out.println(ansi().fgRed().a("no such a command in @taskList is available.").reset());
                commandsLoop=false;

            }

        }

    }

}


