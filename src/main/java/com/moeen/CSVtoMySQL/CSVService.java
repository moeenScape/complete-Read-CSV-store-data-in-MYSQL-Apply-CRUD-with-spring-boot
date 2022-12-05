package com.moeen.CSVtoMySQL;

import com.moeen.CSVtoMySQL.Entity.DeveloperTutorial;
import com.moeen.CSVtoMySQL.Helper.CSVHelper;
import com.moeen.CSVtoMySQL.Repository.DeveloperTutorialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class CSVService {
    @Autowired
    DeveloperTutorialRepository repository;

    public void save(MultipartFile file) {
        try {
            List<DeveloperTutorial> tutorials = CSVHelper.csvToTutorials(file.getInputStream());
            repository.saveAll(tutorials);
        } catch (IOException e) {
            throw new RuntimeException("fail to store csv data: " + e.getMessage());
        }
    }

    public ByteArrayInputStream load() {
        List<DeveloperTutorial> tutorials = repository.findAll();

        ByteArrayInputStream in = CSVHelper.tutorialsToCSV(tutorials);
        return in;
    }

    public List<DeveloperTutorial> getAllTutorials() {
        return repository.findAll();
    }
    public Optional<DeveloperTutorial> getTutorialById(Long id){
        return repository.findById(id);
    }
    public void updateTable(DeveloperTutorial tutorial)
    {
        //DeveloperTutorial Tutorial;
        Optional<DeveloperTutorial> newTutorial=repository.findById(tutorial.getId());

        if (newTutorial.isPresent())
        {
            DeveloperTutorial Tutorial=newTutorial.get();
            Tutorial.setTitle(tutorial.getTitle());
            Tutorial.setDescription(tutorial.getDescription());
            Tutorial.setPublished(tutorial.isPublished());
            repository.save(Tutorial);
        }
    }
    public void deleteTutorialDataById(Long id)
    {
        repository.deleteById(id);

    }
    public List<DeveloperTutorial> customQueryForPublishedTutorial()
    {
        return repository.getCustomTutorial();
    }

    public List<DeveloperTutorial> searchByName(String title)
    {
        return repository.searchName(title);
    }

}
