package learn.kd.graphql.fetcher;

import com.jayway.jsonpath.TypeRef;
import com.netflix.graphql.dgs.DgsQueryExecutor;
import com.netflix.graphql.dgs.autoconfig.DgsAutoConfiguration;
import learn.kd.generated.schema.types.Student;
import learn.kd.graphql.loader.SchoolLoader;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {DgsAutoConfiguration.class, StudentsFetcher.class, SchoolLoader.class})
class StudentsFetcherTest {

    @Autowired
    DgsQueryExecutor dgsQueryExecutor;

    @Test
    void testFetchAllStudents() {
        TypeRef<List<Student>> studentListType = new TypeRef<>() {
        };

        List<Student> students = dgsQueryExecutor.executeAndExtractJsonPathAsObject(
                " {students{id name age school{id name address}}}",
                "data.students",
                studentListType);

        assertThat(students).isNotNull();
        assertThat(students).isNotEmpty();

        students.stream().forEach(student -> assertThat(student.getSchool().getName()).isNotNull());
    }

    @Test
    void testFetchStudentUsingNameFilter() {
        TypeRef<List<Student>> studentListType = new TypeRef<>() {
        };

        List<Student> students = dgsQueryExecutor.executeAndExtractJsonPathAsObject(
                " {students(nameFilter:\"StudentA\"){id name age school{id name address}}}",
                "data.students",
                studentListType);

        assertThat(students).isNotNull();
        assertThat(students).isNotEmpty();
        assertThat(students).hasSize(1);
    }
}