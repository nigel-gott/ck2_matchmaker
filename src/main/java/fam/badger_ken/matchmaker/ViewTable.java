// c(2012) i.am.badger.ken@gmail.com
// You may re-use, so long as I am credited, and you don't charge
// for your work that uses this.
package fam.badger_ken.matchmaker;

import javax.swing.JTable;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

import fam.badger_ken.matchmaker.cell.AgeHandler;
import fam.badger_ken.matchmaker.cell.AttributeHandler;
import fam.badger_ken.matchmaker.cell.ClaimsHandler;
import fam.badger_ken.matchmaker.cell.CultureHandler;
import fam.badger_ken.matchmaker.cell.DynastyHandler;
import fam.badger_ken.matchmaker.cell.GenderHandler;
import fam.badger_ken.matchmaker.cell.HoldingsHandler;
import fam.badger_ken.matchmaker.cell.HomeHandler;
import fam.badger_ken.matchmaker.cell.KidsHandler;
import fam.badger_ken.matchmaker.cell.MarriageHandler;
import fam.badger_ken.matchmaker.cell.NameHandler;
import fam.badger_ken.matchmaker.cell.PietyHandler;
import fam.badger_ken.matchmaker.cell.ReligionHandler;
import fam.badger_ken.matchmaker.cell.TraitsHandler;
import fam.badger_ken.matchmaker.cell.WealthHandler;

/**
 * The table that shows the winners.
 */
public class ViewTable extends JTable {
  private static final long serialVersionUID = -5668144084110568712L;
  private static final int DEFAULT_HEIGHT_PER_ROW = 20;
  private static final int TRAITS_PER_ROW = 4;

  public ViewTable(ViewTableModel viewTableModel) {
    super(viewTableModel);
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

  /*
   * TO ADD A NEW COLUMN TO THE GUI:
   * 1) update setCellRenderers
   * 2) update setColumnWidths
   * 3) update ViewTableModel.columnNames
   * 4) update ViewTableModel.setColumnComparators
   * 5) update Matchmaker.writeWinners() (CSV export)
   * 
   * someday there should be a class per column that wraps all this.
   */

  public void setCellRenderers(Matchmaker matchmaker) {
    int column = 0;
    TableColumnModel columnModel = this.getColumnModel();

    columnModel.getColumn(column++).setCellRenderer(new AgeHandler());
    columnModel.getColumn(column++).setCellRenderer(new GenderHandler(matchmaker));
    columnModel.getColumn(column++).setCellRenderer(new MarriageHandler(matchmaker));
    columnModel.getColumn(column++).setCellRenderer(new KidsHandler());
    columnModel.getColumn(column++).setCellRenderer(new NameHandler(matchmaker));
    columnModel.getColumn(column++).setCellRenderer(new DynastyHandler(matchmaker));
    columnModel.getColumn(column++).setCellRenderer(new HoldingsHandler(matchmaker));
    //columnModel.getColumn(column++).setCellRenderer(new RulerHandler(matchmaker));
    columnModel.getColumn(column++).setCellRenderer(new ClaimsHandler());
    for (int j = 0; j < 5; j++) {
      columnModel.getColumn(column++).setCellRenderer(new AttributeHandler(matchmaker, j));
    }
    columnModel.getColumn(column++).setCellRenderer(new TraitsHandler(matchmaker));
    columnModel.getColumn(column++).setCellRenderer(new ReligionHandler(matchmaker));
    columnModel.getColumn(column++).setCellRenderer(new CultureHandler(matchmaker));
    columnModel.getColumn(column++).setCellRenderer(new PietyHandler());
    columnModel.getColumn(column++).setCellRenderer(new WealthHandler());
    columnModel.getColumn(column++).setCellRenderer(new HomeHandler(matchmaker));
  }	

  public void setColumnWidths() {
    final int shortWidth = 55;
    final int attrWidth = 65;
    int column = 0;
    TableColumnModel columns = getColumnModel();
    columns.getColumn(column++).setMaxWidth(shortWidth);  // age
    columns.getColumn(column++).setMaxWidth(shortWidth);  // gender
    columns.getColumn(column++).setMaxWidth(shortWidth);  // married
    columns.getColumn(column++).setMaxWidth(shortWidth);  // # kids
    column++; // name
    column++;  // dynasty
    column++;  // holdings
    //columns.getColumn(column++).setMaxWidth(shortWidth);  // ruler?
    columns.getColumn(column++).setMaxWidth(shortWidth);  // #claims
    columns.getColumn(column++).setMaxWidth(attrWidth);  // attr
    columns.getColumn(column++).setMaxWidth(attrWidth);  // attr
    columns.getColumn(column++).setMaxWidth(attrWidth);  // attr
    columns.getColumn(column++).setMaxWidth(attrWidth);  // attr
    columns.getColumn(column++).setMaxWidth(attrWidth);  // attr
    column++; // traits
    column++; // religion
    column++;  // culture
    columns.getColumn(column++).setMaxWidth(3* shortWidth);  // piety
    columns.getColumn(column++).setMaxWidth(3* shortWidth);  // wealth
    column++;  // home
  }
}
