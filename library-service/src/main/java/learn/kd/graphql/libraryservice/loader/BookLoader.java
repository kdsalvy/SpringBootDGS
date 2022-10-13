package learn.kd.graphql.libraryservice.loader;

import com.netflix.graphql.dgs.DgsDataLoader;
import learn.kd.generated.schema.types.Book;
import learn.kd.graphql.libraryservice.service.BookService;
import org.dataloader.BatchLoader;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

@DgsDataLoader
public class BookLoader implements BatchLoader<String, List<Book>> {

    private BookService bookService;

    @Autowired
    public void BookLoader(BookService bookService) {
        this.bookService = bookService;
    }

    @Override
    public CompletionStage<List<List<Book>>> load(List<String> ids) {
        return CompletableFuture.supplyAsync(() -> ids.stream()
                .map(id -> bookService.getBooksForStudent(id))
                .collect(Collectors.toList()));
    }
}
