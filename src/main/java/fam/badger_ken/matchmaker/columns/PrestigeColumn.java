package fam.badger_ken.matchmaker.columns;

import fam.badger_ken.matchmaker.*;
import fam.badger_ken.matchmaker.cell.PrestigeHandler;
import fam.badger_ken.matchmaker.filter.PrestigeFilter;
import fam.badger_ken.matchmaker.widget.UnsettableTextField;

import javax.swing.*;
import java.awt.*;
import java.util.Optional;


public class PrestigeColumn extends Column {

    public PrestigeColumn() {
        super("Prestige", new PrestigeHandler(), PrestigeFilter.class, 3 * SHORT_COLUMN_WIDTH);
    }

    @Override
    public String convertPersonToCsv(Person winner, GameConfig gameConfig, SaveState saveState) {
        return String.valueOf(winner.prestige);
    }

    @Override
    public Component setupFiltersAndMakeTab(Matchmaker matchmaker, ResultMaker resultMaker) {
        final PrestigeFilter minPrestigeFilter = new PrestigeFilter(resultMaker, true);
        final PrestigeFilter maxPrestigeFilter = new PrestigeFilter(resultMaker, false);
        matchmaker.addFilter(minPrestigeFilter);
        matchmaker.addFilter(maxPrestigeFilter);
        JPanel pietyFilterPanel = new JPanel();
        pietyFilterPanel.setLayout(new BoxLayout(pietyFilterPanel, BoxLayout.Y_AXIS));

        JPanel panel_7 = new JPanel();
        pietyFilterPanel.add(panel_7);

        JLabel lblNewLabel_11 = new JLabel("min prestige: ");
        panel_7.add(lblNewLabel_11);

        UnsettableTextField minPrestigeField = new UnsettableTextField(3, minPrestigeFilter);
        panel_7.add(minPrestigeField);

        JPanel panel_8 = new JPanel();
        pietyFilterPanel.add(panel_8);

        JLabel lblMaxPrestige = new JLabel("max prestige: ");
        panel_8.add(lblMaxPrestige);

        UnsettableTextField maxPrestigeField = new UnsettableTextField(3, maxPrestigeFilter);
        panel_8.add(maxPrestigeField);


        return pietyFilterPanel;
    }


}
