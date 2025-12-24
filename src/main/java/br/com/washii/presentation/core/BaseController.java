package br.com.washii.presentation.core;

public abstract class BaseController {
    protected SceneManager sceneManager;

    public void setSceneManager(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
    }
}