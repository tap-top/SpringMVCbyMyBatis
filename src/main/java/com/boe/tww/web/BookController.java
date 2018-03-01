package com.boe.tww.web;

import com.boe.tww.dto.AppointExecution;
import com.boe.tww.dto.Result;
import com.boe.tww.entity.Book;
import com.boe.tww.enums.AppointStateEnum;
import com.boe.tww.exception.NoNumberException;
import com.boe.tww.exception.RepeatAppointException;
import com.boe.tww.service.BookService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
public class BookController {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private BookService bookService;

//	使用@ResponseBody返回json数据
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public @ResponseBody  List<Book> list() {
		List<Book> list = bookService.getList();
		return list;
	}
	//	使用@ResponseBody返回json数据
	@RequestMapping(value = "/word", method = RequestMethod.GET)//, produces={"text/html;charset=UTF-8;","application/json;"}
	public @ResponseBody  String word() {
		return "我是中文";
	}
// 没写@ResponseBody 跳转到视图或者链接
	@RequestMapping(value = "/{bookId}/detail", method = RequestMethod.GET)
	private String detail(@PathVariable("bookId") Long bookId, Model model) {
		if (bookId == 1) {
			return "redirect:/list";
		}
		if (bookId == 2) {
			System.out.println("我是中文");
			System.out.println("aaa");
			return "redirect:/list";
		}
		if (bookId == 3) {
			return "redirect:/word";
		}
		Book book = bookService.getById(bookId);
		if (book == null) {
			return "forward:/list";
		}
		model.addAttribute("book", book);
		return "detail";
	}

	// ajax json
	@RequestMapping(value = "/{bookId}/appoint", method = RequestMethod.POST, produces = {
			"application/json; charset=utf-8" })
	@ResponseBody
	private Result<AppointExecution> appoint(@PathVariable("bookId") Long bookId, @RequestParam("studentId") Long studentId) {
		if (studentId == null || studentId.equals("")) {
			return new Result<>(false, "学号不能为空");
		}
		AppointExecution execution = null;
		try {
			execution = bookService.appoint(bookId, studentId);
		} catch (NoNumberException e1) {
			execution = new AppointExecution(bookId, AppointStateEnum.NO_NUMBER);
		} catch (RepeatAppointException e2) {
			execution = new AppointExecution(bookId, AppointStateEnum.REPEAT_APPOINT);
		} catch (Exception e) {
			execution = new AppointExecution(bookId, AppointStateEnum.INNER_ERROR);
		}
		return new Result<AppointExecution>(true, execution);
	}

}
