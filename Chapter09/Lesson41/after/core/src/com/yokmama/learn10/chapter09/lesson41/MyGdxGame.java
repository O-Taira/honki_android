package com.yokmama.learn10.chapter09.lesson41;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

public class MyGdxGame extends ApplicationAdapter {

    // ゲームの状態
    enum GameState {
        Ready, Running, GameOver, GameCleared
    }

    // 現在のゲームの状態
    GameState gameState = GameState.Ready;
    // スコア
    int score;

    SpriteBatch batch;

    // 描画範囲
    int viewportWidth = 800;
    int viewportHeight = 480;

    // カメラ
    OrthographicCamera camera;
    float cameraLeftEdge;

    // UI用カメラ
    OrthographicCamera uiCamera;

    // テキスト
    BitmapFont font;
    Text text;

    // キャラクターの制御オブジェクト
    Hero hero;
    // キャラクターの速度
    float heroVelocityX = 400;

    // 背景
    Texture backgroundClear;
    float bgWidth;
    float bgSpeed;

    // ゴール
    Texture finish;
    float finishX;

    // スコアアイテム毎の添字
    static final int SCORE_ITEM_ONE = 0;
    static final int SCORE_ITEM_TWO = 1;
    static final int SCORE_ITEM_THREE = 2;
    static final int SCORE_ITEM_FOUR = 3;

    // スコアアイテム
    TextureRegion[] chipRegions;
    Array<Chip> chips = new Array<Chip>();
    Array<Chip> chipsToRemove = new Array<Chip>();
    int[] chipScores;
    float chipSize = 50.0f;

    // 障害物
    TextureRegion mineTexture;
    Array<Mine> mines = new Array<Mine>();
    Array<Mine> minesToRemove = new Array<Mine>();
    float mineSize = 50.0f;

    // サウンド
    Music music;
    Sound collision;
    Sound coin;
    Sound finaleClaps;

    @Override
    public void create() {
        batch = new SpriteBatch();

        // カメラ
        camera = new OrthographicCamera();
        camera.setToOrtho(false, viewportWidth, viewportHeight);
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);

        // UI用カメラ
        uiCamera = new OrthographicCamera();
        uiCamera.setToOrtho(false, camera.viewportWidth, camera.viewportHeight);
        uiCamera.update();

        // テキスト
        font = new BitmapFont(Gdx.files.internal("verdana39.fnt"));
        text = new Text(uiCamera);

        // キャラクター
        Texture hero = new Texture("UnityChan.png");
        float[] timePerFrame = new float[] { 0.05f, 0.05f, 0.05f };
        int[] numFrames = new int[] { 4, 7, 5 };
        this.hero = new Hero(hero, 64, 64, timePerFrame, numFrames);

        // 背景
        backgroundClear = new Texture("bg.png");
        bgWidth = viewportHeight * (backgroundClear.getWidth() / backgroundClear.getHeight());
        bgSpeed = 0.2f;

        // ゴール
        finish = new Texture("flag.png");
        finishX = (bgWidth - viewportWidth) / bgSpeed + Hero.HERO_LEFT_X;

        // スコアアイテム
        chipRegions = new TextureRegion[4];
        Texture coins = new Texture("coins.png");
        final int COINS_SIZE = 16;
        chipRegions[SCORE_ITEM_ONE] = new TextureRegion(coins, 0, 0, COINS_SIZE, COINS_SIZE);
        chipRegions[SCORE_ITEM_TWO] = new TextureRegion(coins, COINS_SIZE, 0, COINS_SIZE, COINS_SIZE);
        chipRegions[SCORE_ITEM_THREE] = new TextureRegion(coins, COINS_SIZE * 2, 0, COINS_SIZE, COINS_SIZE);
        chipRegions[SCORE_ITEM_FOUR] = new TextureRegion(coins, COINS_SIZE * 3, 0, COINS_SIZE, COINS_SIZE);

        chipScores = new int[4];
        chipScores[SCORE_ITEM_ONE] = 10;
        chipScores[SCORE_ITEM_TWO] = 20;
        chipScores[SCORE_ITEM_THREE] = 50;
        chipScores[SCORE_ITEM_FOUR] = 100;

        // 障害物
        mineTexture = new TextureRegion(new Texture("fire.png"));

        // サウンド
        music = Gdx.audio.newMusic(Gdx.files.internal("music.mp3"));
        music.setLooping(true);
        music.setVolume(0.5f);
        music.play();

        collision = Gdx.audio.newSound(Gdx.files.internal("laser3.mp3"));
        coin = Gdx.audio.newSound(Gdx.files.internal("coin05.mp3"));
        finaleClaps = Gdx.audio.newSound(Gdx.files.internal("clapping.mp3"));

