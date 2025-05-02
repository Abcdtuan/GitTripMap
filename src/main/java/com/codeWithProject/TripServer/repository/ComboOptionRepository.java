package com.codeWithProject.TripServer.repository;

import com.codeWithProject.TripServer.entity.ComboOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ComboOptionRepository extends JpaRepository<ComboOption, Long> {
}
