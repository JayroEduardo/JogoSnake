
import java.awt.Dimension;
import javax.swing.JFrame;

public class MainTest {

	public static void main(String[] args) {
		JFrame frame = new JFrame("Snake");
		frame.setContentPane(new SnakePainel());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.pack();
		frame.setPreferredSize(new Dimension(SnakePainel.width, SnakePainel.height));
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);

	}
}