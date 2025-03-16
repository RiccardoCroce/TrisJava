package tris;

import java.awt.*; //Label e Panel
import java.awt.event.*;  //ActionListener
import java.util.Random;
import javax.swing.*;

public class Tris {
    //Variabili
    private int larghezza = 850;
    private int lunghezza = 850;
    private int dimensione = 3;
    private int turni = 0;
    private int PuntiX = 0;
    private int PuntiO = 0;
    private int Pareggio = 0;
    private int r,g,b; 
    private String GiocatoreX = "X";
    private String GiocatoreO = "O";
    private String Giocatore = GiocatoreX;
    private boolean FineGioco = false;
    private boolean Vittoria = false;
    
    
    //Implementazioni
    JFrame Finestra = new JFrame("Tris Con Interfaccia Grafica(Con Bottoni inclusi)");
    JLabel Testo = new JLabel();
    JPanel PannelloTitolo = new JPanel();  //il Pannello svolge il ruolo di contenitore, così non interferiamo il titolo con la griglia
    JPanel PannelloGriglia = new JPanel();
    JPanel PannelloAggiuntivo = new JPanel();
    
    JButton[][] GrigliaBottoni = new JButton[dimensione][dimensione];
    JButton Reset = new JButton("Reset");
    
    Random random;
    //Costruttore
    public Tris(){
        Finestra.setVisible(true);
        Finestra.setSize(larghezza, larghezza);
        Finestra.setLocationRelativeTo(null);  //la finestra è bloccata in centro all'avvio
        Finestra.setResizable(false);  //la finestra non si può ridimensionare
        Finestra.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        Testo.setBackground(Color.LIGHT_GRAY);
        Testo.setForeground(Color.black);
        Testo.setFont(new Font("Arial", Font.BOLD, 50));
        Testo.setHorizontalAlignment(JLabel.CENTER);  //Centro
        Testo.setText("--Tris Con Interfaccia Grafica--");
        Testo.setOpaque(true);
        
        PannelloTitolo.setLayout(new BorderLayout()); //creo il bordo del pannello
        PannelloTitolo.add(Testo);
        Finestra.add(PannelloTitolo, BorderLayout.NORTH); //metto sopra il testo scritto dal Label 
        
        PannelloGriglia.setLayout(new GridLayout(3, 3));
        PannelloGriglia.setBackground(Color.gray);
        Finestra.add(PannelloGriglia);
        
        PannelloAggiuntivo.setLayout(new BorderLayout());
        PannelloAggiuntivo.setBackground(Color.red);
        PannelloAggiuntivo.add(Reset);
        Finestra.add(PannelloAggiuntivo, BorderLayout.AFTER_LAST_LINE);
        initGriglia();
        reset();
        
        random = new Random();
        r = random.nextInt(256);
        g = random.nextInt(256);
        b = random.nextInt(256);
    }
    
    //Creo Griglia da giocare
    private void initGriglia(){
        for (int i = 0; i < dimensione; i++) {
            for (int j = 0; j < dimensione; j++) {
               JButton cella = new JButton();
               GrigliaBottoni[i][j] = cella;
               PannelloGriglia.add(cella);
               
               cella.setBackground(Color.darkGray);
               cella.setForeground(Color.white);
               cella.setFont(new Font("Arial", Font.BOLD, 120));
               cella.setFocusable(false);
               
               cella.addActionListener(new ActionListener(){
                   public void actionPerformed(ActionEvent e){
                       if (FineGioco) 
                           return;
                       JButton cella = (JButton) e.getSource();
                       if (cella.getText().equals("")){
                           cella.setText(Giocatore);
                           turni++;
                           ControlloGriglia();
                           if (!FineGioco){
                               Giocatore = Giocatore == GiocatoreX ? GiocatoreO : GiocatoreX; 
                                /*Si può anche scrivere
                                if (Giocatore.equals(GiocatoreX)){
                                    Giocatore = GiocatoreO;
                                }else{
                                    Giocatore = GiocatoreX
                                }
                                */
                               Testo.setText("Tocca a te: "+Giocatore);
                           }
                       }     
                   }
               
               }); 
               
            }
        }
    }
    
