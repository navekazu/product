package tools.filer.controller;

import javafx.event.ActionEvent;
import javafx.scene.input.InputMethodEvent;

public interface CommandInputEvent {
    public void onInputMethodTextChanged(InputMethodEvent event);
    public void onActionEvent(ActionEvent event);
}
