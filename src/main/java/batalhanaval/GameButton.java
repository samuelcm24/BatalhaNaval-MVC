package batalhanaval;

import javax.swing.*;

public class GameButton extends JButton{
	private String tipo;
	private int l, c;
	
	GameButton (String title, String tipo, int l, int c){
		super (title);
		this.tipo = tipo;
		this.l = l;
		this.c = c;
	}

	GameButton(GameButton a)
	{
		super(a.getText());
		this.tipo = a.getTipo();
		this.l = a.getL();
		this.c = a.getC();
		this.setIcon(a.getIcon());
		this.setDisabledIcon(a.getDisabledIcon());
	}
	
	public String getTipo() {
		return tipo;
	}

	public int getL() {
		return l;
	}

	public int getC() {
		return c;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	
}
