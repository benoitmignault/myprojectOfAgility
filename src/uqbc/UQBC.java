package uqbc;

import java.io.BufferedOutputStream;
import java.util.ArrayList;
import java.io.IOException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.OutputStream;
import static java.lang.Math.pow;
import static java.lang.Math.sqrt;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.Collections;
import org.json.*;
import java.util.Date;

/**
 * @author Benoît Mignault
 * @author Marc Giroux
 * @author Dominique Perron
 * @author Alexis Cloutier
 */
public class UQBC {

    private static final String PATH_TO_LISTE_DES_COURS = "fileJson\\listeDesCoursUQBC";
    private static final String PATH_TO_LISTE_DES_EVALUATIONS = "fileJson\\listeDesEvaluationsUQBC";
    private static final String PATH_TO_GROUP_COURS_SUMMARY = "fileTxtBySummaryByEvaluation/";
    private static final String PATH_TO_GROUP_COURS_SUMMARY_JSON = "fileJsonByGroupes-Cours/";
    private static final String PATH_TO_SUMMARY_BY_STUDENT = "fileTxtByStudent/";
    private static final String PATH_TO_GROUP_COURS_FAILURE = "fileTxtByGroupes-CoursWithFailure/";
    private static final String INTRODUCTION_TO_FILE_GROUP_COURS = "Voici le sommaire pour le groupe-cours suivant : ";
    private static final String INTRODUCTION_TO_FILE_GROUP_COURS_FAILURE = "Voici le sommaire pour le groupe-cours avec mention d'échec suivant : ";
    private static final String MOYENNE_GROUPE = " Moyenne du groupe -> ";
    private static final String PONDERATION = " Pondération -> ";
    private static final String REMUSE_COURS = "Résumé du cours : ";
    private static final String PRESENTATION_VALUES_STATS = "Pour l'ensemble du groupe :";
    private static final String NOM_STAT_MOYENNE = "Moyenne -> ";
    private static final String NOM_STAT_MODE = " Mode -> ";
    private static final String WARNING_FAILURE_MESSAGE = "Cette étudiant(e) est actuellement en situation d'échec mais il reste encore des évaluations à passer !";
    private static final String FINAL_FAILURE_MESSAGE = "Cette étudiant(e) est en situation d'échec final pour le cours !";
    private static final String NO_FAILLURE_MESSAGE = "Félicitation ! Aucun de vos étudiants(e) sont actuellement en échec !";
    private static final String NOM_STAT_MEDIANE = " Médiane -> ";
    private static final String NOM_STAT_ECARTTYPE = " Écart-type -> ";
    private static final String NOM_STAT_NBETUDIENTS = " Nombre d'étudiant(s) -> ";
    private static final String NOM_ETUDIANT = "Nom de l'étudiant(e) -> ";
    private static final String CODE_ETUDIANT = "Code permanant de l'étudiant(e) -> ";
    private static final String SUMMARY_TO_FILE_GROUP_COURS = "Voici le sommaire pour -> ";
    private static final String SEPARATION_BETWEEN_SUMMARY = "_______________________________________________________________________________________________";
    private static final String RESULTAT_REMUSE_ETUDIANT = "Voici le résultat à ce jour d'une évaluation jusqu'à la totalité (100% de la score globale) :";
    private static final String NOTE = "La note -> ";
    private static final String NOTE_ON_100 = " La note converti sur 100 -> ";
    private static final String MOYENNE = " La moyenne du groupe -> ";
    private static final String FORMAT = "##.00";
    private static final String ERROR_MESSAGE_READING_FILE_FORMAT = "Erreur de lecture dans le fichier %s (%s)%n";
    private static final String LISTE_ETUDIANTS_COURS = "ListeEtudiantsCours";
    private static final String EVALUATION = "evaluation";
    private static final String FILE_EXTENSION = ".json";
    private static final String FILE_EXTENSION_TXT = ".txt";
    private static final String FILE_JSON_COURSE_NAME = "Nom du cours";
    private static final String FILE_JSON_RESULT_SCORE = "Résultat";
    private static final String FILE_LISTETUDIANTSCOURS_FIELD_DONNEES = "donnees";
    private static final String FILE_LISTETUDIANTSCOURS_FIELD_DONNEES_CODE_PERMANENT = "code_permanent";
    private static final String FILE_LISTETUDIANTSCOURS_FIELD_DONNEES_NOM = "nom";
    private static final String FILE_LISTETUDIANTSCOURS_FIELD_DONNEES_PRENOM = "prenom";
    private static final String FILE_EVALUATION_FIELD_TYPE = "type";
    private static final String FILE_EVALUATION_FIELD_NOM_EVALUATION = "nom_evaluation";
    private static final String FILE_EVALUATION_FIELD_PONDERATION = "ponderation";
    private static final String FILE_EVALUATION_FIELD_DONNES = "donnees";
    private static final String FILE_EVALUATION_FIELD_DONNEES_CODE_PERMANENT = "code_permanent";
    private static final String FILE_EVALUATION_FIELD_DONNEES_NOTE = "note";
    private static final String FILE_EVALUATION_FIELD_DONNEES_NOTE_FINAL = "note finale";

