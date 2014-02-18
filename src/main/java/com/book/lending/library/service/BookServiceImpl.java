package com.book.lending.library.service;

import static org.springframework.data.mongodb.core.query.Criteria.where;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.book.lending.library.exception.BookLendingException;
import com.book.lending.library.model.Book;
import com.book.lending.library.model.BookDetails;
import com.book.lending.library.model.User;
import com.book.lending.library.model.UserDetails;

@Repository(value="bookServiceRepository")
@Transactional
public class BookServiceImpl implements BookService {
	
	public static final String USER_COLLECTION_NAME = "userdetails";
	public static final String BOOK_COLLECTION_NAME = "bookdetails";
	private static final Logger logger = Logger.getLogger("BookBasicService");
	
	 @Autowired
	 private UserSevice personService;

	@Resource(name = "mongoTemplate")
	private MongoTemplate mongoTemplate;

	
	/**
	 * Retrieves all books
	 */
	public List<Book> listBookDoNotUse() {
		List<Book> bookList = new ArrayList<Book>();
		logger.debug("Retrieving all books");
		for (User user : getUsers()) {
			bookList.addAll((user.getHeldBooks()));
		}
		return bookList;
	}

	/**
	 * 
	 * @return all users;
	 */
	private List<User> getUsers() {
		logger.debug("Retrieving all users");
		return mongoTemplate.findAll(User.class);
	}
	
	/**
	 * 
	 * @return all books;
	 */
	public List<BookDetails> listBooks() {
		logger.debug("Retrieving all books");
		return  mongoTemplate.findAll(BookDetails.class,BOOK_COLLECTION_NAME);
	}
	
	public BookDetails getBookById(final String serialNumber) throws BookLendingException{
		logger.debug("fetching the book detail by Id");
		Query query = new Query(where("serialNumber").is(serialNumber));
		List<BookDetails> bookDetails =  mongoTemplate.find(query, BookDetails.class);
		if(null!=bookDetails && bookDetails.size() > 0) return bookDetails.get(0);
		else throw new BookLendingException("1003 ", "Book not found " + serialNumber);
		
	}
	
	/**
	 * 
	 * @param bookName
	 * @param author
	 * @param title
	 * @param keyWords
	 * @return
	 * @throws BookLendingException 
	 */
	public List<BookDetails> getBook(final String author,
			final String title, final String keywords) throws BookLendingException {
		logger.debug("retrieving books with filters");
		Criteria filterCriteria = buildBookDetailCriteria(author, title);
		return mongoTemplate.find(new Query(filterCriteria), BookDetails.class);
	}
	
	
	/**
	 * 
	 * @param author
	 * @param title
	 * @return
	 * @throws BookLendingException 
	 */
	private Criteria buildBookDetailCriteria(String author, String title) throws BookLendingException {
		if(!StringUtils.isEmpty(author) && StringUtils.isEmpty(title)){
			return where("author").is(author);
		}else if(StringUtils.isEmpty(author) && !StringUtils.isEmpty(title)){
			return where("title").is(title);
		}
		else if(!StringUtils.isEmpty(author) && !StringUtils.isEmpty(title)){
			return where("title").is(title).andOperator(where("author").is(author));
		}
		else if(StringUtils.isEmpty(author) && StringUtils.isEmpty(title)){
			throw new BookLendingException("1003", "Invalid Input");
		}
		return null;
	}

