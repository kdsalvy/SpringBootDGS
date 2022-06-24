package learn.kd.graphql.libraryservice.fetcher;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsData;
import com.netflix.graphql.dgs.DgsDataFetchingEnvironment;
import com.netflix.graphql.dgs.DgsEntityFetcher;
import learn.kd.generated.schema.DgsConstants;
import learn.kd.generated.schema.types.Book;
import learn.kd.generated.schema.types.Student;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

@Slf4j
@DgsComponent
public class BooksFetcher {

    private Map<String, List<Book>> studentBookRegister;

    public BooksFetcher() {
        studentBookRegister = Map.of(
                "1", List.of(
                        Book.newBuilder().id("Book1").title("The Book of GraphQL - Part I").build(),
                        Book.newBuilder().id("Book2").title("The Book of GraphQL - Part II").build()
                ),
                "2", List.of(
                        new Book("Book3", "The Book of GraphQL - Part III"),
                        new Book("Book4", "The Book of GraphQL - Part IV"),
                        new Book("Book5", "The Book of GraphQL - Part V"),
                        new Book("Book6", "The Book of GraphQL - Part VI")
                )
        );
    }

    @DgsEntityFetcher(name = DgsConstants.STUDENT.TYPE_NAME)
    public Student student(Map<String, Object> values) {
        return Student.newBuilder().id((String) values.get(DgsConstants.STUDENT.Id)).build();
    }

    @DgsData(parentType = DgsConstants.STUDENT.TYPE_NAME)
    public List<Book> books(DgsDataFetchingEnvironment dfe) {
        Student student = dfe.getSource();
        return studentBookRegister.get(student.getId());
    }
}
