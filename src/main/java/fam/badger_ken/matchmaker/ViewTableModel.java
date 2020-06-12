// c(2012) i.am.badger.ken@gmail.com
// You may re-use, so long as I am credited, and you don't charge
// for your work that uses this.
package fam.badger_ken.matchmaker;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

/**
 * This handles all the data in the table,
 * _including_ the column definitions. These should probably be split
 * into two different places.
 */
public class ViewTableModel extends AbstractTableModel {

  private final List<Column> columns;

  public ViewTableModel(final List<Column> columns) {
    this.columns = columns;
  }

  /**
   * 
   */
  private static final long serialVersionUID = -2664493172757239738L;
  int x = 0;

  private List<Person> winners = new ArrayList<>();

  @Override
  public int getColumnCount() {
    return this.columns.size();
  }

  @Override
  public int getRowCount() {
    return winners.size();
  }



  @Override
  public Object getValueAt(int rowIndex, int colIndex) {
    // the value is the same for all columns, the various cell handlers
    // display them differently.
    if (rowIndex >= winners.size()) {
      return null;  // happens transiently when # of rows change.
    }
    return winners.get(rowIndex);
  }

  @Override
  public String getColumnName(int i) {
    return this.columns.get(i).getColumnName();
  }

  /**
   * Reset the contents in the table, given this list of winners.
   * @param newWinners the winners.
   */
  public void reset(List<Person> newWinners) {
    int oldSize = this.winners == null ? 0 : this.winners.size();
    int newSize = newWinners == null ? 0 : newWinners.size();
    this.winners = newWinners;
    // you must notify the table that it's model has changed.
    if (oldSize > newSize) {
      this.fireTableRowsDeleted(newSize, oldSize - 1);
    } else if (oldSize < newSize) {
      this.fireTableRowsInserted(oldSize, newSize - 1);
    }
  }

  
  @Override
  public Class<?> getColumnClass(int columnIndex) {
    // all columns have Person data.
    return Person.class;
  }

  public void setColumnComparators(ViewTableSorter sorter) {
    for (int i = 0; i < this.columns.size(); i++) {
      sorter.setComparator(i, this.columns.get(i).getComparator());
    }
//    int column = 0;
//    sorter.setComparator(column++, new AgeHandler());
//    sorter.setComparator(column++, new GenderHandler(matchmaker));
//    sorter.setComparator(column++, new MarriageHandler(matchmaker));
//    sorter.setComparator(column++, new KidsHandler());
//    sorter.setComparator(column++, new NameHandler(matchmaker));
//    sorter.setComparator(column++, new DynastyHandler(matchmaker));
//    sorter.setComparator(column++, new HoldingsHandler(matchmaker));
//    //sorter.setComparator(column++, new RulerHandler(matchmaker));
//    sorter.setComparator(column++, new ClaimsHandler());
//    for (int j = 0; j < GameConfig.NUM_ATTRIBUTES; j++) {
//      sorter.setComparator(column++, new AttributeHandler(matchmaker, j));
//    }
//    sorter.setComparator(column++, new TraitsHandler(matchmaker));
//    sorter.setComparator(column++, new ReligionHandler(matchmaker));
//    sorter.setComparator(column++, new CultureHandler(matchmaker));
//    sorter.setComparator(column++, new PietyHandler());
//    sorter.setComparator(column++, new WealthHandler());
//    sorter.setComparator(column++, new HomeHandler(matchmaker));
//    sorter.setComparator(column++, new ArtifactsHandler(matchmaker));

  }

  public List<Column> getColumns() {
    return this.columns;
  }
}
