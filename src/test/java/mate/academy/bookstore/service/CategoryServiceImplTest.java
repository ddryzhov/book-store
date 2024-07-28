package mate.academy.bookstore.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import mate.academy.bookstore.dto.category.CategoryDto;
import mate.academy.bookstore.exception.EntityNotFoundException;
import mate.academy.bookstore.mapper.CategoryMapper;
import mate.academy.bookstore.model.Category;
import mate.academy.bookstore.repository.category.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class CategoryServiceImplTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryMapper categoryMapper;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findAll_ReturnsCategoryDtoList() {
        Category category = new Category();
        CategoryDto categoryDto = new CategoryDto();
        when(categoryRepository.findAll()).thenReturn(Collections.singletonList(category));
        when(categoryMapper.toDtoList(anyList()))
                .thenReturn(Collections.singletonList(categoryDto));

        List<CategoryDto> result = categoryService.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(categoryRepository, times(1)).findAll();
        verify(categoryMapper, times(1)).toDtoList(anyList());
    }

    @Test
    void getById_ValidId_ReturnsCategoryDto() {
        Long id = 1L;
        Category category = new Category();
        CategoryDto categoryDto = new CategoryDto();
        when(categoryRepository.findById(id)).thenReturn(Optional.of(category));
        when(categoryMapper.toDto(category)).thenReturn(categoryDto);

        CategoryDto result = categoryService.getById(id);

        assertNotNull(result);
        verify(categoryRepository, times(1)).findById(id);
        verify(categoryMapper, times(1)).toDto(category);
    }

    @Test
    void getById_InvalidId_ThrowsEntityNotFoundException() {
        Long id = 1L;
        when(categoryRepository.findById(id)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> categoryService.getById(id));

        assertEquals("Can't find category by id " + id, exception.getMessage());
        verify(categoryRepository, times(1)).findById(id);
    }

    @Test
    void save_ValidCategoryDto_ReturnsCategoryDto() {
        CategoryDto categoryDto = new CategoryDto();
        Category category = new Category();
        when(categoryMapper.toEntity(categoryDto)).thenReturn(category);
        when(categoryRepository.save(category)).thenReturn(category);
        when(categoryMapper.toDto(category)).thenReturn(categoryDto);

        CategoryDto result = categoryService.save(categoryDto);

        assertNotNull(result);
        verify(categoryMapper, times(1)).toEntity(categoryDto);
        verify(categoryRepository, times(1)).save(category);
        verify(categoryMapper, times(1)).toDto(category);
    }

    @Test
    void update_ValidIdAndCategoryDto_ReturnsUpdatedCategoryDto() {
        Long id = 1L;
        CategoryDto categoryDto = new CategoryDto();
        Category category = new Category();
        when(categoryRepository.findById(id)).thenReturn(Optional.of(category));
        when(categoryRepository.save(category)).thenReturn(category);
        when(categoryMapper.toDto(category)).thenReturn(categoryDto);

        CategoryDto result = categoryService.update(id, categoryDto);

        assertNotNull(result);
        verify(categoryRepository, times(1)).findById(id);
        verify(categoryRepository, times(1)).save(category);
        verify(categoryMapper, times(1)).toDto(category);
    }

    @Test
    void update_InvalidId_ThrowsEntityNotFoundException() {
        Long id = 1L;
        CategoryDto categoryDto = new CategoryDto();
        when(categoryRepository.findById(id)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> categoryService.update(id, categoryDto));

        assertEquals("Can't find category by id " + id, exception.getMessage());
        verify(categoryRepository, times(1)).findById(id);
    }

    @Test
    void deleteById_ValidId_DeletesCategory() {
        Long id = 1L;

        categoryService.deleteById(id);

        verify(categoryRepository, times(1)).deleteById(id);
    }
}
