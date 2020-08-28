package com.github.mouse0w0.peach.mcmod.model.json.mesh;

import com.github.mouse0w0.peach.mcmod.model.json.JsonModel;
import com.github.mouse0w0.peach.mcmod.model.json.JsonModelHelper;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.DrawMode;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;

import java.nio.file.Files;
import java.nio.file.Paths;

public class Test3D extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    double anchorX, anchorY, rotate;

    @Override
    public void start(Stage primaryStage) throws Exception {
        JsonModel model = JsonModelHelper.load(Paths.get("D:\\Workspace\\Forge\\Peach\\src\\main\\java\\com\\github\\mouse0w0\\peach\\mcmod\\model\\json\\mesh\\rail_flat.json"));
        TextureMapFX textureMap = TextureMapFX.builder().texture("blocks/rail_normal",
                new Image(Files.newInputStream(Paths.get("D:\\Workspace\\Forge\\Peach\\src\\main\\java\\com\\github\\mouse0w0\\peach\\mcmod\\model\\json\\mesh\\rail_normal.png")))).build();
        TriangleMesh mesh = new TriangleMesh();
        MeshGenerator.generate(model, textureMap, new VertexDataConsumer() {
            int index = 0;

            @Override
            public void pos(float x, float y, float z) {
                mesh.getPoints().addAll(x, y, z);
                mesh.getFaces().addAll(index, index);
                index++;
            }

            @Override
            public void texCoord(float u, float v) {
                mesh.getTexCoords().addAll(u, v);
            }

            @Override
            public void normal(float nx, float ny, float nz) {

            }
        });

        MeshView meshView = new MeshView(mesh);
        meshView.setDrawMode(DrawMode.FILL);
        meshView.setRotationAxis(Rotate.Y_AXIS);
        meshView.setScaleX(200);
        meshView.setScaleY(-200);
        meshView.setScaleZ(200);
        meshView.setTranslateX(250);
        meshView.setTranslateY(250);
        PhongMaterial material = new PhongMaterial(Color.WHITE);
        material.setDiffuseMap(textureMap.getImage());
        meshView.setMaterial(material);

        // Create and position camera
        Camera camera = new PerspectiveCamera(false);
        camera.getTransforms().addAll(
                new Rotate(-30, Rotate.Y_AXIS),
                new Rotate(-30, Rotate.X_AXIS)
        );
//                new Translate(0, 0, -5));

        // Build the Scene Graph
        Group root = new Group();
        root.getChildren().addAll(camera, new AmbientLight(Color.WHITE), meshView);
//        root.getChildren().add(new AmbientLight(Color.WHITE));

        // Use a SubScene
        SubScene subScene = new SubScene(root, 500, 500, true, SceneAntialiasing.DISABLED);
        subScene.setFill(Color.BLACK);

        subScene.setCamera(camera);
        Group group = new Group();
        group.getChildren().add(subScene);

        Scene scene = new Scene(group, 500, 500);

        scene.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                anchorX = event.getSceneX();
                anchorY = event.getSceneY();
                rotate = meshView.getRotate();
            }
        });

        scene.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                meshView.setRotate(rotate + anchorX - event.getSceneX());
            }
        });

        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