    public static void main(String[] args) throws FileNotFoundException, IOException {
        File[] coursesFile = getAllFiles(PATH_TO_LISTE_DES_COURS);
        File[] evaluationsFile = getAllFiles(PATH_TO_LISTE_DES_EVALUATIONS);

        ArrayList<GroupCourse> courses = regroupAllCourses(coursesFile);
        ArrayList<Student> studentDetailss = regroupAllStudents(coursesFile);
        ArrayList<Evaluation> evaluations = regroupAllEvaluations(evaluationsFile, studentDetailss);
        ArrayList<SummaryByEvaluation> summaryByEvaluation = regroupSummaryEvaluation(evaluationsFile);
        ArrayList<AllEvaluationsByStudent> evaluationByStudent = regroupAllEvaluationByStudent(evaluations, summaryByEvaluation);
        ArrayList<SchoolFailure> studentDetailsFailure = regroupAllStudentFailure(evaluationByStudent);

        createFileTxtByStudent(evaluationByStudent, summaryByEvaluation);
        createFileJsonByGroupCours(evaluationByStudent, courses);
        createFileTxtByGroupCoursWithSummary(courses, summaryByEvaluation, evaluations);
        createFileTxtByGroupCoursWithFailure(courses, studentDetailsFailure);
    }

    public static ArrayList<GroupCourse> regroupAllCourses(File[] files) {
        ArrayList<GroupCourse> courses = new ArrayList<>();
        for (File file : files) {
            try {
                String courseName = extractNameFromFile(LISTE_ETUDIANTS_COURS, FILE_EXTENSION, file.toPath().toString());
                GroupCourse oneCourse = new GroupCourse(courseName);
                JSONObject jsonObject = fileToJSON(file);
                JSONArray courseStudents = jsonObject.getJSONArray(FILE_LISTETUDIANTSCOURS_FIELD_DONNEES);
                for (int i = 0; i < courseStudents.length(); i++) {
                    try {
                        JSONObject courseStudent = courseStudents.getJSONObject(i);
                        String studentId = courseStudent.get(FILE_LISTETUDIANTSCOURS_FIELD_DONNEES_CODE_PERMANENT).toString();
                        oneCourse.addIdStudent(studentId);
                    } catch (Exception x) {
                        System.err.printf(ERROR_MESSAGE_READING_FILE_FORMAT, file, x.toString());
                    }
                }
                courses.add(oneCourse);
            } catch (Exception x) {
                System.err.printf(ERROR_MESSAGE_READING_FILE_FORMAT, file, x.toString());
            }
        }
        return courses;
    }

