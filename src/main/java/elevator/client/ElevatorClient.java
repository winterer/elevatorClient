package elevator.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.remoting.RemoteAccessException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Simple elevator client that prints current floor and elevator state to {@link System#out} periodically.
 * 
 * @author winterer
 */
@Component
public class ElevatorClient {
	private static final Logger logger = LoggerFactory.getLogger(ElevatorClient.class);

	@Autowired
	private IElevatorController controller;

	private long lastClockTick = Long.MIN_VALUE;

	public ElevatorClient() {
		/* empty */
	}

	@Scheduled(fixedRate = 1000L)
	public void printFloorState() {
		// check clock tick
		try {
			long clockTick = controller.getClockTick();
			if (lastClockTick == clockTick)
				return; // nothing to do
			lastClockTick = clockTick;
		} catch (RemoteAccessException ex) {
			logger.warn(ex.getCause().toString());
			return;
		}

		// get metadata
		final int floors = controller.getFloorNum();
		final int nrOfElevators = controller.getElevatorNum();

		// get floor positions of elevators
		int[] elevatorsPositions = new int[nrOfElevators];
		for (int elevatorIdx = 0; elevatorIdx < nrOfElevators; elevatorIdx++) {
			int elevFloor = controller.getElevatorFloor(elevatorIdx);
			elevatorsPositions[elevatorIdx] = elevFloor;
		}

		// print floor information - every line represents one floor
		printFloorHeader(nrOfElevators);
		for (int floorIdx = floors - 1; floorIdx >= 0; floorIdx--) {
			boolean buttonUp = controller.getFloorButtonUp(floorIdx);
			boolean buttonDown = controller.getFloorButtonDown(floorIdx);

			printFloorLine(floorIdx, buttonUp, buttonDown, elevatorsPositions);
		}
		System.out.println();
	}

	private void printFloorHeader(int nrOfElevators) {
		System.out.println("Floor Btn Elevators");
		System.out.println("-------------------");
	}

	private void printFloorLine(int floorIdx, boolean buttonUp, boolean buttonDown, int... elevatorsFloorIdx) {
		Object btnUpText = buttonUp ? "U" : "";
		Object btnDownText = buttonDown ? "D" : "";

		StringBuilder elevText = new StringBuilder();
		for (int e = 0; e < elevatorsFloorIdx.length; e++) {
			int pos = elevatorsFloorIdx[e];
			elevText.append(pos == floorIdx ? Integer.toString(e) : " ");
		}

		System.out.format("%5d %1s%1s  %s\n", floorIdx, btnUpText, btnDownText, elevText);
	}
}
