// c(2012) i.am.badger.ken@gmail.com
// You may re-use, so long as I am credited, and you don't charge
// for your work that uses this.
package fam.badger_ken.matchmaker;

import fam.badger_ken.matchmaker.columns.Columns;

import javax.swing.JTable;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

import java.util.List;

/**
 * The table that shows the winners.
 */
public class ViewTable extends JTable {
  private static final long serialVersionUID = -5668144084110568712L;
  private static final int DEFAULT_HEIGHT_PER_ROW = 20;
  private static final int TRAITS_PER_ROW = 4;
  private final Columns columns;

  public ViewTable(ViewTableModel viewTableModel) {
    super(viewTableModel);
    this.columns = viewTableModel.getColumns();
    this.columns.registerChangeListener(() -> {
      this.setCellRenderers();
      this.setColumnWidths();
    });
  }

  public void setRowHeights() {
    TableModel model = this.getModel();
    // give extra height to people with more traits or more holdings.
    // note MODEL.getRowCount(), not this.getRowCount(), as it appears
    // that when the model changes # of rows that isn't reflected in myself
    // until later. odd.
    for (int rowNum = 0; rowNum < model.getRowCount(); rowNum++) {
      // the person is in every cell, that includes cell 0...
      Person person = (Person) model.getValueAt(rowNum, 0);
      if (person == null) continue;
      int numTraits = person.traits.size();
      int numHoldings = person.getHoldings() == null ? 0 : person.getHoldings().size();
      int maxNum = Math.max(numTraits, numHoldings);
      setRowHeight(rowNum, DEFAULT_HEIGHT_PER_ROW * (1 + maxNum/TRAITS_PER_ROW));
    }
  }

  public void setCellRenderers() {
    TableColumnModel columnModel = this.getColumnModel();

    for (int i = 0; i < columns.size(); i++) {
      final Column column = columns.get(i);
      columnModel.getColumn(i).setCellRenderer(column.getCellRenderer());
    }
  }

  public void setColumnWidths() {
    for (int i = 0; i < columns.size(); i++) {
      final Column column = columns.get(i);
      int finalI = i;
      column.getMaxColumnWidth().ifPresent((x) -> columnModel.getColumn(finalI).setMaxWidth(x));
    }
  }
}
