// c(2012) i.am.badger.ken@gmail.com
// You may re-use, so long as I am credited, and you don't charge
// for your work that uses this.
package fam.badger_ken.matchmaker.widget;

import java.awt.Dimension;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JComponent;

import fam.badger_ken.matchmaker.Filterer;

/**
 * An UnsettableComboBox is a regular combo box, with these extensions:
 * a) it need not be populated at the start. It's not populated until the
 * populate() method is called.
 * b) its contents are hooked up to notify the given filter when changed.
 * c) it supports the Unsettable interface, by selecting the 0th item.
 */
public class UnsettableComboBox extends JComboBox<AnyDropdownable> implements Unsettable {
  private static final long serialVersionUID = 3267876461751965919L;
  private Filterer filterer = null;

  public UnsettableComboBox() {
    setOpaque(true);
    setPreferredSize(new Dimension(1000, 50));
    setMaximumSize(new Dimension(1000, 75));
  }

  @Override
  public void unset() {
    if (this.getModel().getSelectedItem() != null && filterer != null) {
      setSelectedIndex(0);  // assumes 'any' item is first
      if (filterer != null) {
        filterer.notify(this, this);
      }
    }
  }

  /**
   * Populate the dropdown box
   * @param originalItems the items to show - makes a copy of them
   * @param filterer the filterer to notify on change.
   */
  public void populate(Vector<AnyDropdownable> originalItems, final Filterer filterer) {
    this.filterer = filterer;
    Vector<AnyDropdownable> myItems = new Vector<>(originalItems);
    DefaultComboBoxModel<AnyDropdownable> newModel = new DefaultComboBoxModel<>(myItems);
    setModel(newModel);
    this.addActionListener(arg0 -> {
      if (filterer != null) {
        filterer.notify((JComponent) arg0.getSource(), (JComponent) arg0.getSource());
      }
    });
  }              
}