    public static ArrayList<Student> regroupAllStudents(File[] files) {
        ArrayList<Student> allStudents = new ArrayList<>();
        for (File file : files) {
            try {
                String courseName = extractNameFromFile(LISTE_ETUDIANTS_COURS, FILE_EXTENSION, file.toPath().toString());
                JSONObject jsonObject = fileToJSON(file);
                JSONArray courseStudents = jsonObject.getJSONArray(FILE_LISTETUDIANTSCOURS_FIELD_DONNEES);
                for (int i = 0; i < courseStudents.length(); i++) {
                    try {
                        JSONObject courseStudent = courseStudents.getJSONObject(i);
                        String studentId = courseStudent.get(FILE_LISTETUDIANTSCOURS_FIELD_DONNEES_CODE_PERMANENT).toString();
                        String studentLastName = courseStudent.get(FILE_LISTETUDIANTSCOURS_FIELD_DONNEES_NOM).toString();
                        String studentFirstName = courseStudent.get(FILE_LISTETUDIANTSCOURS_FIELD_DONNEES_PRENOM).toString();
                        allStudents.add(new Student(studentId, studentLastName, studentFirstName, courseName));
                    } catch (Exception x) {
                        System.err.printf(ERROR_MESSAGE_READING_FILE_FORMAT, file, x.toString());
                    }
                }
            } catch (Exception x) {
                System.err.printf(ERROR_MESSAGE_READING_FILE_FORMAT, file, x.toString());
            }
        }
        return allStudents;
    }

    public static ArrayList<Evaluation> regroupAllEvaluations(File[] files, ArrayList<Student> allStudents) {
        ArrayList<Evaluation> listeOfEvaluations = new ArrayList<>();
        for (File file : files) {
            try {
                JSONObject jsonObject = fileToJSON(file);
                String evaluationType = jsonObject.get(FILE_EVALUATION_FIELD_TYPE).toString();
                String evaluationName = jsonObject.get(FILE_EVALUATION_FIELD_NOM_EVALUATION).toString();
                String evaluationNumber = extractNameFromFile(EVALUATION, FILE_EXTENSION, file.toPath().toString());
                float evaluationValue = convertStringToFloat(jsonObject.get(FILE_EVALUATION_FIELD_PONDERATION).toString());
                JSONArray evaluationStudents = jsonObject.getJSONArray(FILE_EVALUATION_FIELD_DONNES);
                for (int i = 0; i < evaluationStudents.length(); i++) {
                    JSONObject evaluationStudent = evaluationStudents.getJSONObject(i);
                    try {
                        for (int j = 0; j < allStudents.size(); j++) {
                            if (evaluationStudent.get(FILE_EVALUATION_FIELD_DONNEES_CODE_PERMANENT).equals(allStudents.get(j).id)) {
                                Student studentFound = allStudents.get(j);
                                int studentScore = evaluationStudent.getInt(FILE_EVALUATION_FIELD_DONNEES_NOTE);
                                Evaluation evaluation = new Evaluation(studentFound, evaluationType, evaluationName, evaluationValue, studentScore, evaluationNumber);
                                listeOfEvaluations.add(evaluation);
                            }
                        }
                    } catch (Exception x) {
                        System.err.printf(ERROR_MESSAGE_READING_FILE_FORMAT, file, x.toString());
                    }
                }
            } catch (Exception x) {
                System.err.printf(ERROR_MESSAGE_READING_FILE_FORMAT, file, x.toString());
            }
        }
        return listeOfEvaluations;
    }

