package uqbc;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import uqbc.Student;

import static uqbc.UQBC.*;

/**
 * @author Benoît Mignault
 * @author Marc Giroux
 * @author Dominique Perron
 * @author Alexis Cloutier
 */
public class UQBCTest {
    
    public String pathToCorrectCourses;
    public String pathTocorrectEvaluations;
    public String pathToFileThatDoNotExist;
    public String pathToFileThatExist;
    
    public Student correctStudent;
    public Student incorrectStudent1;
    public Student incorrectStudent2;
    public Student incorrectStudent3;
    
    public File[] correctStudentsCourseFile;
    public File[] incorrectStudentsCourseFile1;
    public File[] incorrectStudentsCourseFile2;
    public File[] incorrectStudentsCourseFile3;
    public File[] incorrectStudentsCourseFile4;
    public File[] incorrectStudentsCourseFile5;
    
    @Before
    public void setUp() throws Exception {
        pathToCorrectCourses = "fileJson\\listeDesCoursUQBC";
        pathTocorrectEvaluations = "fileJson\\listeDesEvaluationsUQBC";
        pathToFileThatDoNotExist = "test\\fileJson\\thisFileShouldNotExistForTheTest.txt";
        pathToFileThatExist = "test\\fileJson\\thisFileShouldExistForTheTest.txt";
        
        correctStudent = new Student("GIRM04108403", "Giroux", "Marc", "INF2015");
        incorrectStudent1 = new Student("123", "Leponge", "Bob", "BIO1111");
        incorrectStudent2 = new Student("ABC12345",null,"Arnold","INF2120");
        incorrectStudent3 = new Student("MIGB12345","Mignault",null,null);
        
        correctStudentsCourseFile = getAllFiles("test\\fileJson\\correctStudentsCourseFile");
        incorrectStudentsCourseFile1 = getAllFiles("test\\fileJson\\incorrectStudentsCourseFile1");
        incorrectStudentsCourseFile2 = getAllFiles("test\\fileJson\\incorrectStudentsCourseFile2");
        incorrectStudentsCourseFile3 = getAllFiles("test\\fileJson\\incorrectStudentsCourseFile3");
        incorrectStudentsCourseFile4 = getAllFiles("test\\fileJson\\incorrectStudentsCourseFile4");
        incorrectStudentsCourseFile5 = getAllFiles("test\\fileJson\\incorrectStudentsCourseFile5");
    }
    
    @After
    public void tearDown() {
        pathToCorrectCourses = null;
        pathTocorrectEvaluations = null;
        pathToFileThatDoNotExist = null;
        pathToFileThatExist = null;
        
        correctStudent = null;
        incorrectStudent1 = null;
        incorrectStudent2 = null;
        incorrectStudent3 = null;
        
        correctStudentsCourseFile = null;
        incorrectStudentsCourseFile1 = null;
        incorrectStudentsCourseFile2 = null;
        incorrectStudentsCourseFile3 = null;
        incorrectStudentsCourseFile4 = null;
        incorrectStudentsCourseFile5 = null;
    }    
    
    @Test
    public void testRegroupAllStudents() {
//        assertTrue("La liste n'a pas l'étudiant avec des informations valides!",Arrays.equals(null, regroupAllStudents(correctStudentsCourseFile)));
//        assertFalse("La liste contient un étudiant invalide! un code permanent de 3 caractères!",students.contains(incorrectStudent1));
//        assertFalse("La liste contient un étudiant invalide! aucun nom de famille!",students.contains(incorrectStudent2));
//        assertFalse("La liste contient un étudiant invalide! aucun prénom et nom de cours!",students.contains(incorrectStudent3));
    }    

    @Test 
    public void testFindMedianNombreElementPair() {
        ArrayList<Float> listOfScore = new ArrayList<> ();
        listOfScore.add(1f);
        listOfScore.add(5f);
        listOfScore.add(6f);
        listOfScore.add(22f);
        assertEquals(5.5f, UQBC.findMedian(listOfScore),0);
    }
    
    @Test 
    public void testFindMedianNombreElementImPair() {
        ArrayList<Float> listOfScore = new ArrayList<> ();
        listOfScore.add(1f);
        listOfScore.add(5f);
        listOfScore.add(22f);
        assertEquals(5f, UQBC.findMedian(listOfScore),0);
    }
    
