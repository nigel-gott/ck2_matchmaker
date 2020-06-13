package fam.badger_ken.matchmaker.columns;

import fam.badger_ken.matchmaker.*;
import fam.badger_ken.matchmaker.cell.CellHandler;

import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.util.Comparator;
import java.util.Optional;

public abstract class Column {
    protected Boolean shouldRepopulateData = true;
    private final CellHandler cellHandler;
    private final Optional<Integer> maxColumnWidth;
    private final String columnName;
    private final Class<?> filterClass;
    protected final static int SHORT_COLUMN_WIDTH = 55;
    private volatile boolean removed;

    public Column(String columnName, CellHandler cellHandler, Class<?> filterClass, int maxColumnWidth) {
        this(columnName, cellHandler, filterClass, Optional.of(maxColumnWidth));
    }

    private Column(String columnName, CellHandler cellHandler, Class<?> filterClass, Optional<Integer> maxColumnWidth) {
        this.cellHandler = cellHandler;
        this.maxColumnWidth = maxColumnWidth;
        this.columnName = columnName;
        this.filterClass = filterClass;
    }

    public Column(String columnName, CellHandler cellHandler, Class<?> filterClass) {
        this(columnName, cellHandler, filterClass, Optional.empty());
    }

    public void repopulate() {
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


    public abstract Component setupFiltersAndMakeTab(Matchmaker matchmaker, ResultMaker resultMaker);

    public void removed(Matchmaker matchmaker) {
        this.removed = true;
        if (this.filterClass != null) {
            matchmaker.removeFilters(filterer -> !filterer.getClass().isAssignableFrom(filterClass));
        }
    }

    public boolean isRemoved() {
        return removed;
    }

    public boolean isPresent() {
        return !removed;
    }

    public Component added(Matchmaker matchmaker, ResultMaker resultMaker) {
        this.removed = false;
        return this.setupFiltersAndMakeTab(matchmaker, resultMaker);
    }
}