    public static ArrayList<SummaryByEvaluation> regroupSummaryEvaluation(File[] files) {
        ArrayList<SummaryByEvaluation> summaryByEvaluation = new ArrayList<>();
        for (File file : files) {
            try {
                JSONObject jsonObject = fileToJSON(file);
                String nameOfEvaluation = extractNameFromFile(EVALUATION, FILE_EXTENSION, file.toPath().toString());
                String typeOfEvaluation = jsonObject.get(FILE_EVALUATION_FIELD_NOM_EVALUATION).toString();
                String nameOfGroupCourse = nameOfEvaluation.substring(1);
                float evaluationValue = convertStringToFloat(jsonObject.get(FILE_EVALUATION_FIELD_PONDERATION).toString());
                JSONArray evaluationStudents = jsonObject.getJSONArray(FILE_EVALUATION_FIELD_DONNES);
                int numberOfValidStudents = 0;
                float resultSummary = 0;
                ArrayList<Float> scoreList = new ArrayList<>();
                SummaryByEvaluation summary = new SummaryByEvaluation(nameOfEvaluation, nameOfGroupCourse, typeOfEvaluation, evaluationValue);
                for (int i = 0; i < evaluationStudents.length(); i++) {
                    try {
                        JSONObject evaluationStudent = evaluationStudents.getJSONObject(i);
                        String evaluationStudentId = evaluationStudent.get(FILE_EVALUATION_FIELD_DONNEES_CODE_PERMANENT).toString();
                        float evaluationStudentScore = evaluationStudent.getFloat(FILE_EVALUATION_FIELD_DONNEES_NOTE);
                        scoreList.add(evaluationStudentScore);
                        resultSummary += evaluationStudentScore;
                        numberOfValidStudents++;
                        summary.addStudentEvaluation(evaluationStudentId);
                    } catch (Exception x) {
                        System.err.printf(ERROR_MESSAGE_READING_FILE_FORMAT, file, x.toString());
                    }
                }
                float average = resultSummary / (float) numberOfValidStudents;
                summary.addEvaluationAverage(average);
                summary.addNumberOfValidStudents(numberOfValidStudents);
                Collections.sort(scoreList);
                float ecartType = findEcarType(scoreList, numberOfValidStudents, average);
                summary.addEcartType(ecartType);
                float mode = findMode(scoreList);
                summary.addMode(mode);

                float median = findMedian(scoreList);
                summary.addMedian(median);

                summaryByEvaluation.add(summary);
            } catch (Exception x) {
                System.err.printf(ERROR_MESSAGE_READING_FILE_FORMAT, file, x.toString());
            }
        }
        return summaryByEvaluation;
    }

    public static ArrayList<AllEvaluationsByStudent> regroupAllEvaluationByStudent(ArrayList<Evaluation> evaluations, ArrayList<SummaryByEvaluation> summaryByEvaluation) {
        ArrayList<AllEvaluationsByStudent> evaluationByStudent = new ArrayList<>();
        for (int i = 0; i < evaluations.size(); i++) {
            float evaluationValue = evaluations.get(i).value;
            float scoreOfEvaluation = evaluations.get(i).score;
            float groupAverage = 0.0f;
            for (int k = 0; k < summaryByEvaluation.size(); k++) {
                if (evaluations.get(i).courseName.equals(summaryByEvaluation.get(k).evaluationName)) {
                    groupAverage = summaryByEvaluation.get(k).average;
                }
            }
            if (evaluationByStudent.isEmpty()) {
                AllEvaluationsByStudent firstStudent = new AllEvaluationsByStudent(evaluations.get(i).student, evaluations.get(i), evaluationValue, scoreOfEvaluation, groupAverage);
                evaluationByStudent.add(firstStudent);
            } else {
                boolean HasFoundThisStudent = false;
                for (int j = 0; j < evaluationByStudent.size(); j++) {
                    if (evaluations.get(i).student.id.equals(evaluationByStudent.get(j).student.id)) {
                        evaluationByStudent.get(j).addStudentEvaluation(evaluations.get(i));
                        evaluationByStudent.get(j).addEvaluation(evaluationValue);
                        evaluationByStudent.get(j).addCumulativeScore(scoreOfEvaluation);
                        evaluationByStudent.get(j).addCumulativeGroupScore(groupAverage);
                        HasFoundThisStudent = true;
                    }
                }
                if (!HasFoundThisStudent) {
                    AllEvaluationsByStudent studentDetails = new AllEvaluationsByStudent(evaluations.get(i).student, evaluations.get(i), evaluationValue, scoreOfEvaluation, groupAverage);
                    evaluationByStudent.add(studentDetails);
                }
            }
        }
        return evaluationByStudent;
    }

    public static ArrayList<SchoolFailure> regroupAllStudentFailure(ArrayList<AllEvaluationsByStudent> evaluationByStudent) {
        ArrayList<SchoolFailure> studentDetailsFailure = new ArrayList<>();
        for (AllEvaluationsByStudent student : evaluationByStudent) {
            float convertValueOn100 = student.cumulativeValue * 100;
            if (convertValueOn100 == 100.0 && student.cumulativeScore < 60) {
                SchoolFailure studentFailure = new SchoolFailure(student.student, student.cumulativeScore, convertValueOn100);
                studentDetailsFailure.add(studentFailure);
            } else if (convertValueOn100 < 100.0 && ((student.cumulativeScore / convertValueOn100) < 0.60)) {
                float convertScoreOn100 = (student.cumulativeScore / convertValueOn100) * 100;
                SchoolFailure studentFailure = new SchoolFailure(student.student, student.cumulativeScore, convertValueOn100, convertScoreOn100);
                studentDetailsFailure.add(studentFailure);
            }
        }
        return studentDetailsFailure;
    }

