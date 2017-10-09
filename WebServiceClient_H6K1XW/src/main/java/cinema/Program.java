package cinema;

import java.io.Console;
import java.util.List;
import java.util.Scanner;

import javax.xml.ws.BindingProvider;

import cinema.service.ArrayOfSeat;
import cinema.service.CinemaService;
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


public class Program {

	public static void main(String[] args) {
		
		System.out.println("Client started.");
		//Scanner reader = new Scanner(System.in);
		
		try{
			//while(true){
				
				//System.out.println("Give four space separated parameters: Url, Row, Column and a Task (Lock, Reserve, Buy).");
				//String input = reader.nextLine();
				
				/*Console console = System.console();
				String input =  console.readLine();*/
				
				//String[] params = input.split(" ");
				
				//if(params.length==4){
				if(args.length==4){
		
					String url = args[0];//params[0];
					String row = args[1];//params[1];
					String column = args[2];//params[2];
					String task = args[3];//params[3];
					
					CinemaService cinemaService = new CinemaService();
					ICinema cinema = cinemaService.getICinemaHttpSoap11Port();
					BindingProvider bp = (BindingProvider) cinema;
					bp.getRequestContext().put(
							BindingProvider.ENDPOINT_ADDRESS_PROPERTY, 
							url);
					
					Seat seat = new Seat();
					seat.setRow(row);
					seat.setColumn(column);

					try {
						
						if(task.equals("Lock")){
							cinema.lock(seat, 1);
							System.out.println("Seat " + row + column + " is locked now.");
						}
						else if(task.equals("Reserve")){
							String lockId = cinema.lock(seat, 1);
							cinema.reserve(lockId);
							System.out.println("Seat " + row + column + " is locked and reserved now.");
						}
						else if(task.equals("Buy")){
							String lockId = cinema.lock(seat, 1);
							cinema.buy(lockId);
							System.out.println("Seat " + row + column + " is locked and bought now.");
						}
						else System.out.println("Unknown task, try it again.");
						
					} catch (Exception e) {
						System.out.println(e.getMessage());
					}
				}
				else{
					/*if(params[0].equals("Exit")) {
						break;
					}*/
					//else if(params[0].equals("Test_H6K1XW")) test();
					System.out.println("Invalid input! Four paramteres are required: url, row, column, task.");
				}
			//}
		}
		finally{
			//reader.close();
			System.out.println("Client stopped.");
		}
	}
		
	private static void test(){
		
		System.out.println("Client start");
		
		CinemaService cinemaService = new CinemaService();
		ICinema cinema = cinemaService.getICinemaHttpSoap11Port();
		BindingProvider bp = (BindingProvider) cinema;
		bp.getRequestContext().put(
				BindingProvider.ENDPOINT_ADDRESS_PROPERTY, 
				"http://localhost:8080/WebService_H6K1XW/Cinema");
		try {
			cinema.init(5, 5);
			
			ArrayOfSeat aos = cinema.getAllSeats();
			List<Seat> seats = aos.getSeat();
			for(Seat seat : seats)
				System.out.println("Row:" + seat.getRow() + ", column:" + seat.getColumn());
		
			Seat seat = new Seat();
			seat.setColumn("1");
			seat.setRow("A");
			SeatStatus status = cinema.getSeatStatus(seat);
			System.out.println("Status of Seat A1: " + status.toString());
			
			String lockId = cinema.lock(seat, 1);
			System.out.println("Seat A1 is locked");
			status = cinema.getSeatStatus(seat);
			System.out.println("Status of Seat A1: " + status.toString());

			cinema.unlock(lockId);
			System.out.println("Seat A1 is unlocked");
			status = cinema.getSeatStatus(seat);
			System.out.println("Status of Seat A1: " + status.toString());

			lockId = cinema.lock(seat, 1);
			System.out.println("Seat A1 is locked");
			status = cinema.getSeatStatus(seat);
			System.out.println("Status of Seat A1: " + status.toString());

			cinema.reserve(lockId);
			System.out.println("Seat A1 is reserved");
			status = cinema.getSeatStatus(seat);
			System.out.println("Status of Seat A1: " + status.toString());

			cinema.buy(lockId);
			System.out.println("Seat A1 is bought");
			status = cinema.getSeatStatus(seat);
			System.out.println("Status of Seat A1: " + status.toString());
			
		} catch (ICinemaGetSeatStatusCinemaException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ICinemaInitCinemaException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ICinemaGetAllSeatsCinemaException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ICinemaLockCinemaException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ICinemaReserveCinemaException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ICinemaUnlockCinemaException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ICinemaBuyCinemaException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("Client end");
	}
	
}
