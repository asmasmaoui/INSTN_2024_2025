package 'Use Metadata' {
    private import 'Req Metadata'::StandardRequirement;

    part def System;

    requirement RS {
        doc /* When (In_infrared_sensor || Is_opening) && !In_opening_limit_switch then ... */
        @StandardRequirement {
            response = "Out_motor_open_door && Is_opening";
            trigger = "In_infrared_sensor || Is_opening) && !In_opening_limit_switch";
        }
        subject : System;
    }
}