    /**
     * Cette methode calcule l'ecart type dans une liste triée
     *
     * @param listOfScore Cette liste doit absolument être triée en ordre
     * croissant ou décroissant
     * @param numberOfValidStudents
     * @param average
     * @return
     */
    public static float findEcarType(ArrayList<Float> listOfScore, int numberOfValidStudents, float average) {
        double newSum = 0.0;
        for (int i = 0; i < listOfScore.size(); i++) {
            double newValue;
            newValue = pow((listOfScore.get(i) - average), 2);
            newSum += newValue;
        }
        return (float) sqrt(newSum / (numberOfValidStudents));
    }

    /**
     * Cette méthode calcule la médiane dans une liste triée
     *
     * @param listOfScore Cette liste doit absolument être triée en ordre
     * croissant ou décroissant
     * @return
     */
    public static float findMedian(ArrayList<Float> listOfScore) {
        float halfIndex = (listOfScore.size() - 1) / 2;
        float median = 0;

        if (listOfScore.size() % 2 == 0) {
            int x = (int) Math.floor(halfIndex);
            int y = x + 1;
            median = (listOfScore.get(y) + listOfScore.get(x)) / 2;
        } else {
            median = listOfScore.get((int) halfIndex);
        }

        return median;
    }

    /**
     * Cette méthode calcule le mode dans une liste triée
     *
     * @param listOfScore Cette liste doit absolument être triée en ordre
     * croissant ou décroissant
     * @return
     */
    public static float findMode(ArrayList<Float> listOfScore) {
        float maxValue = 0.0f;
        int maxCount = 0;

        for (int i = 0; i < listOfScore.size(); i++) {
            int count = 0;
            float valueOne = listOfScore.get(i);
            for (int j = 0; j < listOfScore.size(); j++) {
                float valueTwo = listOfScore.get(j);
                int isComparable = Float.compare(valueOne, valueTwo);
                if (isComparable == 0) {
                    count++;
                }
            }
            if (count > maxCount) {
                maxCount = count;
                maxValue = listOfScore.get(i);
            }
        }
        return maxValue;
    }

    public static void createFileTxtByStudent(ArrayList<AllEvaluationsByStudent> evaluationByStudent, ArrayList<SummaryByEvaluation> summaryByEvaluation) throws IOException {
        String date = getDateFormat();
        for (AllEvaluationsByStudent summaryByStudent : evaluationByStudent) {
            DecimalFormat format = new DecimalFormat(FORMAT);
            String studentDetailsId = summaryByStudent.student.id;
            String filename = PATH_TO_SUMMARY_BY_STUDENT + studentDetailsId + "_" + date + FILE_EXTENSION_TXT;
            String studentInformations = "";
            studentInformations += REMUSE_COURS + summaryByStudent.student.courseName + System.lineSeparator();
            studentInformations += NOM_ETUDIANT + summaryByStudent.student.firstName + " " + summaryByStudent.student.lastName + System.lineSeparator();
            studentInformations += CODE_ETUDIANT + summaryByStudent.student.id + System.lineSeparator();
            for (Evaluation evaluation : summaryByStudent.evaluationsByStudent) {
                String evaluationName = evaluation.name;
                String courseName = evaluation.courseName;
                studentInformations += evaluationName + " -> " + evaluation.score;
                for (SummaryByEvaluation summary : summaryByEvaluation) {
                    if (courseName.equals(summary.evaluationName)) {
                        int value = (int) (summary.value * 100);
                        studentInformations += MOYENNE_GROUPE + format.format(summary.average) + PONDERATION + value + System.lineSeparator();
                        break;
                    }
                }
            }
            int value = (int) (summaryByStudent.cumulativeValue * 100);
            studentInformations += System.lineSeparator() + RESULTAT_REMUSE_ETUDIANT + System.lineSeparator() + NOTE + format.format(summaryByStudent.cumulativeScore) + MOYENNE + format.format(summaryByStudent.groupAverage) + PONDERATION + value;
            convertStringToFile(studentInformations, filename);
        }
    }

