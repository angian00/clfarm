package ag.clfarm.ui;

import java.util.*;

import ag.clfarm.*;

public class InteractiveUI 
    implements GameEventListener {
    
    public static final String CMD_EXIT = "q";
    public static final String CMD_REFRESH = "r";
    public static final String CMD_HARVEST_ALL = "H";
    public static final String CMD_HARVEST = "h";
    
    private GameStatus game;
    private Scanner scan;
    private boolean terminating = false;
    
    
    public InteractiveUI(GameStatus game)
    {
        this.game = game;
        game.addGameListener(this);
        scan = new Scanner(System.in);
    }
    
    public void runLoop()
        throws Exception
    {
        welcomeMsg();
        showFullStatus();
        
        while (!terminating) {
            getInput();
        }
        
        game.shutdown();
    }
    
    private void getInput()
    {
        boolean moreInput = true;
        
        while (moreInput) {
            moreInput = false;
            String inputLine = promptMsg();
            
            if (inputLine.equals(CMD_REFRESH)) {
                showFullStatus();
                
            } else if (inputLine.equals(CMD_EXIT)) {
                doExitUI();
                
            } else if (inputLine.equals(CMD_HARVEST_ALL)) {
                doHarvestAllFields();
                
            } else if (inputLine.equals(CMD_HARVEST)) {
                boolean moreParamInput = true;
                while (moreParamInput) {
                    moreParamInput = false;
                    inputLine = promptMsg("harvest field #");
                    try {
                        int iField = Integer.parseInt(inputLine);
                        doHarvestField(iField);
                        
                    } catch (NumberFormatException e) {
                        errorMsg("Not a numeric argument");
                        moreParamInput = true;
                    }
                }
                
            } else {
                errorMsg("Unknown command");
                moreInput = true;
            }
        }

    }
    
    //------------------------------------------------------
    // Status-changing actions
    //------------------------------------------------------
    private void doExitUI()
    {
        infoMsg("Goodbye!");
        this.terminating = true;
    }
    
    private void doHarvestField(int iField)
    {
        iField --; // normalize to array index convention
        
        Farm farm = game.getFarm();
        List<Field> fields = farm.getFields();
        if (iField < 0 || iField >= fields.size()) {
            errorMsg("Invalid field index");
            return;
        }

        Field field = fields.get(iField);
        if (!field.isProducing()) {
            errorMsg("Field is not producing");
            return;
        }
        
        if (!field.isProdReady()) {
            errorMsg("Produce is not ready yet");
            return;
        }
        
        infoMsg("Harvested " + field.getCropType() +" from field #" + (iField+1));

        Resource r = Resource.fromCrop(field.getCropType());
        farm.addToStorage(r);
        field.stopProd();
    }
    
    private void doHarvestAllFields()
    {
        Farm farm = game.getFarm();
        List<Field> fields = farm.getFields();
        for (int iField = 0; iField < fields.size(); iField ++) {
            Field field = fields.get(iField);

            if (!field.isProducing())
                continue;
            
            if (!field.isProdReady())
                continue;
            
            infoMsg("Harvested " + field.getCropType() +" from field #" + (iField+1));
        
            Resource r = Resource.fromCrop(field.getCropType());
            farm.addToStorage(r);
            field.stopProd();
        }
    }
    

    //------------------------------------------------------
    // GameEventListener method
    //------------------------------------------------------
    public void handleGameEvent(GameEvent event, CropType info)  //TODO: generalize
    {
        //if (event instanceof ...)
        alertMsg("A new " + info + " crop has ripened!");
    }
    
    //------------------------------------------------------
    // Specific reports
    //------------------------------------------------------
    private void showFullStatus()
    {
        System.out.println("-------- Farm status --------");
        Farm farm = game.getFarm();
        
        showFieldStatus(farm.getFields());
        showStorageStatus(farm.getStorage());
        System.out.println("-----------------------------");
    }
    
    private void showFieldStatus(List<Field> fields)
    {
        System.out.println("  Fields: ");
        int iFields = 1;
        for (Field field: fields) {
            String fieldDesc;
            if (field.isProducing()) {
                fieldDesc = field.getCropType().toString().toLowerCase() + " \t [" +
                        field.getProdStageLabel() + "]"; 
            } else {
                fieldDesc = "<-uncult->";
            }
            
            System.out.println("    (" + iFields + ") " + fieldDesc);
            iFields ++;
        }
        
        System.out.println();
    }
    
    private void showStorageStatus(Map<Resource, Integer> storage)
    {
        System.out.println("  Storage: ");
        for (Resource r: storage.keySet()) {
            int qty = storage.get(r);
            if (qty == 0)
                continue;
            else
                System.out.println("    " + r + ": \t" + qty);
        }
        
        System.out.println();
    }
    
    
    //------------------------------------------------------
    // General output formatting
    //------------------------------------------------------
    private void welcomeMsg()
    {
        System.out.println("-----------------------------");
        System.out.println(" Welcome to CommandLineFarm  ");
        System.out.println("-----------------------------");
        System.out.println();
        System.out.println();
    }

    private String promptMsg()
    {
        return this.promptMsg("");
    }

    private String promptMsg(String msg)
    {
        System.out.print(msg + " > ");
        return scan.nextLine();
    }

    private void errorMsg(String msg)
    {
        System.out.println("?? " + msg);
        System.out.println();
        }

    private void alertMsg(String msg)
    {
        System.out.println();
        System.out.println("!! " + msg);
        System.out.println();
        System.out.print(" > ");
    }

    private void infoMsg(String msg)
    {
        System.out.println("-- " + msg);
        System.out.println();
    }
}
