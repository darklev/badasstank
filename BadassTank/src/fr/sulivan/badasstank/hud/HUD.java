package fr.sulivan.badasstank.hud;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import fr.sulivan.badasstank.config.Configuration;
import fr.sulivan.badasstank.mob.tank.Tank;
import fr.sulivan.badasstank.states.Battle;
import fr.sulivan.badasstank.states.SandBox;
import fr.sulivan.badasstank.util.TypeBarre;
import fr.sulivan.badasstank.util.gui.BarUI;

public class HUD {
	
	private Battle context;
	private BarUI healthPoints;
	private BarUI cooldown;
	
	public HUD(Battle context){
		this.context = context;
		healthPoints = new BarUI(Configuration.HEALTH_BAR_COLOR, new Color(200,200,200), 300, 4, context.getPlayer().getHealth(), context.getPlayer().getMaximumHealth(), TypeBarre.LEFT_TO_RIGHT);
		cooldown = new BarUI(Configuration.COOLDOWN_BAR_COLOR, new Color(200,200,200), 300, 4, context.getPlayer().getCanon().cooldownSpend(), context.getPlayer().getCanon().getCooldown(), TypeBarre.LEFT_TO_RIGHT);
		System.out.println(context.getPlayer().getCanon().getCooldown());
	}
	
	public void render(Graphics g){
		int x = Configuration.SCREEN_WIDTH / 2 - healthPoints.getWidth() / 2;
		healthPoints.render(g, Configuration.SCREEN_WIDTH / 2 - healthPoints.getWidth() / 2, 20);
		cooldown.render(g, Configuration.SCREEN_WIDTH / 2 - cooldown.getWidth() / 2, 30);
		g.setColor(Color.white);
		g.drawString(context.getPlayer().getHealth() + "/" + context.getPlayer().getMaximumHealth(), x, 0);
		g.drawString(context.getPlayer().getName(), x + healthPoints.getWidth() - g.getFont().getWidth(context.getPlayer().getName()), 0);
	}
	
	public void update(){
		healthPoints.setCurrent(context.getPlayer().getHealth());
		cooldown.setCurrent(context.getPlayer().getCanon().cooldownSpend());
		cooldown.setBarColor(!context.getPlayer().canShoot() ? Configuration.COOLDOWN_BAR_COLOR : Configuration.COOLDOWN_FULL_BAR_COLOR);
	}
}
