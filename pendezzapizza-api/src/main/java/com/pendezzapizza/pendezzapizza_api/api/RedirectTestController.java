package com.pendezzapizza.pendezzapizza_api.api;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/redirect")
public class RedirectTestController {

    @GetMapping(produces = MediaType.TEXT_HTML_VALUE)
    public String captureCode(@RequestParam("code") String code) {
        return """
                <!DOCTYPE html>
                <html>
                  <body>
                    <div id="code">%s</div>
                  </body>
                </html>
                """.formatted(code);
    }

}