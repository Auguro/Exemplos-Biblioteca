/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package adventure;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.settings.GameSettings;
import javafx.scene.input.KeyCode;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.physics.CollisionHandler;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import javafx.scene.image.Image;
import javafx.util.Duration;
import javafx.scene.text.Text;
import java.util.Map;
import javafx.scene.text.Font;
import com.almasb.fxgl.texture.Texture;
import javafx.scene.paint.Color;
import com.almasb.fxgl.ui.ProgressBar;
import com.almasb.fxgl.app.FXGL;

/**
 *
 * @author 05200203
 */
public class CoelhoAdventure extends GameApplication {

    private ProgressBar special;
    private int especial;
    protected int Width = 1024;
    protected int Height = 512;
    private Entity player;
    private int vidas = 0;
    private int level = 1;
    private Text vidinhas;
    private int scores = 0;
    private Text score;
    private Entity LightBandit;
    private Entity Knight;
    private Entity Boss;
    private final int EnemyLife = 6;
    private int invencivel = 0;
    private int i = 0;
    private int k = 0;
    
    @Override
    protected void initSettings(GameSettings gs) {
        gs.setWidth(Width);
        gs.setHeight(Height);
        gs.setMenuEnabled(false);
        gs.setFontGame("NiceNightie.ttf");
        gs.setTitle("Coelho Adventure");

    }

    @Override
    protected void initUI() {
        switch (level) {
            case 1:
                try (InputStream is = Files.newInputStream(Paths.get("src/assets/textures/Map/BackGround1.png"))) {
                    getGameScene().setBackgroundRepeat(new Image(is));
                    FXGL.getAudioPlayer().playMusic("LVL1.mp3");
                } catch (IOException ex) {
                    System.err.println("Imagem sapo nÃ£o encontrada.");

                }
                break;
            case 2:
                try (InputStream is = Files.newInputStream(Paths.get("src/assets/textures/Map/BackGround2.png"))) {
                    getGameScene().setBackgroundRepeat(new Image(is));
                    FXGL.getAudioPlayer().playMusic("LVL2.mp3");
                } catch (IOException ex) {
                    System.err.println("Imagem sapo nÃ£o encontrada.");
                }
                break;
            case 3:
                try (InputStream is = Files.newInputStream(Paths.get("src/assets/textures/Map/BackGround3.png"))) {
                    getGameScene().setBackgroundRepeat(new Image(is));
                    getAudioPlayer().playMusic("LVL3.mp3");
                    getAudioPlayer().setGlobalMusicVolume(15);
                } catch (IOException ex) {
                    System.err.println("Imagem sapo nÃ£o encontrada.");
                }
                break;
            default:
                break;
        }
        vidinhas = new Text();
        vidinhas.setTranslateX(80);
        vidinhas.setTranslateY(40);
        vidinhas.setFont(Font.font("NiceNightie", 36));
        vidinhas.setFill(Color.rgb(255, 255, 255));
        getGameScene().addUINode(vidinhas);

        Texture HeartTexture = getAssetLoader().loadTexture("/Map/vida.png");
        HeartTexture.setFitHeight(50);
        HeartTexture.setFitWidth(70);
        HeartTexture.setTranslateX(5);
        HeartTexture.setTranslateY(10);

        getGameScene().addUINode(HeartTexture);

        score = new Text();
        score.setFont(Font.font("NiceNightie", 36));
        score.setFill(Color.rgb(255, 255, 255));
        score.setTranslateX(940);
        score.setTranslateY(40);

        getGameScene().addUINode(score);

    }

