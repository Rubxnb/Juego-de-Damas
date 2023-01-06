package damasgui;

import java.awt.Canvas;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class DamasGUI extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private boolean segundoClic = false;
	private int movIniX = 0;
	private int movIniY = 0;
	private int movFinX = 0;
	private int movFinY = 0;

	public static Damas damas;

	private JPanel contentPane;
	/**
	 * @wbp.nonvisual location=-82,609
	 */
	private final Canvas canvas = new Canvas();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					DamasGUI frame = new DamasGUI();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public DamasGUI() {
		setTitle("Damas");
		ImageIcon img = new ImageIcon("src\\images\\icon.png");
		setIconImage(img.getImage());
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 851, 694);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblNewLabel = new JLabel("Turno para:");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblNewLabel.setBounds(647, 49, 102, 14);
		contentPane.add(lblNewLabel);

		JLabel muestraTruno = new JLabel("");
		muestraTruno.setFont(new Font("Tahoma", Font.BOLD, 17));
		muestraTruno.setBounds(647, 81, 119, 37);
		contentPane.add(muestraTruno);
		
		JLabel contB = new JLabel("");
		contB.setBounds(688, 241, 46, 14);
		contentPane.add(contB);
		
		JLabel contNegras = new JLabel("");
		contNegras.setBounds(688, 266, 46, 14);
		contentPane.add(contNegras);

		damas = new Damas();
		MiCanvas canvas = new MiCanvas(damas.getTablero());
		contB.setText(String.valueOf(damas.getContadorBlancas()));
		contNegras.setText(String.valueOf(damas.getContadorNegras()));
		muestraTruno.setText(muestraTurno());
		canvas.addMouseListener(new MouseAdapter() {
//			@Override
//			public void mouseClicked(MouseEvent e) {
//
//				if (!segundoClic) {
//					movIniX = e.getPoint().x / 75;
//					movIniY = e.getPoint().y / 75;
//
//					if(damas.piezaCorrecta(movIniY, movIniX)) {
//						segundoClic = true;
//					}
//					
//					
//					System.out.println("----------- Inicial" + " x: " + movIniX + " y: " + movIniY);
//
//					// canvas.sombrea(movIni, j.getColumnaActual);
//				} else {
//
//					movFinX = e.getPoint().x / 75;
//					movFinY = e.getPoint().y / 75;
//
//					System.out.println("Mov inicial X: " + movIniX + " Y: " + movIniY);
//
//					System.out.println("Mov final X: " + movFinX + " Y: " + movFinY);
//					boolean b = damas.juego(movIniY, movIniX, movFinY, movFinX);
//
//					segundoClic = false;
//					
//					if(b) {
//						System.out.println("XXXXXXXXXXXXXXXXXX Movimiento hecho");
//						canvas.repaint(movIniX*75, movIniY*75, 75, 75);
//						canvas.repaint(movFinX*75, movFinY*75, 75, 75);
//					}
//					
//
//				}
//
//				muestraTruno.setText(muestraTurno());
//				contB.setText(String.valueOf(damas.getContadorBlancas()));
//				contNegras.setText(String.valueOf(damas.getContadorNegras()));
//				
//			}
			@Override
			public void mousePressed(MouseEvent e) {
				movIniX = e.getPoint().x / 75;
				movIniY = e.getPoint().y / 75;

			}
			@Override
			public void mouseReleased(MouseEvent e) {
				movFinX = e.getPoint().x / 75;
				movFinY = e.getPoint().y / 75;

				System.out.println("Mov inicial X: " + movIniX + " Y: " + movIniY);

				System.out.println("Mov final X: " + movFinX + " Y: " + movFinY);
				boolean b = damas.juego(movIniY, movIniX, movFinY, movFinX);

//				segundoClic = false;
				
				if(b) {
					System.out.println("XXXXXXXXXXXXXXXXXX Movimiento hecho");
					canvas.repaint(movIniX*75, movIniY*75, 75, 75);
					canvas.repaint(movFinX*75, movFinY*75, 75, 75);
				}
				muestraTruno.setText(muestraTurno());
				contB.setText(String.valueOf(damas.getContadorBlancas()));
				contNegras.setText(String.valueOf(damas.getContadorNegras()));
			}
		});
		contentPane.add(canvas);
		
		JLabel lblNewLabel_1 = new JLabel("Piezas restantes");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblNewLabel_1.setBounds(647, 174, 132, 27);
		contentPane.add(lblNewLabel_1);
		
		JLabel lblNewLabel_2 = new JLabel("Blancas:");
		lblNewLabel_2.setBounds(632, 241, 57, 14);
		contentPane.add(lblNewLabel_2);
		
		JLabel lblNewLabel_3 = new JLabel("Negras:");
		lblNewLabel_3.setBounds(632, 266, 46, 14);
		contentPane.add(lblNewLabel_3);
		
	}

	public static String muestraTurno() {
		if (damas.turno() == 1) {
			return "Blancas";
		} else {
			return "Negras";
		}
	}
}
