package cinema.service.impl;

import java.util.ArrayList;

import cinema.service.SeatStatus;

public class LockedExtendedSeats {

	private static Integer newId = 0;
	
	private final String id;
	private ArrayList<ExtendedSeat> seats;
	
	public LockedExtendedSeats() {
		id = newId.toString();
		newId++;
	}
	
	private void setAllStatus(SeatStatus status){
		for (ExtendedSeat seat : seats) {
			seat.setStatus(status);
		}
	}
	
	public void unlockAll(){
		setAllStatus(SeatStatus.FREE);
	}
	public void lockAll(){
		setAllStatus(SeatStatus.LOCKED);
	}
	public void reserveAll(){
		setAllStatus(SeatStatus.RESERVED);
	}
	public void buyAll(){
		setAllStatus(SeatStatus.SOLD);
	}
	
	public String getId() {
		return id;
	}
	public ArrayList<ExtendedSeat> getSeats() {
		return seats;
	}
	public void setSeats(ArrayList<ExtendedSeat> seats) {
		this.seats = seats;
	}
	
}
