package com.orangeschool.schedule.schedule.repository;


import com.orangeschool.schedule.schedule.entity.TodoList;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface TodoListRepository extends PagingAndSortingRepository<TodoList, Long> {

    Optional<TodoList> findByDayDateAndCommonMemberId(LocalDate dayDate, Long commonMemberId);
}
