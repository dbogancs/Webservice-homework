package cinema.service.impl;

import cinema.service.Seat;
import cinema.service.SeatStatus;

public class ExtendedSeat {

	private static int newId;
	
	private final int id;
	private Seat seat;
	private SeatStatus status;
	
	public ExtendedSeat(String row, int column) {
		seat = new Seat();
		seat.setRow(row);
		seat.setColumn(new Integer(column).toString());
		this.status = SeatStatus.FREE;
		id = newId;
		newId++;
	}
	
	public int getId() {
		return id;
	}
	public String getColumn() {
		return seat.getColumn();
	}
	public String getRow() {
		return seat.getRow();
	}
	public SeatStatus getStatus() {
		return status;
	}
	public void setStatus(SeatStatus status) {
		this.status = status;
	}
	public Seat getSeat() {
		return seat;
	}

	
	
}
