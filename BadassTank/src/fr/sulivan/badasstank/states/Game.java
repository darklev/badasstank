package fr.sulivan.badasstank.states;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.particles.ConfigurableEmitter;
import org.newdawn.slick.particles.ParticleIO;
import org.newdawn.slick.particles.ParticleSystem;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.tiled.TiledMap;

import fr.sulivan.badasstank.config.Configuration;
import fr.sulivan.badasstank.hud.HUD;
import fr.sulivan.badasstank.main.Zombiz;
import fr.sulivan.badasstank.map.Map;
import fr.sulivan.badasstank.mob.displayer.Displayer;
import fr.sulivan.badasstank.mob.player.Player;
import fr.sulivan.badasstank.mob.tank.Canon;
import fr.sulivan.badasstank.mob.tank.Carterpillar;

public class Game extends BasicGameState{

	
	private Player player1;
	private Player player2;
	
	private ArrayList<Displayer> displayers;

	private Map map;
	
	private int mapX;
	private int mapY;
	
	private int cursorX;
	private int cursorY;
	
	private HUD hud;
	
	
	@Override
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {
		

		SpriteSheet sprites = new SpriteSheet("resources/spritesheets/carterpillars.png", 7 ,23 ,new Color(255,0,255));
		Animation animation = new Animation(sprites, 0, 0, 2, 0,true, 100, true);
		Carterpillar carterpillar = new Carterpillar(animation, 1.5,3);
		Image body = new Image("resources/spritesheets/bodies.png").getSubImage(0, 0, 16, 18);
		Canon canon = new Canon(new Image("resources/spritesheets/canons.png", new Color(255,0,255)).getSubImage(0, 0, 11, 17), new Image("resources/spritesheets/bullet.png", new Color(255,0,255)).getSubImage(0, 0, 8, 9), null, 3f, 100, 500);
		player1 = new Player(carterpillar, canon, new Color(20,150,20), body, "Sulivan");
		player2 = new Player(carterpillar, canon, new Color(20,20,150), body, "Joueur 2");
		
		
		this.map = new Map(new TiledMap("resources/map/test.tmx"));
		
		ParticleSystem particles = new ParticleSystem("resources/particles/test.png", 1500, new Color(255,0,255));
		
		File xmlFile = new File("resources/particles/test.xml");
		ConfigurableEmitter ce;
		try {
			ce = ParticleIO.loadEmitter(xmlFile);
			particles.addEmitter(ce);
		} catch (IOException e) {
			e.printStackTrace();
		}
		displayers = new ArrayList<Displayer>();
		
		hud = new HUD(this);
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g)
			throws SlickException {
		
		map.drawLayer(0);
		player1.render(true, g);
		g.setColor(Color.white);
		g.drawString("Joueur : " + player1.getX() + " ; " + player1.getY() + "  :  " + player1.getRotation() + "� ", 0, 0);
		g.drawString("Curseur : " + cursorX + " ; " + cursorY, 0, 20);
		for(Displayer d : displayers){
			d.render( d.getX() + mapX,  d.getY() + mapY );
			d.getHitbox().draw(new Color(0,0.7f,0, 0.5f), g);;
		}
		
		map.getHitbox().draw(new Color(0.5f, 0.2f, 0.2f, 0.5f), g);
		player1.getHitbox().draw(new Color(0.2f, 0.2f, 0.5f, 0.5f), g);
		
		hud.render(g);
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException {
		Input in = container.getInput();
		
		mapX = Configuration.SCREEN_WIDTH / 2 - player1.getX();
		mapY = Configuration.SCREEN_HEIGHT / 2 - player1.getY();
		
		map.setDisplayedCoordinate(mapX, mapY);
		
		cursorX = - mapX  + in.getAbsoluteMouseX();
		cursorY = - mapY + in.getAbsoluteMouseY();
		
		if(in.isKeyDown(Input.KEY_Z)){
			player1.setMoving(true, false);
		}
		else if(in.isKeyDown(Input.KEY_S)){
			player1.setMoving(true, true);
		}
		else{
			player1.setMoving(false, false);
		}
		
		if(in.isKeyDown(Input.KEY_Q)){
			//if(!player.getHitbox().copyRotation(player.getRotation() + player.getSpeedRotation()).intersects(map.getHitbox())){
				player1.setRotation(player1.getRotation() + player1.getSpeedRotation());
			//}
		}
		else if(in.isKeyDown(Input.KEY_D)){
			//if(!player.getHitbox().copyRotation(player.getRotation() - player.getSpeedRotation()).intersects(map.getHitbox())){
				player1.setRotation(player1.getRotation() - player1.getSpeedRotation());
			//}
		}
		if(in.isKeyDown(Input.KEY_F1)){
			Zombiz.toggleFullScreen();
		}

		if(in.isMousePressed(Input.MOUSE_LEFT_BUTTON)){
			displayers.add(player1.fire(player1.getX(), player1.getY(), cursorX, cursorY));
		}
		
		player1.getCanon().setRotation(Configuration.SCREEN_WIDTH / 2, Configuration.SCREEN_HEIGHT / 2 , in.getAbsoluteMouseX(), in.getAbsoluteMouseY());
		player1.update(map);
		
		ArrayList<Displayer> disposed = new ArrayList<Displayer>();
		for(Displayer d : displayers){
			d.update(delta, this);
			if(d.isDisposed()){
				disposed.add(d);
			}
		}
		
		for(Displayer d : disposed){
			displayers.remove(d);
		}
		
		hud.update();
	}

	@Override
	public int getID() {
		return ID.GAME;
	}

	public Player getPlayer() {
		return player1;
	}
	
	public Map getMap(){
		return map;
	}

}
