// c(2012) i.am.badger.ken@gmail.com
// You may re-use, so long as I am credited, and you don't charge
// for your work that uses this.
package fam.badger_ken.matchmaker.cell;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;

import fam.badger_ken.matchmaker.Person;

public class TitleHandler implements CellHandler {

  @Override
  public int compare(Person arg0, Person arg1) {
    return arg0.getTitle().compareTo(arg1.getTitle());
  }

  @Override
  public Component getTableCellRendererComponent(JTable table, Object value,
      boolean isSelected, boolean hasFocus, int row, int column) {
    Person person = (Person) value;
    if (person == null) return null;
    String val = person.getTitle().toString();
    return new JLabel(val);
  }

}
