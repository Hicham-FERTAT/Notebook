package com.ora.notebook.service;

import com.ora.notebook.constant.NotebookConst;
import com.ora.notebook.model.NoteBookException;
import com.ora.notebook.model.PyModel;
import com.ora.notebook.model.Result;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.util.*;


@Service
public class RunPyService {

    @Autowired
    protected Environment env;
    public RunPyService(){}
    private final Logger log = LoggerFactory.getLogger(RunPyService.class);
   static Map<String, String> session = new HashMap<>();

    public Result run(PyModel model) {
        log.info("Runing python in progress ...");
        String executionResult = null;

        String[] codeSplit = model.getCode().split(NotebookConst.WHITE_SPACE, 2);
        if(codeSplit.length<= 1)  throw new NoteBookException("Bad request format");
        switch (codeSplit[0]) {
            case NotebookConst.PYTHON_INTERPRETER:
                executionResult = saveStateAndExecute(model, codeSplit);
                break;
            default:
                throw new NoteBookException("Unknown interpreter!");
        }

        return new Result(executionResult);
    }

    private String saveStateAndExecute(PyModel model, String[] codeSplit) {
        String scriptToExecute;
        String executionResult;
        if (StringUtils.isBlank(session.get(model.getSessionId()))) {
            scriptToExecute = codeSplit[1].concat(";").concat(NotebookConst.WHITE_SPACE);
            executionResult = callProcessAndSetState(model, scriptToExecute);

        } else {
            scriptToExecute = session.get(model.getSessionId()).concat(NotebookConst.WHITE_SPACE).concat(codeSplit[1]).concat(NotebookConst.SEMICOLON);
            executionResult = callProcessAndSetState(model, scriptToExecute);

        }
        return executionResult;
    }

    protected String callProcessAndSetState(PyModel model, String scriptToExecute) {
        String executionResult;
        executionResult = runScript(scriptToExecute);
        if (StringUtils.isBlank(executionResult))
            session.put(model.getSessionId(), scriptToExecute);
        return executionResult;
    }

    protected String runScript(String script) {
        StringBuilder write = new StringBuilder();
        StringBuilder writeError = new StringBuilder();
        Process process = null;
        try {
            log.info("Execute python script: " + script);
            String command = "cmd.exe /c python -c \"" + script + "\"";
            process = Runtime.getRuntime().exec(command);
            InputStream outputError = process.getErrorStream();
            BufferedReader bufError = new BufferedReader(new InputStreamReader(outputError));

            readResult(writeError, bufError);
            if(StringUtils.isBlank(writeError.toString())) {
                InputStream output = process.getInputStream();
                BufferedReader buf = new BufferedReader(new InputStreamReader(output));
                readResult(write, buf);
            }else{
                throw new NoteBookException();
            }

        } catch (Exception e) {
            log.error("Process encounters some errors" + writeError.toString());
            throw new NoteBookException(writeError.toString());
        }

        return write.toString();
    }

    private void readResult(StringBuilder write, BufferedReader buf) throws IOException {
        String line;
        while ((line = buf.readLine()) != null) {
            write.append(line);
        }
    }


}
