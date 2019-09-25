/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI_attachement_pkg;

import java.util.Vector;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author ahmed
 */
public class NonEditableModel extends DefaultTableModel {

    public NonEditableModel(Object[][] data, String[] columnNames) {
        super(data, columnNames);
    }

  public  NonEditableModel() {
        super();
    }

    public NonEditableModel(Vector<Object> data, Vector<String> columnNames) {
        super(data, columnNames);
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }
}
