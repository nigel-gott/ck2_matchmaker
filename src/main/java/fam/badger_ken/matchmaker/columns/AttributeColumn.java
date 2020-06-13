package fam.badger_ken.matchmaker.columns;

import fam.badger_ken.matchmaker.*;
import fam.badger_ken.matchmaker.cell.AttributeHandler;
import fam.badger_ken.matchmaker.filter.AttributeFilter;
import fam.badger_ken.matchmaker.widget.UnsettableTextField;

import javax.swing.*;
import java.awt.*;
import java.util.Optional;


public class AttributeColumn extends Column {

    public final int attributeOrd;
    public final String attributeName;

    public AttributeColumn(String attributeName, Matchmaker matchmaker, int attributeOrd) {
        super(attributeName, new AttributeHandler(matchmaker, attributeOrd), AttributeFilter.class, 65);
        this.attributeOrd = attributeOrd;
        this.attributeName = attributeName;
    }

    @Override
    public String convertPersonToCsv(Person winner, GameConfig gameConfig, SaveState saveState) {
        return String.valueOf(winner.getAdjustedAttribute(gameConfig, attributeOrd));
    }

    @Override
    public Component setupFiltersAndMakeTab(Matchmaker matchmaker, ResultMaker resultMaker) {
        final AttributeFilter minFilter = new AttributeFilter(resultMaker, matchmaker, attributeOrd, true);
        matchmaker.addFilter(minFilter);
        final AttributeFilter maxFilter = new AttributeFilter(resultMaker, matchmaker, attributeOrd, false);
        matchmaker.addFilter(maxFilter);
        JPanel attributePanel = new JPanel();
        FlowLayout flowLayout_4 = (FlowLayout) attributePanel.getLayout();
        flowLayout_4.setAlignment(FlowLayout.LEFT);

        JLabel attributeLabel = new JLabel(attributeName + "...");
        attributeLabel.setFont(new Font("Tahoma", Font.BOLD, 13));
        attributePanel.add(attributeLabel);

        JLabel label_7 = new JLabel("min: ");
        attributePanel.add(label_7);

        UnsettableTextField minFilterField = new UnsettableTextField(3, minFilter);
        attributePanel.add(minFilterField);

        JLabel label_8 = new JLabel(" max: ");
        attributePanel.add(label_8);

        UnsettableTextField maxFilterField = new UnsettableTextField(3, maxFilter);
        attributePanel.add(maxFilterField);

        return attributePanel;
    }


}
