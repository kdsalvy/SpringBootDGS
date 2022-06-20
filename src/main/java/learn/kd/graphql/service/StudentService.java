package learn.kd.graphql.service;

import learn.kd.generated.schema.types.Student;

import java.util.List;

public interface StudentService {
    List<Student> listAllStudentsWithFilter(String nameFilter);
}
