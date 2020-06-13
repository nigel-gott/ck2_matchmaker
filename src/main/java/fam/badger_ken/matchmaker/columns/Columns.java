package fam.badger_ken.matchmaker.columns;

import fam.badger_ken.matchmaker.Matchmaker;
import fam.badger_ken.matchmaker.ResultMaker;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class Columns implements Iterable<Column> {
    private final List<Column> columns;
    private final List<Runnable> listeners = new CopyOnWriteArrayList<>();
    private final Matchmaker matchmaker;

    public Columns(Matchmaker matchmaker) {
        this.matchmaker = matchmaker;
        this.columns = new ArrayList<>(Arrays.asList(
                new AgeColumn(),
                new GenderColumn(),
                new SpouseColumn(),
                new KidsColumn(),
                new NameColumn(matchmaker),
                new DynastiesColumn(matchmaker),
                new HoldingsColumn(matchmaker),
                new ClaimsColumn(),
                new PietyColumn(),
                new WealthColumn(),
                new PrestigeColumn(),
                new ArtifactColumn(),
                new TraitsColumn(matchmaker),
                new ReligionColumn(matchmaker),
                new CultureColumn(matchmaker),
                new HomeColumn(matchmaker),
                new HealthColumn()
        ));
        final List<String> attributes = Arrays.asList("Diplomacy", "Martial", "Stewardship", "Intrigue", "Learning");
        for (int i = 0; i < attributes.size(); i++) {
            this.columns.add(new AttributeColumn(attributes.get(i), matchmaker, i));
        }
    }

    private List<Column> visibleColumns() {
        return this.columns.stream().filter(Column::isPresent).collect(Collectors.toList());
    }

    @Override
    public Iterator<Column> iterator() {
        return visibleColumns().iterator();
    }

    @Override
    public void forEach(Consumer<? super Column> action) {
        visibleColumns().forEach(action);
    }

    @Override
    public Spliterator<Column> spliterator() {
        return visibleColumns().spliterator();
    }

    public int size() {
        return visibleColumns().size();
    }

    public Column get(int i) {
        return visibleColumns().get(i);
    }

    public void removeColumn(String title) {
        for (Column column : this.columns) {
            if (column.getColumnName().equals(title)) {
                System.out.println("Removing " + title);
                column.removed(this.matchmaker);
                this.notifyColumnListeners();
                return;
            }
        }
    }

    private void notifyColumnListeners() {
        this.listeners.forEach(Runnable::run);
    }

    public void registerChangeListener(Runnable onChange) {
        this.listeners.add(onChange);
    }

    public Component addColumn(String title, ResultMaker resultMaker) {
        for (Column column : this.columns) {
            if (column.getColumnName().equals(title)) {
                Component tab = column.added(matchmaker, resultMaker);
                this.notifyColumnListeners();
                return tab;
            }
        }
        return null;
    }

    public boolean hasSomeHidden() {
        for (Column column : this.columns) {
            if (column.isRemoved()) {
                return true;
            }
        }
        return false;
    }


    public List<Column> removedColumns() {
        return this.columns.stream().filter(Column::isRemoved) .collect(Collectors.toList());
    }
}
