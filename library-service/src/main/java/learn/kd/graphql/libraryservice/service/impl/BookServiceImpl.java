package learn.kd.graphql.libraryservice.service.impl;

import learn.kd.generated.schema.types.Book;
import learn.kd.graphql.libraryservice.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BookServiceImpl implements BookService {

    private Map<String, List<Book>> studentToBooksMap = null;

    @Autowired
    public void BookServiceImpl() {
        studentToBooksMap = new HashMap<>();

        List<Book> student1Books = new ArrayList<>() {{
            add(Book.newBuilder().id("Book1").title("The Book of GraphQL - Part I").build());
            add(Book.newBuilder().id("Book2").title("The Book of GraphQL - Part II").build());
        }};

        studentToBooksMap.put("1", student1Books);

        List<Book> student2Books = new ArrayList<>() {{
            add(new Book("Book3", "The Book of GraphQL - Part III"));
            add(new Book("Book4", "The Book of GraphQL - Part IV"));
            add(new Book("Book5", "The Book of GraphQL - Part V"));
            add(new Book("Book6", "The Book of GraphQL - Part VI"));
        }};

        studentToBooksMap.put("2", student2Books);
    }


    @Override
    public List<Book> getBooksForStudent(String studentId) {
        return studentToBooksMap.get(studentId);
    }
}
