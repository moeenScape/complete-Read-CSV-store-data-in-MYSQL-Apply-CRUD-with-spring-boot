package com.moeen.CSVtoMySQL.Repository;

import com.moeen.CSVtoMySQL.Entity.DeveloperTutorial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeveloperTutorialRepository extends JpaRepository<DeveloperTutorial,Long> {
    @Query(value = "select *from developer_table where published=1",nativeQuery = true)
    public List<DeveloperTutorial> getCustomTutorial();
    @Query("SELECT d from DeveloperTutorial d where d.title LIKE '%:n%'")
    public List<DeveloperTutorial> searchName(@Param("n") String name);
}
