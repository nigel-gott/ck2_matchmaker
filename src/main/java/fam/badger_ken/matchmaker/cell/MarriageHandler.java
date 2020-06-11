// c(2012) i.am.badger.ken@gmail.com
// You may re-use, so long as I am credited, and you don't charge
// for your work that uses this.
package fam.badger_ken.matchmaker.cell;

import java.awt.Component;
import java.util.Comparator;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import fam.badger_ken.matchmaker.Matchmaker;
import fam.badger_ken.matchmaker.Person;

public class MarriageHandler implements TableCellRenderer, Comparator<Person> {
  Matchmaker matchmaker;
  private JLabel yesLabel;
  private JLabel noLabel;

  public MarriageHandler(Matchmaker matchmaker) {
    yesLabel = new JLabel("y");
    yesLabel.setHorizontalAlignment(JLabel.CENTER);
    noLabel = new JLabel("-");
    noLabel.setHorizontalAlignment(JLabel.CENTER);
  }
  
  @Override
  public int compare(Person arg0, Person arg1) {
    return arg0.getNumSpouses() - arg1.getNumSpouses();
  }

  @Override
  public Component getTableCellRendererComponent(JTable table, Object value,
      boolean isSelected, boolean hasFocus, int row, int column) {
    Person person = (Person) value;
    if (person == null) return null;
    String val = Integer.toString(person.getNumSpouses());
    JLabel label = new JLabel(val);
    label.setHorizontalAlignment(JLabel.CENTER);
    return label;
  }

}
