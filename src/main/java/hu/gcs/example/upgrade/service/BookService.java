package hu.gcs.example.upgrade.service;

import javax.ejb.EJB;
import javax.ejb.Stateless;

@Stateless
public class BookService {

    @EJB
    private BookRepository bookRepository;

    public void saveANewBook() {
        bookRepository.save(new Book("book title"));
    }
}
