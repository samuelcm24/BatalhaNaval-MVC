package batalhanaval;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class EndJanela extends JFrame implements ActionListener{
	
	private GameButton[][] btplr = new GameButton[10][10];
	private GameButton[][] btpc = new GameButton[10][10];
	
	private JButton reiniciarJogo, novoJogo, sair;

	private String nome;
	
		EndJanela (Tempos a, String nome, GameButton[][] btplr, GameButton[][] btpc, long minutos, long segundos, int status){
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setTitle("Batalha Naval - Game Over");
		setSize(1024,720);

        setLayout(new GridBagLayout());
        GridBagConstraints propriedades = new GridBagConstraints();
		
		this.nome = nome;
		
		for (int i=0; i<10; i++)
	 	{
	 		for (int j=0; j<10; j++)
	 		{
	 			this.btplr[i][j] = btplr[i][j];
	 			this.btpc[i][j] = btpc[i][j];
	 		}
	 	}
        
        //-----------------PAINEL DE CIMA----------------------------
		
		JPanel pCima = new JPanel();
        pCima.setBackground(new Color(0,0,200));
        pCima.setLayout(new GridLayout(3,0));
        pCima.setVisible(true);
        propriedades.fill = GridBagConstraints.BOTH; 
        propriedades.weightx = 1; 
        propriedades.weighty = 0.1; 
        propriedades.gridx = 0; 
        propriedades.gridy = 0;
        propriedades.gridwidth = 1;
        propriedades.gridheight = 1;

	        JLabel gameover = new JLabel ("GAME OVER");
	        gameover.setFont(new Font("Serif", Font.BOLD, 45));
	        gameover.setForeground(new Color(255,255,0));
	        gameover.setVisible(true);
	        gameover.setHorizontalAlignment(SwingConstants.CENTER);
	        pCima.add(gameover);
	        
	        JLabel divertido = new JLabel ("Esperamos que tenha se divertido!");
	        divertido.setFont(new Font("Serif", Font.ITALIC, 30));
	        divertido.setForeground(new Color(205, 205, 25));
	        divertido.setVisible(true);
	        divertido.setHorizontalAlignment(SwingConstants.CENTER);
	        pCima.add(divertido);
	        
	        JLabel tempoplayer = new JLabel ("");
	        
	        if (status>0)
	        	tempoplayer.setText("Vitória | " + nome + " | " + String.format("%02d", minutos) + ":" + String.format("%02d", segundos));
	        else if (status<0)
	        	tempoplayer.setText("Derrota | " + nome + " | " + String.format("%02d", minutos) + ":" + String.format("%02d", segundos));
	        else
	        	tempoplayer.setText("Desistência | " + nome + " | " + String.format("%02d", minutos) + ":" + String.format("%02d", segundos));
	        
	        tempoplayer.setFont(new Font("Serif", Font.ITALIC + Font.BOLD, 30));
	        tempoplayer.setForeground(Color.ORANGE);
	        tempoplayer.setVisible(true);
	        tempoplayer.setHorizontalAlignment(SwingConstants.CENTER);
	        pCima.add(tempoplayer);
        
		add(pCima, propriedades);

        //-----------------PAINEL DO MEIO----------------------------
		
		JPanel pMeio = new JPanel();
		pMeio.setLayout(new GridLayout(15,0));
        pMeio.setBackground(Color.GRAY);
        pMeio.setVisible(true);
        propriedades.fill = GridBagConstraints.BOTH; 
        propriedades.weightx = 0; 
        propriedades.weighty = 0.8; 
        propriedades.gridx = 0; 
        propriedades.gridy = 1;
        propriedades.gridwidth = 1;
        propriedades.gridheight = 2;
	        
	        JLabel[] ranking = new JLabel[15];
	        
	        Info[] tops = new Info[15];
	        tops = a.retornaTops();
	        
	        
	        for (int i=0; i<15; i++)
	        {
	        	if (tops[i] != null)
	        	{
		        	ranking[i] = new JLabel ("Top " + (i+1) + ": " + tops[i].getNome() + " | " + String.format("%02d", tops[i].getMin()) + ":" + String.format("%02d", tops[i].getSeg()));
		        	ranking[i].setFont( new Font("Courier", Font.BOLD, 20));
		        	ranking[i].setForeground(new Color(25, 15, 25));
		        	ranking[i].setVisible(true);
		        	ranking[i].setHorizontalAlignment(SwingConstants.CENTER);
			        pMeio.add(ranking[i]);
	        	}
	        }

			
        add(pMeio, propriedades);
        
        //-----------------PAINEL DE BAIXO----------------------------
        
		JPanel pBaixo = new JPanel();
        pBaixo.setBackground(Color.LIGHT_GRAY);
        pBaixo.setVisible(true);
        propriedades.fill = GridBagConstraints.BOTH; 
        propriedades.weightx = 0; 
        propriedades.weighty = 0.1; 
        propriedades.gridx = 0; 
        propriedades.gridy = 5;
        propriedades.gridwidth = 1;
        propriedades.gridheight = 1;
        
        	JPanel pBaixoEsq = new JPanel();
        	pBaixoEsq.setBackground(Color.LIGHT_GRAY);
            pBaixoEsq.setVisible(true);

        	reiniciarJogo = new JButton ("Reiniciar Jogo");
        	reiniciarJogo.setPreferredSize(new Dimension(220, 40));
        	reiniciarJogo.addActionListener(this);
        	pBaixoEsq.add(reiniciarJogo);
        	
        	JPanel pBaixoMeio = new JPanel();
        	pBaixoMeio.setBackground(Color.LIGHT_GRAY);
            pBaixoMeio.setVisible(true);

            novoJogo = new JButton ("Novo Jogo");
        	novoJogo.setPreferredSize(new Dimension(220, 40));
        	novoJogo.addActionListener(this);
        	pBaixoMeio.add(novoJogo);
        	
        	JPanel pBaixoDir = new JPanel();
        	pBaixoDir.setBackground(Color.LIGHT_GRAY);
            pBaixoDir.setVisible(true);
            pBaixoDir.setSize(400,400);
            
        	sair = new JButton ("Sair");
        	sair.setPreferredSize(new Dimension(220, 40));
        	sair.addActionListener(this);
        	pBaixoDir.add(sair);
        	
        	
        pBaixo.add(pBaixoEsq);
        pBaixo.add(pBaixoMeio);
        pBaixo.add(pBaixoDir);
        	
        	
        add(pBaixo, propriedades);
		
		setLocationRelativeTo(null);
		setVisible(true);
		}
		
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == reiniciarJogo)
			{
				GameJanela restart = new GameJanela(nome, this.btplr, this.btpc);
				this.dispose();
			}
			
			if (e.getSource() == novoJogo)
			{
				this.dispose();
				StartJanela novo = new StartJanela();
			}
			
			if (e.getSource() == sair)
			{
				this.dispose();
			}
		}

}
		
