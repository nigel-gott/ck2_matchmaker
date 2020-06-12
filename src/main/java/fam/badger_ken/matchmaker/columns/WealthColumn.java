package fam.badger_ken.matchmaker.columns;

import fam.badger_ken.matchmaker.*;
import fam.badger_ken.matchmaker.cell.WealthHandler;
import fam.badger_ken.matchmaker.filter.WealthFilter;
import fam.badger_ken.matchmaker.widget.UnsettableTextField;

import javax.swing.*;
import java.awt.*;
import java.util.Optional;


public class WealthColumn extends Column {

    public WealthColumn() {
        super("Wealth", new WealthHandler(), 3 * SHORT_COLUMN_WIDTH);
    }

    @Override
    public String convertPersonToCsv(Person winner, GameConfig gameConfig, SaveState saveState) {
        return String.valueOf(winner.getWealth());
    }

    @Override
    public Optional<Component> setupFiltersAndMakeTab(Matchmaker matchmaker, ResultMaker resultMaker) {
        final WealthFilter minWealthFilter = new WealthFilter(resultMaker, true);
        final WealthFilter maxWealthFilter = new WealthFilter(resultMaker, false);
        matchmaker.addFilter(minWealthFilter);
        matchmaker.addFilter(maxWealthFilter);
        JPanel pietyFilterPanel = new JPanel();
        pietyFilterPanel.setLayout(new BoxLayout(pietyFilterPanel, BoxLayout.Y_AXIS));

        JPanel panel_7 = new JPanel();
        pietyFilterPanel.add(panel_7);

        JLabel lblNewLabel_11 = new JLabel("min wealth: ");
        panel_7.add(lblNewLabel_11);

        UnsettableTextField minWealthField = new UnsettableTextField(3, minWealthFilter);
        panel_7.add(minWealthField);

        JPanel panel_8 = new JPanel();
        pietyFilterPanel.add(panel_8);

        JLabel lblMaxWealth = new JLabel("max wealth: ");
        panel_8.add(lblMaxWealth);

        UnsettableTextField maxWealthField = new UnsettableTextField(3, maxWealthFilter);
        panel_8.add(maxWealthField);


        return Optional.of(pietyFilterPanel);
    }


}
