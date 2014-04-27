package ag.clfarm;

import ag.clfarm.ui.InteractiveUI;

public class Launcher {
    
    public static void main (String[] args)
        throws Exception
    {
        GameStatus game = new GameStatus();
        InteractiveUI ui = new InteractiveUI(game);
        
        ui.runLoop();
//        System.out.println("Goodbye!");
    }
    
}
