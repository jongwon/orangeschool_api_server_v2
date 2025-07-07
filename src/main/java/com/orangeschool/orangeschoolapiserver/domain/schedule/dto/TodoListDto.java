package com.orangeschool.orangeschoolapiserver.domain.schedule.dto;


import com.orangeschool.orangeschoolapiserver.common.dto.response.CommonDto;
import com.orangeschool.orangeschoolapiserver.domain.schedule.entity.TodoList;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TodoListDto extends CommonDto {

    private String todoList;

    public static TodoListDto create(TodoList todoList) {

        TodoListDto todoListDto = TodoListDto.builder()
                .todoList(todoList.getTodoList())
                .build();

        todoListDto.setCreatedAt(todoList.getCreatedAt());
        todoListDto.setUpdatedAt(todoList.getUpdatedAt());
        todoListDto.setId(todoList.getId());

        return todoListDto;
    }
}
