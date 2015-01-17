package at.fhv.puzzle2.server.database.entity;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class PuzzlePart {
    Integer _id;
    String _barcode;
    int _order;
    byte[] _image;

    public PuzzlePart(Integer id, String barcode, int order, byte[] image) {
        _id = id;
        _barcode = barcode;
        _order = order;
        _image = image;
    }

    public PuzzlePart(String barcode, int order, byte[] image) {
        this(null, barcode, order, image);
    }

    public Integer getId() {
        return _id;
    }

    public String getBarcode() {
        return _barcode;
    }

    public int getOrder() {
        return _order;
    }

    public byte[] getImage() {
        return _image;
    }

    public InputStream getImageInputStream() {
        return new ByteArrayInputStream(_image);
    }
}
