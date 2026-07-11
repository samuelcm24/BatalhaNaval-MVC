package batalhanaval;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class StartJanela extends JFrame implements ActionListener{
	
	private JTextField nomedocara;
	private Boolean random;
	private JButton jogoRandom, jogoCriado;
	
	public StartJanela (){
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setTitle("Batalha Naval - Start");
		setSize(1024,720);
		
		setLayout (new GridLayout(3,0));
		
        //-----------------PAINEL DE CIMA----------------------------
		
		JPanel pCima = new JPanel();
        pCima.setBackground(Color.LIGHT_GRAY);
        pCima.setLayout(new GridLayout(2,0));
        pCima.setVisible(true);

	        JLabel bemvindo = new JLabel ("Bem vindo à Batalha Naval!");
	        bemvindo.setFont(new Font("Serif", Font.BOLD, 60));
	        bemvindo.setForeground(new Color(25, 25, 255));
	        
	        bemvindo.setVisible(true);
	        bemvindo.setHorizontalAlignment(SwingConstants.CENTER);
	        pCima.add(bemvindo);
        
	        JLabel desenvolvido = new JLabel ("Jogo desenvolvido por: Leandro Tavares e Leonardo Guths");
	        desenvolvido.setFont(new Font("Serif", Font.ITALIC, 25));
	        desenvolvido.setForeground(new Color(25, 25, 175));
	        desenvolvido.setVisible(true);
	        desenvolvido.setHorizontalAlignment(SwingConstants.CENTER);
	        pCima.add(desenvolvido);
        
		add(pCima);

        //-----------------PAINEL DO MEIO----------------------------
		
		JPanel pMeio = new JPanel();
        pMeio.setBackground(Color.GRAY);
        pMeio.setVisible(true);
	        
	        JLabel insiraseunome = new JLabel("Insira seu nome:");
	        insiraseunome.setFont( new Font("Tahoma", Font.BOLD, 20));
	        insiraseunome.setForeground(new Color(25, 25, 150));
	        insiraseunome.setVisible(true);
			pMeio.add(insiraseunome);
			
			nomedocara = new JTextField("");
			nomedocara.setPreferredSize(new Dimension(200, 24));
			nomedocara.setHorizontalAlignment(SwingConstants.CENTER);
			pMeio.add(nomedocara);
			
        add(pMeio);
        
        //-----------------PAINEL DE BAIXO----------------------------
        
		JPanel pBaixo = new JPanel();
		
        pBaixo.setBackground(new Color(0,0,200));
        pBaixo.setVisible(true);
        
        	JPanel pBaixoEsq = new JPanel();
        	pBaixoEsq.setBackground(Color.LIGHT_GRAY);
            pBaixoEsq.setVisible(true);
        	jogoRandom = new JButton ("Gerar jogo aleatório");
        	jogoRandom.setPreferredSize(new Dimension(220, 40));
        	pBaixoEsq.add(jogoRandom);
        	
        	JPanel pBaixoDir = new JPanel();
        	pBaixoDir.setBackground(Color.LIGHT_GRAY);
            pBaixoDir.setVisible(true);
        	jogoCriado = new JButton ("Criar seu tabuleiro");
        	jogoCriado.setPreferredSize(new Dimension(220, 40));
        	pBaixoDir.add(jogoCriado);
        	
        	pBaixo.add(pBaixoEsq);
        	pBaixo.add(pBaixoDir);
        	
        	jogoRandom.addActionListener(this);
        	jogoCriado.addActionListener(this);
        	
        	
        add(pBaixo);
		
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	public void actionPerformed(ActionEvent e) {
		
	if (nomedocara.getText().compareTo("") == 0) {
		JOptionPane.showMessageDialog(null, "Você deve inserir um nome!");
	}
	else {
			if (e.getSource() == jogoRandom)
				this.random = true;
			if (e.getSource() == jogoCriado)
				this.random = false;
			
			SetupJanela setup = new SetupJanela(nomedocara.getText(), random);
			this.dispose();
		}
	}
}
