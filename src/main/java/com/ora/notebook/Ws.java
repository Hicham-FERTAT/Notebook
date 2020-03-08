package com.ora.notebook;

import com.ora.notebook.model.PyModel;
import com.ora.notebook.model.Result;
import com.ora.notebook.service.RunPyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class Ws {

    @Autowired
    private RunPyService service;
    private final Logger log = LoggerFactory.getLogger(Ws.class);
    @RequestMapping("/execute")
    public Result ex(@RequestBody PyModel py){
        log.info("The request body: "+py.toString());
        return service.run(py);
    }
}
