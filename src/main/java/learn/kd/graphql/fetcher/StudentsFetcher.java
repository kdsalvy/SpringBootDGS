package learn.kd.graphql.fetcher;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsData;
import com.netflix.graphql.dgs.DgsDataFetchingEnvironment;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;
import learn.kd.generated.schema.DgsConstants;
import learn.kd.generated.schema.types.School;
import learn.kd.generated.schema.types.Student;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@DgsComponent
public class StudentsFetcher {

    private static final List<Student> students;
    private static final List<School> schools;

    static {
        students = new ArrayList<>() {{
            add(new Student("1", "StudentA", 12, new School("Sch2", null, null)));
            add(new Student("2", "StudentB", 13, new School("Sch2", null, null)));
            add(new Student("3", "StudentC", 10, new School("Sch1", null, null)));
            add(new Student("4", "StudentD", 15, new School("Sch2", null, null)));
            add(new Student("5", "StudentE", 11, new School("Sch1", null, null)));
        }};

        schools = new ArrayList<>() {{
            add(new School("Sch1", "Primary School A", "Address 1"));
            add(new School("Sch2", "Primary School B", "Address 2"));
        }};
    }

    @DgsQuery
    public List<Student> students(@InputArgument String nameFilter) {
        return students.stream()
                .filter(s -> nameFilter == null || s.getName().contains(nameFilter))
                .collect(Collectors.toList());
    }

    // Child Data fetchers :: Useful for cases where some property can take time to return
    // We can return rest of the properties in parent query
    @DgsData(parentType = DgsConstants.STUDENT.TYPE_NAME)
    public Optional<School> school(DgsDataFetchingEnvironment dfe) {
        Student student = dfe.getSource();
        return schools.stream()
                .filter(school -> school.getId().equals(student.getSchool().getId()))
                .findAny();
    }
}