    public static void createFileJsonByGroupCours(ArrayList<AllEvaluationsByStudent> evaluationByStudent, ArrayList<GroupCourse> courses) throws IOException {
        String date = getDateFormat();
        for (GroupCourse course : courses) {
            String filename = PATH_TO_GROUP_COURS_SUMMARY_JSON + course.courseName + "_" + date + FILE_EXTENSION;
            JSONObject newJsonFile = new JSONObject();
            JSONArray resultByStudent = new JSONArray();
            newJsonFile.put(FILE_JSON_COURSE_NAME, course.courseName);
            for (int i = 0; i < course.listOfStudents.size(); i++) {
                for (AllEvaluationsByStudent student : evaluationByStudent) {
                    if (course.listOfStudents.get(i).equals(student.student.id) && student.cumulativeValue == 1.0f) {
                        JSONObject finalScoreByStudent = new JSONObject();
                        finalScoreByStudent.put(FILE_LISTETUDIANTSCOURS_FIELD_DONNEES_CODE_PERMANENT, course.listOfStudents.get(i));
                        finalScoreByStudent.put(FILE_EVALUATION_FIELD_DONNEES_NOTE_FINAL, student.cumulativeScore);
                        resultByStudent.put(finalScoreByStudent);
                    }
                }
            }
            newJsonFile.put(FILE_JSON_RESULT_SCORE, resultByStudent);
            String jsonToString = newJsonFile.toString();
            convertStringToFile(jsonToString, filename);
        }
    }

    public static void createFileTxtByGroupCoursWithSummary(ArrayList<GroupCourse> courses, ArrayList<SummaryByEvaluation> summaryByEvaluation, ArrayList<Evaluation> evaluations) throws IOException {
        String date = getDateFormat();
        DecimalFormat format = new DecimalFormat(FORMAT);
        for (GroupCourse course : courses) {
            String filename = PATH_TO_GROUP_COURS_SUMMARY + course.courseName + "_" + date + FILE_EXTENSION_TXT;
            String summaryByCourseWithAllEvaluation = "";
            summaryByCourseWithAllEvaluation += INTRODUCTION_TO_FILE_GROUP_COURS + course.courseName + System.lineSeparator() + System.lineSeparator();
            if (summaryByEvaluation.isEmpty()) {
                summaryByCourseWithAllEvaluation += NO_FAILLURE_MESSAGE;
            } else {
                for (SummaryByEvaluation summary : summaryByEvaluation) {
                    if (course.courseName.equals(summary.groupCourseName)) {
                        summaryByCourseWithAllEvaluation += SUMMARY_TO_FILE_GROUP_COURS + summary.evaluationType + System.lineSeparator() + System.lineSeparator();
                        for (Evaluation evaluation : evaluations) {
                            if (summary.evaluationName.equals(evaluation.courseName)) {
                                summaryByCourseWithAllEvaluation += evaluation.student.firstName + " " + evaluation.student.lastName + " -> " + evaluation.score + System.lineSeparator();
                            }
                        }
                        summaryByCourseWithAllEvaluation += System.lineSeparator();
                        summaryByCourseWithAllEvaluation += PRESENTATION_VALUES_STATS + System.lineSeparator();
                        summaryByCourseWithAllEvaluation += NOM_STAT_MOYENNE + format.format(summary.average) + NOM_STAT_MODE + format.format(summary.mode) + NOM_STAT_MEDIANE + format.format(summary.median) + NOM_STAT_ECARTTYPE + format.format(summary.ecartType) + NOM_STAT_NBETUDIENTS + summary.validStudents + System.lineSeparator();
                        summaryByCourseWithAllEvaluation += SEPARATION_BETWEEN_SUMMARY + System.lineSeparator() + System.lineSeparator();
                    }
                }
            }
            convertStringToFile(summaryByCourseWithAllEvaluation, filename);
        }
    }