    @Override
    protected void initInput() {

        getInput().addAction(new UserAction("esquerda") {
            @Override
            protected void onAction() {
                player.getComponent(PlayerControl.class).right();
            }

            @Override
            protected void onActionEnd() {
                player.getComponent(PlayerControl.class).stop();
            }
        }, KeyCode.RIGHT);
        getInput().addAction(new UserAction("direita") {

            @Override
            protected void onAction() {
                player.getComponent(PlayerControl.class).left();
            }

            @Override
            protected void onActionEnd() {
                player.getComponent(PlayerControl.class).stop();
            }
        }, KeyCode.LEFT);
        getInput().addAction(new UserAction("pular") {

            @Override
            protected void onActionBegin() {
                player.getComponent(PlayerControl.class).jump();
            }

        }, KeyCode.UP);

        getInput().addAction(new UserAction("attack") {
            @Override
            protected void onActionBegin() {
                player.getComponent(PlayerControl.class).attack();
            }

        }, KeyCode.Z);

       

        
    }

    @Override
    protected void initGameVars(Map<String, Object> vars) {
        vars.put("vidas", 0);
        vars.put("scores", 0);
    }

    @Override
    protected void initGame() {

        /*parede = new AnimationChannel("/Map/forca.png", 1, 32, 64, Duration.seconds(1), 0, 0);
        explosion = new AnimationChannel("/Player/Death.png", 5, 30, 40, Duration.seconds(0.5), 0, 4);
        texture = new AnimatedTexture(parede);*/
        nextLevel();
    }

    protected void nextLevel() {
        if (vidas <= 0) {
            scores = 0;
            vidas = 3;
            
            getGameState().setValue("scores", scores);
            getGameState().increment("vidas", vidas);
            
        }
        getGameWorld().clear();
        getGameWorld().addEntityFactory(new CoelhoFactory());
        getGameWorld().setLevelFromMap("LVL " + level + ".json");
        switch (level) {
            case 1:
                player = getGameWorld().spawn("player", 30, 400);
                LightBandit = getGameWorld().spawn("lightbandit", 1700, 435);
                getGameScene().getViewport().setBounds(-2, 0, 4000, 700);
                getGameScene().getViewport().bindToEntity(player, Width / 2, Height / 2);
                break;
            case 2:
                player = getGameWorld().spawn("player", 10, 10);
                LightBandit = getGameWorld().spawn("lightbandit", 975, 75);
                LightBandit = getGameWorld().spawn("lightbandit", 915, 450);
                LightBandit = getGameWorld().spawn("lightbandit", 2950, 420);
                getGameScene().getViewport().setBounds(-2, 0, 4000, 700);
                getGameScene().getViewport().bindToEntity(player, Width / 2, Height / 2);
                break;
            case 3:
                player = getGameWorld().spawn("player", 10, 10);
                Boss = getGameWorld().spawn("finalboss", 880, 210);
                getGameScene().getViewport().setBounds(0, 0, 1024, 512);
                getGameScene().getViewport().bindToEntity(player, Width / 2, Height / 2);
                break;
            default:
                break;
        }

    }

