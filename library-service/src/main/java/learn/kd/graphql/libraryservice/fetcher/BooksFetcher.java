package learn.kd.graphql.libraryservice.fetcher;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsData;
import com.netflix.graphql.dgs.DgsDataFetchingEnvironment;
import com.netflix.graphql.dgs.DgsEntityFetcher;
import com.netflix.graphql.dgs.DgsSubscription;
import learn.kd.generated.schema.DgsConstants;
import learn.kd.generated.schema.types.Book;
import learn.kd.generated.schema.types.Student;
import learn.kd.graphql.libraryservice.loader.BookLoader;
import lombok.extern.slf4j.Slf4j;
import org.dataloader.DataLoader;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Slf4j
@DgsComponent
public class BooksFetcher {

    @DgsEntityFetcher(name = DgsConstants.STUDENT.TYPE_NAME)
    public Student student(Map<String, Object> values) {
        return Student.newBuilder().id((String) values.get(DgsConstants.STUDENT.Id)).build();
    }

    @DgsData(parentType = DgsConstants.STUDENT.TYPE_NAME)
    public CompletableFuture<List<Book>> books(DgsDataFetchingEnvironment dfe) {
        DataLoader<String, List<Book>> bookLoader = dfe.getDataLoader(BookLoader.class);
        Student student = dfe.getSource();
        return bookLoader.load(student.getId());
    }

    @DgsSubscription(field = DgsConstants.SUBSCRIPTION.Ticker)
    public Publisher<String> publishTicker() {
        return Flux.interval(Duration.ofSeconds(1)).map(t -> Instant.now().toString());
    }
}
