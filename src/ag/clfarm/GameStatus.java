package ag.clfarm;

import java.util.*;



public class GameStatus {
    private Farm theFarm;
    
    private Set<GameEventListener> listeners;
    private Timer timer;

    public GameStatus()
    {
        timer = new Timer("Game event scheduler");
        listeners = new HashSet<GameEventListener>();

        theFarm = Farm.makeSampleFarm(this);
    }

    public void shutdown()
    {
        timer.cancel();
    }
    
    public Farm getFarm()
    {
        return theFarm;
    }

    public void scheduleCrop(long expireTime, final CropType cropType)
    {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                GameStatus.this.dispatchEvent(null, cropType);
            }
        };

        timer.schedule(task, new Date(expireTime));
    }
    
    
    public void addGameListener(GameEventListener listener)
    {
        listeners.add(listener);
    }
    
    private void dispatchEvent(GameEvent event, CropType info)
    {
        for (GameEventListener listener: listeners) {
            listener.handleGameEvent(event, info);
        }
    }
}