    private void reset(){
        Reset.setBackground(Color.black);
        Reset.setForeground(Color.white);
        Reset.setFont(new Font("Arial", Font.BOLD, 20));
        Reset.addActionListener(new ActionListener(){
        public void actionPerformed(ActionEvent f){
            if (f.getSource()==Reset){
                Finestra.remove(PannelloGriglia);
                PannelloGriglia = new JPanel();
                if(Vittoria == true && turni==9){
                    turni++;  
                }
                if (Giocatore.equals(GiocatoreX)&&FineGioco==true&&turni!=9){
                    Giocatore = GiocatoreO;
                    PuntiX++;
                    turni = 0;
                }else if (Giocatore.equals(GiocatoreO)&&FineGioco==true&&turni!=9){
                    Giocatore = GiocatoreX;
                    PuntiO++;
                    turni = 0;
                }else{
                    Pareggio++;
                }
                turni = 0;
                Vittoria = false;
                FineGioco = false;
                Testo.setText("Tocca a te: "+Giocatore);
                PannelloGriglia.setLayout(new GridLayout(3, 3));
                PannelloGriglia.setBackground(Color.gray);
                Finestra.add(PannelloGriglia);
                initGriglia();
                SwingUtilities.updateComponentTreeUI(Finestra);
                System.out.println("Punti di X: " + PuntiX+ "\nPunti di O: " + PuntiO+ "\nPareggi: " + Pareggio);
                System.out.println();
                }
            }
        });
    }
    
    private void ControlloGriglia(){
        //Vittoria Verticale 
        for (int i = 0; i < 3; i++) {
            if (GrigliaBottoni[0][i].getText().equals("")) continue; //bisogna continuare sennò il codice non funziona
            
            if (GrigliaBottoni[0][i].getText().equals(GrigliaBottoni[1][i].getText())&&
                GrigliaBottoni[1][i].getText().equals(GrigliaBottoni[2][i].getText())){
                for (int j = 0; j < 3; j++) {
                    setVincitore(GrigliaBottoni[j][i]);
                }
                FineGioco = true;
                return; //Senza il return succede che quando turni(variabile) arriva a 9 e c'è una vittoria non la conta e dice pareggio 
            }
        }
        
        //Vittoria Orizzontale
        for (int i = 0; i < 3; i++) {
            if (GrigliaBottoni[i][0].getText().equals("")) continue; //bisogna continuare sennò il codice non funziona
            
            if (GrigliaBottoni[i][0].getText().equals(GrigliaBottoni[i][1].getText())&&
                GrigliaBottoni[i][1].getText().equals(GrigliaBottoni[i][2].getText())){
                setVincitore(GrigliaBottoni[i][0]);
                setVincitore(GrigliaBottoni[i][1]);
                setVincitore(GrigliaBottoni[i][2]);
                FineGioco = true;
                return; //Senza il return succede che quando turni(variabile) arriva a 9 e c'è una vittoria non la conta e dice pareggio 
            }
        }
        
        //Vittoria Diagonale Destro
        if (GrigliaBottoni[0][0].getText().equals(GrigliaBottoni[1][1].getText())&&
            GrigliaBottoni[1][1].getText().equals(GrigliaBottoni[2][2].getText())&&
            GrigliaBottoni[0][0].getText()!=""){
            for (int j = 0; j < 3; j++) {
                setVincitore(GrigliaBottoni[j][j]);
            }
            FineGioco = true;
            return; //Senza il return succede che quando turni(variabile) arriva a 9 e c'è una vittoria non la conta e dice pareggio 
            }
        
        
        //Vittoria Diagonale Sinistro
        if (GrigliaBottoni[0][2].getText().equals(GrigliaBottoni[1][1].getText())&&
            GrigliaBottoni[1][1].getText().equals(GrigliaBottoni[2][0].getText())&&
            GrigliaBottoni[0][2].getText() != ""){
            setVincitore(GrigliaBottoni[0][2]);
            setVincitore(GrigliaBottoni[1][1]);
            setVincitore(GrigliaBottoni[2][0]);
            FineGioco = true;
            return; //Senza il return succede che quando turni(variabile) arriva a 9 e c'è una vittoria non la conta e dice pareggio 
            }
        
        //Pareggio
        if (turni == 9){
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    setPareggio(GrigliaBottoni[i][j]);
                }
            }
            FineGioco = true;
        }
    }
    
    private void setVincitore(JButton Cella){
       Cella.setForeground(new Color(r,g,b));
       Cella.setBackground(Color.gray);
       Vittoria = true;
       Testo.setText("Il vincitore e': "+Giocatore);
    }
    
    private void setPareggio(JButton Cella){
        Cella.setForeground(new Color(r,g,b));
        Cella.setForeground(Color.gray);
        Testo.setText("Pareggio");
    }
}