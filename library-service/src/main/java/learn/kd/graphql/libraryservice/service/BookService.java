package learn.kd.graphql.libraryservice.service;

import learn.kd.generated.schema.types.Book;

import java.util.List;

public interface BookService {
    List<Book> getBooksForStudent(String studentId);
}
