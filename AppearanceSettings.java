package com.mycompany.java2project2;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class AppearanceSettings {
    private static AppearanceSettings instance;
    private Color currentColor = Color.WHITE; // Default color changed to white
    private List<AppearanceListener> listeners = new ArrayList<>();

    private AppearanceSettings() {
    }

    public static synchronized AppearanceSettings getInstance() {
        if (instance == null) {
            instance = new AppearanceSettings();
        }
        return instance;
    }

    public Color getCurrentColor() {
        return currentColor;
    }

    public void setCurrentColor(Color color) {
        this.currentColor = color;
        notifyListeners();
    }

    public void addListener(AppearanceListener listener) {
        listeners.add(listener);
    }

    private void notifyListeners() {
        for (AppearanceListener listener : listeners) {
            listener.updateAppearance(currentColor);
        }
    }

    // Interface for listeners
    public interface AppearanceListener {
        void updateAppearance(Color newColor);
    }
}