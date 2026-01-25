package com.pendezzapizza.pendezzapizza_api.core.validation.productphoto;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

public class FileTypeValidator implements ConstraintValidator<FileType , MultipartFile> {

    private List<String> allowedContentTypes;

    @Override
    public void initialize(FileType constraint) {
        this.allowedContentTypes = Arrays.asList(constraint.allowed());
    }

    @Override
    public boolean isValid(MultipartFile multipartFile, ConstraintValidatorContext context) {
        return multipartFile == null
                || this.allowedContentTypes.contains(multipartFile.getContentType());
    }
}

