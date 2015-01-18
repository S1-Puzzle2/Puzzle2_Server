package at.fhv.puzzle2.server.entity;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class PuzzlePart {
    Integer _id;
    String _barcode;
    int _order;
    byte[] _image;

    public PuzzlePart(Integer id, String barcode, int order) {
        _id = id;
        _barcode = barcode;
        _order = order;
    }

    public PuzzlePart(String barcode, int order) {
        this(null, barcode, order);
    }

    public Integer getID() {
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

    public void setImage(byte[] image) {
        _image = image;
    }

    public InputStream getImageInputStream() {
        return new ByteArrayInputStream(_image);
    }

    public void setID(int id) {
        this._id = id;
    }
}
