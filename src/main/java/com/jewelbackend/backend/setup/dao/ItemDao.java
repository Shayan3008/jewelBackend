package com.jewelbackend.backend.setup.dao;

import com.jewelbackend.backend.setup.models.Category;
import com.jewelbackend.backend.setup.models.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ItemDao extends CrudRepository<Item, Integer>, PagingAndSortingRepository<Item, Integer> {

    @Query(value = "select item from Item item where item.category = :category and item.remainingNetWeight is not null and " +
            "item.remainingNetWeight > 0")
    List<Item> findByCategory(Category category);

    @Query("SELECT cat  FROM Item cat WHERE UPPER(cat.category.categoryName) LIKE UPPER(:name) OR UPPER(cat.metalType.metalName) " +
            "LIKE UPPER(:name) OR UPPER(cat.karigar.karigarName) LIKE UPPER(:name)")
    Page<Item> findByCategoryNameOrKarigarNameOrMetalType(String name,PageRequest page);


    @Query(value = "SELECT cat.netWeight FROM Item cat WHERE cat.category = :category")
    List<BigDecimal> getTotalWeightByCategory(Category category);

    @Query(value = "SELECT cat FROM Item cat WHERE cat.category = :category And cat.netWeight > 0")
    List<Item> getAllItemsByCategory(Category category);

    @Query(value = "SELECT item from Item item where item.designNo is not null and  item.itemImagePath is null and " +
            " item.id >= :from and item.id <= :to")
    List<Item> getAllItemWhoHaveImages(Integer from,Integer to);

    @Query(value = "SELECT item from Item item where item.designNo is not null and  item.itemImagePath is null ")
    List<Item> getAllItemWhoHaveImages();

    @Query(value = "SELECT item.id from Item item where item.designNo is not null and item.itemImagePath is null")
    Page<Integer> getFirstIdWithImage(Pageable pageable);

}
