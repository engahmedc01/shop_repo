/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shop.test;

/**
 *
 * @author ahmed
 */
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;

/*   ----------------------------------------------
 *  |         SNo.        |
 *   ----------------------------------------------
 *  |          |     1    |
 *  |   Name   |-----------------------------------
 *  |          |     2    |
 *   ----------------------------------------------
 *  |          |     1    |
 *  |          |-----------------------------------
 *  | Language |     2    |
 *  |          |-----------------------------------
 *  |          |     3    |
 *   ----------------------------------------------
 */

/**
 * @version 1.0 03/06/99
 */
public class MultipleRowHeaderExample extends JFrame {
  Object[][] data;

  Object[] column;

  JTable table;

  MultiSpanCellTable fixedTable;

  public MultipleRowHeaderExample() {
    super("Multiple Row Header Example");
    setSize(400, 150);

    data = new Object[][] { { "SNo.", "" }, { "Name", "1" }, { "", "2" },{ "", "" },
        { "Language", "1" }, { "", "2" }, { "", "3" } };
    column = new Object[] { "A", "B" };

    AttributiveCellTableModel fixedModel = new AttributiveCellTableModel(
        data, column) {
      public boolean CellEditable(int row, int col) {
        return false;
      }
    };

    CellSpan cellAtt = (CellSpan) fixedModel.getCellAttribute();
    cellAtt.combine(new int[] { 0 }, new int[] { 0, 1 });
    cellAtt.combine(new int[] { 1, 2 }, new int[] { 0 });
    cellAtt.combine(new int[] {  4, 5,6 }, new int[] { 0 });
    cellAtt.combine(new int[] {3}, new int[] { 0 ,1});
    DefaultTableModel model = new DefaultTableModel(data.length, 3);

    fixedTable = new MultiSpanCellTable(fixedModel);
    table = new JTable(model);
    table.setRowHeight(25);
    //fixedTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    //fixedTable.setDefaultRenderer(Object.class, new RowHeaderRenderer(
      //  fixedTable));
   // fixedTable.setGridColor(table.getTableHeader().getBackground());
fixedTable.setRowHeight(25);
    JScrollPane scroll = new JScrollPane(fixedTable);
   // JViewport viewport = new JViewport();
//    viewport.setView(fixedTable);
   // viewport.setPreferredSize(fixedTable.getPreferredSize());
   // scroll.setRowHeaderView(viewport);

    getContentPane().add(scroll, BorderLayout.CENTER);
  }

  public static void main(String[] args) {
    MultipleRowHeaderExample frame = new MultipleRowHeaderExample();
    frame.addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        System.exit(0);
      }
    });
    frame.setVisible(true);
  }

  class RowHeaderRenderer extends JLabel implements TableCellRenderer {
    RowHeaderRenderer(JTable table) {
      JTableHeader header = table.getTableHeader();
      setOpaque(true);
      setBorder(UIManager.getBorder("TableHeader.cellBorder"));
      setHorizontalAlignment(CENTER);
      setForeground(header.getForeground());
      setBackground(header.getBackground());
      setFont(header.getFont());
    }

    public Component getTableCellRendererComponent(JTable table,
        Object value, boolean isSelected, boolean hasFocus, int row,
        int column) {
      setText((value == null) ? "" : value.toString());
      return this;
    }
  }

}
