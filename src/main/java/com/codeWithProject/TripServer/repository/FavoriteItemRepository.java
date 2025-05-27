package com.codeWithProject.TripServer.repository;

import com.codeWithProject.TripServer.entity.FavoriteItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FavoriteItemRepository extends JpaRepository<FavoriteItem, Long> {
}
