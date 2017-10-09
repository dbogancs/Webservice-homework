package cinema.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.jws.WebService;

import cinema.service.ArrayOfSeat;
import cinema.service.CinemaException;
import cinema.service.ICinema;
import cinema.service.ICinemaBuyCinemaException;
import cinema.service.ICinemaGetAllSeatsCinemaException;
import cinema.service.ICinemaGetSeatStatusCinemaException;
import cinema.service.ICinemaInitCinemaException;
import cinema.service.ICinemaLockCinemaException;
import cinema.service.ICinemaReserveCinemaException;
import cinema.service.ICinemaUnlockCinemaException;
import cinema.service.Seat;
import cinema.service.SeatStatus;


@WebService(
		name="Hello",
		portName="ICinema_HttpSoap11_Port",
		targetNamespace="http://www.iit.bme.hu/soi/hw/SeatReservation",
		endpointInterface="cinema.service.ICinema",
		wsdlLocation="WEB-INF/wsdl/SeatReservation.wsdl")
public class Cinema2 implements ICinema{

	
	private static ArrayList<ExtendedSeat> allSeats = new ArrayList<>();
	
	private static ArrayList<LockedExtendedSeats> allLocks = new ArrayList<>();
	
	
	private ExtendedSeat getSeat(String row, String column) throws Exception{
		for (int i = 0; i < allSeats.size(); i++) {
					
			ExtendedSeat target = allSeats.get(i);
			if(target.getColumn().equals(column)
					&& target.getRow().equals(row))
				return target;
		}
		throw new Exception("Seat with row "+row+" and column "+column
				+" is not found in the list of seats with "+allSeats.size()+" elements.");
	}
	
	private LockedExtendedSeats getLock(String id) throws Exception {
		for (int i = 0; i < allLocks.size(); i++) {
			LockedExtendedSeats lock = allLocks.get(i);
			if(lock.getId().equals(id))
				return lock;
		}
		throw new Exception("Lock with id "+id+" is not found in the list of locks with "+allSeats.size()+" elements.");
	}
	
	private String numToChar(int i) throws Exception{
		if(i < 1 && i > 26) throw new Exception("Value must be between 1-26.");
		String abc = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		return Character.toString(abc.charAt(i));
	}	
	
	private int charToNum(String s) throws Exception{
		if(s.length()!=1) throw new Exception("Length of value must be 1. The \'" + s + "\' value has not a valid length.");
		String abc = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		return abc.indexOf(s)+1;
	}
	
	@Override
	public void init(int rows, int columns) throws ICinemaInitCinemaException {
		
		if(!(1 <= rows
				|| rows <= 26
				|| 1 <= columns
				|| columns <= 100))
			throw new ICinemaInitCinemaException(
					"The number of rows must be between 1-26 and the number of columns must be between 1-100.", 
					new CinemaException());
		
		allSeats = new ArrayList<ExtendedSeat>();
		allLocks = new ArrayList<LockedExtendedSeats>();
		
		for(int row = 0; row < rows; row++) {
			for(int column = 0; column < columns; column++) {
				
				String trueRow;
				try {
					trueRow = numToChar(row);
				} catch (Exception e) {
					System.out.println("Exception: " + e.getMessage());
					throw new ICinemaInitCinemaException("The number of rows must be between 1-26.",
							new CinemaException());
				}
				int trueColumn = column+1;
				
				ExtendedSeat seat = new ExtendedSeat(trueRow,trueColumn);
				allSeats.add(seat);

				System.out.println("Seat added: row " + trueRow + ", column " + trueColumn);
			}
		}
		System.out.println("All seats are added! Sum: " + allSeats.size());
	}

	@Override
	public ArrayOfSeat getAllSeats() throws ICinemaGetAllSeatsCinemaException {
		ExtendedArrayOfSeat aos = new ExtendedArrayOfSeat();
		List<Seat> seats = new ArrayList<>();
		for(int i = 0; i < allSeats.size(); i++) {
			seats.add(allSeats.get(i).getSeat());
		}
		aos.setSeat(seats);
		return (ArrayOfSeat) aos;
	}

	@Override
	public SeatStatus getSeatStatus(Seat seat) throws ICinemaGetSeatStatusCinemaException {
		try{
			return getSeat(seat.getRow(),seat.getColumn()).getStatus();
		}
		catch(Exception e){
			System.out.println("Exception: " + e.getMessage());
			throw new ICinemaGetSeatStatusCinemaException(e.getMessage(), new CinemaException());
		}
	}

	@Override
	public String lock(Seat seat, int count) throws ICinemaLockCinemaException {

		ArrayList<ExtendedSeat> seats = new ArrayList<>();
		LockedExtendedSeats locked = new LockedExtendedSeats();  
		
		try {			
			for (int i = 0; i < count; i++) {
				String targetColumn = new Integer(Integer.parseInt(seat.getColumn()) + i).toString();
				ExtendedSeat targetSeat = getSeat(seat.getRow(), targetColumn);
				
				if(!targetSeat.getStatus().toString().equals("FREE"))
					throw new Exception("The seat " + seat.getRow() + targetColumn
							+ " is not free. The status is " + targetSeat.getStatus().toString());
				
				seats.add(targetSeat);
			}
			locked.setSeats(seats);
			locked.lockAll();
			allLocks.add(locked);
			
		} catch (Exception e) {
			System.out.println("Exception: " + e.getMessage());
			throw new ICinemaLockCinemaException(e.getMessage(), new CinemaException());
		}
		return locked.getId();
	}

	@Override
	public void unlock(String lockId) throws ICinemaUnlockCinemaException {
		try {
			LockedExtendedSeats lock = getLock(lockId);
			lock.unlockAll();
			
		} catch (Exception e) {
			System.out.println("Exception: " + e.getMessage());
			throw new ICinemaUnlockCinemaException(e.getMessage(), new CinemaException());
		}
	}

	@Override
	public void reserve(String lockId) throws ICinemaReserveCinemaException {
		try {
			LockedExtendedSeats lock = getLock(lockId);
			lock.reserveAll();
			
		} catch (Exception e) {
			System.out.println("Exception: " + e.getMessage());
			throw new ICinemaReserveCinemaException(e.getMessage(), new CinemaException());
		}
	}

	@Override
	public void buy(String lockId) throws ICinemaBuyCinemaException {
		try {
			LockedExtendedSeats lock = getLock(lockId);
			lock.buyAll();
			
		} catch (Exception e) {
			System.out.println("Exception: " + e.getMessage());
			throw new ICinemaBuyCinemaException(e.getMessage(), new CinemaException());
		}
	}


}
