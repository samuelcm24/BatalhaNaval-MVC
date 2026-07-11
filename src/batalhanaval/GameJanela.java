package batalhanaval;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.JRadioButton;
import javax.swing.JOptionPane;

import java.util.Random;

public class GameJanela extends JFrame implements ActionListener{
	
	private GameButton[][] btplr = new GameButton[10][10];
	private GameButton[][] btpc = new GameButton[10][10];
	
    private GameButton[][] btplrcpy = new GameButton[10][10];
	private GameButton[][] btpccpy = new GameButton[10][10];
	
	private JRadioButton disparosimples;
	private JRadioButton disparocascata;
	private JRadioButton disparoestrela;
	private JRadioButton disparoportaavioes;
	private JRadioButton radioDica;
	private JRadioButton radioNulo;
    
    private JButton sair;

    private int x, y, rodadas=0, rodadasplr=0, rodadaspc=0, dicas=3, vportaplr=4, vcacaplr=2, vescoltaplr=3, vsubplr=2, vportapc=4, vcacapc=2, vescoltapc=3, vsubpc=2;
    private boolean portapcatirar=true;
    
    private Random rand = new Random();

    private ImageIcon vazio = new ImageIcon ("vazio.png");
    private ImageIcon hit = new ImageIcon ("hit.png");
    
    private Cronometro cronometro;
    private Thread t;
    private Tempos tempos = new Tempos();
    private String nome;
	
