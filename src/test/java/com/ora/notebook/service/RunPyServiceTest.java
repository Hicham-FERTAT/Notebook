package com.ora.notebook.service;

import com.ora.notebook.model.NoteBookException;
import com.ora.notebook.model.PyModel;
import com.ora.notebook.model.Result;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class RunPyServiceTest {

    RunPyService runPyService = new RunPyService();
@Test
    public void runTest(){
        PyModel pyModel = generatePyModel("%python print(1+1)","s1");
        assertEquals(new Result("2").getResult(), runPyService.run(pyModel).getResult());
    }
    @Test
    public void runScriptTest(){
        PyModel pyModel = generatePyModel("%python print(1+1)","s1");
        assertEquals("2", runPyService.runScript("print(1+1);"));
    }
    @Test
    public void runScriptThrowErrorTest() throws NoteBookException {
        PyModel pyModel = generatePyModel("%python print(1+1)","s1");
        try {
            runPyService.runScript("print(1+1;");
            Assert.fail("Should have thrown NoteBookException");
        }catch (Exception e){
            assertTrue(e instanceof NoteBookException);
        }

    }
    @Test
    public void callProcessAndSetStateWithResultTest(){
        PyModel pyModel = generatePyModel("%python print(1+1)","s1");
        assertEquals("2", runPyService.callProcessAndSetState(pyModel,"print(1+1);"));
        assertEquals(null, runPyService.session.get(pyModel.getSessionId()));
    }
    @Test
    public void callProcessAndSetStateWithOutResultTest(){
        PyModel pyModel = generatePyModel("%python a =1+1","s1");
        assertEquals("", runPyService.callProcessAndSetState(pyModel,"a = 1+1;"));
        assertEquals("a = 1+1;", runPyService.session.get(pyModel.getSessionId()));
    }

    @Test
    public void callProcessAndSetStateMultipleUseTest(){
        PyModel pyModel = generatePyModel("%python a =1+1","s1");
        assertEquals("", runPyService.callProcessAndSetState(pyModel,"a = 1+1;"));
        assertEquals("a = 1+1;", runPyService.session.get(pyModel.getSessionId()));
        String script = runPyService.session.get(pyModel.getSessionId()).concat("print(a);");
        assertEquals("2", runPyService.callProcessAndSetState(pyModel,script));
        String script2 = runPyService.session.get(pyModel.getSessionId()).concat("b = a+1;");
        assertEquals("", runPyService.callProcessAndSetState(pyModel,script2));
        assertEquals("3", runPyService.callProcessAndSetState(pyModel,script2.concat("print(b);")));
    }

    private PyModel generatePyModel(String code, String sessionId){
        return new PyModel(code, sessionId);
    }

}