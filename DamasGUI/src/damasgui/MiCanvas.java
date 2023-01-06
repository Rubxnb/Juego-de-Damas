package damasgui;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;


public class MiCanvas extends Canvas{
	private BufferedImage tablero;
	private BufferedImage blanca;
	private BufferedImage negra;
	private BufferedImage reinaNegra;
	private BufferedImage reinaBlanca;
	private String[][] juego;
	//Tablero
	
    public MiCanvas(String[][] juego) { 
    	this.juego = juego;
    	File f = new File("src\\images\\tablero.png");
    	File fn = new File("src\\images\\fichaNegra.png");
    	File fb = new File("src\\images\\fichaBlanca.png");
    	File rn = new File("src\\images\\reinaNegra.png");
    	File rb = new File("src\\images\\reinaBlanca.png");
    	
    	try {
			tablero = ImageIO.read(f);
			blanca = ImageIO.read(fb);
			negra = ImageIO.read(fn);
			reinaNegra = ImageIO.read(rn);
			reinaBlanca = ImageIO.read(rb);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        setBackground(Color.GRAY);  
        setSize(600, 600);
        
     }  
    
    public void paint(Graphics g){  

    g.drawImage(tablero, 0, 0, null);

	for (int i = 0; i < 8; i++) {
		for (int j = 0; j < 8; j++) {
			String f = juego[i][j];
			if(f.equals("*")) {
				g.drawImage(negra, j*75, i*75, null);
			} else if(f.equals("o")) {
				g.drawImage(blanca, j*75, i*75, null);
			} else if(f.equals("do")) {
				g.drawImage(reinaBlanca, j*75, i*75, null);
			} else if(f.equals("d*")) {
				g.drawImage(reinaNegra, j*75, i*75, null);
			}
		}
	}
	
    
  }  
}
