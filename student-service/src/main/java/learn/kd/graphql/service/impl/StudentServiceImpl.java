package learn.kd.graphql.service.impl;

import com.netflix.graphql.dgs.exceptions.DgsEntityNotFoundException;
import learn.kd.generated.schema.types.School;
import learn.kd.generated.schema.types.Student;
import learn.kd.graphql.service.StudentService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service("studentService")
public class StudentServiceImpl implements StudentService {

    private static final List<Student> students;

    static {
        students = new ArrayList<>() {{
            add(new Student("1", "StudentA", 12, new School("Sch2", null, null)));
            add(new Student("2", "StudentB", 13, new School("Sch2", null, null)));
            add(new Student("3", "StudentC", 10, new School("Sch1", null, null)));
            add(new Student("4", "StudentD", 15, new School("Sch2", null, null)));
            add(new Student("5", "StudentE", 11, new School("Sch1", null, null)));
        }};
    }


    @Override
    public List<Student> listAllStudentsWithFilter(String nameFilter) {
        List<Student> studentsList = students.stream()
                .filter(s -> nameFilter == null || s.getName().contains(nameFilter))
                .collect(Collectors.toList());

        if (studentsList.isEmpty()) {
            throw new DgsEntityNotFoundException("No Data Found");
        }

        return studentsList;
    }
}
