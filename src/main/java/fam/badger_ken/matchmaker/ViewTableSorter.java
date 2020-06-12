// c(2012) i.am.badger.ken@gmail.com
// You may re-use, so long as I am credited, and you don't charge
// for your work that uses this.
package fam.badger_ken.matchmaker;

import javax.swing.table.TableRowSorter;

public class ViewTableSorter extends TableRowSorter<ViewTableModel> {
  final Matchmaker matchmaker;

  public ViewTableSorter(Matchmaker matchmaker, final ViewTableModel viewTableModel) {
    this.matchmaker = matchmaker;
    // 'The setModelWrapper method must be invoked soon after the constructor is called,
    // ideally from within the subclass's constructor.
    // Undefined behavior will result if you use a DefaultRowSorter without specifying a ModelWrapper.'
      //noinspection Convert2Diamond
      this.setModelWrapper(new ModelWrapper<ViewTableModel, Integer>() {
      @Override
      public int getColumnCount() {
        return viewTableModel.getColumnCount();
      }

        @Override
        public Integer getIdentifier(int row) {
            return null;
        }

        @Override
        public ViewTableModel getModel() {
            return viewTableModel;
        }

        @Override
        public int getRowCount() {
            return viewTableModel.getRowCount();
        }

        @Override
        public Object getValueAt(int row, int column) {
            return viewTableModel.getValueAt(row, column);
        }

    });
    // and furthermore, you must tell it the comparators to use.
    viewTableModel.setColumnComparators(this);
  }
}
