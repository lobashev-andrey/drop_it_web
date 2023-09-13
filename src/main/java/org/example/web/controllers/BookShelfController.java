package org.example.web.controllers;

import org.apache.log4j.Logger;
import org.example.app.exceptions.FileIsEmptyException;
import org.example.app.services.BookService;
import org.example.web.dto.Book;
import org.example.web.dto.BookIdToRemove;
import org.example.web.dto.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
@RequestMapping(value = "/books")
@Scope("singleton")
public class BookShelfController {

    private Logger logger = Logger.getLogger(BookShelfController.class);
    private BookService bookService;

    @Autowired
    public BookShelfController(BookService bookService) {
        this.bookService = bookService;
    }


    @GetMapping("/shelf")
    public String books(Model model){
        logger.info("got book shelf");
        model.addAttribute("book", new Book());
        model.addAttribute("bookIdToRemove", new BookIdToRemove());
        model.addAttribute("bookList", bookService.getAllBooks());
        model.addAttribute("removeNote", new Notification(""));
        model.addAttribute("emptyFileNote", new Notification(""));
        return "book_shelf";
    }

    @PostMapping("/save")
    public String saveBook(@Valid Book book, BindingResult bindingResult, Model model){
        if(bindingResult.hasErrors()){
            model.addAttribute("book", book);
            model.addAttribute("bookIdToRemove", new BookIdToRemove());
            model.addAttribute("bookList", bookService.getAllBooks());
            model.addAttribute("removeNote", new Notification(""));
            model.addAttribute("emptyFileNote", new Notification(""));
            return "book_shelf";
        } else {
            bookService.saveBook(book);
            logger.info("current repository size: " + bookService.getAllBooks().size());
            return "redirect:/books/shelf";
        }
    }

    @PostMapping("/remove")
    public String removeBook(@Valid BookIdToRemove bookIdToRemove, BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("book", new Book());
            model.addAttribute("bookIdToRemove", new BookIdToRemove());
            model.addAttribute("bookList", bookService.getAllBooks());
            model.addAttribute("removeNote", new Notification("enter book id (positive digit to 7 signs)"));
            model.addAttribute("emptyFileNote", new Notification(""));
            System.out.println("11111111111111111");
            return "book_shelf";
        }
        if (bookService.removeBookById(bookIdToRemove.getId())){
            System.out.println("222222222222222222");
             return "redirect:/books/shelf";
        } else {
            model.addAttribute("book", new Book());
            model.addAttribute("bookIdToRemove", new BookIdToRemove());
            model.addAttribute("bookList", bookService.getAllBooks());
            model.addAttribute("removeNote", new Notification("id not found"));
            model.addAttribute("emptyFileNote", new Notification(""));
            System.out.println("33333333333333333333333333");
            return "book_shelf";
        }
    }

    @PostMapping("/removeByRegex")
    public String removeByRegex(@RequestParam(value = "queryRegex") String queryRegex){
        Pattern p = Pattern.compile("[A-Za-z'-]+[A-Za-z' -]*|[0-9]{1,4}");
        Matcher m = p.matcher(queryRegex);
        if(queryRegex.equals("") || !m.replaceAll("").equals("")){
            System.out.println( "return: regex_fail_page");
            return "regex_fail_page";
        }

        bookService.removeBookByRegex(queryRegex);
        return "redirect:/books/shelf";
    }

    @PostMapping("/uploadFile")
    public String uploadFile(@RequestParam("file") MultipartFile file) throws Exception {
        if(file.isEmpty()){
            throw new FileIsEmptyException("you chose no one file");
        }
        String name = file.getOriginalFilename(); // имя с расширением
        byte[] bytes = file.getBytes();  // сохраняем сам файл в виде байтов
        // теперь создаем - программно -
        // каталог в домашней директории сервера на случай если его нет или он был удален
        String rootPath = System.getProperty("catalina.home"); // задаем путь до папки сервера
        File dir = new File(rootPath + File.separator + "external_uploads"); // имя папки - путь + имя
        if(!dir.exists()){
            dir.mkdirs(); // если каталог не существует, он будет создан
        }
        // создаем файлы, которые понадобятся
        File servletFile = new File(dir.getAbsolutePath() + File.separator + name);
        BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(servletFile));
        stream.write(bytes);
        stream.close();  // ВАЖНО (или использовать try-catch-with-resources)

        logger.info("new file saved at: " + servletFile.getAbsolutePath());

        return "redirect:/books/shelf";
    }

    @ExceptionHandler(FileIsEmptyException.class)
    public String emptyFileErrorHandler(Model model, FileIsEmptyException exception){

        System.out.println("@EXCEPTION HANDLER emptyFileErrorHandler");

        model.addAttribute("book", new Book());
        model.addAttribute("bookIdToRemove", new BookIdToRemove());
        model.addAttribute("bookList", bookService.getAllBooks());
        model.addAttribute("removeNote", new Notification(""));
        model.addAttribute("emptyFileNote", new Notification(exception.getMessage()));
        return "book_shelf";
    }



}
