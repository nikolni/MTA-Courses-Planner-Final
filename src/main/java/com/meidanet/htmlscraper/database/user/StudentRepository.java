package com.meidanet.htmlscraper.database.user;

import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long >
{
}
