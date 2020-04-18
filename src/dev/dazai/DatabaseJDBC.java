package dev.dazai;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class DatabaseJDBC{

    public static final String DRIVER = "org.sqlite.JDBC";
    //here we have a database name that will be created
    public static final String DB_URL = "jdbc:sqlite:dazaidev_jdbc_tasks_base.db";

    private Connection conn;
    private Statement stat;

    public DatabaseJDBC(){
        //checking for driver
        try{
            Class.forName(DatabaseJDBC.DRIVER);

        }catch (ClassNotFoundException e) {
            System.err.println("Missing JDBC driver!");
            e.printStackTrace();

        }

        try{
            //looking for database
            conn = DriverManager.getConnection(DB_URL);
            stat = conn.createStatement();

        }catch(SQLException e){
            System.err.println("Sth went wrong with connection!");
            e.printStackTrace();

        }
        createTables();

    }

    public boolean createTables()  {
        String createList = "CREATE TABLE IF NOT EXISTS tasklist (id INTEGER PRIMARY KEY AUTOINCREMENT, taskName varchar(48), description varchar(255))";

        try {
            stat.execute(createList);

        } catch (SQLException e) {
            System.err.println("Creating table has been ended with fail!");
            e.printStackTrace();
            return false;

        }
        return true;

    }

    public boolean insertTask(String taskName, String description) {
        try {
            PreparedStatement prepStmt = conn.prepareStatement(
                    "INSERT INTO tasklist VALUES (NULL, ?, ?);");
            prepStmt.setString(1, taskName);
            prepStmt.setString(2, description);
            prepStmt.execute();

        } catch (SQLException e) {
            System.err.println("Error with adding task!");
            e.printStackTrace();
            return false;

        }
        return true;

    }


    public List<UserList> showTask() {
        List<UserList> userlist = new LinkedList<UserList>();
        try {
            ResultSet result = stat.executeQuery("SELECT * FROM tasklist");
            int id;
            String taskName, description;
            while(result.next()) {
                id = result.getInt("id");
                taskName = result.getString("taskName");
                description = result.getString("description");
                userlist.add(new UserList(id, taskName, description));

            }

        } catch (SQLException e) {
            e.printStackTrace();
            return null;

        }
        return userlist;

    }

    public void moreTask(String getId){
        try {
            ResultSet result = stat.executeQuery("SELECT * FROM tasklist WHERE id = "+getId+";");
            System.out.println("TASK_NAME: "+result.getString("taskName")+"\n" +
                    "_________________________________________________________________________");
            System.out.println("DESCRIPTION: "+result.getString("description"));

        } catch (SQLException e) {
            e.printStackTrace();

        }
    }



    public void deleteTask(String getId){
        try {
            stat.executeUpdate("DELETE FROM tasklist WHERE id="+getId+";");

        } catch (SQLException e) {
            e.printStackTrace();

        }

    }

    public void editTask(String getId, String updatedTaskName, String updatedDescription){
        try {
            if(updatedTaskName.equals("") && updatedDescription.equals("")){
                //nothing

            }else if(updatedTaskName.equals("") && !updatedDescription.equals("")){
                stat.executeUpdate("UPDATE tasklist SET description= '"+updatedDescription+"' WHERE id = "+getId+";");

            }else if(!updatedTaskName.equals("") && updatedDescription.equals("")){
                stat.executeUpdate("UPDATE tasklist SET taskName = '"+updatedTaskName+"' WHERE id = "+getId+";");

            }else{
                stat.executeUpdate("UPDATE tasklist SET taskName = '"+updatedTaskName+"', description= '"+updatedDescription+"' WHERE id = "+getId+";");

            }

        } catch (SQLException e) {
            e.printStackTrace();

        }

    }

    public void autoincrementReset(){
        try{
            int i = 1;
            int selectedId;
            List<Integer> listId = new ArrayList<>();
            List<Integer> listSelectedId = new ArrayList<>();
            //result of sql query:
            ResultSet result = stat.executeQuery("SELECT * FROM tasklist");

            //if result has next row execute code:
            while(result.next()){
                //get id from query
                selectedId = result.getInt("id");
                //add i to array
                listId.add(i);
                //add id from query to array
                listSelectedId.add(selectedId);
                i++;

            }

            for(int ii = 0; ii<listSelectedId.size(); ii++){
                //update id
                stat.executeUpdate("UPDATE tasklist SET id = "+listId.get(ii)+" WHERE id = "+listSelectedId.get(ii)+";");

            }
            //start autoincrement from last var i;
            stat.executeUpdate("UPDATE sqlite_sequence SET seq = "+(--i)+" WHERE name = 'tasklist';");

        }catch (SQLException e){
            e.printStackTrace();

        }

        return;
    }

    public void closeConnection() {
        try {
            conn.close();

        } catch (SQLException e) {
            System.err.println("Sth went wrong with closing connection!");
            e.printStackTrace();

        }
    }
}