        resetWorld();
    }

    // ゲームを最初の状態に戻す
    private void resetWorld() {
        score = 0;

        // キャラクターの位置と状態の初期化
        hero.getPosition().set(Hero.HERO_LEFT_X, Hero.HERO_FLOOR_Y);
        hero.getVelocity().set(0, 0);
        hero.init();

        // カメラの位置を開始点へ設定
        camera.position.x = viewportWidth / 2 - Hero.HERO_LEFT_X;
        cameraLeftEdge = camera.position.x - viewportWidth / 2;

        Generator.init(viewportWidth);
        chips.clear();
        mines.clear();
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 153.0f / 255.0f, 204.0f / 255.0f, 1); // #0099CC
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        updateWorld();
        drawWorld();
    }

    // 各種状態を変更するメソッド
    private void updateWorld() {

        float deltaTime = Gdx.graphics.getDeltaTime();

        // 入力制御

        if (Gdx.input.justTouched()) {
            if (gameState == GameState.Ready) {
                gameState = GameState.Running;

                hero.startRunning();
                hero.getVelocity().set(heroVelocityX, 0);
            }
            else if (gameState == GameState.GameOver) {
                gameState = GameState.Ready;
                resetWorld();
            }
            else if (gameState == GameState.GameCleared) {
                gameState = GameState.Ready;
                resetWorld();
            }
            else if (gameState == GameState.Running) {
                hero.jump();
            }
            Gdx.app.log("MyGdxGame", "gameState=" + gameState);
        }

        // オブジェクトの新規生成

        if (Generator.chipGenerationLine < cameraLeftEdge + viewportWidth &&
                Generator.chipGenerationLine + 5 * 50.0f < finishX) {
            Generator.generate(this);
        }

        // オブジェクトの更新

        chipsToRemove.clear();
        for (Chip chip : chips) {
            chip.update(deltaTime);

            if (chip.isDead) {
                chipsToRemove.add(chip);
            }
            else if (chip.position.x + chip.size.x < cameraLeftEdge) {
                chipsToRemove.add(chip);
            }
        }
        for (Chip chip : chipsToRemove) {
            chips.removeValue(chip, false);
        }

        minesToRemove.clear();
        for (Mine mine : mines) {
            mine.update(deltaTime);

            if (mine.position.x + mine.size.x < cameraLeftEdge) {
                minesToRemove.add(mine);
            }
        }
        for (Mine mine : minesToRemove) {
            mines.removeValue(mine, false);
        }

        // キャラクターの状態を更新

        hero.update(deltaTime);

        // カメラの位置をキャラクターに合わせて移動させる

        if (gameState != GameState.GameCleared) {
            camera.position.x = viewportWidth / 2 + hero.getPosition().x - Hero.HERO_LEFT_X;
            cameraLeftEdge = camera.position.x - viewportWidth / 2;
        }

        // ゲームクリアチェック

        if (gameState != GameState.GameCleared) {
            float heroX = hero.getPosition().x;
            if (finishX < heroX) {
                finaleClaps.play();
                gameState = GameState.GameCleared;
                hero.win(); // クリアしたことを通知
            }
        }

        // ゲームオーバーまたはゲームクリア後は衝突判定を行わない

        if (gameState == GameState.GameOver || gameState == GameState.GameCleared) {
            return;
        }

        // 衝突判定

        Rectangle heroCollision = hero.getCollisionRect();

        for (Chip chip : chips) {
            if (!chip.isCollected && Intersector.overlaps(chip.collisionCircle, heroCollision)) {
                chip.collect();
                coin.play();

                score += chipScores[chip.type];
            }
        }

        for (Mine mine : mines) {
            if (!mine.hasCollided && Intersector.overlaps(mine.collisionCircle, heroCollision)) {
                mine.collide();
                collision.play();
                hero.die();
                gameState = GameState.GameOver;
            }
        }
    }

    // 描画メソッド
    private void drawWorld() {
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        // ゲーム描画

        float drawOffset = cameraLeftEdge - cameraLeftEdge * bgSpeed;
        batch.draw(backgroundClear, drawOffset, 0, bgWidth, viewportHeight);

        for (Chip chip : chips) {
            chip.draw(this);
        }

        for (Mine mine : mines) {
            mine.draw(this);
        }

        hero.draw(this);

        batch.draw(finish, finishX, 0,
                finish.getWidth() * 0.35f,
                finish.getHeight() * 0.35f);

        batch.end();
        batch.setProjectionMatrix(uiCamera.combined);
        batch.begin();

        // UI描画

        // 文字列描画

        if (gameState == GameState.Ready) {
            text.drawTextTop(batch, font, "START");
        }
        else if (gameState == GameState.GameCleared) {
            text.drawTextTop(batch, font, "SCORE: " + score);
            text.drawTextCenter(batch, font, "LEVEL CLEAR");
        }
        else if (gameState == GameState.GameOver) {
            text.drawTextTop(batch, font, "SCORE: " + score);
            text.drawTextCenter(batch, font, "GAME OVER");
        }
        else if (gameState == GameState.Running) {
            text.drawTextTop(batch, font, "SCORE: " + score);
        }

        batch.end();
    }
}