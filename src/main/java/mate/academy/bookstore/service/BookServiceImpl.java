package mate.academy.bookstore.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import mate.academy.bookstore.dto.BookDto;
import mate.academy.bookstore.dto.CreateBookRequestDto;
import mate.academy.bookstore.exception.EntityNotFoundException;
import mate.academy.bookstore.mapper.BookMapper;
import mate.academy.bookstore.model.Book;
import mate.academy.bookstore.repository.BookRepository;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    @Override
    public BookDto save(CreateBookRequestDto requestDto) {
        Book book = bookMapper.toModel(requestDto);
        return bookMapper.toDto(bookRepository.save(book));
    }

    @Override
    public List<BookDto> findAll() {
        return bookRepository.findAll().stream()
                .map(bookMapper::toDto)
                .toList();
    }

    @Override
    public BookDto findById(Long id) {
        Book book = bookRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Can't find book by id" + id)
        );
        return bookMapper.toDto(book);
    }

    @Override
    public void deleteById(Long id) {
        bookRepository.deleteById(id);
    }

    @Override
    public BookDto update(Long id, BookDto bookDto) {
        Book existingBook = bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Can't find book by id " + id));

        existingBook.setTitle(bookDto.getTitle());
        existingBook.setAuthor(bookDto.getAuthor());
        existingBook.setDescription(bookDto.getDescription());
        existingBook.setIsbn(bookDto.getIsbn());
        existingBook.setCoverImage(bookDto.getCoverImage());
        existingBook.setPrice(bookDto.getPrice());

        return bookMapper.toDto(bookRepository.save(existingBook));
    }
}