    @Test
    public void testConvertStringToFileCreateNewFile() throws IOException {
        String stringInsideFile = "abcééé";
        Path pathToFile = Paths.get(pathToFileThatDoNotExist);
        Files.deleteIfExists(pathToFile);
        File newFile = new File(pathToFileThatDoNotExist);
        assertFalse(newFile.exists());
        UQBC.convertStringToFile(stringInsideFile, pathToFileThatDoNotExist);
        assertTrue(newFile.exists());
        assertEquals(stringInsideFile, UQBC.stringFromFile(pathToFileThatDoNotExist));
        Files.deleteIfExists(pathToFile);
    }
    
    @Test
    public void testConvertStringToFileOverwriteExistingFile() throws IOException {
        String stringFirstWrite = "abcééé";
        String stringSecondWrite = "ééééé11234555";
        Path pathToFile = Paths.get(pathToFileThatExist);
        File newFile = new File(pathToFileThatExist);
        UQBC.convertStringToFile(stringFirstWrite, pathToFileThatExist);
        assertTrue(newFile.exists());
        UQBC.convertStringToFile(stringSecondWrite, pathToFileThatExist);
        assertTrue(newFile.exists());
        assertEquals(stringSecondWrite, UQBC.stringFromFile(pathToFileThatExist));
    }
    
    @Test
    public void testGetAllFilesGoodNumberOfFiles() {
        assertEquals(2, getAllFiles(pathToCorrectCourses).length);
        assertEquals(9, getAllFiles(pathTocorrectEvaluations).length);
    }
    
    @Test
    public void testGetAllFilesInvalidPathReturnNull() {
        Assert.assertArrayEquals(null, getAllFiles("thisIsNotAValidPath"));
    }
    
    @Test
    public void testFileToJSONValid() {
        File fichierJson = new File("test\\fileJson\\testfileToJSON.txt");
        JSONObject objetJson = UQBC.fileToJSON(fichierJson);
        assertEquals(456, objetJson.getInt("nombreEntier"));
        assertEquals("pauvre ti bébé", objetJson.getString("chainDeCaractere"));
        JSONObject sousObjetJson = objetJson.getJSONObject("objet");
        assertEquals("oui", sousObjetJson.getString("propriete"));
        JSONArray liste = objetJson.getJSONArray("liste");
        assertEquals(1,liste.getInt(0));
        assertEquals(2,liste.getInt(1));
        assertEquals(3,liste.getInt(2));
    }
    
    @Test
    public void testFileToJSONException() {
        File fichierJson = new File("test\\fileJson\\testStringToFileContent.txt");
        try {
            JSONObject objetJson = UQBC.fileToJSON(fichierJson);
        } catch (JSONException e) {
            assertTrue(true);
            return;
        }
            assertTrue(false);
    }
    @Test
    public void testStringFromFile() {
        assertEquals("", UQBC.stringFromFile("test\\fileJson\\testStringToFileEmpty.txt")); 
        assertEquals("qweqweaasdasdasd", UQBC.stringFromFile("test\\fileJson\\testStringToFileContent.txt")); 
        assertEquals("", UQBC.stringFromFile("fileThatDoesNotExists")); 
    }
    
    @Test
    public void testConvertStringToFloatRemovePercentileSign(){
        float fiftyPErcent = UQBC.convertStringToFloat("50 %");
        assertEquals(0.50f, fiftyPErcent, 0.0f);
    }
    
    @Test
    public void testExtractNameFromFileListeEtudiant() {
        assertEquals("BIO1012-Gr10-H18", UQBC.extractNameFromFile("ListeEtudiantsCours", ".json", "fileJson\\listeDesCoursUQBC\\ListeEtudiantsCoursBIO1012-Gr10-H18.json"));
    }
    
    @Test
    public void testExtractNameFromFileEvaluation() {
        assertEquals("1BIO1012-Gr10-H18", UQBC.extractNameFromFile("evaluation", ".json", "fileJson\\listeDesEvaluationsUQBC\\evaluation1BIO1012-Gr10-H18.json"));
    }
    
    @Test
    public void testExtractNameFromFileException() {
        try {
            UQBC.extractNameFromFile(null, null,"invalidFileName");
        } catch (NullPointerException e) {
            assertTrue(true);
            return;
        }
        assertTrue(false);
    }
    
   
    

}