	/**
	 * 
	 * @param bookName
	 * @param author
	 * @param title
	 * @param keyWords
	 * @return
	 */
	private Criteria buildBookFilterSearch(final String author, final String title, final String keyWords) {
		StringBuilder criteriaBuilder = new StringBuilder();
		boolean criteriaBuilderFirst = false;
		if (!StringUtils.isEmpty("")) {
			criteriaBuilderFirst = true;
			criteriaBuilder .append("where(\"heldBooks.bookName\").is(bookName)");
		}
		if (!StringUtils.isEmpty(author)) {
			if (criteriaBuilderFirst) criteriaBuilder.append(" .orOperator(");
			criteriaBuilder.append("where(\"heldBooks.author\").is(author)");
			if (criteriaBuilderFirst) {
				criteriaBuilder.append(")");
				criteriaBuilderFirst = true;
			}
		}
		if (!StringUtils.isEmpty(title)) {
			if (criteriaBuilderFirst) 	criteriaBuilder.append(" .orOperator(");
			criteriaBuilder.append("where(\"heldBooks.title\").is(title)");
			if (criteriaBuilderFirst) criteriaBuilder.append(")");
		}
		// TODO: look at this later
		/*
		 * if (!StringUtils.isEmpty(keyWords)) { if (criteriaBuilderFirst)
		 * criteriaBuilder.append(" .orOperator(");
		 * criteriaBuilder.append("where(\"keyWords\").is(keyWords)"); if
		 * (criteriaBuilderFirst) criteriaBuilder.append(")"); }
		 */
		return new Criteria(criteriaBuilder.toString());

	}
	
	
	/**
	 *  Get Book details along with the user
	 * @param bookName
	 * @param author
	 * @param title
	 * @param keyWords
	 * @return
	 */
	public List<BookDetails> getBookWithUser(
			final String author, final String title, final String keywords) {
		Query searchBookQuery = new Query(buildBookFilterSearch(author, title, keywords));
		return mongoTemplate.find(searchBookQuery, BookDetails.class);
	}

	/**
	 * * Adds a new book with user exist check
	 * 
	 * @param book
	 * @param user
	 * @return
	 * @throws BookLendingException 
	 */
	// TODO: transactional rollback effect.  refactor the code...
	private boolean addBook(Book book, User user) throws BookLendingException {
		logger.debug("Adding a new book " + book.getBookName());
		try {
			if (null != user && null != book) {
				return insertBookDetails(book, user);
			} else {
				logger.error("user or book is null ");
				return false;
			}
		} catch (Exception ex) {
			logger.error("error inserting the document " + ex.getMessage());
			throw new BookLendingException("1008", "Error inserting the book");
		}
	}

	
	
	/**
	 * 
	 */
	public boolean updateBookDetails(BookDetails book) throws BookLendingException {
		logger.debug("updating a new book" + book.getBookName() + "with id " +  book.getSerialNumber());
		try{ 
			BookDetails existingBookDetails = getBookById(book.getSerialNumber());
			if(null!=existingBookDetails){
				// update the user
				int userDetailsSize = book.getUserDetails().size();
				if(userDetailsSize > 1){
					logger.error("updating a new book" + book.getBookName() + "with id " +  book.getSerialNumber() + " invalid request");
					//throw exception
				}
				else if(userDetailsSize == 1){
					UserDetails userFound = personService.findUser(book.getUserDetails().get(0));
					if(null!=userFound){
						/// see if the user is already present in the book collection
						if(existingBookDetails.getUserDetails().contains(userFound)){
							logger.debug("updating a new book" + book.getBookName() + "with id " +  book.getSerialNumber() + " updating a user");
							mongoTemplate.save(book, BOOK_COLLECTION_NAME);
						}
						else{
							// add the user to the book details
							logger.debug("updating a new book" + book.getBookName() + "with id " +  book.getSerialNumber() + " adding a user");
							List<UserDetails> existingUserDetails = existingBookDetails.getUserDetails();
							logger.debug("existing user details from the book " + existingUserDetails.size());
							existingUserDetails.add(book.getUserDetails().get(0));
							book.setUserDetails(existingUserDetails);
							mongoTemplate.save(book, BOOK_COLLECTION_NAME);
						}
					}
					else{
						// user is not found
						logger.error("updating a new book" + book.getBookName() + "with id " +  book.getSerialNumber() + "user is not found");
					}

				}else{
					logger.error("updating a new book" + book.getBookName() + "with id " +  book.getSerialNumber() + " invalid request");
				}
			}
			else{
				//add a new book
				mongoTemplate.insert(book, BOOK_COLLECTION_NAME);
			}
			return true;
		}catch(Exception ex){
			logger.error("error updating the document " + ex.getMessage());
			throw new BookLendingException("1007", "Error updating the book");
		}
	}
	
	/**
	 * * Adds a new book with user exist check
	 * 
	 * @param book
	 * @param user
	 * @return
	 * @throws BookLendingException 
	 */
	public boolean addBookWithBookDetails(BookDetails bookDetails) throws BookLendingException {
		logger.debug("Adding a new book " + bookDetails.getBookName());
		List<User> userInfo = bookDetails.getUser(bookDetails);
		if(userInfo.size() > 0){
			return addBook(bookDetails.getBook(bookDetails),userInfo.get(0));
			}
		else{
			throw new BookLendingException("1005", "User info is null");
		}
	}
	
