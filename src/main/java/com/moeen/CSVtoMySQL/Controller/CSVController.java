package com.moeen.CSVtoMySQL.Controller;

import com.moeen.CSVtoMySQL.CSVService;
import com.moeen.CSVtoMySQL.Entity.DeveloperTutorial;
import com.moeen.CSVtoMySQL.Helper.CSVHelper;
import com.moeen.CSVtoMySQL.Response.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;
import java.util.Optional;

@CrossOrigin("http://localhost:8080")
@RestController
@RequestMapping("/api/csv")

public class CSVController {
    @Autowired
    CSVService fileService;

    @PostMapping("/upload")
    public ResponseEntity<ResponseMessage> uploadFile(@RequestParam("file") MultipartFile file) {
        String message = "";

        if (CSVHelper.hasCSVFormat(file)) {
            try {
                fileService.save(file);

                message = "Uploaded the file successfully: " + file.getOriginalFilename();

                String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                        .path("/api/csv/download/")
                        .path(file.getOriginalFilename())
                        .toUriString();

                return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message,fileDownloadUri));
            } catch (Exception e) {
                message = "Could not upload the file: " + file.getOriginalFilename() + "!";
                return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message,""));
            }
        }

        message = "Please upload a csv file!";
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(message,""));
    }

    @GetMapping("/tutorials")
    public ResponseEntity<List<DeveloperTutorial>> getAllTutorials() {
        try {
            List<DeveloperTutorial> tutorials = fileService.getAllTutorials();

            if (tutorials.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(tutorials, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/{id}")
    public Optional<DeveloperTutorial> getTutorialByID(@PathVariable("id") Long id)
    {
        return fileService.getTutorialById(id);
    }
    @PutMapping("/{id}")
    public ResponseEntity<String> updateTutorial(@RequestBody DeveloperTutorial tutorial,@PathVariable("id") Long id)
    { fileService.updateTable(tutorial);
        return new ResponseEntity<String>("Updated",HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteDataById(@PathVariable Long id)
    {
        fileService.deleteTutorialDataById(id);
        return new ResponseEntity<String>("Deleted",HttpStatus.OK);
    }
//<<<<-------Custom------>
    @GetMapping("/publisher")
    public ResponseEntity<List<DeveloperTutorial>> getPublisher()
    {
        List<DeveloperTutorial> tutorials = fileService.getAllTutorials();
        return new ResponseEntity<>(tutorials,HttpStatus.OK);
    }
    @GetMapping("/download/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName) {
        InputStreamResource file = new InputStreamResource(fileService.load());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
                .contentType(MediaType.parseMediaType("application/csv"))
                .body(file);
    }

}





