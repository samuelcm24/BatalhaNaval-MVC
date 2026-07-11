package batalhanaval;

import java.awt.event.ActionEvent;
import java.awt.event.*;
import java.awt.*;

import javax.swing.*;

import java.util.Random;

public class SetupJanela extends JFrame implements ActionListener{
	
	private GameButton[][] btplr = new GameButton[10][10];
	private GameButton[][] btpc = new GameButton[10][10];
	
	private String nomedocara;
	private Random rand = new Random();
	private int rnx, rny;
	private boolean gerou=false;
	
	private JRadioButton Rporta;
	private JRadioButton Rescolta;
	private JRadioButton Rcaca;
	private JRadioButton Rsub;
	private JRadioButton Rnulo;
	
	private ImageIcon empty = new ImageIcon ("empty.png");
	private ImageIcon porta1 = new ImageIcon("porta1.png");
	private ImageIcon porta2 = new ImageIcon("porta2.png");
	private ImageIcon porta3 = new ImageIcon("porta3.png");
	private ImageIcon porta4 = new ImageIcon("porta4.png");
	private ImageIcon sub1 = new ImageIcon("sub1.png");
	private ImageIcon sub2 = new ImageIcon("sub2.png");
	private ImageIcon caca1 = new ImageIcon("caca1.png");
	private ImageIcon caca2 = new ImageIcon("caca2.png");
	private ImageIcon escolta1 = new ImageIcon("escolta1.png");
	private ImageIcon escolta2 = new ImageIcon("escolta2.png");
	private ImageIcon escolta3 = new ImageIcon("escolta3.png");
	
