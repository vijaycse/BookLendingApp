package com.book.lending.library.web;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.MatrixVariable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.book.lending.library.exception.BookLendingException;
import com.book.lending.library.exception.ErrorInfo;
import com.book.lending.library.model.BookDetails;
import com.book.lending.library.service.BookService;

/**
 * Handles requests for the application home page.
 */
@Controller
@RequestMapping("/books/*")
public class BookServiceController {
	
	 @Autowired
	 private BookService bookServiceRepository;
	
	private static final Logger logger = LoggerFactory.getLogger(BookServiceController.class);
	
	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/home", method = RequestMethod.GET)
	public String home(Locale locale, Model model) {
		logger.info("Welcome home! The client locale is {}.", locale);
		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);
		String formattedDate = dateFormat.format(date);
		model.addAttribute("serverTime", formattedDate );
		return "home";
	}
	
	/*@RequestMapping(value="books", method=RequestMethod.GET)
	public @ResponseBody String returnAllBooks() {
		return "here is the list of all books";
	}*/

	@RequestMapping(value="/book/{id}", method=RequestMethod.GET)
	public @ResponseBody BookDetails returnBookDetailsByID(@PathVariable String id) throws BookLendingException {
		if(!StringUtils.isEmpty(id))
			return bookServiceRepository.getBookById(id);
		else{
			throw new BookLendingException("1001","Invalid Input");
		}
	}
	
	@RequestMapping(value="/", method=RequestMethod.GET)
	public @ResponseBody List<BookDetails> returnBookDetailsFilterBy(
			@RequestParam(value="author",	required=false)String author,
			@RequestParam(value="title", 	required=false)String title,
			@RequestParam(value="keyword",  required=false)String keyword) throws BookLendingException {
		List<BookDetails> bookLists=null;
		if(!StringUtils.isEmpty(author) || !StringUtils.isEmpty(title) || !StringUtils.isEmpty(keyword) ){
			bookLists = bookServiceRepository.getBook(author, title, keyword);
			if(bookLists.size() > 0) return bookLists;
			else throw new BookLendingException("1000","No Books found");
		}
		else{
			bookLists =  bookServiceRepository.listBooks();
			//bookServiceRepository.addBook(new Book(), new User());
			if(null!=bookLists && bookLists.size() > 0) return bookLists;
			else throw new BookLendingException("1000","No Books found");
		}

	}
	
	@RequestMapping(value="path/{var}", method=RequestMethod.GET)
	public @ResponseBody String withPathVariable(@PathVariable String var) {
		return "Obtained 'var' path variable value '" + var + "'";
	}

	@RequestMapping(value="{path}/simple", method=RequestMethod.GET)
	public @ResponseBody String withMatrixVariable(@PathVariable String path, @MatrixVariable String foo) {
		return "Obtained matrix variable 'foo=" + foo + "' from path segment '" + path + "'";
	}

	@RequestMapping(value="{path1}/{path2}", method=RequestMethod.GET)
	public @ResponseBody String withMatrixVariablesMultiple (
			@PathVariable String path1, @MatrixVariable(value="foo", pathVar="path1") String foo1,
			@PathVariable String path2, @MatrixVariable(value="foo", pathVar="path2") String foo2) {

		return "Obtained matrix variable foo=" + foo1 + " from path segment '" + path1
				+ "' and variable 'foo=" + foo2 + " from path segment '" + path2 + "'";
	}

	@RequestMapping(value="header", method=RequestMethod.GET)
	public @ResponseBody String withHeader(@RequestHeader String Accept) {
		return "Obtained 'Accept' header '" + Accept + "'";
	}

	@RequestMapping(value="cookie", method=RequestMethod.GET)
	public @ResponseBody String withCookie(@CookieValue String openid_provider) {
		return "Obtained 'openid_provider' cookie '" + openid_provider + "'";
	}

	@RequestMapping(value="book", method=RequestMethod.POST,headers = "Accept=application/json")
	public @ResponseBody ResponseEntity<String> createBook(@RequestBody BookDetails  body) throws BookLendingException {
		boolean result =  bookServiceRepository.addBookWithBookDetails(body);
		if(result){
			return new ResponseEntity<String>("Book is created",HttpStatus.CREATED);
		}else{
			throw new BookLendingException("1004", "Book is not created");
		}
	}
	
	
	@RequestMapping(value="book", method=RequestMethod.PUT,headers = "Accept=application/json")
	public @ResponseBody ResponseEntity<String> updateBook(@RequestBody BookDetails  book) throws BookLendingException {
		if(StringUtils.hasText(book.getId())) {
			boolean result =  bookServiceRepository.updateBookDetails(book);
			if(result){
				return new ResponseEntity<String>("Book is updated",HttpStatus.OK);
			}else{
				throw new BookLendingException("1004", "Book is not updated");
			}
		}else{
			throw new BookLendingException("1001", "Invalid Book");
		}
	}
	
	@RequestMapping(value="book", method=RequestMethod.DELETE,headers = "Accept=application/json")
	public @ResponseBody ResponseEntity<String> withEntity(@RequestBody BookDetails  book) throws BookLendingException {
		boolean result=false;
		if(null!=book){
			result =  bookServiceRepository.deleteBookDetails(book);
			if(result){
				return new ResponseEntity<String>("Book is deleted",HttpStatus.OK);
			}else{
				throw new BookLendingException("1003", "Book is not deleted");
			}
		}
		else{
			throw new BookLendingException("1001", "Invalid Book");
		}
	}
	
	@RequestMapping("/entity/status")
	public ResponseEntity<String> responseEntityStatusCode() {
		return new ResponseEntity<String>("The String ResponseBody with custom status code (403 Forbidden)",
				HttpStatus.FORBIDDEN);
	}

	@RequestMapping("/entity/headers")
	public ResponseEntity<String> responseEntityCustomHeaders() {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.TEXT_PLAIN);
		return new ResponseEntity<String>("The String ResponseBody with custom header Content-Type=text/plain",
				headers, HttpStatus.OK);
	}
	
	@ExceptionHandler(BookLendingException.class)
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public @ResponseBody ErrorInfo handleException(BookLendingException e) {
	    //return e.getExceptionId() + " " + e.getExceptionMessage();
		return new ErrorInfo(e.getExceptionId(), e.getExceptionMessage());
	}
	
}