	/**
	 * Inserting the books and user -> books
	 * 
	 * @param book
	 * @param user
	 * @return
	 */
	@SuppressWarnings("unused")
	private boolean insertBook(Book book, User user) {
	Query userSearch = new Query(where("userId").is(user.getUserId()));
		List<Book> updateBook = new ArrayList<Book>();
		updateBook.add(book);
		try {
			User userTest = mongoTemplate.findAndModify(userSearch,
					new Update().set("heldBooks", updateBook),
					new FindAndModifyOptions().returnNew(true), User.class);
			
			return (null != userTest ? true : false);
		} catch (Exception ex) {
			logger.error("error in updating insertbooks " + book.getBookName()
					+ user.getFirstName());
			ex.printStackTrace();
		}
		return false;
		
		/*BookDetails tempBook = new BookDetails();
		tempBook.setAuthor("Viay");
		tempBook.setBookName("outlier");
		tempBook.setCategory("");
		tempBook.setSerialNumber("123333");
		tempBook.setTitle("");
		
		UserDetails user1 = new UserDetails();
		user.setDob("01/15/2010");
		user.setFirstName("vijay");
		user.setUserId("1333");
	
		List<UserDetails> userList = new ArrayList<UserDetails>();
		userList.add(user1);
		tempBook.setUserDetails(userList);
		//mongoTemplate.dropCollection(BookDetails.class);
		//mongoTemplate.remove(tempBook, "bookdetails");
		//mongoTemplate.dropCollection(UserDetails.class);
		mongoTemplate.insert(tempBook, "bookdetails");
		
		return true;
		*/
		
	}

	/**
	 * Adding the Book Details with all the users holding Book -Users
	 * 
	 * @param book
	 * @param user
	 * @return
	 * @throws BookLendingException 
	 */
	private boolean insertBookDetails(Book book, User user) throws BookLendingException {
		if (null != book && null != user) {
			BookDetails bookDetails = new BookDetails();

			bookDetails.setAuthor(book.getAuthor());
			bookDetails.setBookName(book.getBookName());
			bookDetails.setSerialNumber(book.getSerialNumber());
			bookDetails.setTitle(book.getTitle());
			bookDetails.setCategory(book.getCategory());

			UserDetails userDetails = new UserDetails();

			userDetails.setDob(user.getDob());
			userDetails.setFirstName(user.getFirstName());
			userDetails.setUserContact(user.getUserContact());

			List<UserDetails> userDetailsList = new ArrayList<UserDetails>();

			userDetailsList.add(userDetails);
			bookDetails.setUserDetails(userDetailsList);
			// try to find the book , if not there, insert else only update the user
			Query bookSearch = new Query(where("serialNumber").is(bookDetails.getSerialNumber()));
			try {
				List<BookDetails> bookDetailsWithFilter = mongoTemplate.find(bookSearch, BookDetails.class);
				if(bookDetailsWithFilter.size() > 0){ 
					// book found. fetch the existing user and  add the new  user
					List<UserDetails> userDetailsListWithBook = bookDetailsWithFilter.get(0).getUserDetails();
					userDetailsListWithBook.add(userDetails);
					BookDetails  bookDetailsUpdated = mongoTemplate.findAndModify(bookSearch,
							new Update().set("userDetails", userDetailsListWithBook),
							new FindAndModifyOptions().returnNew(true), BookDetails.class);
					return (null!=bookDetailsUpdated ? true : false);
				}
				else{
					mongoTemplate.insert(bookDetails, BOOK_COLLECTION_NAME );
					return true;
				} 
			}catch (Exception ex) {
				logger.error(" Error inserting book" + ex.getMessage());
				throw new BookLendingException("1006","Error Inserting Book Details");
			}
		}
		return false;

	}
	
	/****
	 * 
	 */
	@Override
	public boolean deleteBookDetails(BookDetails book)
			throws BookLendingException {
		try{
			mongoTemplate.remove(book, BOOK_COLLECTION_NAME);
			return true;
		}catch(Exception ex){
			logger.error(" Error deleting a new book " + book.getBookName());
			throw new BookLendingException("1007","Error Deleting Book Details");
		}
	}

}
