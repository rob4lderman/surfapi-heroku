package com.surfapi.javadoc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.junit.Rule;
import org.junit.Test;

import com.surfapi.junit.CaptureSystemOutRule;

/**
 * 
 */
public class JavadocProcessTest {

    /**
     * Capture and suppress stdout unless the test fails.
     */
    @Rule
    public CaptureSystemOutRule systemOutRule  = new CaptureSystemOutRule( );

    /**
     * 
     */
    @Test
    public void testBuildCommand2() throws Exception {
        
        String pwd = System.getProperty("user.dir");
        
        File baseDir = new File("src/test/java/com/surfapi/test");
        JavadocProcess javadocProcess = new JavadocProcess(baseDir);
        
        List<String> expectedCommand = new ArrayList<String>( Arrays.asList( new String[] { "javadoc", 
                                                                     "-docletpath",
                                                                     javadocProcess.getDocletPath(),
                                                                     "-doclet",
                                                                     JsonDoclet.class.getCanonicalName(),
                                                                     "-quiet" } ) );
        
        List<String> javaFileNames = FileSystemUtils.listJavaFileNames(baseDir);
        expectedCommand.addAll(javaFileNames);
        
        assertEquals(expectedCommand, javadocProcess.buildCommand2(javaFileNames));
    }
	
    /**
     * 
     */
    @Test
    public void testRemoveWarnings() throws Exception {
        
        List<String> list = Arrays.asList("hello",
                                          "23 warnings",
                                          "no warnings",
                                          "warnings",
                                          "0 warnings",
                                          "1 warning",
                                          "good bye");
        
        List<String> list2 = new JavadocProcess(null).removeWarnings(list);
        
        assertFalse(list2.contains("23 warnings"));
        assertFalse(list2.contains("0 warnings"));
        assertFalse(list2.contains("1 warning"));
        assertTrue(list2.contains("hello"));
        assertTrue(list2.contains("warnings"));
        assertTrue(list2.contains("no warnings"));
    }
    
 
    
    /**
     * 
     */
    // @Test
    public void testLotsOfFiles() throws Exception {
        
        // new JavadocProcessForTesting(new File("/fox/tmp/javadoc/jdk6.src")).run( new FileOutputStream("testLotsOfFiles.out") );
        
        processDir( new File("/fox/tmp/javadoc/jdk6.src/jdk/src/share/classes/java") );
        
    }

    
    /**
     * 
     */
    @Test
    public void testMultipleDirsCollect() throws Exception {

        Pair<List<String>,List<String>> out = new JavadocProcessForTesting( new File("src/main") ).run();
        
        // IOUtils.writeLines(out.getLeft(), "\n", new FileOutputStream("target/testMultipleDirsCollect.out"));
        
        // Verify at a minimum that it can be successfully parsed 
        JSONArray doc = (JSONArray) new JSONParser().parse( "[" + StringUtils.join( out.getLeft(), "" ) + "]" );
    }
    
    /**
     * 
     */
    @Test
    public void testMultipleDirsStream() throws Exception {

        File file = new File("target/testMultipleDirsStream.out");
        FileOutputStream outputStream = new FileOutputStream(file);
        
        IOUtils.write("[\n",outputStream,"ISO-8859-1");
        new JavadocProcessForTesting( new File("src/main") ).run(outputStream);
        IOUtils.write("]\n",outputStream,"ISO-8859-1");
        
        // Verify at a minimum that it can be successfully parsed 
        JSONArray doc = (JSONArray) new JSONParser().parse( new FileReader(file) );
    }

    
    private void processDir(File baseDir) throws Exception {
        new JavadocProcessForTesting(baseDir).run( new FileOutputStream("testLotsOfFiles.out") );
        
        // make sure it parses
        JSONArray objs = (JSONArray) new JSONParser().parse(new FileReader("testLotsOfFiles.out") );
        assertFalse(objs.isEmpty());
    }

}