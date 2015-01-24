package at.fhv.puzzle2.server.database.helper;

public class DBColumnHelper {
    public static final String ID = "id";

    //Puzzle table columns
    public static final String PUZZLE_NAME = "name";

    //Puzzle-Part table columns
    public static final String PART_BARCODE = "barCode";
    public static final String PART_ORDER = "partOrder";
    public static final String PART_IMAGE_DATA = "imageData";
    public static final String PART_PUZZLE_REF = "puzzleREF";

    //Question table columns
    public static final String QUESTION_TEXT = "text";

    //Answer table columns
    public static final String ANSWER_TEXT = "text";
    public static final String ANSWER_IS_CORRECT = "isCorrect";
    public static final String ANSWER_QUESTION_REF = "questionREF";
}
