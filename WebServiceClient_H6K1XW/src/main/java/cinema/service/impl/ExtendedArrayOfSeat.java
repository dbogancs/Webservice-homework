package cinema.service.impl;

import java.util.List;

import cinema.service.ArrayOfSeat;
import cinema.service.Seat;

public class ExtendedArrayOfSeat extends ArrayOfSeat {
	public void setSeat(List<Seat> s) {
		this.seat = s; 
	}
}
