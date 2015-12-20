/*
 *	Author:      Gilbert Maystre
 *	Date:        Dec 20, 2015
 */

package queries;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.List;

import javax.swing.JComponent;

@SuppressWarnings("serial")
public class Graph extends JComponent {
    
    private List<Value> vals;
    private int max;
   
    public Dimension getPreferredSize(){
        return new Dimension(vals.size() * 100, 200);
    }
    
    public void paint(Graphics gr){
        Graphics2D g = (Graphics2D) gr;
        Dimension d = this.getSize();
        int margin = 30;
        
        //background
        g.setColor(Color.WHITE);
        g.fill(this.getVisibleRect());
        
        
        //axis
        g.setColor(Color.BLACK);
        g.drawLine(margin, margin, margin, (int) d.getHeight() - margin); //y
        g.drawLine(margin, (int) d.getHeight() - margin, (int) d.getWidth() - margin, (int) d.getHeight() - margin); //x

        
        //stems
        double scale = (d.getHeight() - 2*margin)/(max + 1);
        double xSpacing = Math.min((d.getWidth() - 2*margin)/(vals.size() + 1), 150);
        
        for(int i = 0; i < vals.size(); i++){
            g.setColor(Color.RED);
            int x = (int) (margin + xSpacing * (i + 1));
            int y1 = (int) (d.getHeight() - margin);
            int y2 = (int) (d.getHeight() - margin - scale * vals.get(i).val);
            
            //the stem
            g.drawLine(x, y1, x, y2);
            
            //the dot
            g.draw(new Ellipse2D.Double(x - 4, y2 - 8, 8, 8));
           
            //the label
            Rectangle2D bounds = g.getFontMetrics().getStringBounds(vals.get(i).date, g);
            g.setColor(Color.DARK_GRAY);
            g.drawString(vals.get(i).date, (int) (x - (bounds.getWidth()/2)), (int)(y1 + bounds.getHeight()));   
        }
        
        
        //scale
        for(int i = 0; i < 100; i += 5){
            int x = margin;
            int y = (int) (d.getHeight() - margin - i * scale);
            
            if(y > margin){
                g.drawLine(x, y, x + 5, y);
                
                String toDraw = ""+i;
                Rectangle2D box  = g.getFontMetrics().getStringBounds(toDraw, g);
                
                g.drawString(toDraw, (int)(x - 5 - box.getWidth()), (int) (y + (box.getHeight()/2)));
            }
        }        
    }

    public void updateVals(List<Value> vals, int max){
        this.vals = vals;
        this.max = max;
        
        this.repaint();
    }
    
    public static class Value{
        
        public String date;
        public int val;
        
        public Value(String date, int val){
            this.date = date;
            this.val = val;
        }
    }
}
