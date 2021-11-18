/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package adventure;

import com.almasb.fxgl.app.FXGL;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.entity.Entity;

/**
 *
 * @author 05200203
 */
public class BolaDeFogoControl extends Component {

    private PhysicsComponent physics;
    private Entity player;

    @Override
    public void onUpdate(double tpf) {
        double x = 200, y = 100;
        if (player.getX() < getEntity().getX()) {
            x = -200;
        }
        if (player.getY() < getEntity().getY()) {
            y = -200;
        }
        physics.setLinearVelocity(x, y);
        physics.setAngularVelocity(50);
    }

    @Override
    public void onAdded() {
        player = FXGL.getApp().getGameWorld().getSingleton(CoelhoType.PLAYER).get();
    }

}
