package fam.badger_ken.matchmaker.cell;

import fam.badger_ken.matchmaker.Person;

import javax.swing.table.TableCellRenderer;
import java.util.Comparator;

public interface CellHandler extends TableCellRenderer, Comparator<Person> {
}
