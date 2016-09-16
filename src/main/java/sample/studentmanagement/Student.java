package sample.studentmanagement;

public class Student {
    private String name;
    private int id;
    
    public String getName() {
        return name;
    }
    
    public int getId() {
        return id;
    }

    public Student setId(int id) {
        this.id = id;
        return this;
    }
    
    public Student setName(String name) {
        this.name = name;
        return this;
    }
}