    public static void createFileTxtByGroupCoursWithFailure(ArrayList<GroupCourse> courses, ArrayList<SchoolFailure> studentDetailsFailure) throws IOException {
        String date = getDateFormat();
        DecimalFormat format = new DecimalFormat(FORMAT);
        for (GroupCourse course : courses) {
            String filename = PATH_TO_GROUP_COURS_FAILURE + course.courseName + "_" + date + FILE_EXTENSION_TXT;
            String summaryBycourseswithFailure = "";
            summaryBycourseswithFailure += INTRODUCTION_TO_FILE_GROUP_COURS_FAILURE + course.courseName + System.lineSeparator() + System.lineSeparator();
            if (studentDetailsFailure.isEmpty()) {
                summaryBycourseswithFailure += NO_FAILLURE_MESSAGE + System.lineSeparator();
            } else {
                int numberStudentFailure = 0;
                for (SchoolFailure student : studentDetailsFailure) {
                    if (course.courseName.equals(student.student.courseName)) {
                        numberStudentFailure++;
                        summaryBycourseswithFailure += CODE_ETUDIANT + student.student.id + System.lineSeparator() + System.lineSeparator();
                        summaryBycourseswithFailure += NOM_ETUDIANT + student.student.firstName + " " + student.student.lastName + System.lineSeparator() + System.lineSeparator();
                        if (student.cumulativeValue != 100.0) {
                            summaryBycourseswithFailure += WARNING_FAILURE_MESSAGE + System.lineSeparator() + System.lineSeparator();
                            summaryBycourseswithFailure += NOTE + student.cumulativeScore + PONDERATION + format.format(student.cumulativeValue) + NOTE_ON_100 + format.format(student.convertScoreOn100) + System.lineSeparator() + System.lineSeparator();
                            summaryBycourseswithFailure += SEPARATION_BETWEEN_SUMMARY + System.lineSeparator() + System.lineSeparator();
                        } else if (student.cumulativeValue == 100.0) {
                            summaryBycourseswithFailure += FINAL_FAILURE_MESSAGE + System.lineSeparator() + System.lineSeparator();
                            summaryBycourseswithFailure += NOTE + student.cumulativeScore + PONDERATION + student.cumulativeValue + System.lineSeparator() + System.lineSeparator();
                            summaryBycourseswithFailure += SEPARATION_BETWEEN_SUMMARY + System.lineSeparator() + System.lineSeparator();
                        }
                    }
                }
                if (numberStudentFailure == 0){
                    summaryBycourseswithFailure += NO_FAILLURE_MESSAGE + System.lineSeparator();
                }
            }
            convertStringToFile(summaryBycourseswithFailure, filename);
        }
    }

    public static String getDateFormat() {
        Date date = new Date();
        return date.toString().replaceAll(":", "-");
    }

    public static void convertStringToFile(String studentInformations, String filename) {
        byte studentDetails[] = studentInformations.getBytes(StandardCharsets.UTF_8);
        Path p = Paths.get(filename);

        try (OutputStream fileWriter = new BufferedOutputStream(Files.newOutputStream(p))) {
            fileWriter.write(studentDetails, 0, studentDetails.length);
        } catch (IOException x) {
            System.err.println(x);
        }
    }

    public static File[] getAllFiles(String path) {
        File directory = new File(path);
        File[] files = directory.listFiles();
        return files;
    }

    public static JSONObject fileToJSON(File file) {
        Path filePath = file.toPath();
        String pathName = filePath.toString();
        String fileContents = stringFromFile(pathName);
        JSONObject jsonObject = new JSONObject(fileContents);
        return jsonObject;
    }

    @SuppressWarnings("CallToPrintStackTrace")
    public static String stringFromFile(String PathToFile) {
        String fileContent = "";
        try {
            fileContent = new String(Files.readAllBytes(Paths.get(PathToFile)), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileContent;
    }

    public static float convertStringToFloat(String allowance) {
        return Float.parseFloat(allowance.replace("%", "")) / 100.00f;
    }

    /**
     * Cette méthode ne valide pas les paramètres d'entrées et ne gère pas les
     * exceptions
     *
     * @param afterFirstWord
     * @param beforeSecondWord
     * @param filePath
     * @return
     */
    public static String extractNameFromFile(String afterFirstWord, String beforeSecondWord, String filePath) {
        return filePath.split(afterFirstWord)[1].split(beforeSecondWord)[0];
    }
}
