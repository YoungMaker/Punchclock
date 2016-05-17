package main;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.Menu;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

public class gui implements ActionListener, MouseMotionListener {
static gui _instance = new gui();
PopupMenu pop = null;
TrayIcon ic = null;
MenuItem exitItem = null;
MenuItem ClockItem = null;
MenuItem ResetItem = null;
MenuItem SaveItem = null;
	
	public gui() {
		return;
	}
	
	public void setupGUI() {
		if(!SystemTray.isSupported()) {
			throw new UnsupportedOperationException();
		}
		else {
			final SystemTray tray = SystemTray.getSystemTray();
			pop = new PopupMenu();
			ic = new TrayIcon(createImage("img\\clock.png", "Not clocked in Image"));
			exitItem= new MenuItem("Exit");
			ClockItem= new MenuItem("Clock In");
			Menu options = new Menu("Options");
			ResetItem = new MenuItem("Reset Hours");
			SaveItem = new MenuItem("Save current Hours");
			SaveItem.setEnabled(false);
			
			
			options.add(ResetItem);
			options.add(SaveItem);
			pop.add(options);
			pop.addSeparator();
			pop.add(ClockItem);
			pop.add(exitItem);
			
			ic.setPopupMenu(pop);
			
			ic.setImageAutoSize(true);
			ic.setToolTip("Not Currently Clocked In");
			
			exitItem.addActionListener(this);
			ClockItem.addActionListener(this);
			ic.addMouseMotionListener(this);
			ResetItem.addActionListener(this);
			SaveItem.addActionListener(this);
			
			try {
				tray.add(ic);
			} catch (AWTException e) {
				e.printStackTrace(); //change?
			}
		}
	}
	
	public void changeState(boolean type) {
		SystemTray.getSystemTray().remove(ic);
	
		if(!type) {
			ic.setImage(createImage("img\\clock.png", "Not clocked in Image"));
			ic.setToolTip("Not Clocked in");
			ClockItem.setLabel("Clock In");
			ResetItem.setEnabled(true);
			SaveItem.setEnabled(false);
		}
		else {
			ic.setImage(createImage("img\\clockedin.png", "Clocked in Image"));
			ClockItem.setLabel("Clock Out");
			ResetItem.setEnabled(false);
			SaveItem.setEnabled(true);
		}
		
		try {
			SystemTray.getSystemTray().add(ic);
		} catch (AWTException e) {
			e.printStackTrace();
		}
	}
	
	private Image createImage(String imgUrl, String descript) {
		File f = new File(imgUrl);
		if(!f.exists() || f.isDirectory()) {
			System.err.println("image does not exist!");
			return null;
		}
		else {
			System.out.println("loading image: " + descript);
			return new ImageIcon(imgUrl).getImage();
		}
	}


	static gui getInstance() {
		return _instance;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		if(source == exitItem) {
			if(Run.getTimeHandler().getState()) {
				int n = JOptionPane.showConfirmDialog(null,
						"Are you sure you want to exit?\n You have " + Run.getTimeHandler().getTimeIN() + " clocked.\n Clock out or use the save option to save.",
						"Are you sure?",
						JOptionPane.YES_NO_OPTION);
				if(n == 0) {
					System.exit(0);
				}
			}
			else {
				int n = JOptionPane.showConfirmDialog(null,
						"Are you sure you want to exit?",
						"Are you sure?",
						JOptionPane.YES_NO_OPTION);
				if(n == 0) {
					System.exit(0);
				}
			}
		}
		else if(source == ClockItem) {
			if(Run.getTimeHandler().getState()) {
				JOptionPane.showMessageDialog(null, "Hours Worked\n You have worked " + Run.getTimeHandler().getTimeIN() + " hours clocked\n", "Clocked out", JOptionPane.INFORMATION_MESSAGE);
			}
			Run.getTimeHandler().flipState();
			changeState(Run.getTimeHandler().getState());
			
		}
		else if(source == ResetItem) {
			int n = JOptionPane.showConfirmDialog(null,
					"Are you sure you want to Reset?\n You have " + Run.getTimeHandler().getTotalTime() + " saved!\n It cannot be undone!",
					"Are you sure?",
					JOptionPane.YES_NO_OPTION);
			if(n == 0) {
				Run.getTimeHandler().ResetTime();
			}
		}
		else if(source == SaveItem){
			//TODO:Save the hours
			Run.getTimeHandler().saveHours();
			JOptionPane.showMessageDialog(null, "Hours saved\n You have " + Run.getTimeHandler().getTotalTime() + " saved", "Hours Saved", JOptionPane.INFORMATION_MESSAGE);
		}
		
	}


	@Override
	public void mouseMoved(MouseEvent e) {
		if(Run.getTimeHandler().getState()){
			ic.setToolTip("Time Clocked: " + Run.getTimeHandler().getTimeIN());
		}
		else {
			ic.setToolTip("Not Clocked in. Total Time clocked: " + Run.getTimeHandler().getTotalTime());
		}
	}

	@Override
	public void mouseDragged(MouseEvent e) {
	}
	
}
