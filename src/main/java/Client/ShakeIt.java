package Client;

import javafx.animation.TranslateTransition;
import javafx.scene.Node;
import javafx.util.Duration;

public class ShakeIt {
    private TranslateTransition tt;

    public ShakeIt(Node node){
        tt = new TranslateTransition(Duration.millis(70), node);
        tt.setFromX(0f);
        tt.setByX(10f);
        tt.setAutoReverse(true);
        tt.setCycleCount(4);

    }

    public void playAnim(){
        tt.playFromStart();
    }
}
