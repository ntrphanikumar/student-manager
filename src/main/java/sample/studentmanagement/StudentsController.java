package sample.studentmanagement;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/students")
public class StudentsController {

    private final AtomicInteger NEXT_ID = new AtomicInteger();
    private final Map<Integer, Student> studentsById = new HashMap<>();

    public StudentsController() {
        createStudent(new Student().setName("Dummy Student"));
    }

    @RequestMapping(method = RequestMethod.GET)
    public Collection<Student> listAllStudents() {
        return studentsById.values();
    }

    @RequestMapping(method = RequestMethod.GET, path = "/{studentId}")
    public Student getStudent(@PathVariable("studentId") int studentId) {
        return studentsById.get(studentId);
    }

    @RequestMapping(method = RequestMethod.POST)
    public Student createStudent(@RequestBody Student student) {
        student.setId(NEXT_ID.getAndIncrement());
        studentsById.put(student.getId(), student);
        return student;
    }

    @RequestMapping(method = RequestMethod.PUT, path="/{studentId}")
    public Student updateStudent(@PathVariable("studentId") int studentId, @RequestBody Student student) {
        Student existing = studentsById.get(studentId);
        if (existing != null) {
            existing.setName(student.getName());
            studentsById.put(studentId, existing);
            return existing;
        }
        return student;
    }
}