    @Override
    protected void initPhysics() {
        getPhysicsWorld().addCollisionHandler(new CollisionHandler(CoelhoType.PLAYER, CoelhoType.COIN) {
            @Override
            protected void onCollisionBegin(Entity player, Entity coin) {
                getAudioPlayer().playSound("Coin.wav");
                scores = scores + 100;
                getGameState().increment("scores", 100);
                
                coin.removeFromWorld();
            }
        });
        getPhysicsWorld().addCollisionHandler(new CollisionHandler(CoelhoType.PLAYER, CoelhoType.DOOR) {
            @Override
            protected void onCollisionBegin(Entity player, Entity door) {
                level = level + 1;
                nextLevel();
                initUI();
            }
        });
        getPhysicsWorld().addCollisionHandler(new CollisionHandler(CoelhoType.PLAYER, CoelhoType.LIGHTBANDIT) {
            @Override
            protected void onCollisionBegin(Entity player, Entity enemy) {
                enemy.getComponent(LightBanditControl.class).attacar();
                if (enemy.getView().getScaleX() < 0) {
                    getMasterTimer().runOnceAfter(() -> {
                        getGameWorld().spawn("hitboxenemy", enemy.getX() + 30, enemy.getY());
                    }, Duration.seconds(0.2));
                } else if (enemy.getView().getScaleX() > 0) {
                    getMasterTimer().runOnceAfter(() -> {
                        getGameWorld().spawn("hitboxenemy", enemy.getX() - 30, enemy.getY());
                    }, Duration.seconds(0.2));
                }
            }
        });

        getPhysicsWorld().addCollisionHandler(new CollisionHandler(CoelhoType.PLAYER, CoelhoType.KNIGHT) {
            @Override
            protected void onCollisionBegin(Entity player, Entity enemy) {
                if (enemy.getComponent(KnightControl.class).EndBlock() == false) {
                    enemy.getComponent(KnightControl.class).block();
                    if (enemy.getView().getScaleX() < 0) {
                        getMasterTimer().runOnceAfter(() -> {
                            getGameWorld().spawn("block", enemy.getX() + 19, enemy.getY());
                        }, Duration.seconds(0.2));
                    } else if (enemy.getView().getScaleX() > 0) {
                        getMasterTimer().runOnceAfter(() -> {
                            getGameWorld().spawn("block", enemy.getX() - 19, enemy.getY());
                        }, Duration.seconds(0.2));
                    }
                } else {
                    enemy.getComponent(KnightControl.class).attacar();
                    if (enemy.getView().getScaleX() < 0) {
                        getMasterTimer().runOnceAfter(() -> {
                            getGameWorld().spawn("hitboxenemy", enemy.getX() + 30, enemy.getY());
                        }, Duration.seconds(0.2));
                    } else if (enemy.getView().getScaleX() > 0) {
                        getMasterTimer().runOnceAfter(() -> {
                            getGameWorld().spawn("hitboxenemy", enemy.getX() - 30, enemy.getY());
                        }, Duration.seconds(0.2));
                    }
                }

            }
        });

        getPhysicsWorld().addCollisionHandler(new CollisionHandler(CoelhoType.PLAYER, CoelhoType.LIFE) {
            @Override
            protected void onCollisionBegin(Entity player, Entity Life) {
                vidas++;
                getGameState().increment("vidas", 1);
                Life.removeFromWorld();
            }
        });

        getPhysicsWorld().addCollisionHandler(new CollisionHandler(CoelhoType.PLAYER, CoelhoType.BROKEN) {
            @Override
            protected void onCollisionBegin(Entity player, Entity broken) {
                getMasterTimer().runOnceAfter(() -> {
                    broken.removeFromWorld();
                }, Duration.seconds(2));
            }
        });
        
        getPhysicsWorld().addCollisionHandler(new CollisionHandler(CoelhoType.PLAYER, CoelhoType.BROKENL) {
            @Override
            protected void onCollisionBegin(Entity player, Entity broken) {
                getMasterTimer().runOnceAfter(() -> {
                    broken.removeFromWorld();
                }, Duration.seconds(2));
            }
        });

        getPhysicsWorld().addCollisionHandler(new CollisionHandler(CoelhoType.PLAYER, CoelhoType.END) {
            @Override
            protected void onCollisionBegin(Entity player, Entity end) {
                vidas = 0;
                getGameState().setValue("vidas", 0);
                showGameOver();
            }
        });
        getPhysicsWorld().addCollisionHandler(new CollisionHandler(CoelhoType.HITBOX, CoelhoType.LIGHTBANDIT) {
            @Override
            protected void onCollisionBegin(Entity hitbox, Entity enemy) {
                getAudioPlayer().playSound("Hit.wav");
                if (invencivel == 0) {
                    
                    i = i + 1;
                    enemy.getComponent(LightBanditControl.class).damage();
                    enemy.getComponent(LightBanditControl.class).setLife(EnemyLife - i);
                    if (enemy.getComponent(LightBanditControl.class).dead == true) {
                        enemy.removeFromWorld();
                        enemy.getComponent(LightBanditControl.class).setLife(EnemyLife);
                        i = 0;
                    }
                    invencivel = 1;

                }

            }
        });
        getPhysicsWorld().addCollisionHandler(new CollisionHandler(CoelhoType.HITBOX, CoelhoType.KNIGHT) {
            @Override
            protected void onCollisionBegin(Entity hitbox, Entity enemy) {
                getAudioPlayer().playSound("Hit.wav");
                if (invencivel == 0) {
                    System.out.println(invencivel);
                    i = i + 1;
                    enemy.getComponent(KnightControl.class).damage();
                    enemy.getComponent(KnightControl.class).setLife(EnemyLife - i);
                    if (enemy.getComponent(KnightControl.class).dead == true) {
                        enemy.removeFromWorld();
                        enemy.getComponent(KnightControl.class).setLife(EnemyLife);
                        i = 0;
                    }
                    invencivel = 1;
                }
            }
        });
        getPhysicsWorld().addCollisionHandler(new CollisionHandler(CoelhoType.HITBOX, CoelhoType.FORCA) {
            @Override
            protected void onCollisionBegin(Entity hitbox, Entity forca) {
                forca.removeFromWorld();
            }
        });
        getPhysicsWorld().addCollisionHandler(new CollisionHandler(CoelhoType.HITBOXENEMY, CoelhoType.PLAYER) {
            @Override
            protected void onCollisionBegin(Entity hitbox, Entity player) {
                getAudioPlayer().playSound("Hurt.wav");
                DamageHero();
                vidas = vidas - 1;
                getGameState().increment("vidas", -1);
                if (vidas <= 0) {
                    showGameOver();
                }
            }
        });

        getPhysicsWorld().addCollisionHandler(new CollisionHandler(CoelhoType.HITBOX, CoelhoType.BLOCK) {
            @Override
            protected void onCollisionBegin(Entity hitbox, Entity player) {
                
            }
        });

        getPhysicsWorld().addCollisionHandler(new CollisionHandler(CoelhoType.HITBOX, CoelhoType.FINALBOSS) {
            @Override
            protected void onCollisionBegin(Entity hitbox, Entity boss) {
                k = k + 1;
                boss.getComponent(FinalBossControl.class).setLife(3 - k);
            }
        });
        
        getPhysicsWorld().addCollisionHandler(new CollisionHandler(CoelhoType.PLAYER, CoelhoType.BOLADEFOGO) {
            @Override
            protected void onCollisionBegin(Entity hitbox, Entity bola) {
                bola.removeFromWorld();
                getAudioPlayer().playSound("Hurt.wav");
                DamageHero();
                vidas = vidas - 1;
                getGameState().increment("vidas", -1);
                if (vidas <= 0) {
                    showGameOver();
                }
            }
        });
        getPhysicsWorld().addCollisionHandler(new CollisionHandler(CoelhoType.PLATAFORMA, CoelhoType.BOLADEFOGO) {
            @Override
            protected void onCollisionBegin(Entity hitbox, Entity bola) {
                bola.removeFromWorld();
                
            }
        });
    }

    @Override
    protected void onUpdate(double tpf) {
        if (invencivel == 1) {
            getMasterTimer().runOnceAfter(() -> {
                invencivel = 0;
            }, Duration.seconds(0.6));
        }
        vidinhas.textProperty().bind(getGameState().intProperty("vidas").asString());
        score.textProperty().bind(getGameState().intProperty("scores").asString());
        if(level == 3){
            if (getGameWorld().getEntitiesByType(CoelhoType.KNIGHT).isEmpty()) {
            Boss.getComponent(FinalBossControl.class).setEscudo(0);
        }
        }

    }

    private void showGameOver() {
        getDisplay().showMessageBox("Sua Jornada acaba aqui companheiro, você morreu D:", this::nextLevel);
    }

    private void DamageHero() {
        player.getComponent(PlayerControl.class).damage();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public void spawnenemies() {
        Knight = getGameWorld().spawn("knight", 750, 200);
    }
    
}
