package fam.badger_ken.matchmaker.columns;

import fam.badger_ken.matchmaker.*;
import fam.badger_ken.matchmaker.cell.PietyHandler;
import fam.badger_ken.matchmaker.filter.PietyFilter;
import fam.badger_ken.matchmaker.widget.UnsettableTextField;

import javax.swing.*;
import java.awt.*;
import java.util.Optional;


public class PietyColumn extends Column {

    public PietyColumn() {
        super("Piety", new PietyHandler(), 3 * SHORT_COLUMN_WIDTH);
    }

    @Override
    public String convertPersonToCsv(Person winner, GameConfig gameConfig, SaveState saveState) {
        return String.valueOf(winner.getPiety());
    }

    @Override
    public Optional<Component> setupFiltersAndMakeTab(Matchmaker matchmaker, ResultMaker resultMaker) {
        final PietyFilter minPietyFilter = new PietyFilter(resultMaker, true);
        final PietyFilter maxPietyFilter = new PietyFilter(resultMaker, false);
        matchmaker.addFilter(minPietyFilter);
        matchmaker.addFilter(maxPietyFilter);
        JPanel pietyFilterPanel = new JPanel();
        pietyFilterPanel.setLayout(new BoxLayout(pietyFilterPanel, BoxLayout.Y_AXIS));

        JPanel panel_7 = new JPanel();
        pietyFilterPanel.add(panel_7);

        JLabel lblNewLabel_11 = new JLabel("min piety: ");
        panel_7.add(lblNewLabel_11);

        UnsettableTextField minPietyField = new UnsettableTextField(3, minPietyFilter);
        panel_7.add(minPietyField);

        JPanel panel_8 = new JPanel();
        pietyFilterPanel.add(panel_8);

        JLabel lblMaxPiety = new JLabel("max piety: ");
        panel_8.add(lblMaxPiety);

        UnsettableTextField maxPietyField = new UnsettableTextField(3, maxPietyFilter);
        panel_8.add(maxPietyField);


        return Optional.of(pietyFilterPanel);
    }


}
