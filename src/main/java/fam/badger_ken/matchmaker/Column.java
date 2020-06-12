package fam.badger_ken.matchmaker;

import fam.badger_ken.matchmaker.cell.CellHandler;

import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public abstract class Column {
    protected Boolean shouldRepopulateData = true;
    private final CellHandler cellHandler;
    private final Optional<Integer> maxColumnWidth;
    private final String columnName;
    protected final static int SHORT_COLUMN_WIDTH = 55;

    public Column(String columnName, CellHandler cellHandler, int maxColumnWidth) {
        this.cellHandler = cellHandler;
        this.maxColumnWidth = Optional.of(maxColumnWidth);
        this.columnName = columnName;
    }
    public Column(String columnName, CellHandler cellHandler) {
        this.cellHandler = cellHandler;
        this.maxColumnWidth = Optional.empty();
        this.columnName = columnName;
    }

    public void repopulate(){
        this.shouldRepopulateData = true;
    }

    public TableCellRenderer getCellRenderer() {
        return cellHandler;
    }

    public Optional<Integer> getMaxColumnWidth() {
        return maxColumnWidth;
    }

    public String getColumnName() {
        return columnName;
    }

    public Comparator<Person> getComparator() {
        return cellHandler;
    }

    public abstract String convertPersonToCsv(Person winner, GameConfig gameConfig, SaveState saveState);

    public abstract Optional<Component> setupFiltersAndMakeTab(Matchmaker matchmaker, ResultMaker resultMaker);
}
