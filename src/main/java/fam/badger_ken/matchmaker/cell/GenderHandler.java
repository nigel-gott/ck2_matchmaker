// c(2012) i.am.badger.ken@gmail.com
// You may re-use, so long as I am credited, and you don't charge
// for your work that uses this.
package fam.badger_ken.matchmaker.cell;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;

import fam.badger_ken.matchmaker.Person;

public class GenderHandler implements CellHandler {
    private final JLabel maleLabel;
    private final JLabel femaleLabel;

    public GenderHandler() {
        maleLabel = new JLabel("m");
        maleLabel.setHorizontalAlignment(JLabel.CENTER);
        femaleLabel = new JLabel("f");
        femaleLabel.setHorizontalAlignment(JLabel.CENTER);
    /* cute, but less informative.
		try {
			ImageIcon maleIcon = new ImageIcon(new URL("http://icons.iconarchive.com/icons/icons-land/vista-love/16/Sex-Male-icon.png"));
			maleLabel = new JLabel(maleIcon);
			ImageIcon femaleIcon = new ImageIcon(new URL("http://icons.iconarchive.com/icons/icons-land/vista-love/16/Sex-Female-icon.png"));
			femaleLabel = new JLabel(femaleIcon);
		} catch (MalformedURLException e) {
			maleLabel = new JLabel("M");
			femaleLabel = new JLabel("F");
		}
     */
    }

    @Override
    public int compare(Person arg0, Person arg1) {
        if (arg0.isMale == arg1.isMale) return 0;
        return arg0.isMale ? -1 : 1;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus, int row, int column) {
        Person person = (Person) value;
        if (person == null) return null;
        return person.isMale ? maleLabel : femaleLabel;
    }

}
