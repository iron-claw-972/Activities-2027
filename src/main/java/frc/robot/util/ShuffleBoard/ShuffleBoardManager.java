package frc.robot.util.ShuffleBoard;

import java.util.ArrayList;

import org.wpilib.shuffleboard.Shuffleboard;
import org.wpilib.smartdashboard.SmartDashboard;
import org.wpilib.command2.Command;
import org.wpilib.command2.CommandScheduler;
import frc.robot.subsystems.Drivetrain;
import frc.robot.util.ShuffleBoard.Tabs.AutoTab;
import frc.robot.util.ShuffleBoard.Tabs.DriveTab;
import frc.robot.util.ShuffleBoard.Tabs.SubsystemTab;

public class ShuffleBoardManager {

    private ArrayList<ShuffleBoardTabs> tabs = new ArrayList<>();
    
    private Field feild;

    private DriveTab driveTab;
    private AutoTab autoTab;
    private SubsystemTab subsystemTab;

    // TODO 2.3.12: Add parameter to constructor
    public ShuffleBoardManager(Drivetrain drive){
        driveTab = new DriveTab(drive);
        autoTab = new AutoTab(drive);
        subsystemTab = new SubsystemTab();
        tabs.add(driveTab);
        tabs.add(autoTab);
        tabs.add(subsystemTab);

        for (ShuffleBoardTabs tab : tabs){
            tab.createEntries();
        }
        
        feild = new Field(drive);

        // This doesn't need to be stored in its own class
        Shuffleboard.getTab("Scheduler").add("Command Scheduler", CommandScheduler.getInstance());
        SmartDashboard.putData("Command Scheduler", CommandScheduler.getInstance());
    }

    public void update(){
        for (ShuffleBoardTabs tab : tabs){
            tab.update();
        }
        feild.updateFeild();
    }

    public Command getSelectedCommand(){
        return autoTab.getChooser().getSelected();
    }
}
