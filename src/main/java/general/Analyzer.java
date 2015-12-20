/*
 *	Author:      Gilbert Maystre
 *	Date:        Dec 18, 2015
 */

package general;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import queries.Graph;
import queries.WrapperUtility;

public class Analyzer {

    private final WrapperUtility wrapper;
    
    private JPanel main; 
    private JComboBox<String> methodCombo;
    private JComboBox<String> playerCombo;
    private JComboBox<String> attributeCombo;
    private Graph graph;

    public static void main(String[] args){
        new Analyzer();
    }
    
    public Analyzer(){
        this.wrapper = new WrapperUtility();
        
        prepareComponents();
        addListeners();
        setLayout();
        
        //now we build the window
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Database visualizer");
        frame.setContentPane(main);
        frame.setSize(main.getPreferredSize()); 
        frame.setMinimumSize(main.getPreferredSize());
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
    
    private void prepareComponents(){
        //the combos
        this.methodCombo = new JComboBox<>(wrapper.getComputations());
        this.attributeCombo = new JComboBox<>(wrapper.getAttributes());
        this.playerCombo = new JComboBox<>(wrapper.getNames());
        
        //the graph        
        this.graph = new Graph();
        notifyGraph();
    }
    
    private void addListeners(){
        methodCombo.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                notifyGraph();
            }
            
        });
        
        attributeCombo.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                notifyGraph();
            }
            
        });
        
        playerCombo.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                notifyGraph();
            }
            
        });
    }
    
    private void setLayout(){
        //first build the SQL querry constructor
        JPanel selector = new JPanel();
        selector.setLayout(new BoxLayout(selector, BoxLayout.LINE_AXIS));
        selector.add(Box.createHorizontalGlue());
        selector.add(Box.createHorizontalStrut(10));
        selector.add(new JLabel("Show "));
        selector.add(methodCombo);
        selector.add(new JLabel(" results of "));
        selector.add(playerCombo);
        selector.add(new JLabel(" on "));
        selector.add(attributeCombo);
        selector.add(Box.createHorizontalStrut(10));
        selector.add(Box.createHorizontalGlue());
        
        //then the main itself
        this.main = new JPanel(new BorderLayout());
        main.add(selector, BorderLayout.PAGE_START);
        main.add(graph, BorderLayout.CENTER); 
    }
    
    private void notifyGraph(){
        String name = (String) playerCombo.getSelectedItem();
        String attribute = (String) attributeCombo.getSelectedItem();
        String method = (String) methodCombo.getSelectedItem();
        
        graph.updateVals(wrapper.getValues(method, attribute, name), wrapper.getMaxValue(method, attribute));
    }    

}
