import javax.swing.*;
 public class Principal{
public static void main(String[] args){int arreglo[] = {1,2,3,6,7};int count;count=0;double avg;avg=0.0;while(count<5){avg=arreglo[count]+avg;count++;}avg=avg/5;JOptionPane.showMessageDialog(null,"El promedio de los valores del arreglo es: "+avg);}
}