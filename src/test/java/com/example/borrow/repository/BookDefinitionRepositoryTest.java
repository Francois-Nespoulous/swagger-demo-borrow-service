package com.example.borrow.repository;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
@EnableJpaRepositories(basePackages = "com.example.borrow.repository")
@EntityScan(basePackages = "com.example.borrow.persistence.repository.entity")
class BookDefinitionRepositoryTest {
//	@Autowired
//	private BookDefinitionRepository bookDefinitionRepository;
//	@Autowired
//	private BorrowRepository borrowRepository;
//
//	@BeforeEach
//	void setUp() {
//		borrowRepository.deleteAll();
//		bookDefinitionRepository.deleteAll();
//		bookDefinitionRepository.save(new BookDefinitionEntity(null, "Book 1", "Jean Bon"));
//		bookDefinitionRepository.save(new BookDefinitionEntity(null, "Book 2", "Jean Bon"));
//		bookDefinitionRepository.save(new BookDefinitionEntity(null, "Book 1", "Not Jeanbon"));
//		bookDefinitionRepository.save(new BookDefinitionEntity(null, "Another book", "Someone Else"));
//	}
//
//	@Test
//	public void findBookByTitleAndAuthor() {
//		//WHEN
//		List<BookDefinitionEntity> books = bookDefinitionRepository.findByTitleContainingIgnoreCaseAndAuthorContainingIgnoreCase(
//				"Book 1",
//				"Jean Bon"
//		);
//
//		//THEN
//		Assertions.assertEquals(1, books.size());
//		Assertions.assertEquals("Book 1", books.getFirst().getTitle());
//		Assertions.assertEquals("Jean Bon", books.getFirst().getAuthor());
//	}
//
//	@Test
//	public void findBookByTitleOnly() {
//		//WHEN
//		List<BookDefinitionEntity> books = bookDefinitionRepository.findByTitleContainingIgnoreCaseAndAuthorContainingIgnoreCase(
//				"Book 1",
//				""
//		);
//
//		//THEN
//		Assertions.assertEquals(2, books.size());
//		Assertions.assertEquals("Book 1", books.get(0).getTitle());
//		Assertions.assertEquals("Book 1", books.get(1).getTitle());
//		Assertions.assertEquals("Jean Bon", books.get(0).getAuthor());
//		Assertions.assertEquals("Not Jeanbon", books.get(1).getAuthor());
//	}
//
//	@Test
//	public void findBookByAuthorOnly() {
//		//WHEN
//		List<BookDefinitionEntity> books = bookDefinitionRepository.findByTitleContainingIgnoreCaseAndAuthorContainingIgnoreCase(
//				"",
//				"Jean Bon"
//		);
//
//		//THEN
//		Assertions.assertEquals(2, books.size());
//		Assertions.assertEquals("Book 1", books.get(0).getTitle());
//		Assertions.assertEquals("Book 2", books.get(1).getTitle());
//		Assertions.assertEquals("Jean Bon", books.get(0).getAuthor());
//		Assertions.assertEquals("Jean Bon", books.get(1).getAuthor());
//	}

}
