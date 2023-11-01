package com.infinite.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.infinite.model.Chart;

@Repository
public interface IChartDAO extends JpaRepository<Chart, Integer>
{

}
