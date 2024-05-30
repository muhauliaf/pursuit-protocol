package edu.uchicago.mauliafirmansyah.view;

import lombok.Getter;

import javax.swing.*;
import java.awt.*;

@Getter
// Represents a panel with weighted components for layout
public class WeightedPanel extends JPanel {
    // The panel containing the weighted components
    private JPanel panel;

    // Constructor to create a weighted panel with specific layout
    public WeightedPanel(
            JPanel panel,
            int panelWidth,
            int panelHeight,
            int componentX,
            int componentY,
            int componentWidth,
            int componentHeight
    ){
        super();
        this.panel = panel;
        this.setLayout(new GridBagLayout());
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.fill = GridBagConstraints.BOTH;

        // Top empty component with specific weights and position
        EmptyComponent topEmpty = new EmptyComponent();
        gridBagConstraints.weightx = panelWidth;
        gridBagConstraints.weighty = componentY;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 1;
        gridBagConstraints.gridwidth = 3;
        this.add(topEmpty, gridBagConstraints);

        // Left empty component with specific weights and position
        EmptyComponent leftEmpty = new EmptyComponent();
        gridBagConstraints.weightx = componentX;
        gridBagConstraints.weighty = componentHeight;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridheight = 1;
        gridBagConstraints.gridwidth = 1;
        this.add(leftEmpty, gridBagConstraints);

        // Panel component with specific weights and position
        gridBagConstraints.weightx = componentWidth;
        gridBagConstraints.weighty = componentHeight;
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridheight = 1;
        gridBagConstraints.gridwidth = 1;
        this.add(this.panel, gridBagConstraints);

        // Right empty component with specific weights and position
        EmptyComponent rightEmpty = new EmptyComponent();
        gridBagConstraints.weightx = panelWidth - componentX - componentWidth;
        gridBagConstraints.weighty = componentHeight;
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridheight = 1;
        gridBagConstraints.gridwidth = 1;
        this.add(rightEmpty, gridBagConstraints);

        // Bottom empty component with specific weights and position
        EmptyComponent bottomEmpty = new EmptyComponent();
        gridBagConstraints.weightx = panelWidth;
        gridBagConstraints.weighty = panelHeight - componentY - componentHeight;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridheight = 1;
        gridBagConstraints.gridwidth = 3;
        this.add(bottomEmpty, gridBagConstraints);
    }

    // Convenience constructor with default values
    public WeightedPanel(JPanel panel){
        this(panel,3,3,1,1,1,1);
    }

    // Convenience constructor with default panel
    public WeightedPanel(
            int panelWidth,
            int panelHeight,
            int componentX,
            int componentY,
            int componentWidth,
            int componentHeight
    ){
        this(
                new JPanel(),
                panelWidth,
                panelHeight,
                componentX,
                componentY,
                componentWidth,
                componentHeight
        );
    }

    // Default constructor
    public WeightedPanel(){
        this(new JPanel());
    }
}
