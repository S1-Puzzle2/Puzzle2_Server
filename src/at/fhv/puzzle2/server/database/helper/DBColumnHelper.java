package at.fhv.puzzle2.server.database.helper;

public class DBColumnHelper {
    public static final String ID = "id";

    //Puzzle table columns
    public static final String PUZZLE_NAME = "name";

    //Puzzle-Part table columns
    public static final String PART_BARCODE = "barcode";
    public static final String PART_ORDER = "order";
    public static final String PART_IMAGE = "image";
    public static final String PART_PUZZLE_REF = "puzzleREF";

    //ShowQuestion table columns
    public static final String QUESTION_TEXT = "text";

    //Answer table columns
    public static final String ANSWER_TEXT = "text";
    public static final String ANSWER_IS_CORRECT = "isCorrect";
    public static final String ANSWER_QUESTION_REF = "questionREF";
}
