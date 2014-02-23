package com.github.danfickle.flyingsaucer.bootstrap;

import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class Test {
	public static void main(String... args)
	{
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				JPanel panel = new JPanel(new FlowLayout());
				JFrame frame = new JFrame("Bootstrap demo");
				frame.add(panel);
							
				BsButton button = new BsButton("btn btn-default", "btn btn-default");
				panel.add(button);
				
				button = new BsButton("btn btn-primary", "btn btn-primary");
				panel.add(button);

				button = new BsButton("btn btn-success", "btn btn-success");
				panel.add(button);

				button = new BsButton("btn btn-info", "btn btn-info");
				panel.add(button);
				
				button = new BsButton("btn btn-danger", "btn btn-danger");
				panel.add(button);
				
				button = new BsButton("btn btn-warning", "btn btn-warning");
				panel.add(button);
				
				button = new BsButton("btn btn-link", "btn btn-link");
				panel.add(button);
				
				frame.setPreferredSize(new Dimension(400, 300));
				frame.pack();
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setVisible(true);
			}
		});
	}
}
