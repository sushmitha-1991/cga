package com.dsp.cga.assurance.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dsp.cga.assurance.entity.Departments;

@Repository

public interface DepartmentRepository extends JpaRepository<Departments, Long> {

}
