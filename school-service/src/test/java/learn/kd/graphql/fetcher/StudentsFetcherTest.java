package learn.kd.graphql.fetcher;

import com.jayway.jsonpath.TypeRef;
import com.netflix.graphql.dgs.DgsQueryExecutor;
import com.netflix.graphql.dgs.client.codegen.BaseProjectionNode;
import com.netflix.graphql.dgs.client.codegen.GraphQLQuery;
import com.netflix.graphql.dgs.client.codegen.GraphQLQueryRequest;
import graphql.ExecutionResult;
import learn.kd.generated.schema.client.StudentsGraphQLQuery;
import learn.kd.generated.schema.client.StudentsProjectionRoot;
import learn.kd.generated.schema.types.Student;
import learn.kd.graphql.SchoolServiceApplication;
import learn.kd.graphql.service.StudentService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {SchoolServiceApplication.class})
class StudentsFetcherTest {

    public static final String STUDENT_A = "StudentA";
    public static final String UNKNOW_STUDENT = "UnknowStudent";

    @Autowired
    private DgsQueryExecutor dgsQueryExecutor;

    @SpyBean
    private StudentService studentService;

    @Test
    void testQueryAllStudents() {
        TypeRef<List<Student>> studentListType = new TypeRef<>() {
        };

        // Basic hardcoded query string test

        // List<Student> students = dgsQueryExecutor.executeAndExtractJsonPathAsObject(
        //        " {students{id name age school{id name address}}}",
        //        "data.students",
        //        studentListType);

        // Using Typesafe query builder and projection

        GraphQLQuery fetchAllStudentsQuery = new StudentsGraphQLQuery.Builder().build();
        BaseProjectionNode projectStudentAndSchool = new StudentsProjectionRoot().id().age().name().school().id().name().address();

        GraphQLQueryRequest queryRequest = new GraphQLQueryRequest(
                fetchAllStudentsQuery,
                projectStudentAndSchool
        );

        List<Student> students = dgsQueryExecutor.executeAndExtractJsonPathAsObject(
                queryRequest.serialize(),
                "data.students",
                studentListType
        );

        Assertions.assertThat(students).isNotNull();
        Assertions.assertThat(students).isNotEmpty();

        students.stream().forEach(student -> Assertions.assertThat(student.getSchool().getName()).isNotNull());
    }

    @Test
    void testQueryStudentUsingNameFilter() {
        TypeRef<List<Student>> studentListType = new TypeRef<>() {
        };

        // Basic hardcoded query string test
        // List<Student> students = dgsQueryExecutor.executeAndExtractJsonPathAsObject(
        //        " {students(nameFilter:\"StudentA\"){id name age school{id name address}}}",
        //        "data.students",
        //        studentListType);

        GraphQLQuery studentByNameQuery = StudentsGraphQLQuery.newRequest().nameFilter(STUDENT_A).build();
        BaseProjectionNode projectStudent = new StudentsProjectionRoot().id().name().age();

        GraphQLQueryRequest queryRequest = new GraphQLQueryRequest(
                studentByNameQuery,
                projectStudent
        );

        List<Student> students = dgsQueryExecutor.executeAndExtractJsonPathAsObject(
                queryRequest.serialize(),
                "data.students",
                studentListType);


        Assertions.assertThat(students).isNotNull();
        Assertions.assertThat(students).isNotEmpty();
        Assertions.assertThat(students).hasSize(1);
    }

    @Test
    void testQueryStudentsWithCustomExceptionHandler() {
        GraphQLQuery studentByNameQuery = StudentsGraphQLQuery.newRequest().nameFilter(UNKNOW_STUDENT).build();
        BaseProjectionNode projectStudent = new StudentsProjectionRoot().id().name().age();

        GraphQLQueryRequest queryRequest = new GraphQLQueryRequest(
                studentByNameQuery,
                projectStudent
        );

        ExecutionResult result = dgsQueryExecutor.execute(queryRequest.serialize());
        Assertions.assertThat(result.getErrors()).isNotEmpty();
        Assertions.assertThat(result.getErrors().get(0).getMessage()).isEqualTo("This is from custom Exception Handler");
    }
}