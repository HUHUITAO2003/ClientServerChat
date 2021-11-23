package client;

import java.awt.*;

import javax.swing.JButton;


public class Bottoni extends JButton{//istanza per facilitare la creazione dei bottoni
    public Bottoni(String nome){
        setText(nome);
        setPreferredSize(new Dimension(220,50));
    }
}