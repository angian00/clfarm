package ag.clfarm;

public interface GameEventListener {

    public void handleGameEvent(GameEvent event, CropType info);  //TODO: generalize
   
}
