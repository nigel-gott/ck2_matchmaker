// c(2012) i.am.badger.ken@gmail.com
// You may re-use, so long as I am credited, and you don't charge
// for your work that uses this.
package fam.badger_ken.matchmaker.widget;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import javax.swing.JComponent;
import javax.swing.JTextField;

import fam.badger_ken.matchmaker.Filterer;

/**
 * An UnsettableTextField is a Text field that knows how to unset itself.
 */
public class UnsettableTextField extends JTextField implements Unsettable {
  /**
   * 
   */
  private static final long serialVersionUID = -2801857933262851016L;
  private final Filterer filterer;
  
  public UnsettableTextField(int numColumns, final Filterer filterer) {
    this.filterer = filterer;
    this.setColumns(numColumns);
    this.setOpaque(true);
    
    this.addFocusListener(new FocusAdapter() {
      @Override
      public void focusLost(FocusEvent arg0) {
        filterer.notify(
            (JComponent) arg0.getSource(), (JComponent) arg0.getSource());
      }
    });
  }

  @Override
  public void unset() {
    this.setText("");
    filterer.notify(this, this);
  }

}
