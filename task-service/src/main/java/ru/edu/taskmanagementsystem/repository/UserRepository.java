package ru.edu.taskmanagementsystem.repository;

import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import ru.edu.taskmanagementsystem.model.User;

@Repository
public interface UserRepository extends JpaRepository<@NonNull User, @NonNull Long>,
        PagingAndSortingRepository<@NonNull User, @NonNull Long> {

}