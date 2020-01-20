package models;

public class ListItem {

    private String todo;
    private String desc;
    private String id;
    private boolean checkbox;

    public ListItem() {
    }

    public ListItem(String todo, String desc, String id, boolean checkbox) {
        this.todo = todo;
        this.desc = desc;
        this.id = id;
        this.checkbox = checkbox;
    }

    public String getTodo() {
        return todo;
    }

    public void setTodo(String todo) {
        this.todo = todo;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isCheckbox() {
        return checkbox;
    }

    public void setCheckbox(boolean checkbox) {
        this.checkbox = checkbox;
    }
}
