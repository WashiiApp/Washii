package br.com.washii.presentation.utils;

public abstract class BaseController {
    protected SceneManager sceneManager;

    public void setSceneManager(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
    }
}