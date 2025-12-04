package com.pendezzapizza.pendezzapizza_api.core.validation.productPhoto;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.util.unit.DataSize;
import org.springframework.web.multipart.MultipartFile;

public class FileSizeValidator implements ConstraintValidator<FileSize , MultipartFile> {

    private DataSize maxSize ;

    @Override
    public void initialize(FileSize constraintAnnotation) {
        this.maxSize =  DataSize.parse(constraintAnnotation.max());
    }

    @Override
    public boolean isValid(MultipartFile multipartFile, ConstraintValidatorContext constraintValidatorContext) {
//        Não validamos arquivos nulos (não é nossa obrigação e não faz sentido medir o tamanho de algo nulo) e avaliamos o tamanho máximo passado pelo parâmetro
        return multipartFile == null || multipartFile.getSize() <= this.maxSize.toBytes() ;
    }
}
