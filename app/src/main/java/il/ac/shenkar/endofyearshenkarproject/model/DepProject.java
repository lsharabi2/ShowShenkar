package il.ac.shenkar.endofyearshenkarproject.model;

public class DepProject {
    private String name;
    private String student;

    public DepProject()
    {}

    public DepProject(String name, String student)
    {
        setName(name);
        setStudent(student);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStudent() {
        return student;
    }

    public void setStudent(String student) {
        this.student = student;
    }

}