	 public GameJanela(String nome, GameButton[][] btplr, GameButton[][] btpc) {
		 	
		 	for (int i=0; i<10; i++)
		 	{
		 		for (int j=0; j<10; j++)
		 		{
		 			this.btplrcpy[i][j] = new GameButton (btplr[i][j]);
		 			this.btpccpy[i][j] = new GameButton (btpc[i][j]);
		 		}
		 	}
		 	
		 	this.nome = nome;
	        // -- Caracteristicas da Janela Principal 
	        setTitle("BATALHA NAVAL - GAME");
	        setSize(1600,900);
	        setLocationRelativeTo(null);
	        setDefaultCloseOperation(EXIT_ON_CLOSE);
	        
	        // -- Definição do leiaute do painel principal 
	        setLayout(new GridBagLayout());
	        // -- Objeto que mantém as propriedades do leiaute aplicado ao painel 
	        GridBagConstraints propriedades = new GridBagConstraints();
	        
	        // -- Definição do painel superior superior
	        JPanel pSuperiorsuperior = new JPanel();
	        pSuperiorsuperior.setBackground(Color.GRAY);
	        pSuperiorsuperior.setVisible(true);
	        propriedades.fill = GridBagConstraints.BOTH; 
	        propriedades.weightx = 0; 
	        propriedades.weighty = 0.03;
	        propriedades.gridx = 1; 
	        propriedades.gridy = 0;
	        propriedades.gridwidth = 2; 

	        JLabel superiorlabel = new JLabel ("BATALHA NAVAL   ");
		    superiorlabel.setFont(new Font("Serif", Font.BOLD, 60));
		    superiorlabel.setForeground(Color.BLUE);
		    pSuperiorsuperior.add(superiorlabel);

		    sair = new JButton("");
		    sair.setIcon(new ImageIcon("exitsign.png"));
		    sair.setPreferredSize(new Dimension (160,50));
		    sair.addActionListener(this);
		    pSuperiorsuperior.add(sair);
	        
	        add(pSuperiorsuperior, propriedades);	        
	        
	        // -- Definição do painel superior superior esq
	        JPanel pSuperiorsuperioresq = new JPanel();
	        pSuperiorsuperioresq.setLayout(new GridLayout(2,0));
	        pSuperiorsuperioresq.setBackground(Color.BLACK);
	        pSuperiorsuperioresq.setVisible(true);
	        propriedades.fill = GridBagConstraints.BOTH; 
	        propriedades.weightx = 0.2; 
	        propriedades.weighty = 0.03; 
	        propriedades.gridx = 0; 
	        propriedades.gridy = 0;
	        propriedades.gridwidth = 1; 

	        JLabel playername = new JLabel ("Jogador: " + nome);
	        playername.setFont(new Font("Serif", Font.BOLD, 35));
	        playername.setForeground(Color.RED);
	        playername.setHorizontalAlignment(SwingConstants.CENTER);
	        playername.setVerticalAlignment(SwingConstants.CENTER);
		    pSuperiorsuperioresq.add(playername);
		    
		    JLabel timecounting = new JLabel ("");
		    timecounting.setFont(new Font("Serif", Font.BOLD, 25));
		    timecounting.setForeground(Color.GREEN);
		    timecounting.setHorizontalAlignment(SwingConstants.CENTER);
		    timecounting.setVerticalAlignment(SwingConstants.CENTER);
		    pSuperiorsuperioresq.add(timecounting);
		    
	        
	        add(pSuperiorsuperioresq, propriedades);
	        
	        // -- Definição do painel superior esqalto
	        JPanel pSuperioresqalto = new JPanel();
	        pSuperioresqalto.setBackground(Color.BLACK);
	        pSuperioresqalto.setVisible(true);
	        propriedades.fill = GridBagConstraints.BOTH; 
	        propriedades.weightx = 0; 
	        propriedades.weighty = 0; 
	        propriedades.gridx = 0;
	        propriedades.gridy = 1;
	        propriedades.gridwidth = 1; 

	        JLabel labelDisparos = new JLabel ("Selecione seu disparo:");
	        labelDisparos.setFont(new Font("Serif", Font.BOLD, 20));
	        labelDisparos.setForeground(Color.RED);
	        
	        pSuperioresqalto.add(labelDisparos);
	        add (pSuperioresqalto, propriedades);
	        
	        // -- Definição do painel superior esqbaixo
	        JPanel pSuperioresqbaixo = new JPanel();
	        pSuperioresqbaixo.setBackground(Color.BLACK);
	        pSuperioresqbaixo.setVisible(true);
	        propriedades.fill = GridBagConstraints.BOTH;
	        propriedades.weightx = 0; 
	        propriedades.weighty = 0; 
	        propriedades.gridx = 0; 
	        propriedades.gridy = 2;
	        propriedades.gridwidth = 1;
	        
	        ButtonGroup disparos = new ButtonGroup();
	        
	        disparosimples = new JRadioButton("Disparo Unico", false);
	        disparocascata = new JRadioButton("Disparo Cascata", false);
	        disparoestrela = new JRadioButton("Disparo Estrela", false);
	        disparoportaavioes = new JRadioButton("Disparo Porta Aviões", false);
	        radioDica = new JRadioButton("Dica", false);
	        radioNulo = new JRadioButton("", false);

	        disparosimples.setBackground(Color.BLACK);
	        disparosimples.setForeground(Color.RED);
	        disparocascata.setBackground(Color.BLACK);
	        disparocascata.setForeground(Color.RED);
	        disparoestrela.setBackground(Color.BLACK);
	        disparoestrela.setForeground(Color.RED);
	        disparoportaavioes.setBackground(Color.BLACK);
	        disparoportaavioes.setForeground(Color.RED);
	        radioDica.setBackground(Color.BLACK);
	        radioDica.setForeground(Color.CYAN);
	        
	        disparos.add(disparosimples);
	        disparos.add(disparocascata);
	        disparos.add(disparoestrela);
	        disparos.add(disparoportaavioes);
	        disparos.add(radioDica);
	        disparos.add(radioNulo);

	        pSuperioresqbaixo.add(disparosimples);
	        pSuperioresqbaixo.add(disparocascata);
	        pSuperioresqbaixo.add(disparoestrela);
	        pSuperioresqbaixo.add(disparoportaavioes);
	        pSuperioresqbaixo.add(radioDica);

	        add(pSuperioresqbaixo, propriedades);
	        
	        // -- Definição do painel superior meio
	        JPanel pSuperiormeio = new JPanel();
	        pSuperiormeio.setBackground(Color.GRAY);
	        pSuperiormeio.setVisible(true);
	        propriedades.fill = GridBagConstraints.BOTH; 
	        propriedades.weightx = 0; 
	        propriedades.weighty = 0; 
	        propriedades.gridx = 1; 
	        propriedades.gridy = 1;
	        propriedades.gridwidth = 1;
	        propriedades.gridheight = 2;
		    JLabel xxx = new JLabel ("vs.");
		    xxx.setFont(new Font("Serif", Font.BOLD, 30));
		    xxx.setForeground(Color.BLACK);
		        
		    pSuperiormeio.add(xxx);
	        add(pSuperiormeio, propriedades);
	        
	        propriedades.gridheight = 1;
	        
	        // -- Definição do painel superior direita
	        JPanel pSuperiordir = new JPanel();
	        pSuperiordir.setBackground(new Color(215, 0, 0));
	        pSuperiordir.setVisible(true);
	        propriedades.fill = GridBagConstraints.BOTH; 
	        propriedades.weightx = 0;
	        propriedades.weighty = 0;
	        propriedades.gridx = 2;
	        propriedades.gridy = 1;
	        propriedades.gridwidth = 1;
	        propriedades.gridheight = 2;
		    JLabel adversario = new JLabel ("Adversário (computador)");
		    adversario.setFont(new Font("Tahoma", Font.BOLD, 35));
		    adversario.setForeground(Color.BLACK);
		        
		    pSuperiordir.add(adversario);
	        add(pSuperiordir, propriedades);
	        
	        propriedades.gridheight = 1;
	        propriedades.gridwidth = 1; 
	        
	        // -- Definição do painel esquerdo
	        JPanel pEsquerda = new JPanel();
	        pEsquerda.setBackground(new Color (0, 162, 232));
	        pEsquerda.setVisible(true);
	        propriedades.fill = GridBagConstraints.BOTH; 
	        propriedades.weightx = 0.45; 
	        propriedades.weighty = 0.9; 
	        propriedades.gridx = 0; 
	        propriedades.gridy = 3;
	        
	        pEsquerda.setLayout(new GridBagLayout());
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
                pEsquerda.add(colunas,c);
            }
                
        for (int i=1; i<11; i++) {
                c.gridy = i;
                c.gridx = 0;
                linhas = new JLabel((char)(64+i)+"  ");
                linhas.setFont(new Font ("Serif", Font.BOLD, 20));
                linhas.setForeground(Color.WHITE);
                pEsquerda.add(linhas,c);
                
                for (int j=1; j<11; j++) 
                {
                    c.gridx = j;
                    c.gridy = i;
                    this.btplr[i-1][j-1] = btplr[i-1][j-1];
                    this.btplr[i-1][j-1].setPreferredSize(new Dimension(65,65));
                    
                    pEsquerda.add(this.btplr[i-1][j-1],c);
                }
        }

        add(pEsquerda, propriedades);
	        
	        // -- Definição do painel divisor
	        JPanel pDivisor = new JPanel();
	        pDivisor.setBackground(Color.GRAY);
	        pDivisor.setVisible(true);
	        propriedades.fill = GridBagConstraints.BOTH; 
	        propriedades.weightx = 0.03; 
	        propriedades.weighty = 0.9; 
	        propriedades.gridx = 1;
	        propriedades.gridy = 3;
	        add(pDivisor, propriedades); 
	        
