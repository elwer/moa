/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package moa.gui.visualization;

import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeSet;
import weka.core.DenseInstance;
import weka.core.Instance;

/**
 *
 * @author jansen
 */
public class DataPoint extends DenseInstance{
    protected int timestamp;
    private HashMap<String, String> measure_values;

    public DataPoint(Instance nextInstance, Integer timestamp) {
        super(nextInstance);
        this.setDataset(nextInstance.dataset());
        this.timestamp = timestamp;
        measure_values = new HashMap<String, String>();
        
    }

    public void updateWeight(int cur_timestamp, double decay_rate){
        //setWeight(Math.exp((-1.0)*decay_rate*(cur_timestamp-timestamp)));
        setWeight(Math.pow(2,(-1.0)*decay_rate*(cur_timestamp-timestamp)));
    }

    public void setMeasureValue(String measureKey, double value){
        synchronized(measure_values){
            measure_values.put(measureKey, Double.toString(value));
        }
    }

    public void setMeasureValue(String measureKey,String value){
        synchronized(measure_values){
            measure_values.put(measureKey, value);
        }
    }

    public String getMeasureValue(String measureKey){
        if(measure_values.containsKey(measureKey))
            synchronized(measure_values){
                return measure_values.get(measureKey);
            }
        else
            return "";
    }

    public int getTimestamp(){
        return timestamp;
    }
    
    public String getInfo(int x_dim, int y_dim) {
        StringBuffer sb = new StringBuffer();
        sb.append("<html><table>");
        sb.append("<tr><td>Point</td><td>"+timestamp+"</td></tr>");
        for (int i = 0; i < m_AttValues.length-1; i++) {
            String label = "Dim "+i;
            if(i == x_dim)
                 label = "<b>X</b>";
            if(i == y_dim)
                 label = "<b>Y</b>";
            sb.append("<tr><td>"+label+"</td><td>"+value(i)+"</td></tr>");
        }
        sb.append("<tr><td>Decay</td><td>"+weight()+"</td></tr>");
        sb.append("<tr><td>True cluster</td><td>"+classValue()+"</td></tr>");
        sb.append("</table>");
        sb.append("<br>");
        sb.append("<b>Evaluation</b><br>");
        sb.append("<table>");

        TreeSet<String> sortedset;
        synchronized(measure_values){
            sortedset = new TreeSet<String>(measure_values.keySet());
        }

        Iterator miterator = sortedset.iterator();
         while(miterator.hasNext()) {
             String key = (String)miterator.next();
             sb.append("<tr><td>"+key+"</td><td>"+measure_values.get(key)+"</td></tr>");
         }

        sb.append("</table></html>");
        return sb.toString();
    }

    public double getDistance(DataPoint other){
        double distance = 0.0;
        int numDims = numAttributes();
        if(classIndex()!=0) numDims--;

        for (int i = 0; i < numDims; i++) {
            double d = value(i) - other.value(i);
            distance += d * d;
        }
        return Math.sqrt(distance);
    }

//    public DataPoint(Instance instance, int timestamp, Integer timestamp_reference){
//        this.instance = instance;
//    }



}