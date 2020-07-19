package com.book.manager.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.book.manager.dao.BookMapper;
import com.book.manager.dao.UsersMapper;
import com.book.manager.entity.Book;
import com.book.manager.entity.Users;
import com.book.manager.repos.BookRepository;
import com.book.manager.repos.UsersRepository;
import com.book.manager.util.po.BookOut;
import com.book.manager.util.vo.PageIn;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @Description 图书业务类
 * @Date 2020/7/14 16:31
 * @Author by 尘心
 */
@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private BookMapper bookMapper;


    /**
     * 添加用户
     * @param book 图书
     * @return 返回添加的图书
     */
    public Book addBook(Book book) {
        return bookRepository.saveAndFlush(book);
    }

    /**
     * 编辑用户
     * @param book 图书对象
     * @return true or false
     */
    public boolean updateBook(Book book) {
        return bookMapper.updateBook(BeanUtil.beanToMap(book))>0;
    }

    /**
     * 图书详情
     * @param id 主键
     * @return 图书详情
     */
    public BookOut findBookById(Integer id) {
        Optional<Book> optional = bookRepository.findById(id);
        if (optional.isPresent()) {
            Book book = optional.get();
            BookOut out = new BookOut();
            BeanUtil.copyProperties(book,out);
            out.setPublishTime(DateUtil.format(book.getPublishTime(),"yyyy-MM-dd"));
            return out;
        }
        return null;
    }

    /**
     * 删除图书
     * @param id 主键
     * @return true or false
     */
    public void deleteBook(Integer id) {
        bookRepository.deleteById(id);
    }


    /**
     * 图书搜索查询(mybatis 分页)
     * @param pageIn
     * @return
     */
    public PageInfo<BookOut> getBookList(PageIn pageIn) {

        PageHelper.startPage(pageIn.getCurrPage(),pageIn.getPageSize());
        List<Book> list = bookMapper.findBookListByLike(pageIn.getKeyword());
        List<BookOut> bookOuts = new ArrayList<>();
        for (Book book : list) {
            BookOut out = new BookOut();
            BeanUtil.copyProperties(book,out);
            out.setPublishTime(DateUtil.format(book.getPublishTime(),"yyyy-MM-dd"));
            bookOuts.add(out);
        }

        return new PageInfo<>(bookOuts);
    }


}
