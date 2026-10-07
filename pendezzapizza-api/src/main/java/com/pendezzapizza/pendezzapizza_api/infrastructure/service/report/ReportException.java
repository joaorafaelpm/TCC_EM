package com.pendezzapizza.pendezzapizza_api.infrastructure.service.report;

import java.io.Serial;

/**
 * Excessão específica para erro na formação do PDF
 */
public class ReportException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    public ReportException(String message) {
        super(message);
    }
    public ReportException(String message , Throwable cause) {
        super(message , cause);
    }
}
