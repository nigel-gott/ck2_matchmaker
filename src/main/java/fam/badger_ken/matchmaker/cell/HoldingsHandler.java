// c(2012) i.am.badger.ken@gmail.com
// You may re-use, so long as I am credited, and you don't charge
// for your work that uses this.
package fam.badger_ken.matchmaker.cell;

import java.awt.Component;
import java.util.Comparator;
import java.util.Iterator;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import fam.badger_ken.matchmaker.Holding;
import fam.badger_ken.matchmaker.Matchmaker;
import fam.badger_ken.matchmaker.Person;

public class HoldingsHandler implements TableCellRenderer, Comparator<Person> {
  private Matchmaker matchmaker;

  public HoldingsHandler(Matchmaker matchmaker) {
    this.matchmaker = matchmaker;
  }

  @Override
  public int compare(Person arg0, Person arg1) {
    boolean has0 = arg0.getHoldings() != null && !arg0.getHoldings().isEmpty();
    boolean has1 = arg1.getHoldings() != null && !arg1.getHoldings().isEmpty();
    if (!has0 && !has1) return 0;
    if (has0 && !has1) return -1;
    if (!has0 && has1) return 1;
    // they both have holdings - whoever has the highest-ranked holding wins.
    // if a tie, whoever has the most holdings wins.
    Iterator<Holding> iter0 = arg0.getHoldings().iterator();
    Iterator<Holding> iter1 = arg1.getHoldings().iterator();
    for (;;) {
      if (!iter0.hasNext() && !iter0.hasNext()) return 0;
      if (iter0.hasNext() && !iter1.hasNext()) return -1;
      if (!iter0.hasNext() && iter1.hasNext()) return 1;
      Holding holding0 = iter0.next();
      Holding holding1 = iter1.next();
      int delta = holding0.holdingLevel.compareTo(holding1.holdingLevel);
      if (delta != 0) return delta;
    }  // keep going
  }

  @Override
  public Component getTableCellRendererComponent(JTable table, Object value,
      boolean isSelected, boolean hasFocus, int row, int column) {
    Person person = (Person) value;
    if (person == null) return null;
    return new JLabel(person.getDisplayableHoldings(matchmaker.gameConfig));
  }

}
