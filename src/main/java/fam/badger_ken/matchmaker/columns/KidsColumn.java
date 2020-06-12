package fam.badger_ken.matchmaker.columns;

import fam.badger_ken.matchmaker.*;
import fam.badger_ken.matchmaker.cell.KidsHandler;
import fam.badger_ken.matchmaker.filter.KidsFilter;
import fam.badger_ken.matchmaker.widget.UnsettableTextField;

import javax.swing.*;
import java.awt.*;
import java.util.Optional;


public class KidsColumn extends Column {

    public KidsColumn() {
        super("Kids", new KidsHandler(), SHORT_COLUMN_WIDTH);
    }

    @Override
    public String convertPersonToCsv(Person winner, GameConfig gameConfig, SaveState saveState) {
        return String.valueOf(winner.getNumKids());
    }

    @Override
    public Optional<Component> setupFiltersAndMakeTab(Matchmaker matchmaker, ResultMaker resultMaker) {
        final KidsFilter minKidsFilter = new KidsFilter(resultMaker, true);
        final KidsFilter maxKidsFilter = new KidsFilter(resultMaker, false);
        matchmaker.addFilter(minKidsFilter);
        matchmaker.addFilter(maxKidsFilter);

        JPanel kidsFilterPanel = new JPanel();
        kidsFilterPanel.setLayout(new BoxLayout(kidsFilterPanel, BoxLayout.Y_AXIS));

        JPanel panel_5 = new JPanel();
        kidsFilterPanel.add(panel_5);

        JLabel lblMinOf_1 = new JLabel("min # of Kids: ");
        panel_5.add(lblMinOf_1);

        UnsettableTextField minKidsField = new UnsettableTextField(3, minKidsFilter);
        panel_5.add(minKidsField);


        JPanel panel_6 = new JPanel();
        kidsFilterPanel.add(panel_6);

        JLabel lblMaxOf = new JLabel("max # of Kids: ");
        panel_6.add(lblMaxOf);

        UnsettableTextField maxKidsField = new UnsettableTextField(3, maxKidsFilter);
        panel_6.add(maxKidsField);

        return Optional.of(kidsFilterPanel);
    }


}
