package client;

import java.awt.*;

import javax.swing.JButton;


public class Bottoni extends JButton{
    public Bottoni(String nome){
        setText(nome);
        setPreferredSize(new Dimension(330,50));
        setFocusPainted(false);
        setBorderPainted(true);
    }
}