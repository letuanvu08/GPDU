package hcmut.thesis.gpduserver.service;

import hcmut.thesis.gpduserver.models.Exception.ValidationException;
import hcmut.thesis.gpduserver.models.entity.Storage;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.stream.Stream;

public interface StorageService {
    void init() throws Exception;

    void store(MultipartFile file) throws ValidationException, Exception;

    Stream<Path> loadAll() throws Exception;

    Path load(String filename);

    Resource loadAsResource(String filename) throws Exception;

    Storage getStorage();

    void deleteAll();
}
