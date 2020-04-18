package dev.dazai;

public class UserList {
    private int id;
    private String taskName;
    private String description;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getDescriptionn() {
        return description;
    }

    public void getDescriptionn(String description) {
        this.description = description;
    }

    public UserList(int id, String taskName, String description){
        this.id = id;
        this.taskName = taskName;
        this.description = description;

    }

    @Override
    public String toString(){
        String reId = String.valueOf(id);
        String reTaskName = taskName;
        String reDesciptrion = description;

        if(reId.length()<2){
            reId += " ";

        }

        if(reTaskName.length()>18){
            reTaskName = reTaskName.substring(0, 17) + "(...)";

        }else{
            while(reTaskName.length()<22){
                reTaskName += " ";

            }

        }

        if(reDesciptrion.length()>30){
            reDesciptrion = reDesciptrion.substring(0, 29) + "(...)";

        }

        return reId+"  "+reTaskName+"     "+reDesciptrion;
    }

}
