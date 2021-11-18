/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package adventure;

import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.entity.components.ViewComponent;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import javafx.util.Duration;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.app.FXGL;
import com.almasb.fxgl.time.LocalTimer;
import java.util.Random;

/**
 *
 * @author augus
 */
public class FinalBossControl extends Component {

    private int escudo = 0;

    
    private int life = 3;
    private Entity player;
    private ViewComponent view;
    private PhysicsComponent physics;
    private final AnimatedTexture texture;
    private final AnimationChannel idle;
    private final AnimationChannel attack, hurt;
    boolean dead = false;
    protected CoelhoAdventure control;
    private LocalTimer attacktime;
    protected Random rand;
    private Entity boladefogo;
    

    public FinalBossControl() {
        idle = new AnimationChannel("/Enemy/Final Boss/idleteste.png", 2, 100, 175, Duration.seconds(0.5), 0, 1);
        attack = new AnimationChannel("/Enemy/Final Boss/attack.png", 7, 100, 175, Duration.seconds(1), 0, 6);
        hurt = new AnimationChannel("/Enemy/Final Boss/hurt.png", 7, 100, 175, Duration.seconds(1), 0, 6);
        texture = new AnimatedTexture(idle);
    }

    @Override
    public void onAdded() {
        rand = new Random();
        control = new CoelhoAdventure();
        view.getView().addNode(texture);
        texture.loopAnimationChannel(idle);
        attacktime = FXGL.newLocalTimer();
        getEntity().setScaleX(-1);
        player = FXGL.getApp().getGameWorld().getSingleton(CoelhoType.PLAYER).get();
    }

    @Override
    public void onUpdate(double tpf) {
        if (attacktime.elapsed(Duration.seconds(1.2))) {
            int k;
            k = rand.nextInt(6);
            if(k == 1){
               attack();
            }
            attacktime.capture();
        }

    }

    public int getLife() {
        return life;
    }

    public void setLife(int life) {
        System.out.println("yay");
        if (escudo == 0) {
            this.life = life;
            start();
        }
        if (this.life <= 0) {
            isDead();
        }

    }

    public boolean isDead() {
        return dead = true;
    }

    public void start() {
        attacktime.capture();
        
        texture.playAnimationChannel(hurt);
        texture.setOnCycleFinished(() -> {
            texture.loopAnimationChannel(idle);
        });
        escudo = 1;
        for (int i = 0; i < 2; i++) {
            control.spawnenemies();
        }
    }
    
    public void attack(){
        texture.playAnimationChannel(attack);
        texture.setOnCycleFinished(() -> {
            texture.loopAnimationChannel(idle);
        });
        boladefogo = control.getGameWorld().spawn("boladefogo", 700,100);
    }
    
    
    public void setEscudo(int escudo) {
        this.escudo = escudo;
    }
}
