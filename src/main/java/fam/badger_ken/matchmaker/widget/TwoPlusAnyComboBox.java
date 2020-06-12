// c(2012) i.am.badger.ken@gmail.com
// You may re-use, so long as I am credited, and you don't charge
// for your work that uses this.
package fam.badger_ken.matchmaker.widget;

import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JComponent;

import fam.badger_ken.matchmaker.Filterer;

/**
 * A 'TwoPlusAnyComboBox' is a combo box which
 * a) contains an 'Any' item
 * b) contains exactly two other items
 * c) can be Unset - unsetting selects the 'Any' item
 * d) notifies a Filterer when its value changes, or its unset.
 *
 */
public class TwoPlusAnyComboBox extends JComboBox<AnyDropdownable> implements Unsettable {
  private static final long serialVersionUID = 3911110800889661981L;
  private final Filterer filterer;
  
  public TwoPlusAnyComboBox(String label1, String label2, final Filterer filterer) {
    this.filterer = filterer;
    Vector<AnyDropdownable> items = new Vector<>();
    // put the 'Any' item first, that makes it selected by default
    items.add(new AnyItemForDropdown());
    // now the item with the first label:
    items.add(new AnyItemForDropdown(false, label1, label1));
    // and the second
    items.add(new AnyItemForDropdown(false, label2, label2));
    
    setModel(new DefaultComboBoxModel<>(items));
    setMaximumRowCount(4);
    setOpaque(true);   
    
    addActionListener(arg0 -> filterer.notify((JComponent) arg0.getSource(), (JComponent) arg0.getSource()));
  }

  @Override
  public void unset() {
    setSelectedIndex(0);  // assumes 'any' item is first
    filterer.notify(this, this);
  }

}