	        // -- Definição do painel direita
	        JPanel pDireita = new JPanel();
	        pDireita.setBackground(new Color(215, 0, 0));
	        pDireita.setVisible(true);
	        propriedades.fill = GridBagConstraints.BOTH;
	        propriedades.weightx = 0.45;
	        propriedades.weighty = 0.9;
	        propriedades.gridx = 2;
	        propriedades.gridy = 3;
	        
	        pDireita.setLayout(new GridBagLayout());

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
                pDireita.add(colunas,c);
            }
                
        for (int i=1; i<11; i++) {
                c.gridy = i;
                c.gridx = 0;
                linhas = new JLabel((char)(64+i)+"  ");
                linhas.setFont(new Font ("Serif", Font.BOLD, 20));
                linhas.setForeground(Color.WHITE);
                pDireita.add(linhas,c);
                
                for (int j=1; j<11; j++) 
                {
                    c.gridx = j;
                    c.gridy = i;
                    this.btpc[i-1][j-1] = btpc[i-1][j-1];
                    this.btpc[i-1][j-1].setPreferredSize(new Dimension(65,65));
                    pDireita.add(this.btpc[i-1][j-1],c);
                    this.btpc[i-1][j-1].addActionListener(this);
                }
        }

        add(pDireita, propriedades);
	        
	        this.setVisible(true);
	        
	        // -- Lendo o arquivo com os tempos
	        tempos.ler();


	        // -- Iniciando a thread que cronometra o tempo
	        cronometro = new Cronometro(timecounting);
	        t = new Thread(cronometro);
	        t.start();
	    }
	    
	 public void actionPerformed(ActionEvent e) {
		 
		 
		 if (e.getSource().getClass() == (GameButton.class)) 
         {

             x = ((GameButton)e.getSource()).getL();
             y = ((GameButton)e.getSource()).getC();
            

            	 
             if (disparosimples.isSelected() 
            		 || disparocascata.isSelected()
            		 || disparoestrela.isSelected()
            		 || disparoportaavioes.isSelected())
             {
            	 rodadaspc++;
            	 
	             if(disparosimples.isSelected())
	             {
	                 rodadas++;
	                 if(rodadas==2 || rodadasplr==2)
	                    disparoportaavioes.setEnabled(true);
	                 
	                 btpc[x][y].setEnabled(false);
	                 
	                 switch (btpc[x][y].getTipo()) {
	                 case "porta":
	                	 vportapc--;
	                	 btpc[x][y].setText("");
	                	 btpc[x][y].setIcon(hit);
	                	 btpc[x][y].setDisabledIcon(hit);
	                	 break;
	                	 
	                 case "caca":
	                	 vcacapc--;
	                	 btpc[x][y].setText("");
	                	 btpc[x][y].setIcon(hit);
	                	 btpc[x][y].setDisabledIcon(hit);
	                	 break;
	                	 
	                 case "escolta":
	                	 vescoltapc--;
	                	 btpc[x][y].setText("");
	                	 btpc[x][y].setIcon(hit);
	                	 btpc[x][y].setDisabledIcon(hit);
	                	 break;
	                	 
	                 case "sub":
	                	 vsubpc--;
	                	 btpc[x][y].setText("");
	                	 btpc[x][y].setIcon(hit);
	                	 btpc[x][y].setDisabledIcon(hit);
	                	 break;
	                	 
	                 default:
	                	 btpc[x][y].setText("");
	                	 btpc[x][y].setIcon(vazio);
	                	 btpc[x][y].setDisabledIcon(vazio);
	                }
	
	             }
	             else if(disparocascata.isSelected())
	             {
	                rodadas++;
	                if(rodadas==2 || rodadasplr==2)
	                    disparoportaavioes.setEnabled(true);
	                
	                 btpc[x][y].setEnabled(false);
	                 switch (btpc[x][y].getTipo()) {
	                 case "porta":
	                	 vportapc--;
	                	 btpc[x][y].setText("");
	                	 btpc[x][y].setIcon(hit);
	                	 btpc[x][y].setDisabledIcon(hit);
	                	 break;
	                	 
	                 case "caca":
	                	 vcacapc--;
	                	 btpc[x][y].setText("");
	                	 btpc[x][y].setIcon(hit);
	                	 btpc[x][y].setDisabledIcon(hit);
	                	 break;
	                	 
	                 case "escolta":
	                	 vescoltapc--;
	                	 btpc[x][y].setText("");
	                	 btpc[x][y].setIcon(hit);
	                	 btpc[x][y].setDisabledIcon(hit);
	                	 break;
	                	 
	                 case "sub":
	                	 vsubpc--;
	                	 btpc[x][y].setText("");
	                	 btpc[x][y].setIcon(hit);
	                	 btpc[x][y].setDisabledIcon(hit);
	                	 break;
	                	 
	                 default:
	                	 btpc[x][y].setText("");
	                	 btpc[x][y].setIcon(vazio);
	                	 btpc[x][y].setDisabledIcon(vazio);
	                }
	                 if (y<9 && btpc[x][y+1].isEnabled()) 
	                 {
		                 btpc[x][y+1].setEnabled(false);
		                 
		                 switch (btpc[x][y+1].getTipo()) {
		                 case "porta":
		                	 vportapc--;
		                	 btpc[x][y+1].setText("");
		                	 btpc[x][y+1].setIcon(hit);
		                	 btpc[x][y+1].setDisabledIcon(hit);
		                	 break;
		                	 
		                 case "caca":
		                	 vcacapc--;
		                	 btpc[x][y+1].setText("");
		                	 btpc[x][y+1].setIcon(hit);
		                	 btpc[x][y+1].setDisabledIcon(hit);
		                	 break;
		                	 
		                 case "escolta":
		                	 vescoltapc--;
		                	 btpc[x][y+1].setText("");
		                	 btpc[x][y+1].setIcon(hit);
		                	 btpc[x][y+1].setDisabledIcon(hit);
		                	 break;
		                	 
		                 case "sub":
		                	 vsubpc--;
		                	 btpc[x][y+1].setText("");
		                	 btpc[x][y+1].setIcon(hit);
		                	 btpc[x][y+1].setDisabledIcon(hit);
		                	 break;
		                	 
		                 default:
		                	 btpc[x][y+1].setText("");
		                	 btpc[x][y+1].setIcon(vazio);
		                	 btpc[x][y+1].setDisabledIcon(vazio);
		                }
	                 }
	             }
	             else if(disparoestrela.isSelected())
	             {
	                rodadas++;
	                if(rodadas==2 || rodadasplr==2)
	                    disparoportaavioes.setEnabled(true);
	                
	                btpc[x][y].setEnabled(false);
	                
	                switch (btpc[x][y].getTipo()) {
	                case "porta":
			             vportapc--;
			             btpc[x][y].setText("");
			             btpc[x][y].setIcon(hit);
			             btpc[x][y].setDisabledIcon(hit);
		               	 break;
	               	 
	                case "caca":
		               	 vcacapc--;
		               	 btpc[x][y].setText("");
		               	 btpc[x][y].setIcon(hit);
		               	 btpc[x][y].setDisabledIcon(hit);
		               	 break;
		               	 
	                case "escolta":
		               	 vescoltapc--;
		               	 btpc[x][y].setText("");
		               	 btpc[x][y].setIcon(hit);
		               	 btpc[x][y].setDisabledIcon(hit);
		               	 break;
	               	 
	                case "sub":
		               	 vsubpc--;
		               	 btpc[x][y].setText("");
		               	 btpc[x][y].setIcon(hit);
		               	 btpc[x][y].setDisabledIcon(hit);
		               	 break;
	               	 
	                default:
	                	btpc[x][y].setText("");
	                	btpc[x][y].setIcon(vazio);
	                	btpc[x][y].setDisabledIcon(vazio);
	               }
	                
	                if(x>0 && btpc[x-1][y].isEnabled())
	                {
	                	btpc[x-1][y].setEnabled(false);
	                	switch (btpc[x-1][y].getTipo()) {
	                    case "porta":
		                   	 vportapc--;
		                   	 btpc[x-1][y].setText("");
		                	 btpc[x-1][y].setIcon(hit);
		                	 btpc[x-1][y].setDisabledIcon(hit);
		                	 break;
	                   	 
	                    case "caca":
		                   	 vcacapc--;
		                   	 btpc[x-1][y].setText("");
		                	 btpc[x-1][y].setIcon(hit);
		                	 btpc[x-1][y].setDisabledIcon(hit);
		                   	 break;
	                   	 
	                    case "escolta":
		                   	 vescoltapc--;
		                   	 btpc[x-1][y].setText("");
		                	 btpc[x-1][y].setIcon(hit);
		                	 btpc[x-1][y].setDisabledIcon(hit);
		                   	 break;
	                   	 
	                    case "sub":
		                   	 vsubpc--;
		                   	 btpc[x-1][y].setText("");
		                	 btpc[x-1][y].setIcon(hit);
		                	 btpc[x-1][y].setDisabledIcon(hit);
		                   	 break;
	                   	 
	                    default:
		                    btpc[x-1][y].setText("");
			                btpc[x-1][y].setIcon(vazio);
			                btpc[x-1][y].setDisabledIcon(vazio);
	                   }
	                }
	                if(x<9 && btpc[x+1][y].isEnabled())
	                {
	                	btpc[x+1][y].setEnabled(false);
	                	switch (btpc[x+1][y].getTipo()) {
	                    case "porta":
		                   	 vportapc--;
		                   	 btpc[x+1][y].setText("");
		                	 btpc[x+1][y].setIcon(hit);
		                	 btpc[x+1][y].setDisabledIcon(hit);
		                   	 break;
	                   	 
	                    case "caca":
		                   	 vcacapc--;
		                   	 btpc[x+1][y].setText("");
		                	 btpc[x+1][y].setIcon(hit);
		                	 btpc[x+1][y].setDisabledIcon(hit);
		                   	 break;
	                   	 
	                    case "escolta":
		                   	 vescoltapc--;
		                   	 btpc[x+1][y].setText("");
		                	 btpc[x+1][y].setIcon(hit);
		                	 btpc[x+1][y].setDisabledIcon(hit);
		                   	 break;
	                   	 
	                    case "sub":
		                   	 vsubpc--;
		                   	 btpc[x+1][y].setText("");
		                	 btpc[x+1][y].setIcon(hit);
		                	 btpc[x+1][y].setDisabledIcon(hit);
		                   	 break;
	                   	 
	                    default:
	                    	 btpc[x+1][y].setText("");
		                	 btpc[x+1][y].setIcon(vazio);
		                	 btpc[x+1][y].setDisabledIcon(vazio);
	                   }
	                }
	                if(y>0 && btpc[x][y-1].isEnabled())
	                {
	                	btpc[x][y-1].setEnabled(false);
	                	switch (btpc[x][y-1].getTipo()) {
	                    case "porta":
	                   	 vportapc--;
	                   	btpc[x][y-1].setText("");
	                	 btpc[x][y-1].setIcon(hit);
	                	 btpc[x][y-1].setDisabledIcon(hit);
	                   	 break;
	                   	 
	                    case "caca":
	                   	 vcacapc--;
	                   	btpc[x][y-1].setText("");
	                	 btpc[x][y-1].setIcon(hit);
	                	 btpc[x][y-1].setDisabledIcon(hit);
	                   	 break;
	                   	 
	                    case "escolta":
	                   	 vescoltapc--;
	                   	btpc[x][y-1].setText("");
	                	 btpc[x][y-1].setIcon(hit);
	                	 btpc[x][y-1].setDisabledIcon(hit);
	                   	 break;
	                   	 
	                    case "sub":
	                   	 vsubpc--;
	                   	btpc[x][y-1].setText("");
	                	 btpc[x][y-1].setIcon(hit);
	                	 btpc[x][y-1].setDisabledIcon(hit);
	                   	 break;
	                   	 
	                    default:
	                    	btpc[x][y-1].setText("");
		                	 btpc[x][y-1].setIcon(vazio);
		                	 btpc[x][y-1].setDisabledIcon(vazio);
	                   }
	                }
	                if(y<9 && btpc[x][y+1].isEnabled())
	                {
	                	btpc[x][y+1].setEnabled(false);
	                	switch (btpc[x][y+1].getTipo()) {
	                    case "porta":
	                   	 vportapc--;
	                   	btpc[x][y+1].setText("");
	                	 btpc[x][y+1].setIcon(hit);
	                	 btpc[x][y+1].setDisabledIcon(hit);
	                   	 break;
	                   	 
	                    case "caca":
	                   	 vcacapc--;
	                   	btpc[x][y+1].setText("");
	                	 btpc[x][y+1].setIcon(hit);
	                	 btpc[x][y+1].setDisabledIcon(hit);
	                   	 break;
	                   	 
	                    case "escolta":
	                   	 vescoltapc--;
	                   	btpc[x][y+1].setText("");
	                	 btpc[x][y+1].setIcon(hit);
	                	 btpc[x][y+1].setDisabledIcon(hit);
	                   	 break;
	                   	 
	                    case "sub":
	                   	 vsubpc--;
	                   	btpc[x][y+1].setText("");
	                	 btpc[x][y+1].setIcon(hit);
	                	 btpc[x][y+1].setDisabledIcon(hit);
	                   	 break;
	                   	 
	                    default:
	                    	btpc[x][y+1].setText("");
		                	 btpc[x][y+1].setIcon(vazio);
		                	 btpc[x][y+1].setDisabledIcon(vazio);
	                   }
	                }
	             }
	             else if (disparoportaavioes.isEnabled())
	             {
	                btpc[x][y].setEnabled(false);
	                
	                disparoportaavioes.setEnabled(false);
	                
	                if (!(vescoltaplr<=0 && vcacaplr<=0 && vsubplr<=0))
	                	radioNulo.setSelected(true);
	                
	                switch (btpc[x][y].getTipo()) {
	                case "porta":
	               	 vportapc--;
	               	btpc[x][y].setText("");
               	 btpc[x][y].setIcon(hit);
               	 btpc[x][y].setDisabledIcon(hit);
	               	 break;
	               	 
	                case "caca":
	               	 vcacapc--;
	               	btpc[x][y].setText("");
               	 btpc[x][y].setIcon(hit);
               	 btpc[x][y].setDisabledIcon(hit);
	               	 break;
	               	 
	                case "escolta":
	               	 vescoltapc--;
	               	btpc[x][y].setText("");
               	 btpc[x][y].setIcon(hit);
               	 btpc[x][y].setDisabledIcon(hit);
	               	 break;
	               	 
	                case "sub":
	               	 vsubpc--;
	               	btpc[x][y].setText("");
               	 btpc[x][y].setIcon(hit);
               	 btpc[x][y].setDisabledIcon(hit);
	               	 break;
	               	 
	                default:
	                	btpc[x][y].setText("");
	                	 btpc[x][y].setIcon(vazio);
	                	 btpc[x][y].setDisabledIcon(vazio);
	               }
	                
	                rodadas = 0;
	                rodadasplr = 0;
	             }
	             

	             
	             if (rodadaspc == 2)
	            	 portapcatirar=true;
	             
	             do {
	            	//JOGADAS DO PC ---------##########################################################################
	            	//JOGADAS DO PC ---------##########################################################################
	            	//JOGADAS DO PC ---------##########################################################################
	            	//JOGADAS DO PC ---------##########################################################################
	            	//JOGADAS DO PC ---------##########################################################################
		                
	            	 if(vcacapc>0)
		             {
	            		 rodadaspc++;
			                if(rodadaspc==2)
			                    portapcatirar=true;
			                do {
				                x = rand.nextInt(9);
				                y = rand.nextInt(9);
			                }while (!btplr[x][y].isEnabled());
			                
			                btplr[x][y].setEnabled(false);
			                switch (btplr[x][y].getTipo()) {
			                case "porta":
			               	 vportaplr--;
			               	btplr[x][y].setText("");
		               	 btplr[x][y].setIcon(hit);
		               	 btplr[x][y].setDisabledIcon(hit);
			               	 break;
			               	 
			                case "caca":
			               	 vcacaplr--;
			               	btplr[x][y].setText("");
			               	btplr[x][y].setIcon(hit);
			               	btplr[x][y].setDisabledIcon(hit);
			               	 break;
			               	 
			                case "escolta":
			               	 vescoltaplr--;
			               	btplr[x][y].setText("");
			               	btplr[x][y].setIcon(hit);
			               	btplr[x][y].setDisabledIcon(hit);
			               	 break;
			               	 
			                case "sub":
			               	 vsubplr--;
			               	btplr[x][y].setText("");
			               	btplr[x][y].setIcon(hit);
			               	btplr[x][y].setDisabledIcon(hit);
			               	 break;
			               	 
			                default:
			                	btplr[x][y].setText("");
			                	 btplr[x][y].setIcon(vazio);
			                	 btplr[x][y].setDisabledIcon(vazio);
			               }
			                
			                if(x>0 && btplr[x-1][y].isEnabled())
			                {
			                	btplr[x-1][y].setEnabled(false);
			                	switch (btplr[x-1][y].getTipo()) {
			                    case "porta":
			                   	 vportaplr--;
			                   	 btplr[x-1][y].setText("");
			                	 btplr[x-1][y].setIcon(hit);
			                	 btplr[x-1][y].setDisabledIcon(hit);
			                   	 break;
			                   	 
			                    case "caca":
			                   	 vcacaplr--;
			                   	 btplr[x-1][y].setText("");
			                	 btplr[x-1][y].setIcon(hit);
			                	 btplr[x-1][y].setDisabledIcon(hit);
			                   	 break;
			                   	 
			                    case "escolta":
			                   	 vescoltaplr--;
			                   	 btplr[x-1][y].setText("");
			                	 btplr[x-1][y].setIcon(hit);
			                	 btplr[x-1][y].setDisabledIcon(hit);
			                   	 break;
			                   	 
			                    case "sub":
			                   	 vsubplr--;
			                   	 btplr[x-1][y].setText("");
			                	 btplr[x-1][y].setIcon(hit);
			                	 btplr[x-1][y].setDisabledIcon(hit);
			                   	 break;
			                   	 
			                    default:
			                    	btplr[x-1][y].setText("");
				                	 btplr[x-1][y].setIcon(vazio);
				                	 btplr[x-1][y].setDisabledIcon(vazio);
			                   }
			                }
			                if(x<9 && btplr[x+1][y].isEnabled())
			                {
			                	btplr[x+1][y].setEnabled(false);
			                	switch (btplr[x+1][y].getTipo()) {
			                    case "porta":
			                   	 vportaplr--;
			                   	btplr[x+1][y].setText("");
			                	 btplr[x+1][y].setIcon(hit);
			                	 btplr[x+1][y].setDisabledIcon(hit);
			                   	 break;
			                   	 
			                    case "caca":
			                   	 vcacaplr--;
			                   	btplr[x+1][y].setText("");
			                	 btplr[x+1][y].setIcon(hit);
			                	 btplr[x+1][y].setDisabledIcon(hit);
			                   	 break;
			                   	 
			                    case "escolta":
			                   	 vescoltaplr--;
			                   	btplr[x+1][y].setText("");
			                	 btplr[x+1][y].setIcon(hit);
			                	 btplr[x+1][y].setDisabledIcon(hit);
			                   	 break;
			                   	 
			                    case "sub":
			                   	 vsubplr--;
			                   	btplr[x+1][y].setText("");
			                	 btplr[x+1][y].setIcon(hit);
			                	 btplr[x+1][y].setDisabledIcon(hit);
			                   	 break;
			                   	 
			                    default:
			                    	btplr[x+1][y].setText("");
				                	 btplr[x+1][y].setIcon(vazio);
				                	 btplr[x+1][y].setDisabledIcon(vazio);
			                   }
			                }
			                if(y>0 && btplr[x][y-1].isEnabled())
			                {
			                	btplr[x][y-1].setEnabled(false);
			                	switch (btplr[x][y-1].getTipo()) {
			                    case "porta":
			                   	 vportaplr--;
			                   	btplr[x][y-1].setText("");
			                	 btplr[x][y-1].setIcon(hit);
			                	 btplr[x][y-1].setDisabledIcon(hit);
			                   	 break;
			                   	 
			                    case "caca":
			                   	 vcacaplr--;
			                   	btplr[x][y-1].setText("");
			                	 btplr[x][y-1].setIcon(hit);
			                	 btplr[x][y-1].setDisabledIcon(hit);
			                   	 break;
			                   	 
			                    case "escolta":
			                   	 vescoltaplr--;
			                   	btplr[x][y-1].setText("");
			                	 btplr[x][y-1].setIcon(hit);
			                	 btplr[x][y-1].setDisabledIcon(hit);
			                   	 break;
			                   	 
			                    case "sub":
			                   	 vsubplr--;
			                   	btplr[x][y-1].setText("");
			                	 btplr[x][y-1].setIcon(hit);
			                	 btplr[x][y-1].setDisabledIcon(hit);
			                   	 break;
			                   	 
			                    default:
			                    	btplr[x][y-1].setText("");
				                	 btplr[x][y-1].setIcon(vazio);
				                	 btplr[x][y-1].setDisabledIcon(vazio);
			                   }
			                }
			                if(y<9 && btplr[x][y+1].isEnabled())
			                {
			                	btplr[x][y+1].setEnabled(false);
			                	switch (btplr[x][y+1].getTipo()) {
			                    case "porta":
			                   	 vportaplr--;
			                   	btplr[x][y+1].setText("");
			                	 btplr[x][y+1].setIcon(hit);
			                	 btplr[x][y+1].setDisabledIcon(hit);
			                   	 break;
			                   	 
			                    case "caca":
			                   	 vcacaplr--;
			                   	btplr[x][y+1].setText("");
			                	 btplr[x][y+1].setIcon(hit);
			                	 btplr[x][y+1].setDisabledIcon(hit);
			                   	 break;
			                   	 
			                    case "escolta":
			                   	 vescoltaplr--;
			                   	btplr[x][y+1].setText("");
			                	 btplr[x][y+1].setIcon(hit);
			                	 btplr[x][y+1].setDisabledIcon(hit);
			                   	 break;
			                   	 
			                    case "sub":
			                   	 vsubplr--;
			                   	btplr[x][y+1].setText("");
			                	 btplr[x][y+1].setIcon(hit);
			                	 btplr[x][y+1].setDisabledIcon(hit);
			                   	 break;
			                   	 
			                    default:
			                    	btplr[x][y+1].setText("");
				                	 btplr[x][y+1].setIcon(vazio);
				                	 btplr[x][y+1].setDisabledIcon(vazio);
			                   }
			                }
			             
		
		             }
		             else if(vescoltapc>0)
		             {
		                rodadaspc++;
		                if(rodadaspc==2)
		                    portapcatirar=true;
		                
		                do {
			                x = rand.nextInt(10);
			                y = rand.nextInt(9);
		                }while (!btplr[x][y].isEnabled());
		                
		                
		                 btplr[x][y].setEnabled(false);
		                 
		                 switch (btplr[x][y].getTipo()) {
		                 case "porta":
		                	 vportaplr--;
		                	 btplr[x][y].setText("");
		                	 btplr[x][y].setIcon(hit);
		                	 btplr[x][y].setDisabledIcon(hit);
		                	 break;
		                	 
		                 case "caca":
		                	 vcacaplr--;
		                	 btplr[x][y].setText("");
		                	 btplr[x][y].setIcon(hit);
		                	 btplr[x][y].setDisabledIcon(hit);
		                	 break;
		                	 
		                 case "escolta":
		                	 vescoltaplr--;
		                	 btplr[x][y].setText("");
		                	 btplr[x][y].setIcon(hit);
		                	 btplr[x][y].setDisabledIcon(hit);
		                	 break;
		                	 
		                 case "sub":
		                	 vsubplr--;
		                	 btplr[x][y].setText("");
		                	 btplr[x][y].setIcon(hit);
		                	 btplr[x][y].setDisabledIcon(hit);
		                	 break;
		                	 
		                 default:
		                	 btplr[x][y].setText("");
		                	 btplr[x][y].setIcon(vazio);
		                	 btplr[x][y].setDisabledIcon(vazio);
		                }
		                 if (y<9 && btplr[x][y+1].isEnabled()) 
		                 {
			                 btplr[x][y+1].setEnabled(false);
			                 
			                 switch (btplr[x][y+1].getTipo()) {
			                 case "porta":
			                	 vportaplr--;
			                	 btplr[x][y+1].setText("");
			                	 btplr[x][y+1].setIcon(hit);
			                	 btplr[x][y+1].setDisabledIcon(hit);
			                	 break;
			                	 
			                 case "caca":
			                	 vcacaplr--;
			                	 btplr[x][y+1].setText("");
			                	 btplr[x][y+1].setIcon(hit);
			                	 btplr[x][y+1].setDisabledIcon(hit);
			                	 break;
			                	 
			                 case "escolta":
			                	 vescoltaplr--;
			                	 btplr[x][y+1].setText("");
			                	 btplr[x][y+1].setIcon(hit);
			                	 btplr[x][y+1].setDisabledIcon(hit);
			                	 break;
			                	 
			                 case "sub":
			                	 vsubplr--;
			                	 btplr[x][y+1].setText("");
			                	 btplr[x][y+1].setIcon(hit);
			                	 btplr[x][y+1].setDisabledIcon(hit);
			                	 break;
			                	 
			                 default:
			                	 btplr[x][y+1].setText("");
			                	 btplr[x][y+1].setIcon(vazio);
			                	 btplr[x][y+1].setDisabledIcon(vazio);
			                }
		                 }
		             }
		             else if(vsubpc>0)
		             {
		            	 rodadaspc++;
		            	 if(rodadaspc==2)
			                    portapcatirar=true;
			                 
		            	 
		            	 do {
				                x = rand.nextInt(10);
				                y = rand.nextInt(10);
			                }while (!btplr[x][y].isEnabled());
			                
			                 btplr[x][y].setEnabled(false);
			                 
			                 switch (btplr[x][y].getTipo()) {
			                 case "porta":
			                	 vportaplr--;
			                	 btplr[x][y].setText("");
			                	 btplr[x][y].setIcon(hit);
			                	 btplr[x][y].setDisabledIcon(hit);
			                	 break;
			                	 
			                 case "caca":
			                	 vcacaplr--;
			                	 btplr[x][y].setText("");
			                	 btplr[x][y].setIcon(hit);
			                	 btplr[x][y].setDisabledIcon(hit);
			                	 break;
			                	 
			                 case "escolta":
			                	 vescoltaplr--;
			                	 btplr[x][y].setText("");
			                	 btplr[x][y].setIcon(hit);
			                	 btplr[x][y].setDisabledIcon(hit);
			                	 break;
			                	 
			                 case "sub":
			                	 vsubplr--;
			                	 btplr[x][y].setText("");
			                	 btplr[x][y].setIcon(hit);
			                	 btplr[x][y].setDisabledIcon(hit);
			                	 break;
			                	 
			                 default:
			                	 btplr[x][y].setText("");
			                	 btplr[x][y].setIcon(vazio);
			                	 btplr[x][y].setDisabledIcon(vazio);
			                }
		             }
		                
		             else if (vportapc>0 && portapcatirar)
		             {
		                do {
			                x = rand.nextInt(10);
			                y = rand.nextInt(10);
		                }while (!btplr[x][y].isEnabled());
		                
		                btplr[x][y].setEnabled(false);
		                
		                portapcatirar=false;
		                rodadaspc = 0;
		                
		                switch (btplr[x][y].getTipo()) {
		                case "porta":
		               	 vportaplr--;
		               	btplr[x][y].setText("");
	               	 btplr[x][y].setIcon(hit);
	               	 btplr[x][y].setDisabledIcon(hit);
		               	 break;
		               	 
		                case "caca":
		               	 vcacaplr--;
		               	btplr[x][y].setText("");
	               	 btplr[x][y].setIcon(hit);
	               	 btplr[x][y].setDisabledIcon(hit);
		               	 break;
		               	 
		                case "escolta":
		               	 vescoltaplr--;
		               	btplr[x][y].setText("");
	               	 btplr[x][y].setIcon(hit);
	               	 btplr[x][y].setDisabledIcon(hit);
		               	 break;
		               	 
		                case "sub":
		               	 vsubplr--;
		               	btplr[x][y].setText("");
	               	 btplr[x][y].setIcon(hit);
	               	 btplr[x][y].setDisabledIcon(hit);
		               	 break;
		               	 
		                default:
		                	btplr[x][y].setText("");
		                	 btplr[x][y].setIcon(vazio);
		                	 btplr[x][y].setDisabledIcon(vazio);
		               }

		             }
	            	 
	            	 

		             rodadasplr++;
	             }
	             while (rodadasplr < 2 
	            		 && vsubplr <= 0
	            		 && vcacaplr <= 0
	            		 && vescoltaplr <= 0);
	             
	             if(rodadasplr==2 	&& vsubplr <= 0
	            		 			&& vcacaplr <= 0
	            		 			&& vescoltaplr <= 0)
	                    disparoportaavioes.setEnabled(true);
	             
	             //CONFERE QUAL VEICULO DO JOGADOR ESTA VIVO E DESABILITA O DISPARO DE QUEM MORREU
	             if (vportaplr<=0)
	             {
            		 disparoportaavioes.setEnabled(false);
            		 if (disparoportaavioes.isSelected())
            			 radioNulo.setSelected(true);
	             }
            	 if (vcacaplr<=0)
            	 {
            		 disparoestrela.setEnabled(false);
            		 if (disparoestrela.isSelected())
            			 radioNulo.setSelected(true);
            	 }
            	 if (vsubplr<=0)
            	 {
            		 disparosimples.setEnabled(false);
            		 if (disparosimples.isSelected())
            			 radioNulo.setSelected(true);
            	 }
            	 if (vescoltaplr<=0)
            	 {
            		 disparocascata.setEnabled(false);
            		 if (disparocascata.isSelected())
            			 radioNulo.setSelected(true);
            	 }

             }//fechamento do if (se selecionou algum disparo)
             else
             {
            	 if (radioDica.isSelected())
            	 {
            		 dicas--;
            		 if (dicas>=0)
            		 {
	            		 boolean existe=false;
	            		 
	            		 x = ((GameButton)e.getSource()).getL();
	                     y = ((GameButton)e.getSource()).getC();
	                     
	                     for (int j=0; j<10; j++)
	                     {
	                    	 if (btpc[x][j].getTipo() != "vazio")
	                    		 existe=true;
	                     }
	                     for (int i=0; i<10; i++)
	                     {
	                    	 if (btpc[i][y].getTipo() != "vazio")
	                    		 existe=true;
	                     }
	                     
	                     if (existe)
	                    	 JOptionPane.showMessageDialog(null, "Existe um veículo nesta linha ou coluna!");
	                     else
	                    	 JOptionPane.showMessageDialog(null, "Não existe um veículo nesta linha ou coluna!");
	                     
	                     if (dicas<=0)
	                     {
	                    	 radioDica.setEnabled(false);
	                    	 radioNulo.setSelected(true);
	                     }
            		 }
            		 else
            		 {
            			 radioDica.setEnabled(false);
            			 radioNulo.setSelected(true);
            		 }
            	 }
            	 else
            	 {
            		 JOptionPane.showMessageDialog(null, "Selecione um modo de disparo!");
            	 }
             }

             
             if ((vportaplr<=0 && vsubplr<=0 && vcacaplr<=0 && vescoltaplr<=0)
            		 ||
            		 (vportapc<=0 && vsubpc<=0 && vcacapc<=0 && vescoltapc<=0))

             {
                if (vportaplr<=0
                     	&& vsubplr<=0
                     	&& vcacaplr<=0
                     	&& vescoltaplr<=0)
                 {
                	 //JOGADOR PERDEU
                	((Cronometro)cronometro).setStatus(false);
                    try {
                        t.join();
                    } catch (InterruptedException ex) {

                    }
                    JOptionPane.showMessageDialog(null, "GAME OVER! Você perdeu!\nTempo total de jogo: "+ String.format("%02d", ((Cronometro)cronometro).getMinutes()) + ":" + String.format("%02d", ((Cronometro)cronometro).getSeconds()));
                    
                    EndJanela end = new EndJanela (tempos, nome, btplrcpy, btpccpy, ((Cronometro)cronometro).getMinutes(), ((Cronometro)cronometro).getSeconds(), -1);
                    this.dispose();
                 }
                 else
                 {
                	 //JOGADOR VENCEU
                	 ((Cronometro)cronometro).setStatus(false);
                     try {
                         t.join();
                     } catch (InterruptedException ex) {
                         
                     }
                     JOptionPane.showMessageDialog(null, "GAME OVER! Você VENCEU!\nTempo total de jogo: "+ String.format("%02d", ((Cronometro)cronometro).getMinutes()) + ":" + String.format("%02d", ((Cronometro)cronometro).getSeconds()));
                     
                     tempos.insere(nome,((Cronometro)cronometro).getMinutes(), ((Cronometro)cronometro).getSeconds(), ((Cronometro)cronometro).getTempo(), ((Cronometro)cronometro).getData());
                     
                     tempos.escreve();
                     
                     EndJanela end = new EndJanela (tempos, nome, btplrcpy, btpccpy, ((Cronometro)cronometro).getMinutes(), ((Cronometro)cronometro).getSeconds(), 1);
                     this.dispose();
                 }
             }
             }//fechamento do if (se for um GameButton)


		 else if (e.getSource() == sair){
			 
			 ((Cronometro)cronometro).setStatus(false);
             
			 for (int i=0; i<10; i++)
				{
					for (int j=0; j<10; j++)
					{
						btpc[i][j].removeActionListener(this);
					}
				}
			 
			  EndJanela end = new EndJanela (tempos, nome, btplrcpy, btpccpy, ((Cronometro)cronometro).getMinutes(), ((Cronometro)cronometro).getSeconds(), 0);
              this.dispose();
		 }
	 }
}
