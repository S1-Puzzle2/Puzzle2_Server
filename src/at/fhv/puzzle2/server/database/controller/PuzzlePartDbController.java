package at.fhv.puzzle2.server.database.controller;

import at.fhv.puzzle2.server.database.DatabaseConnection;
import at.fhv.puzzle2.server.database.UncheckedSQLException;
import at.fhv.puzzle2.server.database.helper.DBColumnHelper;
import at.fhv.puzzle2.server.database.helper.DbTableHelper;
import at.fhv.puzzle2.server.entity.Puzzle;
import at.fhv.puzzle2.server.entity.PuzzlePart;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class PuzzlePartDbController extends DbController {
    public PuzzlePartDbController(DatabaseConnection connection) {
        super(connection);
    }

    public List<PuzzlePart> getPuzzlePartsByPuzzle(Puzzle puzzle) {
        String query = "SELECT * FROM " + DbTableHelper.PUZZLE_PART_TABLE +
                " WHERE " + DBColumnHelper.PART_PUZZLE_REF + " = " + puzzle.getID();

        ResultSet result = _connection.executeQuery(query);

        List<PuzzlePart> partList = new LinkedList<>();
        try {
            while(result.next()) {
                PuzzlePart part = parsePart(result);

                partList.add(part);
            }
        } catch (SQLException e) {
            throw new UncheckedSQLException(e);
        }

        return partList;
    }

    public void persistPuzzlePart(PuzzlePart part, Puzzle puzzle) {
        String query = "INSERT INTO " + DbTableHelper.PUZZLE_PART_TABLE +
                " (" + DBColumnHelper.PART_BARCODE + ", " + DBColumnHelper.PART_ORDER + ", " +
                DBColumnHelper.PART_PUZZLE_REF + ", " + DBColumnHelper.PART_IMAGE_DATA + ") " +
                "VALUES (?, ?, ?, ?)";

        try {
            PreparedStatement statement = _connection.createPreparedStatement(query, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, part.getBarcode());
            statement.setInt(2, part.getOrder());
            statement.setInt(3, puzzle.getID());
            statement.setBinaryStream(4, part.getImageInputStream());

            part.setID(statement.executeUpdate());
        } catch(SQLException e) {
            throw new UncheckedSQLException(e);
        }
    }

    public Optional<PuzzlePart> getPuzzlePartByID(Integer id) {
        String query = "SELECT * FROM  " + DbTableHelper.PUZZLE_PART_TABLE + " WHERE " + DBColumnHelper.ID + " = " + id;

        ResultSet result = _connection.executeQuery(query);
        try {
            if(result.next()) {
                return Optional.of(parsePart(result));
            }
        } catch (SQLException e) {
            throw new UncheckedSQLException(e);
        }

        return Optional.empty();
    }

    private PuzzlePart parsePart(ResultSet result) {
        try {
            PuzzlePart part = new PuzzlePart(result.getInt(DBColumnHelper.ID),
                    result.getString(DBColumnHelper.PART_BARCODE),
                    result.getInt(DBColumnHelper.PART_ORDER));

            InputStream imageStream = result.getBinaryStream(DBColumnHelper.PART_IMAGE_DATA);
            ByteArrayOutputStream byteArrayStream = new ByteArrayOutputStream();

            //Read the BLOB from the result
            int b;
            try {
                while((b = imageStream.read()) != -1) {
                    byteArrayStream.write(b);
                }
            } catch (IOException e) {
                //There is no way such an exception is thrown
            }

            part.setImage(byteArrayStream.toByteArray());

            return part;
        } catch(SQLException e) {
            throw new UncheckedSQLException(e);
        }
    }
}
