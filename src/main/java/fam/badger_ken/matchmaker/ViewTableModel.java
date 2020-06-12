// c(2012) i.am.badger.ken@gmail.com
// You may re-use, so long as I am credited, and you don't charge
// for your work that uses this.
package fam.badger_ken.matchmaker;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import fam.badger_ken.matchmaker.cell.*;

/**
 * This handles all the data in the table,
 * _including_ the column definitions. These should probably be split
 * into two different places.
 */
public class ViewTableModel extends AbstractTableModel {
  String [] columnNames = {
      "Age",
      "Gender",
      "#Spouses",
      "Kids",
      "Name",
      "Dynasty",
      "Holdings",
      //"Ruler?",
      "#Claims",
      "Diplomacy",
      "Martial",
      "Steward",
      "Intrigue",
      "Learning",
      "Traits",
      "Religion",
      "Culture",
      "Piety",
      "Wealth",
      "Home",
          "Artefacts"
  };

  private Matchmaker matchmaker;

  public ViewTableModel(Matchmaker matchmaker) {
    this.matchmaker = matchmaker;
  }

  /**
   * 
   */
  private static final long serialVersionUID = -2664493172757239738L;
  int x = 0;

  private List<Person> winners = new ArrayList<Person>();

  @Override
  public int getColumnCount() {
    return columnNames.length;
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
  public String getColumnName(int arg0) {
    return columnNames[arg0];
  }

  /**
   * Reset the contents in the table, given this list of winners.
   * @param winners the winners.
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
    int column = 0;
    sorter.setComparator(column++, new AgeHandler());
    sorter.setComparator(column++, new GenderHandler(matchmaker));
    sorter.setComparator(column++, new MarriageHandler(matchmaker));
    sorter.setComparator(column++, new KidsHandler());
    sorter.setComparator(column++, new NameHandler(matchmaker));
    sorter.setComparator(column++, new DynastyHandler(matchmaker));
    sorter.setComparator(column++, new HoldingsHandler(matchmaker));
    //sorter.setComparator(column++, new RulerHandler(matchmaker));
    sorter.setComparator(column++, new ClaimsHandler());
    for (int j = 0; j < GameConfig.NUM_ATTRIBUTES; j++) {
      sorter.setComparator(column++, new AttributeHandler(matchmaker, j));
    }
    sorter.setComparator(column++, new TraitsHandler(matchmaker));
    sorter.setComparator(column++, new ReligionHandler(matchmaker));
    sorter.setComparator(column++, new CultureHandler(matchmaker));
    sorter.setComparator(column++, new PietyHandler());
    sorter.setComparator(column++, new WealthHandler());
    sorter.setComparator(column++, new HomeHandler(matchmaker));
    sorter.setComparator(column++, new ArtifactsHandler(matchmaker));

  }
}
