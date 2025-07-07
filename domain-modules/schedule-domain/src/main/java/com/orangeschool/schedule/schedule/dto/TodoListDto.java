package com.orangeschool.schedule.schedule.dto;


import com.orangeschool.common.dto.response.CommonDto;
import com.orangeschool.schedule.schedule.entity.TodoList;
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
