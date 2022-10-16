package ru.hogwarts.school.HW6_tests_controllers.model;
import javax.persistence.*;
import java.util.Objects;

@Entity
public class Student {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private int age;
    @ManyToOne //(fetch = FetchType.LAZY) // что Spring загрузит сущность или коллекцию отложено, при первом обращении к ней из кода
    @JoinColumn(name = "faculty_id")
    private Faculty faculty;

    public Student() {
    }
    public Student(long id, String name, int age, Faculty faculty) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.faculty = faculty;
    }

    public Student(String name, int age) {
    }


    public Faculty getFaculty() {
        return faculty;
    }
    public long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Student)) return false;
        Student student = (Student) o;
        return getAge() == student.getAge() && Objects.equals(getId(), student.getId()) && Objects.equals(getName(), student.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getAge());
    }
    @Override
    public String toString() {
    return "Student{" +
            "id=" + id +'}';
    //  @Override
    //public String toString() {
    //    return "Student{" +
      //          "id=" + id +
     //           ", name='" + name + '\'' +
     //           ", age=" + age +
     //           ", faculty=" + faculty +
     //           '}';
    }
}
