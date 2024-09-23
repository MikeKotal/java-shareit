package ru.practicum.shareit.item.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {

    List<Item> findAllByOwnerId(Long ownerId);

    @Query("""
            select it
            from Item as it
            join it.owner as ow
            where it.isAvailable = true
            and (it.name ilike %?1%
            or it.description ilike %?1%)
            """)
    List<Item> findItemsByText(String text);
}
