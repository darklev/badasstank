package fr.sulivan.badasstank.states;


import java.net.InetAddress;
import java.net.UnknownHostException;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.TextField;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import fr.sulivan.badasstank.config.Configuration;
import fr.sulivan.badasstank.main.BadassTank;
import fr.sulivan.badasstank.util.gui.TexturedButtonGUI;

public class JoinConfiguration extends BasicGameState{

	private TextField textFieldAddress;
	private TexturedButtonGUI goButton;
	
	@Override
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {



		textFieldAddress = new TextField(container, container.getGraphics().getFont(), 0,0, 200, 30);
		textFieldAddress.setBackgroundColor(Color.black);
		textFieldAddress.setBorderColor(Color.white);
		textFieldAddress.setTextColor(Color.white);
		
		textFieldAddress.setLocation(Configuration.SCREEN_WIDTH / 2 - textFieldAddress.getWidth() / 2, 
									100);
		textFieldAddress.setFocus(true);
		
		
		goButton = new TexturedButtonGUI(new Image(Configuration.RESOURCES_FOLDER+"textures/yandb.png"), 200, 30, "Go");
		goButton.setTextureMouseOver(new Image(Configuration.RESOURCES_FOLDER+"textures/yandb_mouseover.png"));
		goButton.setBorder(2, Color.white);
		goButton.setX(Configuration.SCREEN_WIDTH / 2 - goButton.getWidth() / 2);
		goButton.setY(150);
		
		goButton.setOnClick(() -> {
			try {
				BadassTank.game().join(textFieldAddress.getText());
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}
	

	
	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g)
			throws SlickException {

		textFieldAddress.render(container, g);
		goButton.render(g);
	}
	
	
	@Override
	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException {
		textFieldAddress.setFocus(true);
		goButton.update(container);
	}
	@Override
	public int getID() {
		return ID.JOIN_CONFIGURATION;
	}
}
