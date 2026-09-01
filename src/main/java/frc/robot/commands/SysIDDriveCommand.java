package frc.robot.commands;

import com.ctre.phoenix6.SignalLogger;
import org.wpilib.units.Units;
import org.wpilib.command2.SequentialCommandGroup;
import org.wpilib.command2.WaitCommand;
import org.wpilib.command2.sysid.SysIdRoutine.Config;
import org.wpilib.command2.sysid.SysIdRoutine.Direction;
import frc.robot.subsystems.Drivetrain;
import frc.robot.util.SysId;

public class SysIDDriveCommand extends SequentialCommandGroup {

    private Config config = new Config();
    private SysId sysId;
    public SysIDDriveCommand(Drivetrain drive) {
        config = new Config(
            Units.Volts.of(0.5).per(Units.Seconds),
            Units.Volts.of(3),
            Units.Seconds.of(6),
            (x)->SignalLogger.writeString("state", x.toString())
        );
        sysId = new SysId(
            "Drivetrain",
            x ->{
                    drive.tankDriveVolts(x.magnitude(), x.magnitude());
                },
            drive,
            config
        );
        addCommands(
            sysId.runQuasisStatic(Direction.kForward),
            new WaitCommand(0.5),
            sysId.runQuasisStatic(Direction.kReverse),
            new WaitCommand(0.5),
            sysId.runDynamic(Direction.kForward),
            new WaitCommand(0.5),
            sysId.runDynamic(Direction.kReverse)
        );
    }

    

}