	 public SetupJanela(String nome, Boolean random) {
		 if (!random) 
		 {

	        setTitle("BATALHA NAVAL - SETUP BOARD");
	        setSize(1600, 900);
	        setLocationRelativeTo(null);
	        setDefaultCloseOperation(EXIT_ON_CLOSE);
	        this.nomedocara = nome;
	        
	        // -- Definição do leiaute do painel principal
	        setLayout(new GridBagLayout());
	        GridBagConstraints propriedades = new GridBagConstraints();
	        
	        // -- Definição do painel superior superior
	        JPanel pSuperiorsuperior = new JPanel();
	        pSuperiorsuperior.setBackground(Color.BLACK);
	        pSuperiorsuperior.setVisible(true);
	        propriedades.fill = GridBagConstraints.BOTH; 
	        propriedades.weightx = 0; 
	        propriedades.weighty = 0.03; 
	        propriedades.gridx = 0; 
	        propriedades.gridy = 0;
	        propriedades.gridwidth = 3;
	        
	        JLabel superiorlabel = new JLabel (" SETUP BOARD ");
		    superiorlabel.setFont(new Font("Serif", Font.ITALIC + Font.BOLD, 60));
		    superiorlabel.setForeground(new Color(55, 45, 255));
		    pSuperiorsuperior.add(superiorlabel);

	        
	        add(pSuperiorsuperior, propriedades);	        

	        // -- Definição do painel superior esq
	        JPanel pSuperioresq = new JPanel();
	        pSuperioresq.setBackground(new Color(5, 30, 150));
	        pSuperioresq.setVisible(true);
	        propriedades.fill = GridBagConstraints.BOTH; 
	        propriedades.weightx = 0; 
	        propriedades.weighty = 0.06; 
	        propriedades.gridx = 0; 
	        propriedades.gridy = 1;
	        propriedades.gridwidth = 1; 
		    JLabel playername = new JLabel ("Jogador: " + nome);
		    playername.setFont(new Font("Serif", Font.BOLD, 30));
		    playername.setForeground(Color.WHITE);
		        
		    pSuperioresq.add(playername);
	        add(pSuperioresq, propriedades);
	        
	        // -- Definição do painel superior meio-direita
	        JPanel pSuperiormeio = new JPanel();
	        pSuperiormeio.setBackground(new Color(5, 30, 150));
	        pSuperiormeio.setVisible(true);
	        propriedades.fill = GridBagConstraints.BOTH; 
	        propriedades.weightx = 0.03; 
	        propriedades.weighty = 0.06;
	        propriedades.gridx = 1; 
	        propriedades.gridy = 1;
	        propriedades.gridwidth = 2; 
		    JLabel escolha = new JLabel ("Escolha a posição dos seus veículos:");
		    escolha.setFont(new Font("Serif", Font.BOLD, 30));
		    escolha.setForeground(Color.WHITE);
		        
		    pSuperiormeio.add(escolha);
	        add(pSuperiormeio, propriedades);
	        
	        
	        propriedades.gridwidth = 1;
	        
	        // -- Definição do painel dos botoes dos veiculos (esquerda cima)
	        JPanel pEsquerdacima = new JPanel();
	        pEsquerdacima.setBackground(new Color(5, 30, 150));
	        pEsquerdacima.setVisible(true);
	        propriedades.fill = GridBagConstraints.BOTH; 
	        propriedades.weightx = 0.3; 
	        propriedades.weighty = 0; 
	        propriedades.gridx = 0; 
	        propriedades.gridy = 2;

	        ButtonGroup veiculos = new ButtonGroup();
	        
	        Rporta = new JRadioButton("Porta-avião", false);
	        Rescolta = new JRadioButton("Navio de Escolta", false);
	        Rcaca = new JRadioButton("Caça", false);
	        Rsub = new JRadioButton("Submarino", false);
	        
	        Rnulo = new JRadioButton("", false);
	        
	        
	        Rporta.setBackground(new Color(5, 30, 150));
	        Rporta.setForeground(Color.WHITE);
	        Rescolta.setBackground(new Color(5, 30, 150));
	        Rescolta.setForeground(Color.WHITE);
	        Rcaca.setBackground(new Color(5, 30, 150));
	        Rcaca.setForeground(Color.WHITE);
	        Rsub.setBackground(new Color(5, 30, 150));
	        Rsub.setForeground(Color.WHITE);
	        
	        veiculos.add(Rporta);
	        veiculos.add(Rescolta);
	        veiculos.add(Rcaca);
	        veiculos.add(Rsub);
	        veiculos.add(Rnulo);
	        
	        Rporta.setHorizontalTextPosition(SwingConstants.LEFT);
	        Rescolta.setHorizontalTextPosition(SwingConstants.RIGHT);

	        pEsquerdacima.add(Rporta);
	        pEsquerdacima.add(Rescolta);
	        
	        add(pEsquerdacima, propriedades);
	        
	        // -- Definição do painel dos botoes de veiculo (esquerda meio)
	        JPanel pEsquerdameio = new JPanel();
	        pEsquerdameio.setBackground(new Color(5, 30, 150));
	        pEsquerdameio.setVisible(true);
	        propriedades.fill = GridBagConstraints.BOTH; 
	        propriedades.weightx = 0.3; 
	        propriedades.weighty = 0;
	        propriedades.gridx = 0;
	        propriedades.gridy = 3;

	        Rcaca.setHorizontalTextPosition(SwingConstants.LEFT);
	        Rsub.setHorizontalTextPosition(SwingConstants.RIGHT);
	        
	        pEsquerdameio.add(Rcaca);
	        pEsquerdameio.add(Rsub);
	        
	        add(pEsquerdameio, propriedades);

	        
	        // -- Definição do painel dos botao de start (esquerda baixo)
	        JPanel pEsquerdabaixo = new JPanel();
	        pEsquerdabaixo.setBackground(new Color(5, 30, 150));
	        pEsquerdabaixo.setVisible(true);
	        propriedades.fill = GridBagConstraints.BOTH; 
	        propriedades.weightx = 0.3; 
	        propriedades.weighty = 0.2; 
	        propriedades.gridx = 0; 
	        propriedades.gridy = 4;
	        
	        JButton startgame = new JButton("");
	        startgame.setIcon(new ImageIcon ("start.png"));
	        startgame.setPreferredSize(new Dimension(350,100));
	        startgame.addActionListener(this);
	        pEsquerdabaixo.add(startgame);


	        add(pEsquerdabaixo, propriedades);
	        
	        // -- Definição do painel divisor 
	        JPanel pDivisor = new JPanel();
	        pDivisor.setBackground(new Color (0, 162, 232));
	        pDivisor.setVisible(true);
	        propriedades.fill = GridBagConstraints.BOTH; 
	        propriedades.weightx = 0.7; 
	        propriedades.weighty = 0.9; 
	        propriedades.gridx = 1;
	        propriedades.gridy = 2;
	        propriedades.gridwidth = 2;
	        propriedades.gridheight = 3;
	        
	        pDivisor.setLayout(new GridBagLayout());
	        GridBagConstraints c = new GridBagConstraints();
            
            JLabel colunas;
            JLabel linhas;
            c.gridwidth = 1;
            c.weightx = 0;
            c.weighty = 0;
            c.insets = new Insets (0,0,0,0);
            c.ipady = 0;
            c.ipadx = 0;
            
            for(int i=1; i<11; i++)
            {
                c.gridy = 0;
                c.gridx = i;
                colunas = new JLabel(i+"");
                colunas.setFont(new Font ("Serif", Font.BOLD, 20));
                colunas.setForeground(Color.WHITE);
                pDivisor.add(colunas,c);
            }
                
        for (int i=1; i<11; i++) {
                c.gridy = i;
                c.gridx = 0;
                linhas = new JLabel((char)(64+i)+"  ");
                linhas.setFont(new Font ("Serif", Font.BOLD, 20));
                linhas.setForeground(Color.WHITE);
                pDivisor.add(linhas,c);
                
                for (int j=1; j<11; j++) 
                {
                    c.gridx = j;
                    c.gridy = i;
                    btplr[i-1][j-1] = new GameButton ("", "vazio", i-1, j-1);
                    btplr[i-1][j-1].setIcon(empty);
                    btplr[i-1][j-1].setPreferredSize(new Dimension(65,65));
                    btplr[i-1][j-1].addActionListener(this);
                    pDivisor.add(btplr[i-1][j-1],c);
                }
        	}
        	add (pDivisor, propriedades);
        	
	        this.setVisible(true);
		 }// -- FECHAMENTO DO IF (CASO FOR TABULEIRO RANDOM)
		 else
		 {
			 	// -- GERAÇÃO DOS BOTOES DO PLAYER EM OFF - RANDOM (OPTADO)
		        for (int i=0; i<10; i++) {
		        	for (int j=0; j<10; j++) {
		        		btplr[i][j] = new GameButton ("", "vazio", i, j);
	                    btplr[i][j].setIcon(empty);
		        		
		        	}
		        }

		        // -- GERADOR DE VEICULOS DO PLAYER RANDOM
		        rnx = rand.nextInt(7);
		        rny = rand.nextInt(7);

		        btplr[rnx][rny].setIcon(porta1);
		        btplr[rnx][rny+1].setIcon(porta2);
		        btplr[rnx][rny+2].setIcon(porta3);
		        btplr[rnx][rny+3].setIcon(porta4);
		        
		        btplr[rnx][rny].setTipo("porta");
		        btplr[rnx][rny+1].setTipo("porta");
		        btplr[rnx][rny+2].setTipo("porta");
		        btplr[rnx][rny+3].setTipo("porta");
		        
		        btplr[rnx][rny].setText("");
		        btplr[rnx][rny+1].setText("");
		        btplr[rnx][rny+2].setText("");
		        btplr[rnx][rny+3].setText("");
		        
		        
		        gerou = false;
		        while (!gerou)
		        {
			        rnx = rand.nextInt(9);
			        rny = rand.nextInt(9);
			        
			        if ((btplr[rnx][rny].getTipo() == "vazio") 
			        		&& (btplr[rnx][rny+1].getTipo() == "vazio"))
			        {
				        btplr[rnx][rny].setTipo("sub");
				        btplr[rnx][rny+1].setTipo("sub");

				        btplr[rnx][rny].setIcon(sub1);
				        btplr[rnx][rny+1].setIcon(sub2);
				        
				        btplr[rnx][rny].setText("");
				        btplr[rnx][rny+1].setText("");
				        
				        gerou = true;
			        }
			        
		        }
		        
		        gerou = false;
		        while (!gerou)
		        {
			        rnx = rand.nextInt(9);
			        rny = rand.nextInt(9);
			        
			        if ((btplr[rnx][rny].getTipo() == "vazio") 
			        		&& (btplr[rnx][rny+1].getTipo() == "vazio"))
			        {
				        btplr[rnx][rny].setTipo("caca");
				        btplr[rnx][rny+1].setTipo("caca");

				        btplr[rnx][rny].setIcon(caca1);
				        btplr[rnx][rny+1].setIcon(caca2);
				        
				        btplr[rnx][rny].setText("");
				        btplr[rnx][rny+1].setText("");
				        
				        gerou = true;
			        }
			        
		        }
		        
		        gerou = false;
		        while (!gerou)
		        {
			        rnx = rand.nextInt(8);
			        rny = rand.nextInt(8);
			        
			        if ((btplr[rnx][rny].getTipo() == "vazio") 
			        		&& (btplr[rnx][rny+1].getTipo() == "vazio")
			        		&& (btplr[rnx][rny+2].getTipo() == "vazio"))
			        {
				        btplr[rnx][rny].setTipo("escolta");
				        btplr[rnx][rny+1].setTipo("escolta");
				        btplr[rnx][rny+2].setTipo("escolta");
				        
				        btplr[rnx][rny].setIcon(escolta1);
				        btplr[rnx][rny+1].setIcon(escolta2);
				        btplr[rnx][rny+2].setIcon(escolta3);
				        
				        btplr[rnx][rny].setText("");
				        btplr[rnx][rny+1].setText("");
				        btplr[rnx][rny+2].setText("");

				        gerou = true;
			        }
			        
		        }
			        
		        }
		        
		        // -- GERAÇÃO DOS BOTOES DO PC EM OFF - SEMPRE RANDOM 
		        for (int i=0; i<10; i++) {
		        	for (int j=0; j<10; j++) {
		        		btpc[i][j] = new GameButton ("", "vazio", i, j);
	                    btpc[i][j].setIcon(empty);
		        	}
		        }
		        // -- GERADOR DE VEICULOS RANDOM PRO PC
		        rnx = rand.nextInt(7);
		        rny = rand.nextInt(7);

		        btpc[rnx][rny].setTipo("porta");
		        btpc[rnx][rny+1].setTipo("porta");
		        btpc[rnx][rny+2].setTipo("porta");
		        btpc[rnx][rny+3].setTipo("porta");
		        
		        gerou = false;
		        while (!gerou)
		        {
			        rnx = rand.nextInt(9);
			        rny = rand.nextInt(9);
			        
			        if ((btpc[rnx][rny].getTipo() == "vazio") 
			        		&& (btpc[rnx][rny+1].getTipo() == "vazio"))
			        {
				        btpc[rnx][rny].setTipo("sub");
				        btpc[rnx][rny+1].setTipo("sub");
				        
				        gerou = true;
			        }
			        
		        }
		        
		        gerou = false;
		        while (!gerou)
		        {
			        rnx = rand.nextInt(9);
			        rny = rand.nextInt(9);
			        
			        if ((btpc[rnx][rny].getTipo() == "vazio") 
			        		&& (btpc[rnx][rny+1].getTipo() == "vazio"))
			        {
				        btpc[rnx][rny].setTipo("caca");
				        btpc[rnx][rny+1].setTipo("caca");

				        gerou = true;
			        }
			        
		        }
		        
		        gerou = false;
		        while (!gerou)
		        {
			        rnx = rand.nextInt(8);
			        rny = rand.nextInt(8);
			        
			        if ((btpc[rnx][rny].getTipo() == "vazio") 
			        		&& (btpc[rnx][rny+1].getTipo() == "vazio")
			        		&& (btpc[rnx][rny+2].getTipo() == "vazio"))
			        {
				        btpc[rnx][rny].setTipo("escolta");
				        btpc[rnx][rny+1].setTipo("escolta");
				        btpc[rnx][rny+2].setTipo("escolta");

				        gerou = true;
			        }
		        }

		        
		        if (random)
		        {
			        this.nomedocara = nome;
			        
			        GameJanela game = new GameJanela(nomedocara, btplr, btpc);
		        }
		 }
	 
	 
	 public void actionPerformed(ActionEvent e) 
	 {
		 
		 if (e.getSource().getClass() == GameButton.class)
		 {
			 if (Rporta.isSelected() || Rcaca.isSelected() || Rsub.isSelected() || Rescolta.isSelected())
			 {
				 rnx = ((GameButton)e.getSource()).getL();
				 rny = ((GameButton)e.getSource()).getC();
				 
				 if (Rporta.isSelected())
				 {
					 if (rny<7 && btplr[rnx][rny].getTipo() == "vazio"
							 	&& btplr[rnx][rny+1].getTipo() == "vazio"
							 	&& btplr[rnx][rny+2].getTipo() == "vazio"
							 	&& btplr[rnx][rny+3].getTipo() == "vazio")
					 {
					 	btplr[rnx][rny].setIcon(porta1);
				        btplr[rnx][rny+1].setIcon(porta2);
				        btplr[rnx][rny+2].setIcon(porta3);
				        btplr[rnx][rny+3].setIcon(porta4);
				        
				        btplr[rnx][rny].setDisabledIcon(porta1);
				        btplr[rnx][rny+1].setDisabledIcon(porta2);
				        btplr[rnx][rny+2].setDisabledIcon(porta3);
				        btplr[rnx][rny+3].setDisabledIcon(porta4);
				        
				        btplr[rnx][rny].setTipo("porta");
				        btplr[rnx][rny+1].setTipo("porta");
				        btplr[rnx][rny+2].setTipo("porta");
				        btplr[rnx][rny+3].setTipo("porta");
				        
				        btplr[rnx][rny].setText("");
				        btplr[rnx][rny+1].setText("");
				        btplr[rnx][rny+2].setText("");
				        btplr[rnx][rny+3].setText("");
				        
				        btplr[rnx][rny].setEnabled(false);
				        btplr[rnx][rny+1].setEnabled(false);
				        btplr[rnx][rny+2].setEnabled(false);
				        btplr[rnx][rny+3].setEnabled(false);
				        
				        Rnulo.setSelected(true);
				        Rporta.setEnabled(false);
					 }
					 else
						JOptionPane.showMessageDialog(null, "Esta posiçao não é válida para o Porta-avião (4 células).");
				 }
				 
				 
				 if (Rcaca.isSelected())
				 {
					 if (rny<9 && btplr[rnx][rny].getTipo() == "vazio"
							 	&& btplr[rnx][rny+1].getTipo() == "vazio")
					 {
					 	btplr[rnx][rny].setTipo("caca");
				        btplr[rnx][rny+1].setTipo("caca");
	
				        btplr[rnx][rny].setIcon(caca1);
				        btplr[rnx][rny+1].setIcon(caca2);
				        
				        btplr[rnx][rny].setDisabledIcon(caca1);
				        btplr[rnx][rny+1].setDisabledIcon(caca2);
				        
				        btplr[rnx][rny].setText("");
				        btplr[rnx][rny+1].setText("");
				        
				        btplr[rnx][rny].setEnabled(false);
				        btplr[rnx][rny+1].setEnabled(false);
				        
				        Rnulo.setSelected(true);
				        Rcaca.setEnabled(false);
					 }
					 else
						 JOptionPane.showMessageDialog(null, "Esta posiçao não é válida para o Caça (2 células).");
				 }
				 
				 if (Rsub.isSelected())
				 {
					 if (rny<9 && btplr[rnx][rny].getTipo() == "vazio"
							 	&& btplr[rnx][rny+1].getTipo() == "vazio")
					 {
					 	btplr[rnx][rny].setTipo("sub");
				        btplr[rnx][rny+1].setTipo("sub");
	
				        btplr[rnx][rny].setIcon(sub1);
				        btplr[rnx][rny+1].setIcon(sub2);
				        
				        btplr[rnx][rny].setDisabledIcon(sub1);
				        btplr[rnx][rny+1].setDisabledIcon(sub2);
				        
				        btplr[rnx][rny].setText("");
				        btplr[rnx][rny+1].setText("");
				        
				        btplr[rnx][rny].setEnabled(false);
				        btplr[rnx][rny+1].setEnabled(false);
				        
				        Rnulo.setSelected(true);
				        Rsub.setEnabled(false);
					 }
				        else
							 JOptionPane.showMessageDialog(null, "Esta posiçao não é válida para o Submarino (2 células).");
				 }
				 
				 if (Rescolta.isSelected())
				 {
					 if (rny<8 && btplr[rnx][rny].getTipo() == "vazio"
							 	&& btplr[rnx][rny+1].getTipo() == "vazio"
							 	&& btplr[rnx][rny+2].getTipo() == "vazio")
					 {
					 	btplr[rnx][rny].setTipo("escolta");
				        btplr[rnx][rny+1].setTipo("escolta");
				        btplr[rnx][rny+2].setTipo("escolta");
				        
				        btplr[rnx][rny].setIcon(escolta1);
				        btplr[rnx][rny+1].setIcon(escolta2);
				        btplr[rnx][rny+2].setIcon(escolta3);
				        
				        btplr[rnx][rny].setDisabledIcon(escolta1);
				        btplr[rnx][rny+1].setDisabledIcon(escolta2);
				        btplr[rnx][rny+2].setDisabledIcon(escolta3);
				        
				        btplr[rnx][rny].setText("");
				        btplr[rnx][rny+1].setText("");
				        btplr[rnx][rny+2].setText("");
				        
				        btplr[rnx][rny].setEnabled(false);
				        btplr[rnx][rny+1].setEnabled(false);
				        btplr[rnx][rny+2].setEnabled(false);
				        
				        Rnulo.setSelected(true);
				        Rescolta.setEnabled(false);
					 }
					 else
						 JOptionPane.showMessageDialog(null, "Esta posiçao não é válida para o Navio de escolta (3 células).");
				 }
			 }
			 else
			 {
				 JOptionPane.showMessageDialog(null, "Escolha um veículo para posicionar!");
			 }
		 }
		 
		 
		 if (e.getSource().getClass() == JButton.class)
		 {
	    	if (Rporta.isEnabled() || Rcaca.isEnabled() || Rsub.isEnabled() || Rescolta.isEnabled()) 
	    	{
	    		JOptionPane.showMessageDialog(null, "Você deve utilizar todos veículos!");
	    	}
	    	else 
	    	{
	    			for (int i=0; i<10; i++)
	    			{
	    				for (int j=0; j<10; j++)
	    				{
	    					btplr[i][j].removeActionListener(this);
	    					btplr[i][j].setEnabled(true);
	    				}
	    			}

	    			GameJanela game = new GameJanela(nomedocara, btplr, btpc);
	    			
	    			this.dispose();
	    	}
	    }
	 	
	 }
}
