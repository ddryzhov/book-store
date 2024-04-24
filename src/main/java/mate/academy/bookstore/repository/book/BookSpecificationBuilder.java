package mate.academy.bookstore.repository.book;

import lombok.RequiredArgsConstructor;
import mate.academy.bookstore.dto.BookSearchParameters;
import mate.academy.bookstore.model.Book;
import mate.academy.bookstore.repository.SpecificationBuilder;
import mate.academy.bookstore.repository.SpecificationProviderManager;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BookSpecificationBuilder implements SpecificationBuilder<Book> {
    private final SpecificationProviderManager<Book> bookSpecificationProviderManager;

    @Override
    public Specification<Book> build(BookSearchParameters searchParameters) {
        Specification<Book> spec = Specification.where(null);
        spec = addSpecifications(spec, "title", searchParameters.titles());
        spec = addSpecifications(spec, "author", searchParameters.authors());
        return spec;
    }

    private Specification<Book> addSpecifications(Specification<Book> spec,
                                                  String field, String[] values) {
        if (values != null && values.length > 0) {
            return spec.and(bookSpecificationProviderManager.getSpecificationProvider(field)
                    .getSpecification(values));
        }
        return spec;
    }
}
