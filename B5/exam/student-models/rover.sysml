package Rover {
	public import SI::W;
	public import SI::'Ω';
	public import SI::A;
	public import SI::V;
	public import SI::rad;
	public import SI::Hz;
	public import ISQ::PowerValue;
	public import ISQ::ResistanceValue;
	public import ISQ::ElectricCurrentValue;
	public import ISQ::ElectricPotentialValue;
	public import ISQSpaceTime::AngularMeasureValue;
	public import MeasurementReferences::DerivedUnit;

	package Requirements {
		public import System::Rover;

		requirement def <ReqDef6> PowerConsumptionRequirement {
			doc
			/*
			 * Assuming the max consumption greater than 0, the actual consumption shall be less than or equal to the max consumption.
			 * Assuming the min consumption greater than 0, the actual consumption shall be greater than or equal to the max consumption.
			 */
			attribute actualPowerConsumption : PowerValue;
			attribute minConsumption : PowerValue;
			attribute maxConsumption : PowerValue;
			assume constraint { maxConsumption > 0 [W] }
			assume constraint { minConsumption > 0 [W] }
			require constraint { actualPowerConsumption <= maxConsumption }
			require constraint { actualPowerConsumption >= minConsumption }
		}

		requirement ObstacleDetection {
			doc
			/*
			 * If an obstacle is detected in the current path of the rover, the motors should
			 * stop. The control system should then recalculate a new path.
			 */
		}
	}

	package System {
		public import SignalDefinitions::RoverCommand;

		part def Rover {
			part steeringSubsystem : SteeringSystem {
				part leftSteeringWheel : SteeringWheel;
				part rightSteeringWheel : SteeringWheel;
			}

			part leftWheel : MotionWheel;

			part rightWheel : MotionWheel;
			
			// missing parts, missing connections
		}

		part def SteeringSystem {
			attribute steeringAngle : AngularMeasureValue;
			attribute powerConsumption : PowerValue;
			attribute resistance : ResistanceValue;
			attribute intensity : ElectricCurrentValue;
			attribute potential : ElectricPotentialValue;
		}

		abstract part def Wheel {
			attribute diameter : ScalarValues::Real;
			RPM: DerivedUnit = 1 / SI::min;
			port rpm : IRPM;
		}

		part def SteeringWheel :> Wheel {
		}

		part def MotionWheel :> Wheel {
		}

		// --- port definitions ---
		port def IRemoteControl {
			in cmd : RoverCommand;
		}

		port def IRPM {
			in rpm : ScalarValues::Real;
		}

		// --- behaviors ---
		package StatesBehaviors {
			// ...
		}

		package SignalDefinitions {
			attribute def GoForwardSignal :> Attributes::AttributeValue;
			attribute def GoBackwardSignal;
			attribute def TurnRightSignal;
			attribute def TurnLeftSignal;
			attribute def StopSignal;

			item def RoverCommand {
				attribute cmd : CommandKind;
				ref timestamp;
			}

			enum def CommandKind {
				// ...
			}
		}
	}

	package PowerModel {
		// calc defs ...
	}
}
